package com.hw.hwdroid.foundation.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hw.hwdroid.foundation.app.annotation.HAtomicData;
import com.hw.hwdroid.foundation.app.annotation.HContentViewResEx;
import com.orhanobut.logger.Logger;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import common.android.ui.myxlistview.libraries.PinnedSectionedListAdapter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by ChenJ on 16/8/26.
 */
public abstract class JBasePinnedSectionedListAdapter<Section extends HWExGroup<Bean>, Bean, SectionHeaderHolderView extends HWBaseViewHolder, HolderView extends HWBaseViewHolder> extends PinnedSectionedListAdapter {

    protected LayoutInflater layoutInflater;
    private Context context;

    protected List<Section> dataList = new ArrayList<>();

    public JBasePinnedSectionedListAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /**
     * get Context
     *
     * @return
     */
    public Context getContext() {
        return context;
    }

    /**
     * Data List
     *
     * @return
     */
    public List<Section> getData() {
        if (null == dataList) {
            dataList = new ArrayList<>();
        }

        return dataList;
    }

    /**
     * 设置数据
     *
     * @param sectionData
     */
    public void setData(List<Section> sectionData) {
        setData(sectionData, false);
    }

    /**
     * 设置数据
     *
     * @param sectionData
     * @param notifyDataSetChanged 更新UI
     */
    public void setData(List<Section> sectionData, boolean notifyDataSetChanged) {
        clear();

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
            dataList = sectionData;
            if (notifyDataSetChanged) {
                notifyDataSetChanged();
            }
            return;
        }

        reAddData(sectionData);
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * 添加数据，并清除旧数据
     *
     * @param list
     */
    public void reAddData(List<Section> list) {
        reAddData(list, false);
    }

