package com.hw.hwdroid.foundation.app

import android.content.Context
import android.view.View
import android.view.ViewGroup
import java.lang.reflect.ParameterizedType

/**
 * BaseAdapter
 * Created by ChenJ on 2017/2/16.
 */
abstract class HBaseAdapter<Bean, Holder : HWBaseViewHolder>(_Context: Context) : HWBaseAdapter<Bean>(_Context) {

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

    @Suppress("UNCHECKED_CAST")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: Holder? = convertView?.tag as Holder?
        if (convertView == null || holder == null) {
            val vh = onCreateViewHolder(getItemViewType(position), parent)
            onBindViewHolder(position, vh.itemView, getItem(position), vh)
            return vh.itemView
        }

        // 绑定数据
        onBindViewHolder(position, convertView, getItem(position), holder)
        return convertView
    }

    /**
     * Create View Holder
     * eg: new ViewHolder(inflate(R.layout.xx, parent, false))
     */
    abstract fun onCreateViewHolder(viewType: Int, parent: ViewGroup?): Holder

    /**
     * 数据绑定
     * 用于List显示
     * @param position
     * @param view
     * @param bean
     * @param holder
     */
    protected abstract fun onBindViewHolder(position: Int, view: View, bean: Bean?, holder: Holder)

}
