package com.hw.hwdroid.foundation.app

import android.content.Context
import android.view.View
import android.view.ViewGroup
import java.lang.reflect.ParameterizedType

/**
 * Base ExpandableListAdapter
 * Created by ChenJ on 16/8/9.
 */
abstract class HBaseExpandableListAdapter<Group : HWExGroup<Child>, Child, GHolder : HWBaseViewHolder, CHolder : HWBaseViewHolder>(_Context: Context) : HWBaseExpandableListAdapter<Group, Child>(_Context) {

    /**
     * Group HolderView Class
     * 注意：本方法只针对子类作用，否则异常
     * @return
     */
    //    val groupHolderViewClass: Class<GHolder>
//        get() {
//            val cls = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[2] as Class<GHolder>
//            return cls
//        }
    open fun getGroupHolderViewClass(): Class<GHolder>? {
        return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<GHolder>?
    }

    /**
     * Child HolderView Class
     * @return
     */
    //    val childHolderViewClass: Class<CHolder>
//        get() {
//            val cls = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[3] as Class<CHolder>
//            return cls
//        }
    open fun getChildHolderViewClass(): Class<CHolder>? {
        return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<CHolder>?
    }

    @Suppress("UNCHECKED_CAST")
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val holder: GHolder? = convertView?.tag as GHolder?
        if (convertView == null || holder == null) {
            val vh = onCreateViewGroupHolder(getGroupType(groupPosition), parent)
            onBindGroupViewHolder(groupPosition, isExpanded, vh.itemView, getGroup(groupPosition), vh)
            return vh.itemView
        }

        onBindGroupViewHolder(groupPosition, isExpanded, convertView, getGroup(groupPosition), holder)
        return convertView
    }

    /**
     * 初始化Group View Holder
     * eg: new ViewHolder(inflate(R.layout.xx, parent, false))
     */
    protected abstract fun onCreateViewGroupHolder(viewType: Int, parent: ViewGroup?): GHolder

    protected abstract fun onBindGroupViewHolder(groupPosition: Int, isExpanded: Boolean, view: View, group: Group?, holder: GHolder)

    @Suppress("UNCHECKED_CAST")
    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val bean = getChild(groupPosition, childPosition)
        val holder = convertView?.tag as CHolder?

        if (convertView == null || holder == null) {
            val vh = onCreateViewChildHolder(getChildType(groupPosition, childPosition), parent)
            onBindChildViewHolder(groupPosition, childPosition, isLastChild, vh.itemView, bean, vh)
            return vh.itemView
        }

        onBindChildViewHolder(groupPosition, childPosition, isLastChild, convertView, bean, holder)
        return convertView
    }

    /**
     * 初始化Child View Holder
     * eg: new ViewHolder(inflate(R.layout.xx, parent, false))
     */
    protected abstract fun onCreateViewChildHolder(viewType: Int, parent: ViewGroup?): CHolder

    protected abstract fun onBindChildViewHolder(groupPosition: Int, childPosition: Int, isLastChild: Boolean, view: View, child: Child?, holder: CHolder)

}