package com.hw.hwdroid.foundation.app;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hw.hwdroid.foundation.app.annotation.HAtomicData;
import com.hw.hwdroid.foundation.app.annotation.HContentViewRes;
import com.hw.hwdroid.foundation.utils.StringUtils;
import com.orhanobut.logger.Logger;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by ChenJ on 2017/2/16.
 */

public abstract class HBaseAdapter<Bean, Holder extends HBaseViewHolder> extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    protected List<Bean> mDataList = new ArrayList<>();

    public HBaseAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    /**
     * get Context
     *
     * @return
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 获取所有数据
     *
     * @return
     */
    public List<Bean> getDataList() {
        if (null == mDataList) {
            return new ArrayList<>();
        }

        return mDataList;
    }

    /**
     * set Data
     *
     * @param list
     */
    public void setData(List<Bean> list) {
        setData(list, false);
    }

    /**
     * set Data
     *
     * @param list
     * @param notifyDataSetChanged 更新ListView
     */
    public void setData(List<Bean> list, boolean notifyDataSetChanged) {
        boolean atomicData = false;
        // 使用原子数据，即直接将DataList替换为List，否则清除并add
        if (getClass().isAnnotationPresent(HAtomicData.class)) {
            HAtomicData annotation = getClass().getAnnotation(HAtomicData.class);
            if (annotation.value()) {
                atomicData = true;
            }
        }

        if (atomicData) {
            mDataList = list;
        } else {
            reAddData(list);
        }

        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * 添加数据，并清除旧数据
     *
     * @param list
     */
    public void reAddData(List<Bean> list) {
        reAddData(list, false);
    }

    /**
     * 添加数据，并清除旧数据
     *
     * @param list
     * @param notifyDataSetChanged 更新ListView
     */
    public void reAddData(List<Bean> list, boolean notifyDataSetChanged) {
        clear();
        addAll(list);

        //        Flowable.fromIterable(list).filter(new Predicate<Bean>() {
        //            @Override
        //            public boolean test(Bean bean) throws Exception {
        //                return bean != null;
        //            }
        //        }).subscribe(new Consumer<Bean>() {
        //            @Override
        //            public void accept(Bean bean) throws Exception {
        //
        //            }
        //        }, new Consumer<Throwable>() {
        //            @Override
        //            public void accept(Throwable throwable) throws Exception {
        //
        //            }
        //        }, new Action() {
        //            @Override
        //            public void run() throws Exception {
        //
        //            }
        //        });

        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * add data
     *
     * @param list
     */
    public void addAll(List<Bean> list) {
        addAll(list, false);
    }

    /**
     * add Data
     *
     * @param list
     * @param notifyDataSetChanged 更新ListView
     */
    public void addAll(List<Bean> list, boolean notifyDataSetChanged) {
        if (null == list || list.isEmpty()) {
            return;
        }

        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(list);

        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * 删除数据
     *
     * @param bean 更新ListView
     * @return
     */
    public boolean remove(Bean bean) {
        return remove(bean, false);
    }

    /**
     * 删除数据
     *
     * @param bean
     * @param notifyDataSetChanged
     * @return
     */
    public boolean remove(Bean bean, boolean notifyDataSetChanged) {
        if (mDataList == null || mDataList.isEmpty()) {
            return false;
        }

        final boolean r = mDataList.remove(bean);

        if (r && notifyDataSetChanged) {
            notifyDataSetChanged();
        }

        return r;
    }

    /**
     * 删除数据
     *
     * @param list
     * @return
     */
    public boolean removeAll(Collection<Bean> list) {
        return removeAll(list, false);
    }

    /**
     * 删除数据
     *
     * @param list
     * @param notifyDataSetChanged 更新ListView
     * @return
     */
    public boolean removeAll(Collection<Bean> list, boolean notifyDataSetChanged) {
        if (mDataList == null || mDataList.isEmpty()) {
            return false;
        }

        final boolean r = mDataList.removeAll(list);
        if (r && notifyDataSetChanged) {
            notifyDataSetChanged();
        }

        return r;
    }

    /**
     * clear data
     */
    public void clear() {
        clear(false);
    }

    /**
     * clear data
     *
     * @param notifyDataSetChanged 更新ListView
     */
    public void clear(boolean notifyDataSetChanged) {
        if (mDataList == null) {
            return;
        }

        if (!mDataList.isEmpty()) {
            mDataList.clear();

            if (notifyDataSetChanged) {
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 是否为空
     *
     * @return
     */
    @Override
    public boolean isEmpty() {
        return getCount() <= 0;
    }

    @Override
    public int getCount() {
        return getDataList().size();
    }

    /**
     * 根据位置获取
     *
     * @param position
     * @return
     */
    @Override
    public Bean getItem(int position) {
        try {
            return getDataList().get(position);
        } catch (Exception e) {
            Logger.e(e);
            return null;
        }
    }

    @Override
    public void notifyDataSetChanged() {
        // super.notifyDataSetChanged()
        Observable.just(0).subscribeOn(AndroidSchedulers.mainThread()).subscribe(number -> super.notifyDataSetChanged(), onError -> Logger.e(onError));
    }

    @Override
    public void notifyDataSetInvalidated() {
        // super.notifyDataSetInvalidated()
        Observable.just(1).subscribeOn(AndroidSchedulers.mainThread()).subscribe(number -> super.notifyDataSetInvalidated(), onError -> Logger.e(onError));
    }

    /**
     * Get String for Resource
     *
     * @param resId
     * @param formatArgs
     * @return
     */
    public String getString(@StringRes int resId, Object... formatArgs) {
        if (mContext == null) {
            return "";
        }

        try {
            return StringUtils.changeNull(mContext.getString(resId, formatArgs));
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * HolderView Class
     * 注意：本方法只针对子类作用，否则异常
     *
     * @return
     */
    public Class<Holder> getHolderViewClass() {
        Class<Holder> cls = (Class<Holder>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        return cls;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = bindView(convertView);

        final Bean bean = getItem(position);

        if (bean == null) {
            return convertView;
        }

        final Holder tag = (Holder) convertView.getTag();

        // 绑定数据
        if (null != convertView && null != tag && null != bean) {
            bindData(position, convertView, parent, bean, tag);
        }

        return convertView;
    }

    /**
     * 绑定View
     *
     * @param view
     * @return
     */
    protected View bindView(View view) {
        if (view != null && view.getTag() != null) {
            return view;
        }

        // 初始化view
        try {
            if (null == view || getClass().isAnnotationPresent(HContentViewRes.class)) {
                HContentViewRes annotation = getClass().getAnnotation(HContentViewRes.class);
                if (annotation.value() != View.NO_ID) {
                    view = mLayoutInflater.inflate(annotation.value(), null);
                }
            }
        } catch (Exception e) {
            Logger.e(e);
            return view;
        }

        Holder tag = initViewHolder(view);
        view.setTag(tag);

        return view;
    }

    /** 初始化View Holder */
    protected abstract Holder initViewHolder(View view);

    /**
     * 数据绑定
     * 用于List显示
     *
     * @param position
     * @param view
     * @param viewGroup
     * @param bean
     * @param holder
     */
    protected abstract void bindData(int position, View view, ViewGroup viewGroup, Bean bean, Holder holder);


}
