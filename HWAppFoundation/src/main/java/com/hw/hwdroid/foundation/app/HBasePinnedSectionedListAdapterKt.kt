package com.hw.hwdroid.foundation.app

import android.content.Context
import android.view.View
import android.view.ViewGroup
import java.lang.reflect.ParameterizedType

/**
 * Base PinnedSectionedListAdapterKt
 * Created by ChenJ on 16/8/26.
 */
abstract class HBasePinnedSectionedListAdapterKt<Section : HWExGroup<Bean>, Bean, SectionHeaderHolderView : HWBaseViewHolder, HolderView : HWBaseViewHolder>(_Context: Context) : HWBasePinnedSectionedListAdapter<Section, Bean>(_Context) {

    /**
     * Section HolderView Class
     * @return
     */
    val sectionHeaderHolderViewClass: Class<SectionHeaderHolderView>
        get() {
            val cls = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[2] as Class<SectionHeaderHolderView>
            return cls
        }

    /**
     * Item HolderView Class
     * @return
     */
    val itemHolderViewClass: Class<HolderView>
        get() {
            val cls = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[3] as Class<HolderView>
            return cls
        }

    @Suppress("UNCHECKED_CAST")
    override fun getSectionHeaderView(section: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: SectionHeaderHolderView? = convertView?.tag as SectionHeaderHolderView?
        if (convertView == null || holder == null) {
            val vh = onCreateSectionViewHolder(getSectionHeaderViewType(section), parent)
            onBindSectionViewHolder(section, vh.itemView, getSectionItem(section), vh)
            return vh.itemView
        }

        onBindSectionViewHolder(section, convertView, getSectionItem(section), holder)
        return convertView
    }

    abstract fun onCreateSectionViewHolder(viewType: Int, parent: ViewGroup?): SectionHeaderHolderView
    abstract fun onBindSectionViewHolder(section: Int, view: View, bean: Section?, holder: SectionHeaderHolderView)

    abstract fun onCreateItemViewHolder(viewType: Int, parent: ViewGroup?): HolderView
    abstract fun onBindItemViewHolder(section: Int, positionInSection: Int, view: View, bean: Bean?, holder: HolderView)

    @Suppress("UNCHECKED_CAST")
    override fun getItemView(section: Int, positionInSection: Int, convertView: View?, parent: ViewGroup?): View {
        val holderView = convertView?.tag as HolderView?
        if (convertView == null || holderView == null) {
            val vh = onCreateItemViewHolder(getItemViewType(section, positionInSection), parent)
            onBindItemViewHolder(section, positionInSection, vh.itemView, getItem(section, positionInSection), vh)
            return vh.itemView
        }

        onBindItemViewHolder(section, positionInSection, convertView, getItem(section, positionInSection), holderView)
        return convertView
    }

}