    /**
     * 添加数据，并清除旧数据
     *
     * @param list
     * @param notifyDataSetChanged 更新UI
     */
    public void reAddData(List<Section> list, boolean notifyDataSetChanged) {
        clear();
        addAll(list);

        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * add dataList
     *
     * @param list
     */
    public void addAll(List<Section> list) {
        addAll(list, false);
    }

    /**
     * add dataList
     *
     * @param list
     * @param notifyDataSetChanged 更新UI
     */
    public void addAll(List<Section> list, boolean notifyDataSetChanged) {
        if (null == list || list.isEmpty()) {
            return;
        }

        if (dataList == null) {
            dataList = new ArrayList<>();
        }

        dataList.addAll(list);

        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * add all Data 并检测数据
     *
     * @param list
     */
    public void addAllAndCheckItems(List<Section> list) {
        addAllAndCheckItems(list, false);
    }

    /**
     * add all Data 并检测数据
     *
     * @param list
     * @param notifyDataSetChanged 更新UI
     */
    public void addAllAndCheckItems(List<Section> list, boolean notifyDataSetChanged) {
        if (null == list || list.isEmpty()) {
            return;
        }

        if (dataList == null) {
            dataList = new ArrayList<>();
            dataList.addAll(list);
            if (notifyDataSetChanged) {
                notifyDataSetChanged();
            }
            return;
        }

        if (dataList.isEmpty()) {
            dataList.addAll(list);
            if (notifyDataSetChanged) {
                notifyDataSetChanged();
            }
            return;
        }

        // 遍历新数据
        for (Section section : list) {
            if (null == section) {
                continue;
            }

            int indexOf = dataList.indexOf(section);
            if (indexOf != -1) {
                if (null != section.getChildren()) {
                    dataList.get(indexOf).getChildren().addAll(section.getChildren());
                }
            } else {
                dataList.add(section);
            }
        }

        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * add all Data 并检测随后一项数据
     *
     * @param list
     */
    public void addAllAndCheckLastItem(List<Section> list) {
        addAllAndCheckLastItem(list, false);
    }

    /**
     * add all Data 并检测随后一项数据
     *
     * @param list
     * @param notifyDataSetChanged 更新UI
     */
    public void addAllAndCheckLastItem(List<Section> list, boolean notifyDataSetChanged) {
        if (null == list || list.isEmpty()) {
            return;
        }

        if (dataList == null) {
            dataList = new ArrayList<>();
            dataList.addAll(list);
            if (notifyDataSetChanged) {
                notifyDataSetChanged();
            }
            return;
        }

        if (dataList.isEmpty()) {
            dataList.addAll(list);
            if (notifyDataSetChanged) {
                notifyDataSetChanged();
            }
            return;
        }

        // 原列表最后一项
        Section oldLastItem = dataList.get(dataList.size() - 1);

        if (null == oldLastItem) {
            this.dataList.addAll(list);
            if (notifyDataSetChanged) {
                notifyDataSetChanged();
            }
            return;
        }

        // 新数据第一项
        Section newFirstItem = list.get(0);

        // 原列表最后一项与新数据第一项相同
        if (oldLastItem.equals(newFirstItem)) {
            Section rLastItem = list.remove(list.size() - 1);
            if (null != rLastItem) {
                oldLastItem.getChildren().addAll(rLastItem.getChildren());
            }
            if (!list.isEmpty()) {
                dataList.addAll(list);
            }
            if (notifyDataSetChanged) {
                notifyDataSetChanged();
            }
            return;
        }

        dataList.addAll(list);
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * 删除数据
     *
     * @param section
     * @return
     */
    public boolean remove(Section section) {
        return remove(section, false);
    }

    /**
     * 删除数据
     *
     * @param section
     * @param notifyDataSetChanged 更新UI
     * @return
     */
    public boolean remove(Section section, boolean notifyDataSetChanged) {
        if (dataList == null || dataList.isEmpty()) {
            return false;
        }

        if (!dataList.remove(section)) {
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
    public boolean removeAll(Collection<Section> list) {
        return removeAll(list, false);
    }

    /**
     * 删除数据
     *
     * @param list
     * @param notifyDataSetChanged 更新UI
     * @return
     */
    public boolean removeAll(Collection<Section> list, boolean notifyDataSetChanged) {
        if (dataList == null || dataList.isEmpty()) {
            return false;
        }

        final boolean r = dataList.removeAll(list);
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }

        return r;
    }

    /**
     * clear dataList
     */
    public void clear() {
        clear(false);
    }

    /**
     * clear dataList
     *
     * @param notifyDataSetChanged 更新UI
     */
    public void clear(boolean notifyDataSetChanged) {
        if (dataList == null) {
            return;
        }

        if (!dataList.isEmpty()) {
            dataList.clear();
            if (notifyDataSetChanged) {
                notifyDataSetChanged();
            }
        }
    }

    /**
     * Item Data List
     *
     * @return
     */
    public List<List<Bean>> getItemData() {
        if (null == dataList) {
            return new ArrayList<>();
        }

        List<List<Bean>> itemList = new ArrayList<>();

        for (Section g : dataList) {
            if (null == g) {
                itemList.add(new ArrayList<>());
            } else {
                itemList.add(g.getChildren());
            }
        }

        return itemList;
    }

    /**
     * set item dataList
     *
     * @param list
     */
    public void setItem(int section, List<Bean> list) {
        setItem(section, list, false);
    }

    /**
     * set item dataList
     *
     * @param section
     * @param list
     * @param notifyDataSetChanged 更新UI
     */
    public void setItem(int section, List<Bean> list, boolean notifyDataSetChanged) {
        Section g = getSectionItem(section);

        if (null == g) {
            return;
        }

        g.getChildren().clear();

        if (null == list || list.isEmpty()) {
            if (notifyDataSetChanged) {
                notifyDataSetChanged();
            }
            return;
        }

        g.getChildren().addAll(list);
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * add item dataList
     *
     * @param list
     */
    public void addAllItem(int sectionPosition, List<Bean> list) {
        addAllItem(sectionPosition, list, false);
    }

    /**
     * add item dataList
     *
     * @param sectionPosition
     * @param list
     * @param notifyDataSetChanged 更新UI
     */
    public void addAllItem(int sectionPosition, List<Bean> list, boolean notifyDataSetChanged) {
        if (null == list || list.isEmpty()) {
            return;
        }

        Section g = getSectionItem(sectionPosition);

        if (null == g || null == g.getChildren()) {
            return;
        }

        g.getChildren().addAll(list);
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    /**
     * add item dataList
     *
     * @param item
     */
    public void addItem(int sectionPosition, Bean item) {
        addItem(sectionPosition, item, false);
    }

    /**
     * add item dataList
     *
     * @param sectionPosition
     * @param item
     * @param notifyDataSetChanged 更新UI
     */
    public void addItem(int sectionPosition, Bean item, boolean notifyDataSetChanged) {
        Section g = getSectionItem(sectionPosition);

        if (null == g || null == g.getChildren()) {
            return;
        }

        g.getChildren().add(item);
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int section, int position) {
        return 1;
    }

    @Override
    public int getItemViewTypeCount() {
        return 1;
    }

    @Override
    public int getSectionHeaderViewType(int section) {
        return 0;
    }

    @Override
    public int getSectionHeaderViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public Bean getItem(int section, int position) {
        try {
            List<Bean> childList = getSectionItem(section).getChildren();

            if (null == childList) {
                return null;
            }

            return childList.get(position);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public Section getSectionItem(int section) {
        try {
            return getData().get(section);
        } catch (Exception e) {
            Logger.e(e);
            return null;
        }
    }

    @Override
    public int getSectionCount() {
        return getData().size();
    }

    @Override
    public int getCountForSection(int section) {
        try {
            return getSectionItem(section).getChildren().size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void notifyDataSetChanged() {
        // super.notifyDataSetChanged();
        Observable.just(0).subscribeOn(AndroidSchedulers.mainThread()).subscribe(n -> super.notifyDataSetChanged(), err -> Logger.e(err));
    }

    @Override
    public void notifyDataSetInvalidated() {
        // super.notifyDataSetInvalidated();
        Observable.just(1).subscribeOn(AndroidSchedulers.mainThread()).subscribe(n -> super.notifyDataSetInvalidated(), e -> Logger.e(e));
    }

    /**
     * Section HolderView Class
     *
     * @return
     */
    public Class<SectionHeaderHolderView> getSectionHeaderHolderViewClass() {
        Class<SectionHeaderHolderView> cls = (Class<SectionHeaderHolderView>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[2];
        return cls;
    }

    /**
     * Item HolderView Class
     *
     * @return
     */
    public Class<HolderView> getItemHolderViewClass() {
        Class<HolderView> cls = (Class<HolderView>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
        return cls;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        convertView = bindSectionHeaderView(convertView);

        final Section bean = getSectionItem(section);

        if (bean == null) {
            return convertView;
        }

        final SectionHeaderHolderView tag = (SectionHeaderHolderView) convertView.getTag();

        // 绑定数据
        if (convertView != null && tag != null && bean != null) {
            bindSectionData(section, convertView, parent, bean, tag);
        }

        return convertView;
    }

    /**
     * 绑定Section Header View
     *
     * @param view
     * @return
     */
    protected View bindSectionHeaderView(View view) {
        if (view != null && view.getTag() != null) {
            return view;
        }

        // 初始化view
        try {
            if (null == view || getClass().isAnnotationPresent(HContentViewResEx.class)) {
                HContentViewResEx annotation = getClass().getAnnotation(HContentViewResEx.class);
                if (annotation.groupResId() != View.NO_ID) {
                    view = layoutInflater.inflate(annotation.groupResId(), null);
                }
            }
        } catch (Exception e) {
            Logger.e(e);
            return view;
        }

        SectionHeaderHolderView tag = initSectionHeaderHolderView(view);
        view.setTag(tag);

        return view;
    }


    /** 初始化SectionHeaderView Holder */
    protected abstract SectionHeaderHolderView initSectionHeaderHolderView(View view);

    /**
     * Section数据绑定
     * 用于SectionHeaderView显示
     *
     * @param section
     * @param view
     * @param parent
     * @param bean
     * @param holder
     */
    protected abstract void bindSectionData(int section, View view, ViewGroup parent, Section bean, SectionHeaderHolderView holder);

    @Override
    public View getItemView(int section, int positionInSection, View convertView, ViewGroup parent) {
        convertView = bindView(convertView);

        final Bean bean = getItem(section, positionInSection);

        if (bean == null) {
            return convertView;
        }

        final HolderView tag = (HolderView) convertView.getTag();

        // 绑定数据
        if (convertView != null && tag != null && bean != null) {
            bindData(section, positionInSection, convertView, parent, bean, tag);
        }

        return convertView;
    }

    /**
     * 绑定Item View
     *
     * @param view
     * @return
     */
    protected View bindView(View view) {
        if (view != null && view.getTag() != null) {
            return view;
        }

        // 初始化view
        try {
            if (null == view || getClass().isAnnotationPresent(HContentViewResEx.class)) {
                HContentViewResEx annotation = getClass().getAnnotation(HContentViewResEx.class);
                if (annotation.childResId() != View.NO_ID) {
                    view = layoutInflater.inflate(annotation.childResId(), null);
                }
            }
        } catch (Exception e) {
            Logger.e(e);
            return view;
        }

        HolderView tag = initViewChildHolder(view);
        view.setTag(tag);

        return view;
    }

    /** 初始化Holder View */
    protected abstract HolderView initViewChildHolder(View view);

    /**
     * 数据绑定
     *
     * @param section
     * @param position
     * @param view
     * @param viewGroup
     * @param bean
     * @param holder
     */
    protected abstract void bindData(int section, int position, View view, ViewGroup viewGroup, Bean bean, HolderView holder);


}
