package com.hw.hwdroid.dialog;

import android.view.View;

/**
 * 保存DialogFragment各种CallBack的容器
 */
public class DialogCallBackContainer {
    public DialogCallBackContainer() {
    }

    /**
     * 单选按钮的点击回调
     */
    public DialogHandleEvent singleClickCallBack;

    /**
     * Positive按钮的点击回调
     */
    public DialogHandleEvent positiveClickCallBack;

    /**
     * Negative按钮的点击回调
     */
    public DialogHandleEvent negativeClickCallBack;

    /**
     * Fragment在Dismiss的时候，进行回调
     */
    public DialogHandleEvent dismissCallBack;

    /**
     * 自定义的View
     */
    public View customView;

    /**
     * Fragment在onStop的时候，进行回调
     */
    public DialogHandleEvent onStopCallBack;

    /**
     * Fragment在onCancel的时候，进行回调
     */
    public DialogHandleEvent onCancelCallBack;

    /**
     * 存在containerCallback中外部传入的回调 (带Tag区分)
     */
    public SingleDialogFragmentCallBack containerSingleCallBack;

}
