package com.hw.hwdroid.dialog;


public interface SpaceAndCancelCallBack {
    /**
     * 空白点击回调
     * {#onCanceled}
     *
     * @param tag
     */
    // @Deprecated
    //void onSpaceClick(String tag);

    /**
     * 取消事件回调
     *
     * @param tag
     */
    void onCanceled(String tag);
}
