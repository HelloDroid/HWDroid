package com.hw.hwdroid.foundation.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.hw.hwdroid.foundation.app.annotation.HAtomicData;
import com.hw.hwdroid.foundation.app.annotation.HContentViewResEx;
import com.orhanobut.logger.Logger;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by ChenJ on 16/8/9.
 */
public abstract class HBaseExpandableListAdapterEx<Group, Child, GHolder extends HBaseViewHolder, CHolder extends HBaseViewHolder>
        extends BaseExpandableListAdapter {

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    protected List<Group> mGroupList = new ArrayList<>();
    private List<List<Child>> mChildList = new ArrayList<>();

    public HBaseExpandableListAdapterEx(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    /**
     * get Context
     *
     * @return
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * Group Data List
     *
     * @return
     */
    public List<Group> getGroupData() {
        if (null == mGroupList) {
            mGroupList = new ArrayList<>();
        }

        return mGroupList;
    }

    /**
     * 设置数据
     *
     * @param groupData
     * @param childData
     */
    public void setData(List<Group> groupData, List<List<Child>> childData) {
        setData(groupData, childData, false);
    }

    /**
     * 设置数据
     *
     * @param groupData
     * @param childData
     * @param notifyDataSetChanged 更新ListView
     */
    public void setData(List<Group> groupData, List<List<Child>> childData, boolean notifyDataSetChanged) {
        boolean useAtomicData = false;
        try {
            // 使用原子数据，即直接将DataList替换为List，否则清除并add
            if (getClass().isAnnotationPresent(HAtomicData.class)) {
                HAtomicData annotation = getClass().getAnnotation(HAtomicData.class);
                useAtomicData = annotation.value();
            }
        } catch (Exception e) {
            Logger.e(e);
        }

        if (useAtomicData) {
            mGroupList = groupData;

            if (isEmpty()) {
                clearChild();
            } else {
                mChildList = childData;
            }

            if (notifyDataSetChanged) {
                notifyDataSetChanged();
            }
            return;
        }

        clearGroup();
        reAddGroupData(groupData);

        clearChild();
        reAddChildData(childData);

        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * set Group Data
     *
     * @param list
     */
    public void setGroupData(List<Group> list) {
        setGroupData(list, false);
    }

    /**
     * set Group Data
     *
     * @param list
     * @param notifyDataSetChanged 更新ListView
     */
    public void setGroupData(List<Group> list, boolean notifyDataSetChanged) {
        try {
            // 使用原子数据，即直接将DataList替换为List，否则清除并add
            if (getClass().isAnnotationPresent(HAtomicData.class)) {
                HAtomicData annotation = getClass().getAnnotation(HAtomicData.class);
                if (annotation.value()) {
                    mGroupList = list;
                    if (notifyDataSetChanged) {
                        notifyDataSetChanged();
                    }
                    return;
                }
            }
        } catch (Exception e) {
            Logger.e(e);
        }

        reAddGroupData(list);
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * 添加Group数据，并清除旧数据
     *
     * @param list
     */
    public void reAddGroupData(List<Group> list) {
        reAddGroupData(list, false);
    }

    /**
     * 添加Group数据，并清除旧数据
     *
     * @param list
     * @param notifyDataSetChanged 更新ListView
     */
    public void reAddGroupData(List<Group> list, boolean notifyDataSetChanged) {
        clearGroup();
        addAllGroup(list);

        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * add Group data
     *
     * @param list
     */
    public void addAllGroup(List<Group> list) {
        addAllGroup(list, false);
    }

    /**
     * add Group data
     *
     * @param list
     * @param notifyDataSetChanged 更新ListView
     */
    public void addAllGroup(List<Group> list, boolean notifyDataSetChanged) {
        if (null == list || list.isEmpty()) {
            return;
        }

        if (mGroupList == null) {
            mGroupList = new ArrayList<>();
        }

        this.mGroupList.addAll(list);
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * 删除Group数据
     *
     * @param group
     * @return
     */
    public boolean removeGroup(Group group) {
        return removeGroup(group, false);
    }

    /**
     * 删除Group数据
     *
     * @param group
     * @param notifyDataSetChanged 更新ListView
     * @return
     */
    public boolean removeGroup(Group group, boolean notifyDataSetChanged) {
        if (mGroupList == null || mGroupList.isEmpty()) {
            return false;
        }

        if (!mGroupList.remove(group)) {
            return false;
        }

        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }

        return true;
    }

    /**
     * 删除Group数据
     *
     * @param list
     * @return
     */
    public boolean removeAllGroup(Collection<Group> list) {
        return removeAllGroup(list, false);
    }

    /**
     * 删除Group数据
     *
     * @param list
     * @param notifyDataSetChanged 更新ListView
     * @return
     */
    public boolean removeAllGroup(Collection<Group> list, boolean notifyDataSetChanged) {
        if (mGroupList == null || mGroupList.isEmpty()) {
            return false;
        }

        final boolean r = mGroupList.removeAll(list);
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }

        return r;
    }

    /**
     * clear Group data
     */
    public void clearGroup() {
        clearGroup(false);
    }

    /**
     * clear Group data
     *
     * @param notifyDataSetChanged 更新ListView
     */
    public void clearGroup(boolean notifyDataSetChanged) {
        if (mGroupList == null) {
            return;
        }

        if (!mGroupList.isEmpty()) {
            mGroupList.clear();
            if (notifyDataSetChanged) {
                notifyDataSetChanged();
            }
        }
    }

    /**
     * Child Data List
     *
     * @return
     */
    public List<List<Child>> getChildData() {
        if (null == mChildList) {
            mChildList = new ArrayList<>();
        }

        return mChildList;
    }

    /**
     * Child List Item
     *
     * @param location
     * @return
     */
    public List<Child> getChildDataItem(int location) {
        List<Child> list = getChildData().get(location);

        if (null == list) {
            list = new ArrayList<>();
        }

        return list;
    }

    /**
     * set Child Data
     *
     * @param list
     */
    public void setChildData(List<List<Child>> list) {
        setChildData(list, false);
    }

    /**
     * set Child Data
     *
     * @param list
     * @param notifyDataSetChanged 更新ListView
     */
    public void setChildData(List<List<Child>> list, boolean notifyDataSetChanged) {
        try {
            // 使用原子数据，即直接将DataList替换为List，否则清除并add
            if (getClass().isAnnotationPresent(HAtomicData.class)) {
                HAtomicData annotation = getClass().getAnnotation(HAtomicData.class);
                if (annotation.value()) {
                    mChildList = list;
                    if (notifyDataSetChanged) {
                        notifyDataSetChanged();
                    }
                    return;
                }
            }
        } catch (Exception e) {
            Logger.e(e);
        }

        reAddChildData(list);
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * 添加Child数据，并清除旧数据
     *
     * @param list
     */
    public void reAddChildData(List<List<Child>> list) {
        reAddChildData(list, false);
    }

    /**
     * 添加Child数据，并清除旧数据
     *
     * @param list
     * @param notifyDataSetChanged 更新ListView
     */
    public void reAddChildData(List<List<Child>> list, boolean notifyDataSetChanged) {
        clearChild();
        addAllChild(list);

        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * add child data
     *
     * @param list
     */
    public void addAllChild(List<List<Child>> list) {
        addAllChild(list, false);
    }

    /**
     * add child data
     *
     * @param list
     * @param notifyDataSetChanged 更新ListView
     */
    public void addAllChild(List<List<Child>> list, boolean notifyDataSetChanged) {
        if (null == list || list.isEmpty()) {
            return;
        }

        if (mChildList == null) {
            mChildList = new ArrayList<>();
        }

        this.mChildList.addAll(list);
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * add child item data
     *
     * @param child
     */
    public void addChild(List<Child> child) {
        addChild(child, false);
    }

    /**
     * add child item data
     *
     * @param child
     * @param notifyDataSetChanged 更新ListView
     */
    public void addChild(List<Child> child, boolean notifyDataSetChanged) {
        if (mChildList == null) {
            mChildList = new ArrayList<>();
        }

        mChildList.add(child);
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * 删除Child数据
     *
     * @param childList
     * @return
     */
    public boolean removeChild(List<Child> childList) {
        return removeChild(childList, false);
    }

    /**
     * 删除Child数据
     *
     * @param childList
     * @param notifyDataSetChanged 更新ListView
     * @return
     */
    public boolean removeChild(List<Child> childList, boolean notifyDataSetChanged) {
        if (mChildList == null || mChildList.isEmpty()) {
            return false;
        }

        if (!mChildList.remove(childList)) {
            return false;
        }

        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }

        return true;
    }

    /**
     * 删除Child数据
     *
     * @param list
     * @return
     */
    public boolean removeAllChild(Collection<Child> list) {
        return removeAllChild(list, false);
    }

    /**
     * 删除Child数据
     *
     * @param list
     * @param notifyDataSetChanged 更新ListView
     * @return
     */
    public boolean removeAllChild(Collection<Child> list, boolean notifyDataSetChanged) {
        if (mChildList == null || mChildList.isEmpty()) {
            return false;
        }

        final boolean r = mChildList.removeAll(list);
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }

        return r;
    }

    /**
     * clear Child data
     */
    public void clearChild() {
        clearChild(false);
    }

    /**
     * clear Child data
     *
     * @param notifyDataSetChanged 更新ListView
     */
    public void clearChild(boolean notifyDataSetChanged) {
        if (mChildList == null) {
            return;
        }

        if (!mChildList.isEmpty()) {
            mChildList.clear();
            if (notifyDataSetChanged) {
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public int getGroupCount() {
        return getGroupData().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            return getChildData().get(groupPosition).size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Group getGroup(int groupPosition) {
        try {
            return getGroupData().get(groupPosition);
        } catch (Exception e) {
            Logger.e(e);
            return null;
        }
    }

    @Override
    public Child getChild(int groupPosition, int childPosition) {
        try {
            return getChildData().get(groupPosition).get(childPosition);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void notifyDataSetChanged() {
        // super.notifyDataSetChanged();
        Observable.just(0).subscribeOn(AndroidSchedulers.mainThread()).subscribe(n -> super.notifyDataSetChanged(), err -> Logger.e(err));
    }

    @Override
    public void notifyDataSetInvalidated() {
        // super.notifyDataSetInvalidated();
        Observable.just(1).subscribeOn(AndroidSchedulers.mainThread()).subscribe(n -> super.notifyDataSetInvalidated(), err -> Logger.e(err));
    }

    /**
     * Group HolderView Class
     *
     * @return
     */
    public Class<GHolder> getGroupHolderViewClass() {
        Class<GHolder> cls = (Class<GHolder>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[2];
        return cls;
    }

    /**
     * Child HolderView Class
     *
     * @return
     */
    public Class<CHolder> getChildHolderViewClass() {
        Class<CHolder> cls = (Class<CHolder>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
        return cls;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = bindGroupView(convertView);

        final Group bean = getGroup(groupPosition);

        if (bean == null) {
            return convertView;
        }

        final GHolder tag = (GHolder) convertView.getTag();

        // 绑定数据
        if (null != convertView && null != tag && null != bean) {
            bindGroupData(groupPosition, isExpanded, convertView, parent, bean, tag);
        }

        return convertView;
    }

    /**
     * 绑定Group View
     *
     * @param view
     * @return
     */
    protected View bindGroupView(View view) {
        if (view != null && view.getTag() != null) {
            return view;
        }

        // 初始化view
        try {
            if (null == view || getClass().isAnnotationPresent(HContentViewResEx.class)) {
                HContentViewResEx annotation = getClass().getAnnotation(HContentViewResEx.class);
                if (annotation.groupResId() != View.NO_ID) {
                    view = mLayoutInflater.inflate(annotation.groupResId(), null);
                }
            }
        } catch (Exception e) {
            Logger.e(e);
            return view;
        }

        GHolder tag = initViewGroupHolder(view);
        view.setTag(tag);

        initGroupView(view, tag);

        return view;
    }

    /**
     * 初始化GroupView
     *
     * @param view
     * @param holder
     */
    protected void initGroupView(View view, GHolder holder) {

    }


    /** 初始化Group View Holder */
    protected abstract GHolder initViewGroupHolder(View view);

    /**
     * Group数据绑定
     * 用于GroupList显示
     *
     * @param groupPosition
     * @param isExpanded
     * @param view
     * @param viewGroup
     * @param bean
     * @param holder
     */
    protected abstract void bindGroupData(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup, Group bean, GHolder
            holder);


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = bindChildView(convertView);

        final Child bean = getChild(groupPosition, childPosition);

        if (bean == null) {
            return convertView;
        }

        final CHolder tag = (CHolder) convertView.getTag();

        // 绑定数据
        if (null != convertView && null != tag && null != bean) {
            bindChildData(groupPosition, childPosition, isLastChild, convertView, parent, bean, tag);
        }

        return convertView;
    }

    /**
     * 绑定Child View
     *
     * @param view
     * @return
     */
    protected View bindChildView(View view) {
        if (view != null && view.getTag() != null) {
            return view;
        }

        // 初始化view
        try {
            if (null == view || getClass().isAnnotationPresent(HContentViewResEx.class)) {
                HContentViewResEx annotation = getClass().getAnnotation(HContentViewResEx.class);
                if (annotation.childResId() != View.NO_ID) {
                    view = mLayoutInflater.inflate(annotation.childResId(), null);
                }
            }
        } catch (Exception e) {
            Logger.e(e);
            return view;
        }

        CHolder tag = initViewChildHolder(view);
        view.setTag(tag);

        initChildView(view, tag);

        return view;
    }

    /**
     * 初始化ChildView
     *
     * @param view
     * @param holder
     */
    protected void initChildView(View view, CHolder holder) {

    }

    /** 初始化Child View Holder */
    protected abstract CHolder initViewChildHolder(View view);

    /**
     * Child数据绑定
     * 用于Child List显示
     *
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param view
     * @param viewGroup
     * @param bean
     * @param holder
     */
    protected abstract void bindChildData(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup,
                                          Child bean, CHolder holder);

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}
