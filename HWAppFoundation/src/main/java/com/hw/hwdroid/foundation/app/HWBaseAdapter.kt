package com.hw.hwdroid.foundation.app

import android.content.Context
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.hw.hwdroid.foundation.app.annotation.HAtomicData
import com.hw.hwdroid.foundation.utils.StringUtils
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

/**
 * BaseAdapter
 */
abstract class HWBaseAdapter<Bean>(_Context: Context) : BaseAdapter() {

    val context: Context = _Context
    private var dataList: MutableList<Bean?> = ArrayList()
    protected val layoutInflater: LayoutInflater = LayoutInflater.from(_Context)

    init {
    }

    /**
     * setter and getter Data
     */
    var data: MutableList<Bean?>
        set(value) = setData(value, false)
        get() = dataList
//        set(value) {
//            // 使用原子数据，即直接将DataList替换为List，否则清除并add
//            if (javaClass.isAnnotationPresent(HAtomicData::class.java) && javaClass.getAnnotation(HAtomicData::class.java).value) {
//                dataList = value ?: ArrayList()
//            } else {
//                reAddData(value)
//            }
//        }

    /**
     * set Data
     *
     * @param data
     * @param notifyDataSetChanged 更新ListView
     */
    fun setData(data: MutableList<Bean?>?, notifyDataSetChanged: Boolean = false) {
        // 使用原子数据，即直接将DataList替换为List，否则清除并add
        if (javaClass.isAnnotationPresent(HAtomicData::class.java) && javaClass.getAnnotation(HAtomicData::class.java).value) {
            dataList = data ?: ArrayList()

            if (notifyDataSetChanged) {
                notifyDataSetChanged()
            }

            return
        }

        reAddData(data, notifyDataSetChanged)
    }

    /**
     * 添加数据，并清除旧数据
     *
     * @param data
     * @param notifyDataSetChanged 更新ListView
     */
    @JvmOverloads fun reAddData(data: List<Bean?>?, notifyDataSetChanged: Boolean = false) {
        clear()
        addAll(data, notifyDataSetChanged)
    }

    /**
     * add Data
     *
     * @param data
     * @param notifyDataSetChanged 更新ListView
     */
    @JvmOverloads fun addAll(data: List<Bean?>?, notifyDataSetChanged: Boolean = false) {
        if (data?.isEmpty() ?: false) {
            return
        }

        data?.let {
            dataList.addAll(data)

            if (notifyDataSetChanged) {
                notifyDataSetChanged()
            }
        }
    }

    /**
     * 删除数据
     *
     * @param bean
     * @param notifyDataSetChanged
     * @return
     */
    @JvmOverloads fun remove(bean: Bean?, notifyDataSetChanged: Boolean = false): Boolean {
        if (bean == null || isEmpty || !dataList.remove(bean)) {
            return false
        }

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }

        return true
    }

    /**
     * 删除数据
     *
     * @param data
     * @param notifyDataSetChanged 更新ListView
     * @return
     */
    @JvmOverloads fun removeAll(data: Collection<Bean>?, notifyDataSetChanged: Boolean = false): Boolean {
        if (isEmpty || data?.isEmpty() ?: false) {
            return false
        }

        if (data != null && dataList.removeAll(data)) {
            if (notifyDataSetChanged) {
                notifyDataSetChanged()
            }

            return true
        }

        return false
    }

    /**
     * clear dataList
     *
     * @param notifyDataSetChanged 更新ListView
     */
    fun clear(notifyDataSetChanged: Boolean = false) {
        if (dataList.isEmpty()) {
            return
        }

        dataList.clear()

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /**
     * 是否为空
     * @return
     */
    override fun isEmpty(): Boolean {
        return count <= 0
    }

    override fun getCount(): Int {
        return dataList.size
    }

    /**
     * 根据位置获取
     *
     * @param position
     * @return
     */
    override fun getItem(position: Int): Bean? {
        try {
            return dataList[position]
        } catch (e: Exception) {
            Logger.e(e)
            return null
        }
    }

    override fun notifyDataSetChanged() {
        Observable.just(0).subscribeOn(AndroidSchedulers.mainThread()).subscribe({ super.notifyDataSetChanged() }) { onError -> Logger.e(onError) }
    }

    override fun notifyDataSetInvalidated() {
        Observable.just(1).subscribeOn(AndroidSchedulers.mainThread()).subscribe({ super.notifyDataSetInvalidated() }) { onError -> Logger.e(onError) }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getViewTypeCount(): Int {
        return super.getViewTypeCount()
    }

    override abstract fun getView(position: Int, convertView: View?, parent: ViewGroup?): View

    /**
     * Get String for Resource
     *
     * @param resId
     * @param formatArgs
     * @return
     */
    final fun getString(@StringRes resId: Int, vararg formatArgs: Any): String {
        try {
            return StringUtils.changeNull(context.getString(resId, *formatArgs))
        } catch (e: Exception) {
            return String()
        }
    }

}
