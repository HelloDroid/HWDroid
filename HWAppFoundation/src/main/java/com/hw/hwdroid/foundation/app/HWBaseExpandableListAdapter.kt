package com.hw.hwdroid.foundation.app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.hw.hwdroid.foundation.app.annotation.HAtomicData
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * BaseExpandableListAdapter
 *
 * Created by ChenJ on 2017/6/17.
 */
abstract class HWBaseExpandableListAdapter<Group : HWExGroup<Child>, Child>(_Context: Context) : BaseExpandableListAdapter() {

    val context: Context = _Context
    protected val layoutInflater: LayoutInflater
    protected var groupList: MutableList<Group?> = ArrayList()

    init {
        layoutInflater = LayoutInflater.from(this.context)
    }


    /**
     * setter and getter Data
     */
    var data: MutableList<Group?>
        get() {
            return groupList
        }
        set(groupData) = setData(groupData, false)

    /**
     * 设置数据
     * @param groupList
     * @param notifyDataSetChanged 更新ListView
     */
    fun setData(groupList: MutableList<Group?>?, notifyDataSetChanged: Boolean = false) {
        // 使用原子数据，即直接将DataList替换为List，否则清除并add
        if (javaClass.isAnnotationPresent(HAtomicData::class.java) && javaClass.getAnnotation(HAtomicData::class.java).value) {
            this.groupList = groupList ?: ArrayList()

            if (notifyDataSetChanged) {
                notifyDataSetChanged()
            }

            return
        }

        reAddData(groupList, notifyDataSetChanged)
    }

    /**
     * 添加数据，并清除旧数据
     * @param groupList
     * @param notifyDataSetChanged 更新ListView
     */
    @JvmOverloads fun reAddData(groupList: List<Group?>?, notifyDataSetChanged: Boolean = false) {
        clear()
        addAll(groupList, notifyDataSetChanged)
    }

    /**
     * add dataList
     * @param groupList
     * @param notifyDataSetChanged 更新ListView
     */
    @JvmOverloads fun addAll(groupList: List<Group?>?, notifyDataSetChanged: Boolean = false) {
        if (groupList == null || groupList.isEmpty()) {
            return
        }

        this.groupList.addAll(groupList)

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /**
     * 删除数据
     * @param bean
     * @param notifyDataSetChanged 更新ListView
     * @return
     */
    @JvmOverloads fun remove(bean: Group?, notifyDataSetChanged: Boolean = false): Boolean {
        if (bean == null || isEmpty || !groupList.remove(bean)) {
            return false
        }

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }

        return true
    }

    /**
     * 删除数据
     * @param groupList
     * @param notifyDataSetChanged 更新ListView
     * @return
     */
    @JvmOverloads fun removeAll(groupList: Collection<Group?>?, notifyDataSetChanged: Boolean = false): Boolean {
        if (groupList == null || groupList.isEmpty() || isEmpty) {
            return false
        }

        if (this.groupList.removeAll(groupList)) {
            if (notifyDataSetChanged) {
                notifyDataSetChanged()
            }

            return true
        }

        return false
    }

    /**
     * clear dataList
     * @param notifyDataSetChanged 更新ListView
     */
    @JvmOverloads fun clear(notifyDataSetChanged: Boolean = false) {
        if (isEmpty) {
            return
        }

        groupList.clear()

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /**
     * Child Data List
     * @return
     */
    val children: List<List<Child?>>
        get() {
//            val arr: MutableList<MutableList<Child?>> = ArrayList()
//            groupList.forEach({ value -> arr.add(value?.children ?: ArrayList()) })
            return groupList.map { it?.children ?: ArrayList() }
        }

    /**
     * set child dataList
     * @param groupPosition
     * @param children
     * @param notifyDataSetChanged 更新ListView
     */
    @JvmOverloads fun setChildren(groupPosition: Int, children: MutableList<Child?>?, notifyDataSetChanged: Boolean = false) {
        val group = getGroup(groupPosition) ?: return
        group.children = children ?: ArrayList()

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /**
     * add child dataList
     * @param groupPosition
     * @param children
     * @param notifyDataSetChanged 更新ListView
     */
    @JvmOverloads fun addAllChildren(groupPosition: Int, children: List<Child?>?, notifyDataSetChanged: Boolean = false) {
        if (children == null || children.isEmpty()) return
        val group = getGroup(groupPosition) ?: return

        if (group.children?.addAll(children) ?: false && notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /**
     * add child item dataList
     * @param groupPosition
     * @param child
     * @param notifyDataSetChanged 更新ListView
     */
    @JvmOverloads fun addChild(groupPosition: Int, child: Child?, notifyDataSetChanged: Boolean = false) {
        val group = getGroup(groupPosition) ?: return

        if (group.children?.add(child) ?: false && notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    override fun isEmpty(): Boolean {
        return groupList.size > 0
    }

    override fun getGroupCount(): Int {
        return groupList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        try {
            return getGroup(groupPosition)?.children?.size ?: 0
        } catch (e: Exception) {
            return 0
        }
    }

    override fun getGroup(groupPosition: Int): Group? {
        try {
            return groupList[groupPosition]
        } catch (e: Exception) {
            Logger.e(e)
            return null
        }
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Child? {
        try {
            val children = getGroup(groupPosition)?.children ?: return null
            return children[childPosition]
        } catch (e: Exception) {
            return null
        }
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun notifyDataSetChanged() {
        Observable.just(0).subscribeOn(AndroidSchedulers.mainThread()).subscribe({ super.notifyDataSetChanged() }) { onErr -> Logger.e(onErr) }
    }

    override fun notifyDataSetInvalidated() {
        Observable.just(1).subscribeOn(AndroidSchedulers.mainThread()).subscribe({ super.notifyDataSetInvalidated() }) { onErr -> Logger.e(onErr) }
    }

    override fun getGroupType(groupPosition: Int): Int {
        return super.getGroupType(groupPosition)
    }

    override fun getGroupTypeCount(): Int {
        return super.getGroupTypeCount()
    }

    override fun getChildType(groupPosition: Int, childPosition: Int): Int {
        return super.getChildType(groupPosition, childPosition)
    }

    override fun getChildTypeCount(): Int {
        return super.getChildTypeCount()
    }

    override abstract fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View
    override abstract fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View


    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }

}