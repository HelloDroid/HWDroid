package com.hw.hwdroid.foundation.app.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * ViewModle Data
 * <p>
 * Created by ChenJ on 2017/2/16.
 */

public class ViewModel implements Serializable, Cloneable {

    private static final long serialVersionUID = 8640288529866574521L;

    // total 可以用于总页数(默认情况)，也可用于总数据，重写hasNextPage来改变其意义
    // 当前页（有些接口以0开始，有些接口以1开始，目前API接口开发那边没有统一）
    // 起始页，默认以0为起始页
    public int total = 0;
    public short pageIdx = 0;
    public short pageIdxStart = 0;

    // 正在加载
    public boolean isLoading;

    /** 事件执行时间存储器 */
    public final HashMap<String, Long> eventMap = new HashMap<>(5);

    // 权限
    public boolean superOnRequestPermissionsResult = true;
    public final HashMap<Integer, List<String>> requestPermissionMap = new HashMap<>(5);

    public ViewModel() {
        total = 0;
        pageIdx = pageIdxStart;
    }

    public boolean isValidate() {
        return true;
    }

    /**
     * key
     *
     * @return
     */
    public String getKey() {
        return hashCode() + "###" + getClass().getName();
    }

    /**
     * 是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return false;
    }

    public void reset() {
    }

    /**
     * 清除
     */
    public void clean() {
    }

    /**
     * 是否正在加载数据
     *
     * @return
     */
    public boolean isLoadingStatus() {
        return isLoading;
    }

    /**
     * 停止所有加载状态图
     */
    public void stopAllLoadingStatus() {
        isLoading = false;
    }

    /**
     * 设置加载状态
     *
     * @param loading
     */
    public void setLoadingStatus(boolean loading) {
        isLoading = loading;
    }

    public void setPageIdx(short pageIdx) {
        if (pageIdx < 0) {
            this.pageIdx = 0;
            return;
        }
        this.pageIdx = pageIdx;
    }

    public void setTotal(int total) {
        if (total < 0) {
            this.total = 0;
            return;
        }

        this.total = total;
    }

    /**
     * 还有下一页
     * total用于记录总页数
     *
     * @return
     */
    public boolean hasNextPage() {
        return this.total > this.pageIdx;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ViewModel cloneObj = (ViewModel) super.clone();
        return cloneObj;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
