package com.hw.hwdroid.foundation.app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.hw.hwdroid.foundation.app.annotation.HAtomicData
import com.hw.hwdroid.foundation.app.annotation.HContentViewResEx
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.lang.reflect.ParameterizedType
import java.util.*

/**
 * Base ExpandableListAdapter
 * Created by ChenJ on 16/8/9.
 */
abstract class HBaseExpandableListAdapter<Group : HExGroup<Child>, Child, GHolder : HBaseViewHolder, CHolder : HBaseViewHolder>(_Context: Context) : BaseExpandableListAdapter() {

    val context: Context = _Context
    protected val layoutInflater: LayoutInflater
    protected var groupList: MutableList<Group> = ArrayList()

    init {
        layoutInflater = LayoutInflater.from(this.context)
    }


    /**
     * setter and getter Data
     */
    var data: MutableList<Group>
        get() {
            return groupList
        }
        set(groupData) = setData(groupData, false)

    /**
     * 设置数据
     * @param groupList
     * @param notifyDataSetChanged 更新ListView
     */
    fun setData(groupList: MutableList<Group>?, notifyDataSetChanged: Boolean = false) {
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
    @JvmOverloads fun reAddData(groupList: List<Group>?, notifyDataSetChanged: Boolean = false) {
        clear()
        addAll(groupList)

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /**
     * add data
     * @param groupList
     * @param notifyDataSetChanged 更新ListView
     */
    @JvmOverloads fun addAll(groupList: List<Group>?, notifyDataSetChanged: Boolean = false) {
        groupList?.let {
            this.groupList.addAll(groupList)

            if (notifyDataSetChanged) {
                notifyDataSetChanged()
            }
        }
    }

    /**
     * 删除数据
     * @param bean
     * @param notifyDataSetChanged 更新ListView
     * @return
     */
    @JvmOverloads fun remove(bean: Group, notifyDataSetChanged: Boolean = false): Boolean {
        if (isEmpty || !groupList.remove(bean)) {
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
    @JvmOverloads fun removeAll(groupList: Collection<Group>?, notifyDataSetChanged: Boolean = false): Boolean {
        if (isEmpty) {
            return false
        }

        groupList?.let {
            val r = this.groupList.removeAll(groupList)
            if (notifyDataSetChanged) {
                notifyDataSetChanged()
            }

            return r
        }

        return false
    }

    /**
     * clear data
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
    val children: List<List<Child>>
        get() {
            return groupList.map { it.children }
        }

    /**
     * set child data
     * @param groupPosition
     * @param children
     * @param notifyDataSetChanged 更新ListView
     */
    @JvmOverloads fun setChildren(groupPosition: Int, children: MutableList<Child>?, notifyDataSetChanged: Boolean = false) {
        val g = getGroup(groupPosition) ?: return

        g.children = children ?: ArrayList()

        if (notifyDataSetChanged) {
            notifyDataSetChanged()
        }
    }

    /**
     * add child data
     * @param groupPosition
     * @param children
     * @param notifyDataSetChanged 更新ListView
     */
    @JvmOverloads fun addAllChildren(groupPosition: Int, children: List<Child>?, notifyDataSetChanged: Boolean = false) {
        val g = getGroup(groupPosition) ?: return
        val oldChildren = g.children

        oldChildren.clear()

        children?.let {
            oldChildren.addAll(children)
            if (notifyDataSetChanged) {
                notifyDataSetChanged()
            }
        }
    }

    /**
     * add child item data
     * @param groupPosition
     * @param child
     * @param notifyDataSetChanged 更新ListView
     */
    @JvmOverloads fun addChild(groupPosition: Int, child: Child, notifyDataSetChanged: Boolean = false) {
        val g = getGroup(groupPosition) ?: return

        g.children.add(child)

        if (notifyDataSetChanged) {
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
        // super.notifyDataSetChanged()
        Observable.just(0).subscribeOn(AndroidSchedulers.mainThread()).subscribe({ super.notifyDataSetChanged() }) { onErr -> Logger.e(onErr) }
    }

    override fun notifyDataSetInvalidated() {
        // super.notifyDataSetInvalidated();
        Observable.just(1).subscribeOn(AndroidSchedulers.mainThread()).subscribe({ super.notifyDataSetInvalidated() }) { onErr -> Logger.e(onErr) }
    }

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

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view = bindGroupView(convertView)
        val holder = view.tag as GHolder?
        val bean = getGroup(groupPosition)

        // 绑定数据
        if (holder != null) {
            bindGroupData(groupPosition, isExpanded, view, parent, bean, holder)
        }

        return view
    }

    /**
     * 绑定Group View
     * @param view
     * @return
     */
    protected fun bindGroupView(view: View?): View {
        var convertView: View? = null
        if (view != null && view.tag != null) {
            convertView = view
            return convertView
        }

        // 初始化view
        try {
            if (null == view || javaClass.isAnnotationPresent(HContentViewResEx::class.java)) {
                val annotation = javaClass.getAnnotation(HContentViewResEx::class.java)
                if (annotation.groupResId != View.NO_ID) {
                    convertView = layoutInflater.inflate(annotation.groupResId, null)
                }
            }
        } catch (e: Exception) {
            Logger.e(e)
        }

        if (convertView != null) {
            val tag = initViewGroupHolder(convertView)
            convertView.tag = tag

            initGroupView(convertView, tag)
        }

        return convertView ?: View(context)
    }

    /**
     * 初始化GroupView
     * @param view
     * @param holder
     */
    protected fun initGroupView(view: View, holder: GHolder) {
    }

    /** 初始化Group View Holder  */
    protected abstract fun initViewGroupHolder(view: View): GHolder

    /**
     * Group数据绑定
     * 用于GroupList显示
     * @param groupPosition
     * @param isExpanded
     * @param view
     * @param parent
     * @param group
     * @param holder
     */
    protected abstract fun bindGroupData(groupPosition: Int, isExpanded: Boolean, view: View, parent: ViewGroup?, group: Group?, holder: GHolder)


    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        val view = bindChildView(convertView)
        val bean = getChild(groupPosition, childPosition)
        val holder = view.tag as CHolder?

        // 绑定数据
        if (holder != null) {
            bindChildData(groupPosition, childPosition, isLastChild, view, parent, bean, holder)
        }

        return view
    }


    /**
     * 绑定Child View
     * @param view
     * @return
     */
    protected fun bindChildView(view: View?): View {
        var convertView: View? = null
        if (view != null && view.tag != null) {
            convertView = view
            return convertView
        }

        // 初始化view
        try {
            if (null == view || javaClass.isAnnotationPresent(HContentViewResEx::class.java)) {
                val annotation = javaClass.getAnnotation(HContentViewResEx::class.java)
                if (annotation.childResId != View.NO_ID) {
                    convertView = layoutInflater.inflate(annotation.childResId, null)
                }
            }
        } catch (e: Exception) {
            Logger.e(e)
        }

        if (convertView != null) {
            val tag = initViewChildHolder(convertView)
            convertView.tag = tag

            initChildView(convertView, tag)
        }

        return convertView ?: View(context)
    }

    /**
     * 初始化ChildView
     * @param view
     * @param holder
     */
    protected fun initChildView(view: View, holder: CHolder) {
    }

    /** 初始化Child View Holder  */
    protected abstract fun initViewChildHolder(view: View): CHolder

    /**
     * Child数据绑定
     * 用于Child List显示
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param view
     * @param parent
     * @param child
     * @param holder
     */
    protected abstract fun bindChildData(groupPosition: Int, childPosition: Int, isLastChild: Boolean, view: View, parent: ViewGroup?, child: Child?, holder: CHolder)

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }

}