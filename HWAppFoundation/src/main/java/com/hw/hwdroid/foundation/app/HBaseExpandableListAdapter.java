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
public abstract class HBaseExpandableListAdapter<Group extends HExGroup<Child>, Child, GHolder extends HBaseViewHolder, CHolder extends
        HBaseViewHolder> extends BaseExpandableListAdapter {

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    protected List<Group> mGroupList = new ArrayList<>();

    public HBaseExpandableListAdapter(Context context) {
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
     * Data List
     *
     * @return
     */
    public List<Group> getData() {
        if (null == mGroupList) {
            mGroupList = new ArrayList<>();
        }

        return mGroupList;
    }

    /**
     * 设置数据
     *
     * @param groupData
     */
    public void setData(List<Group> groupData) {
        setData(groupData, false);
    }

    /**
     * 设置数据
     *
     * @param groupData
     * @param notifyDataSetChanged 更新ListView
     */
    public void setData(List<Group> groupData, boolean notifyDataSetChanged) {
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
        } else {
            clear();
            reAddData(groupData);
        }

        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * 添加数据，并清除旧数据
     *
     * @param list
     */
    public void reAddData(List<Group> list) {
        reAddData(list, false);
    }

    /**
     * 添加数据，并清除旧数据
     *
     * @param list
     * @param notifyDataSetChanged 更新ListView
     */
    public void reAddData(List<Group> list, boolean notifyDataSetChanged) {
        clear();
        addAll(list);

        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * add data
     *
     * @param list
     */
    public void addAll(List<Group> list) {
        addAll(list, false);
    }

    /**
     * add data
     *
     * @param list
     * @param notifyDataSetChanged 更新ListView
     */
    public void addAll(List<Group> list, boolean notifyDataSetChanged) {
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
     * 删除数据
     *
     * @param g
     * @return
     */
    public boolean remove(Group g) {
        return remove(g, false);
    }

    /**
     * 删除数据
     *
     * @param g
     * @param notifyDataSetChanged 更新ListView
     * @return
     */
    public boolean remove(Group g, boolean notifyDataSetChanged) {
        if (mGroupList == null || mGroupList.isEmpty()) {
            return false;
        }

        if (!mGroupList.remove(g)) {
            return false;
        }

        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }

        return true;
    }

    /**
     * 删除数据
     *
     * @param list
     * @return
     */
    public boolean removeAll(Collection<Group> list) {
        return removeAll(list, false);
    }

    /**
     * 删除数据
     *
     * @param list
     * @param notifyDataSetChanged 更新ListView
     * @return
     */
    public boolean removeAll(Collection<Group> list, boolean notifyDataSetChanged) {
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
     * clear data
     */
    public void clear() {
        clear(false);
    }

    /**
     * clear data
     *
     * @param notifyDataSetChanged 更新ListView
     */
    public void clear(boolean notifyDataSetChanged) {
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
        if (null == mGroupList) {
            return new ArrayList<>();
        }

        List<List<Child>> childList = new ArrayList<>();

        for (Group g : mGroupList) {
            if (null == g) {
                childList.add(new ArrayList<>());
            } else {
                childList.add(g.getChildren());
            }
        }

        return childList;
    }

    /**
     * set child data
     *
     * @param list
     */
    public void setChild(int groupPosition, List<Child> list) {
        setChild(groupPosition, list, false);
    }

    /**
     * set child data
     *
     * @param groupPosition
     * @param list
     * @param notifyDataSetChanged 更新ListView
     */
    public void setChild(int groupPosition, List<Child> list, boolean notifyDataSetChanged) {
        Group g = getGroup(groupPosition);

        if (null == g) {
            return;
        }

        g.getChildren().clear();
        if (null != list && !list.isEmpty()) {
            g.getChildren().addAll(list);
        }

        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * add child data
     *
     * @param list
     */
    public void addAllChild(int groupPosition, List<Child> list) {
        addAllChild(groupPosition, list, false);
    }

    /**
     * add child data
     *
     * @param groupPosition
     * @param list
     * @param notifyDataSetChanged 更新ListView
     */
    public void addAllChild(int groupPosition, List<Child> list, boolean notifyDataSetChanged) {
        if (null == list || list.isEmpty()) {
            return;
        }

        Group g = getGroup(groupPosition);

        if (null == g || null == g.getChildren()) {
            return;
        }

        g.getChildren().addAll(list);
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * add child item data
     *
     * @param child
     */
    public void addChild(int groupPosition, Child child) {
        addChild(groupPosition, child, false);
    }

    /**
     * add child item data
     *
     * @param groupPosition
     * @param child
     * @param notifyDataSetChanged 更新ListView
     */
    public void addChild(int groupPosition, Child child, boolean notifyDataSetChanged) {
        Group g = getGroup(groupPosition);

        if (null == g || null == g.getChildren()) {
            return;
        }

        g.getChildren().add(child);
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public int getGroupCount() {
        return getData().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            return getGroup(groupPosition).getChildren().size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Group getGroup(int groupPosition) {
        try {
            return getData().get(groupPosition);
        } catch (Exception e) {
            Logger.e(e);
            return null;
        }
    }

    @Override
    public Child getChild(int groupPosition, int childPosition) {
        try {
            List<Child> childList = getGroup(groupPosition).getChildren();

            if (null == childList) {
                return null;
            }

            return childList.get(childPosition);
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
        // super.notifyDataSetChanged()
        Observable.just(0).subscribeOn(AndroidSchedulers.mainThread()).subscribe(number -> super.notifyDataSetChanged(), onErr -> Logger.e(onErr));
    }

    @Override
    public void notifyDataSetInvalidated() {
        // super.notifyDataSetInvalidated();
        Observable.just(1).subscribeOn(AndroidSchedulers.mainThread()).subscribe(number -> super.notifyDataSetInvalidated(), onErr -> Logger.e(onErr));
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
