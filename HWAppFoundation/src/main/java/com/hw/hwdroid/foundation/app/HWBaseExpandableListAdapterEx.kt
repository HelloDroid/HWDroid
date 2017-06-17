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
import java.util.*

/**
 * Created by ChenJ on 2017/6/17.
 */
abstract class HWBaseExpandableListAdapterEx<Group, Child>(_Context: Context) : BaseExpandableListAdapter() {

    val context: Context = _Context
    val layoutInflater: LayoutInflater

    protected var groupList: MutableList<Group> = ArrayList()
    private var childList: MutableList<List<Child>> = ArrayList()

    init {
        this.layoutInflater = LayoutInflater.from(this.context)
    }

    var groupData: MutableList<Group>
        get() {
            return groupList
        }
        set(list) = setGroupData(list, false)

    /**
     * 设置数据
     * @param groupData
     * @param childData
     * @param notifyDataSetChanged 更新ListView
     */
    @JvmOverloads fun setData(groupData: MutableList<Group>, childData: MutableList<List<Child>>, notifyDataSetChanged: Boolean = false) {
        var useAtomicData = false
        try {
            // 使用原子数据，即直接将DataList替换为List，否则清除并add
            if (javaClass.isAnnotationPresent(HAtomicData::class.java)) {
                val annotation = javaClass.getAnnotation(HAtomicData::class.java)
                useAtomicData = annotation.value
            }
        } catch (e: Exception) {
            Logger.e(e)
        }

        if (useAtomicData) {
            groupList = groupData

            if (isEmpty) {
                clearChild()
            } else {
                childList = childData
            }

            if (notifyDataSetChanged) {
                notifyDataSetChanged()
            }
            return
        }

        clearGroup()
        reAddGroupData(groupData)

        clearChild()
        reAddChildData(childData)

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /**
     * set Group Data
     * @param groupList
     * @param notifyDataSetChanged  更新ListView
     */
    fun setGroupData(groupList: MutableList<Group>?, notifyDataSetChanged: Boolean) {
        // 使用原子数据，即直接将DataList替换为List，否则清除并add
        if (javaClass.isAnnotationPresent(HAtomicData::class.java) && javaClass.getAnnotation(HAtomicData::class.java).value) {
            val annotation = javaClass.getAnnotation(HAtomicData::class.java)
            this.groupList = groupList ?: ArrayList()

            if (notifyDataSetChanged) {
                notifyDataSetChanged()
            }

            return
        }

        reAddGroupData(groupList)

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /**
     * 添加Group数据，并清除旧数据
     * @param groupList
     * @param notifyDataSetChanged      更新ListView
     */
    @JvmOverloads fun reAddGroupData(groupList: List<Group>?, notifyDataSetChanged: Boolean = false) {
        clearGroup()
        addAllGroup(groupList)

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /**
     * add Group dataList
     * @param groupList
     * @param notifyDataSetChanged      更新ListView
     */
    @JvmOverloads fun addAllGroup(groupList: List<Group>?, notifyDataSetChanged: Boolean = false) {
        if (groupList == null || groupList.isEmpty()) {
            return
        }

        this.groupList.addAll(groupList)
        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /**
     * 删除Group数据
     * @param group
     * @param notifyDataSetChanged      更新ListView
     */
    @JvmOverloads fun removeGroup(group: Group?, notifyDataSetChanged: Boolean = false): Boolean {
        if (group == null || !groupList.remove(group)) {
            return false
        }

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }

        return true
    }

    /**
     * 删除Group数据
     * @param groupList
     * @param notifyDataSetChanged      更新ListView
     */
    @JvmOverloads fun removeAllGroup(groupList: Collection<Group>?, notifyDataSetChanged: Boolean = false): Boolean {
        if (groupList == null || groupList.isEmpty() || !this.groupList.removeAll(groupList)) {
            return false
        }

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }

        return true
    }

    /**
     * clear Group dataList
     * @param notifyDataSetChanged      更新ListView
     */
    @JvmOverloads fun clearGroup(notifyDataSetChanged: Boolean = false) {
        if (!groupList.isEmpty()) {
            groupList.clear()

            if (notifyDataSetChanged) {
                notifyDataSetChanged()
            }
        }
    }

    /**
     * set Child Data
     */
    var childData: MutableList<List<Child>>
        get() {
            return childList
        }
        set(childList) = setChildData(childList, false)

    /**
     * Child List Item
     * @param location
     * @return
     */
    fun getChildDataItem(location: Int): List<Child> {
        return childData[location] ?: ArrayList<Child>()
    }

    /**
     * set Child Data
     * @param childList
     * @param notifyDataSetChanged      更新ListView
     */
    fun setChildData(childList: MutableList<List<Child>>?, notifyDataSetChanged: Boolean = false) {
        // 使用原子数据，即直接将DataList替换为List，否则清除并add
        if (javaClass.isAnnotationPresent(HAtomicData::class.java) && javaClass.getAnnotation(HAtomicData::class.java).value) {
            this.childList = childList ?: ArrayList()

            if (notifyDataSetChanged) {
                notifyDataSetChanged()
            }

            return
        }

        reAddChildData(childList)

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /**
     * 添加Child数据，并清除旧数据
     * @param childList
     * @param notifyDataSetChanged      更新ListView
     */
    @JvmOverloads fun reAddChildData(childList: List<List<Child>>?, notifyDataSetChanged: Boolean = false) {
        clearChild()
        addAllChild(childList)

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /**
     * add child dataList
     * @param childList
     * @param notifyDataSetChanged      更新ListView
     */
    @JvmOverloads fun addAllChild(childList: List<List<Child>>?, notifyDataSetChanged: Boolean = false) {
        if (childList == null || childList.isEmpty()) {
            return
        }

        this.childList.addAll(childList)
        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /**
     * add child item dataList
     * @param notifyDataSetChanged      更新ListView
     */
    @JvmOverloads fun addChild(child: List<Child>?, notifyDataSetChanged: Boolean = false) {
        if (child == null || child.isEmpty()) {
            return
        }

        this.childList.add(child)
        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /**
     * 删除Child数据
     * @param child
     * @param notifyDataSetChanged      更新ListView
     */
    @JvmOverloads fun removeChild(child: List<Child>?, notifyDataSetChanged: Boolean = false): Boolean {
        if (!this.childList.remove(child)) {
            return false
        }

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }

        return true
    }

    /**
     * 删除Child数据
     * @param childList
     * @param notifyDataSetChanged 更新ListView
     * @return
     */
    @JvmOverloads fun removeAllChild(childList: MutableList<List<Child>>, notifyDataSetChanged: Boolean = false): Boolean {
        val r = this.childList.removeAll(childList)
        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }

        return r
    }

    /**
     * clear Child dataList
     * @param notifyDataSetChanged 更新ListView
     */
    @JvmOverloads fun clearChild(notifyDataSetChanged: Boolean = false) {
        if (!childList.isEmpty()) {
            childList.clear()
            if (notifyDataSetChanged) {
                notifyDataSetChanged()
            }
        }
    }

    override fun isEmpty(): Boolean {
        return groupData.size <= 0
    }

    override fun getGroupCount(): Int {
        return groupData.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        try {
            return childData[groupPosition].size
        } catch (e: Exception) {
            return 0
        }
    }

    override fun getGroup(groupPosition: Int): Group? {
        try {
            return groupData[groupPosition]
        } catch (e: Exception) {
            Logger.e(e)
            return null
        }
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Child? {
        try {
            return childData[groupPosition][childPosition]
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
        Observable.just(0).subscribeOn(AndroidSchedulers.mainThread()).subscribe({ super.notifyDataSetChanged() }) { err -> Logger.e(err) }
    }

    override fun notifyDataSetInvalidated() {
        Observable.just(1).subscribeOn(AndroidSchedulers.mainThread()).subscribe({ super.notifyDataSetInvalidated() }) { err -> Logger.e(err) }
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }

    override abstract fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View
    override abstract fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View

}