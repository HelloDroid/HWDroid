package com.hw.hwdroid.dialog;


public interface HandleDialogFragmentEvent {
    /**
     * 确认点击
     *
     * @param tag
     */
    void onPositiveBtnClick(String tag);

    /**
     * 取消点击
     *
     * @param tag
     */
    void onNegtiveBtnClick(String tag);
}
