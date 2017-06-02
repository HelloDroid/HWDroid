package com.hw.hwdroid.foundation.app.rx.bus;

import android.support.annotation.NonNull;

import com.hw.hwdroid.foundation.app.rx.bus.annotation.HSubscribe;
import com.hw.hwdroid.foundation.app.rx.bus.annotation.HUseRxBus;
import com.hw.hwdroid.foundation.app.rx.bus.pojo.HRxBusMsg;
import com.orhanobut.logger.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Rx Bus
 * <p>
 * Created by nJ on 2017/2/17.
 */

public class HRxBus {
    // TAG默认值
    public static final int TAG_DEFAULT = -1000;
    public static final int TAG_UPDATE = -1010;
    public static final int TAG_CHANGE = -1020;
    public static final int TAG_OTHER = -1030;
    public static final int TAG_ERROR = -1090;

    // TAG-class
    private static Map<Class, Integer> tag4Class = new HashMap<>();

    // 存放订阅者信息
    private Map<Object, CompositeDisposable> subscriptions = new HashMap<>();

    // 发布者
    private final Subject bus;


    private static class SingletonHolder {
        private static final HRxBus INSTANCE = new HRxBus();
    }

    public static HRxBus getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * PublishSubject 创建一个可以在订阅之后把数据传输给订阅者Subject
     * SerializedSubject 序列化Subject为线程安全的Subject RxJava2 暂无
     */
    private HRxBus() {
        bus = PublishSubject.create().toSerialized();
    }

    /**
     * 添加序列
     * 根据object 生成唯一id
     */
    private int tag = TAG_DEFAULT;

    private void addTag4Class(Class cla) {
        tag4Class.put(cla, tag);
        tag--;
    }

    /**
     * tag值使用RxBus.getInstance().getTag(class,value)获取
     * 使用getTag主要用于后期维护方便，可以及时找到发布事件的对应处理。
     *
     * @param cla   为Rxbus事件处理的类
     * @param value 是事件处理的tag
     * @return tag
     */
    public int getTag(@NonNull Class cla, int value) {
        if (null == tag4Class || null == tag4Class.get(cla)) {
            return TAG_ERROR;
        }

        return tag4Class.get(cla) + value;
    }

    /**
     * 判断是否需要订阅，如果需要订阅那么自动控制生命周期
     */
    public void init(@NonNull Object object) {
        Flowable.just(object)
                .filter(o -> o != null)
                .map(o -> o.getClass().getAnnotation(HUseRxBus.class))
                .filter(useRxBus -> useRxBus != null)
                .subscribe(useRxBus -> {
                    addTag4Class(object.getClass());
                    register(object);
                }, throwable -> {
                });
    }

    /**
     * 订阅者注册
     *
     * @param subscriber
     */
    public void register(@NonNull Object subscriber) {
        Flowable.just(subscriber)
                .filter(s -> s != null)                     // 判断订阅者不为空
                .filter(s -> subscriptions.get(s) == null)  // 判断订阅者没有在序列中
                .flatMap(s -> Flowable.fromArray(s.getClass().getDeclaredMethods()))    // 获取订阅者方法并且用Observable装载
                .map(method -> {                            // 使非public方法可以被invoke,并且关闭安全检查提升反射效率
                    method.setAccessible(true);
                    return method;
                })
                .filter(method -> method.isAnnotationPresent(HSubscribe.class))          // 方法必须被Subscribe注解
                .subscribe(method -> addSubscription(method, subscriber), throwable -> Logger.e(throwable));
    }

    /**
     * 发布事件
     *
     * @param tag 值使用getInstance().getTag(class,value)获取
     * @param obj 为需要被处理的事件
     */
    public void post(int tag, @NonNull Object obj) {
        if (TAG_ERROR == tag) {
            return;
        }

        bus.onNext(new HRxBusMsg(tag, obj));
    }

    /**
     * 发布事件
     *
     * @param targetCls
     * @param code
     * @param obj
     */
    public void post(@NonNull Class targetCls, int code, @NonNull Object obj) {
        if (null == tag4Class.get(targetCls)) {
            return;
        }

        post(getTag(targetCls, code), obj);
    }

    public Observable<Object> tObservable() {
        return tObservable(Object.class);
    }

    public <T> Observable<T> tObservable(Class<T> eventType) {
        return tObservable(TAG_DEFAULT, eventType);
    }

    /**
     * 订阅事件
     *
     * @return
     */
    public <T> Observable<T> tObservable(final int code, final Class<T> eventType) {
        // Logger.d("订阅事件 tObservable code=%d", code);
        //        return ((Observable<EbkRxBusMsg>) bus.ofType(EbkRxBusMsg.class)).filter(msg -> msg.code == code).map(msg -> msg.object).cast(eventType);

        return bus
                .ofType(HRxBusMsg.class) // filter-Predicate.test: 判断接收事件类型, 这里判断只能收到与code相等的数据
                .filter(new Predicate<HRxBusMsg>() {
                    @Override
                    public boolean test(HRxBusMsg msg) throws Exception {
                        return msg.code == code;
                    }
                })
                .map(new Function<HRxBusMsg, Object>() {
                    @Override
                    public Object apply(HRxBusMsg msg) throws Exception {
                        return msg.object;
                    }
                })
                .cast(eventType);
    }

    /**
     * 添加订阅
     *
     * @param method     方法
     * @param subscriber 订阅者
     */
    private void addSubscription(@NonNull Method method, Object subscriber) {
        // 获取方法内参数
        Class[] parameterType = method.getParameterTypes();

        // 只获取第一个方法参数，否则默认为Object
        Class cla = Object.class;
        if (parameterType.length > 1) {
            cla = parameterType[0];
        }

        final HSubscribe sub = method.getAnnotation(HSubscribe.class);
        final int tag = getTag(subscriber.getClass(), sub.tag());

        // Logger.d(subscriber.getClass());
        // Logger.d("tag=%d  sub.tag=%d", tag, sub.tag());

        // 订阅事件
        Disposable disposable = tObservable(tag, cla).subscribe(o -> {
                    try {
                        method.invoke(subscriber, o);
                    } catch (IllegalAccessException e) {
                        Logger.e(e);
                    } catch (InvocationTargetException e) {
                        Logger.e(e);
                    }
                },
                e -> Logger.d("this object is not invoke"));
        putSubscriptionsData(subscriber, disposable);
    }

    /**
     * 添加订阅者到map空间来unRegister
     *
     * @param subscriber 订阅者
     * @param disposable 订阅者 Subscription
     */
    protected void putSubscriptionsData(Object subscriber, Disposable disposable) {
        CompositeDisposable subs = subscriptions.get(subscriber);
        if (subs == null) {
            subs = new CompositeDisposable();
        }
        subs.add(disposable);
        subscriptions.put(subscriber, subs);
    }

    /**
     * 解除订阅者
     *
     * @param subscriber 订阅者
     */
    public void unRegister(final @NonNull Object subscriber) {
        Flowable.just(subscriber)
                .filter(s -> null != s)
                .map(s -> subscriptions.get(s))
                .filter(subscription -> subscription != null)
                .subscribe(subscription -> {
                    if (null != subscription) {
                        subscription.dispose();
                    }
                    subscriptions.remove(subscriber);
                }, throwable -> Logger.e(throwable));
    }

}
