@file:Suppress("UNCHECKED_CAST")

package com.hw.hwdroid.foundation.app

import android.content.Context
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.hw.hwdroid.foundation.app.annotation.HAtomicData
import com.hw.hwdroid.foundation.app.annotation.HContentViewRes
import com.hw.hwdroid.foundation.utils.StringUtils
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.lang.reflect.ParameterizedType
import java.util.*

/**
 * BaseAdapter
 * Created by ChenJ on 2017/2/16.
 */
abstract class HBaseAdapter<Bean, Holder : HBaseViewHolder>(_Context: Context) : BaseAdapter() {

    val context: Context = _Context
    private var dataList: MutableList<Bean> = ArrayList()
    protected val layoutInflater: LayoutInflater = LayoutInflater.from(_Context)

    init {
    }

    /**
     * setter and getter Data
     */
    var data: MutableList<Bean>?
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
     * @param list
     * @param notifyDataSetChanged 更新ListView
     */
    fun setData(list: MutableList<Bean>?, notifyDataSetChanged: Boolean = false) {
        // 使用原子数据，即直接将DataList替换为List，否则清除并add
        if (javaClass.isAnnotationPresent(HAtomicData::class.java) && javaClass.getAnnotation(HAtomicData::class.java).value) {
            dataList = list ?: ArrayList()

            if (notifyDataSetChanged) {
                notifyDataSetChanged()
            }

            return
        }

        reAddData(list, notifyDataSetChanged)
    }

    /**
     * 添加数据，并清除旧数据
     *
     * @param list
     * @param notifyDataSetChanged 更新ListView
     */
    @JvmOverloads fun reAddData(list: List<Bean>?, notifyDataSetChanged: Boolean = false) {
        clear()
        addAll(list)

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /**
     * add Data
     *
     * @param list
     * @param notifyDataSetChanged 更新ListView
     */
    @JvmOverloads fun addAll(list: List<Bean>?, notifyDataSetChanged: Boolean = false) {
        if (list?.isEmpty() ?: false) {
            return
        }

        list?.let {
            dataList.addAll(list)

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
    @JvmOverloads fun remove(bean: Bean, notifyDataSetChanged: Boolean = false): Boolean {
        if (isEmpty || !dataList.remove(bean)) {
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
     * @param list
     * @param notifyDataSetChanged 更新ListView
     * @return
     */
    @JvmOverloads fun removeAll(list: Collection<Bean>?, notifyDataSetChanged: Boolean = false): Boolean {
        if (isEmpty || list?.isEmpty() ?: false) {
            return false
        }

        val r = dataList.removeAll(list!!)
        if (r && notifyDataSetChanged) {
            notifyDataSetChanged()
        }

        return r
    }

    /**
     * clear dataList
     *
     * @param notifyDataSetChanged 更新ListView
     */
    fun clear(notifyDataSetChanged: Boolean = false) {
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

    /**
     * Get String for Resource
     *
     * @param resId
     * @param formatArgs
     * @return
     */
    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String {
        try {
            return StringUtils.changeNull(context.getString(resId, *formatArgs))
        } catch (e: Exception) {
            Logger.e(e)
            return String()
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * HolderView Class
     * 注意：本方法只针对子类作用，否则异常
     * @return
     */
    //    open val holderViewClass: Class<Holder>
//        get() {
//            val cls = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<Holder>
//            return cls
//        }
    open fun getHolderViewClass(): Class<Holder>? {
        return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<Holder>?
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = bindView(convertView)
        val bean = getItem(position)
        val holder: Holder? = view.tag as Holder?

        // 绑定数据
        if (holder != null) {
            bindData(position, view, parent, bean, holder)
        }

        return view
    }

    /**
     * 绑定View
     *
     * @param view
     * @return
     */
    protected fun bindView(view: View?): View {
        var convertView: View? = null
        if (view != null && view.tag != null) {
            convertView = view
            return convertView
        }

        // 初始化view
        try {
            if (javaClass.isAnnotationPresent(HContentViewRes::class.java)) {
                val annotation = javaClass.getAnnotation(HContentViewRes::class.java)
                if (annotation.value != View.NO_ID) {
                    convertView = layoutInflater.inflate(annotation.value, null)
                }
            }
        } catch (e: Exception) {
            Logger.e(e)
        }

        if (convertView != null) {
            convertView.tag = initViewHolder(convertView)
        }

        return convertView ?: View(context)
    }

    /** 初始化View Holder  */
    abstract fun initViewHolder(view: View): Holder

    /**
     * 数据绑定
     * 用于List显示
     * @param position
     * @param view
     * @param viewGroup
     * @param bean
     * @param holder
     */
    protected abstract fun bindData(position: Int, view: View, viewGroup: ViewGroup?, bean: Bean?, holder: Holder)

}
