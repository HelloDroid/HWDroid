package com.hw.hwdroid.foundation.app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hw.hwdroid.foundation.app.annotation.HAtomicData
import com.orhanobut.logger.Logger
import common.android.ui.myxlistview.libraries.PinnedSectionedListAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by ChenJ on 2017/6/17.
 */
abstract class HWBasePinnedSectionedListAdapter<Section : HWExGroup<Bean?>, Bean>(val _Context: Context) : PinnedSectionedListAdapter() {

    val context: Context = _Context
    protected var layoutInflater: LayoutInflater
    protected var dataList: MutableList<Section?> = ArrayList()

    init {
        layoutInflater = LayoutInflater.from(context)
    }

    var data: MutableList<Section?>
        get() = dataList
        set(sectionData) = setData(sectionData, false)

    /**
     * 设置数据
     * @param sectionData
     * @param notifyDataSetChanged  更新UI
     */
    fun setData(sectionData: MutableList<Section?>?, notifyDataSetChanged: Boolean = false) {
        try {
            // 使用原子数据，即直接将DataList替换为List，否则清除并add
            if (javaClass.isAnnotationPresent(HAtomicData::class.java) && javaClass.getAnnotation(HAtomicData::class.java).value) {
                dataList = sectionData ?: ArrayList()

                if (notifyDataSetChanged) {
                    notifyDataSetChanged()
                }
                return
            }
        } catch (e: Exception) {
            Logger.e(e)
        }

        reAddData(sectionData, notifyDataSetChanged)
    }

    /**
     * 添加数据，并清除旧数据
     * @param sectionData
     * @param notifyDataSetChanged      更新UI
     */
    @JvmOverloads fun reAddData(sectionData: List<Section?>?, notifyDataSetChanged: Boolean = false) {
        clear()
        addAll(sectionData, notifyDataSetChanged)
    }

    /**
     * add dataList
     * @param sectionData
     * @param notifyDataSetChanged      更新UI
     */
    @JvmOverloads fun addAll(sectionData: List<Section?>?, notifyDataSetChanged: Boolean = false) {
        if (sectionData == null || sectionData.isEmpty()) {
            return
        }

        dataList.addAll(sectionData)

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /**
     * add all Data 并检测数据
     * @param sectionData
     * @param notifyDataSetChanged  更新UI
     */
    @JvmOverloads fun addAllAndCheckItems(sectionData: List<Section?>?, notifyDataSetChanged: Boolean = false) {
        if (sectionData == null || sectionData.isEmpty()) {
            return
        }

        if (dataList.isEmpty()) {
            dataList.addAll(sectionData)
            if (notifyDataSetChanged) {
                notifyDataSetChanged()
            }
            return
        }

        // 遍历新数据
        for (section in sectionData) {
            val indexOf = dataList.indexOf(section)
            if (indexOf != -1) {
                if (section != null) {
                    if (dataList[indexOf] == null) {
                        dataList[indexOf] = section
                    } else {
                        if (dataList[indexOf]?.children == null) {
                            dataList[indexOf]?.children = section.children
                        } else if (section.children?.isNotEmpty() ?: false) {
                            dataList[indexOf]?.children?.addAll(section.children!!)
                        }
                    }
                }
            } else {
                dataList.add(section)
            }
        }

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /**
     * 删除数据
     * @param section
     * @param notifyDataSetChanged      更新UI
     * @return
     */
    @JvmOverloads fun remove(section: Section?, notifyDataSetChanged: Boolean = false): Boolean {
        if (!dataList.remove(section)) {
            return false
        }

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }

        return true
    }

    /**
     * 删除数据
     * @param sectionData
     * @param notifyDataSetChanged      更新UI
     * @return
     */
    @JvmOverloads fun removeAll(sectionData: Collection<Section?>?, notifyDataSetChanged: Boolean = false): Boolean {
        if (sectionData == null || dataList.isEmpty() || !dataList.removeAll(sectionData)) {
            return false
        }

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }

        return true
    }

    /**
     * clear dataList
     * @param notifyDataSetChanged      更新UI
     */
    @JvmOverloads fun clear(notifyDataSetChanged: Boolean = false) {
        if (!dataList.isEmpty()) {
            dataList.clear()
            if (notifyDataSetChanged) {
                notifyDataSetChanged()
            }
        }
    }

    /**
     * Item Data List
     * @return
     */
    val itemData: List<List<Bean?>?>
        get() {
            return dataList.map { it?.children ?: ArrayList() }
        }

    /**
     * set item dataList
     * @param sectionPosition
     * @param list
     * @param notifyDataSetChanged      更新UI
     */
    @JvmOverloads fun setItem(sectionPosition: Int, list: List<Bean?>?, notifyDataSetChanged: Boolean = false) {
        val g = getSectionItem(sectionPosition) ?: return

        g.children?.clear()

        if (list != null && !list.isEmpty()) {
            g.children?.addAll(list)
        }

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /**
     * add item dataList
     * @param sectionPosition
     * @param list
     * @param notifyDataSetChanged      更新UI
     */
    @JvmOverloads fun addAllItem(sectionPosition: Int, list: List<Bean?>?, notifyDataSetChanged: Boolean = false) {
        if (list?.isEmpty() ?: false) {
            return
        }

        list?.let {
            getSectionItem(sectionPosition)?.children?.addAll(list)
            if (notifyDataSetChanged) {
                notifyDataSetChanged()
            }
        }
    }

    /**
     * add item dataList
     * @param sectionPosition
     * @param item
     * @param notifyDataSetChanged      更新UI
     */
    @JvmOverloads fun addItem(sectionPosition: Int, item: Bean?, notifyDataSetChanged: Boolean = false) {
        val g = getSectionItem(sectionPosition)

        item?.let {
            g?.children?.add(item)
            if (notifyDataSetChanged) {
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemViewType(section: Int, position: Int): Int {
        return 1
    }

    override fun getItemViewTypeCount(): Int {
        return 1
    }

    override fun getSectionHeaderViewType(section: Int): Int {
        return 0
    }

    override fun getSectionHeaderViewTypeCount(): Int {
        return 1
    }

    override fun isEmpty(): Boolean {
        return super.isEmpty()
    }

    override fun getItem(section: Int, position: Int): Bean? {
        try {
            val childList = getSectionItem(section)?.children ?: return null
            return childList[position]
        } catch (e: Exception) {
            return null
        }
    }

    override fun getItemId(section: Int, position: Int): Long {
        return position.toLong()
    }

    override fun getSectionItem(section: Int): Section? {
        try {
            return data[section]
        } catch (e: Exception) {
            Logger.e(e)
            return null
        }
    }

    override fun getSectionCount(): Int {
        return data.size
    }

    override fun getCountForSection(section: Int): Int {
        try {
            return getSectionItem(section)?.children?.size ?: 0
        } catch (e: Exception) {
            return 0
        }
    }

    override fun notifyDataSetChanged() {
        Observable.just(0).subscribeOn(AndroidSchedulers.mainThread()).subscribe({ super.notifyDataSetChanged() }) { err -> Logger.e(err) }
    }

    override fun notifyDataSetInvalidated() {
        Observable.just(1).subscribeOn(AndroidSchedulers.mainThread()).subscribe({ super.notifyDataSetInvalidated() }) { e -> Logger.e(e) }
    }


    override abstract fun getSectionHeaderView(section: Int, convertView: View?, parent: ViewGroup?): View
    override abstract fun getItemView(section: Int, positionInSection: Int, convertView: View?, parent: ViewGroup?): View

}