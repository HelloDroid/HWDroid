package common.android.foundation.app

import android.app.Activity
import com.annimon.stream.Stream
import com.orhanobut.logger.Logger
import java.util.*

/**
 * Activity 管理栈
 *
 *
 * Created by ChenJ on 2017/4/21.
 */

object HActivityStack {

    private val stack = Stack<Activity>()

    /**
     * 压入堆栈顶部
     *
     * @param activity
     */
    fun push(activity: Activity) {
        synchronized(stack) {
            stack.push(activity)
        }
    }

    /**
     * 获取当前Activity(最后一个入栈的)
     */
    fun curr(): Activity? {
        synchronized(stack) {
            try {
                return stack.lastElement()
            } catch (ex: Exception) {
                return null
            }
        }
    }

    /**
     * 移除堆栈顶部的Activity
     * 出栈，即finish
     */
    fun pop() {
        synchronized(stack) {
            if (stack.empty()) {
                return
            }

            var activity: Activity? = stack.peek()

            if (activity != null) {
                activity = stack.pop()
                if (null != activity && !activity.isFinishing) {
                    activity.finish()
                }
            }
        }
    }

    fun pop(cls: Class<*>) {
        pop(get(cls))
    }

    /**
     * 移除堆栈中的Activity
     *
     * @param activity
     * @param finish        Activity finish
     */
    @JvmOverloads fun pop(activity: Activity?, finish: Boolean = true) {
        synchronized(stack) {
            activity?.let {
                if (finish) {
                    activity.finish()
                }

                if (!stack.empty() && stack.contains(activity)) {
                    stack.remove(activity)
                }
            }
        }
    }

    /**
     * 移除栈
     *
     * @param activity
     */
    fun remove(activity: Activity) {
        synchronized(stack) {
            try {
                stack.remove(activity)
            } catch (e: Exception) {
                Logger.e(e)
            }
        }
    }

    /**
     * 结束指定Activity
     *
     * @param activity
     */
    @JvmOverloads fun finish(activity: Activity? = curr()) {
        synchronized(stack) {
            stack.remove(activity)

            activity?.let {
                if (!activity.isFinishing) {
                    activity.finish()
                }
            }
        }
    }

    /**
     * 移除堆栈中的Activit
     *
     * @param activitys
     */
    fun popAll(activitys: List<Activity>?) {
        synchronized(stack) {
            activitys?.let {
                if (activitys.isEmpty()) return

                Stream.of(activitys).filter { a -> a != null }.forEach { a -> pop(a, true) }
            }
        }
    }

    /**
     * 移除堆栈中指定位置的Activity
     *
     * @param index
     */
    fun pop(index: Int) {
        synchronized(stack) {
            if (index < 0) {
                return
            }

            if (!stack.empty() && index < stack.size) {
                val activity = stack[index]
                if (stack.contains(activity)) {
                    pop(activity)
                }
            }
        }
    }

    /**
     * 返回指定位置的Activity
     * @param index
     * @return
     */
    operator fun get(index: Int): Activity? {
        synchronized(stack) {
            if (index < 0) {
                return null
            }

            if (!stack.empty() && index < stack.size) {
                return stack[index]
            }

            return null
        }
    }

    /**
     * 返回Class对应的Activity对象
     *
     * @param cls
     * @return
     */
    operator fun get(cls: Class<*>): Activity? {
        synchronized(stack) {
            val optional = Stream.of(stack).filter { a -> a != null && a.javaClass == cls }.findFirst()
            return if (null != optional && optional.isPresent) optional.get() else null
        }
    }

    /**
     * 返回Class对应的Activity的所有对象
     *
     * @param cls
     * @return
     */
    fun getList(cls: Class<*>): List<Activity> {
        synchronized(stack) {
            return Stream.of(stack).filter { a -> a != null && a.javaClass == cls }.toList()
        }
    }

    /**
     * 返回前一个 Activity
     *
     * @return
     */
    fun pre(): Activity? {
        synchronized(stack) {
            val index = currIndex()
            return get(index - 1)
        }
    }

    /**
     * 当前Activity索引位置
     * @return
     */
    fun currIndex(): Int {
        synchronized(stack) {
            val activity = curr() ?: return -1

            if (!stack.empty() && stack.contains(activity)) {
                return stack.indexOf(activity)
            }

            return -1
        }
    }

    /**
     * Class对应第一个Activity对象的位置索引
     *
     * @param cls
     * @return
     */
    fun index(cls: Class<*>): Int {
        synchronized(stack) {
            val activity = get(cls) ?: return -1

            if (!stack.empty() && stack.contains(activity)) {
                return stack.indexOf(activity)
            }

            return -1
        }
    }

    /**
     * 移除Activity至某一个Activity为止
     * @param cls first-activity
     */
    fun popAllExceptOne(cls: Class<*>) {
        synchronized(stack) {
            while (true) {
                val activity = curr() ?: break

                if (activity.javaClass == cls) {
                    break
                }

                pop(activity)
            }
        }
    }

    /**
     * 移除栈中的所有Activity
     */
    fun popAll() {
        synchronized(stack) {
            if (stack.isEmpty()) {
                return
            }

            stack.indices
                    .reversed()
                    .map { stack[it] }
                    .forEach { it?.finish() }

            stack.clear()
        }
    }

//    /**
//     * 获取所有的acticvity
//     *
//     * @return
//     */
//    val all: ArrayList<Activity>
//        get() = synchronized(stack) {
//            val activityList = ArrayList<Activity>()
//            Stream.of(stack).filter { activity -> activity != null }.forEach { activityList.add(it) }
//            return activityList
//        }

    /**
     * 获取所有的acticvity
     */
    fun all(): ArrayList<Activity> = synchronized(stack) {
        val activityList = ArrayList<Activity>()
        Stream.of(stack).filter { activity -> activity != null }.forEach { activityList.add(it) }
        return activityList
    }

    fun size(): Int = synchronized(stack) {
        return stack.size
    }

    /**
     * 栈中是否存在Calss对应的对象
     * @param cls
     * @return
     */
    fun exist(cls: Class<*>): Boolean {
        synchronized(stack) {
            return Stream.of(stack).filter { a -> a != null && cls == a.javaClass }.count() > 0
        }
    }

    /**
     * 栈中是存在Calss对应的对象数
     * @param cls
     * @return
     */
    fun existCount(cls: Class<*>): Int {
        synchronized(stack) {
            return Stream.of(stack).filter { a -> a != null && cls == a.javaClass }.count().toInt()
        }
    }

}