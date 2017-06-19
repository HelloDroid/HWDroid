package com.hw.hwdroid.foundation.app

import android.content.Context
import android.view.View
import android.view.ViewGroup
import java.lang.reflect.ParameterizedType

/**
 * Base ExpandableListAdapterEx
 *
 * Created by ChenJ on 16/8/9.
 */
abstract class HBaseExpandableListAdapterEx<Group, Child, GHolder : HWBaseViewHolder, CHolder : HWBaseViewHolder>(_Context: Context) : HWBaseExpandableListAdapterEx<Group, Child>(_Context) {

    /**
     * Group HolderView Class
     * @return
     */
    val groupHolderViewClass: Class<GHolder>
        get() {
            val cls = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[2] as Class<GHolder>
            return cls
        }

    /**
     * Child HolderView Class
     * @return
     */
    val childHolderViewClass: Class<CHolder>
        get() {
            val cls = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[3] as Class<CHolder>
            return cls
        }

    @Suppress("UNCHECKED_CAST")
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val holder: GHolder? = convertView?.tag as GHolder?
        if (convertView == null || holder == null) {
            val pair = onCreateViewGroupHolder(getGroupType(groupPosition), parent)
            onBindGroupViewHolder(groupPosition, isExpanded, pair.first, getGroup(groupPosition), pair.second)
            return pair.first
        }

        onBindGroupViewHolder(groupPosition, isExpanded, convertView, getGroup(groupPosition), holder)
        return convertView
    }

    /**
     * 初始化初始化Group View Holder
     * eg: View v = inflate(R.layout.xx, parent, false)
     *     Holder h = new ViewHolder(v)
     *     return Pair(v, h)
     */
    protected abstract fun onCreateViewGroupHolder(viewType: Int, parent: ViewGroup?): Pair<View, GHolder>

    protected abstract fun onBindGroupViewHolder(groupPosition: Int, isExpanded: Boolean, view: View, group: Group?, holder: GHolder)

    @Suppress("UNCHECKED_CAST")
    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val bean = getChild(groupPosition, childPosition)
        val holder = convertView?.tag as CHolder?

        if (convertView == null || holder == null) {
            val pair = onCreateViewChildHolder(getChildType(groupPosition, childPosition), parent)
            onBindChildViewHolder(groupPosition, childPosition, isLastChild, pair.first, bean, pair.second)
            return pair.first
        }

        onBindChildViewHolder(groupPosition, childPosition, isLastChild, convertView, bean, holder)
        return convertView
    }

    /**
     * 初始化初始化Child View Holder
     * eg: View v = inflate(R.layout.xx, parent, false)
     *     Holder h = new ViewHolder(v)
     *     return Pair(v, h)
     */
    protected abstract fun onCreateViewChildHolder(viewType: Int, parent: ViewGroup?): Pair<View, CHolder>

    protected abstract fun onBindChildViewHolder(groupPosition: Int, childPosition: Int, isLastChild: Boolean, view: View, child: Child?, holder: CHolder)

}