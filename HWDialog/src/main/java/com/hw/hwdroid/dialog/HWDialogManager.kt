package com.hw.hwdroid.dialog

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.hw.hwdroid.dialog.HDialogManager.DIALOG_REQUEST_CODE
import com.hw.hwdroid.dialog.model.DialogType
import com.hw.hwdroid.dialog.model.exchangeModel.DialogExchangeModel

/**
 * HW Dialog Manager
 *
 * Created by ChenJ on 2017/6/2.
 */
object HWDialogManager {

    /**
     * 弹框方法
     *
     *
     * fragment与fragmentActivity不可同时为NULL
     * DialogCallBackContainer,添加这个参数是因为，目前DialogFagment将DialogExchangeModel通过SetAguments的形式保存为Fragment的私有变量mArguments。
     * 而mArguments会在onSavedInstance被序列化，如果其中的几个ExcuteCallBack和CustomView被赋值，则会导致序列化失败，直接Crash掉。
     * 所以这几个callBack和view不能通过DialogExchangeModel来赋值，或者说，不能赋值给会被序列化的类。
     *
     * @param fragmentManager           FragmentManager
     * @param dialogEM                  DialogExchangeModel
     * @param callBackContainer         DialogCallBackContainer|一些CallBack可以通过这个容器传递
     * @param fragment                  Fragment
     * @param activity                  FragmentActivity
     * @return BaseDialogFragment
     */
    @JvmStatic fun showDialogFragment(fragmentManager: FragmentManager, dialogEM: DialogExchangeModel, callBackContainer: DialogCallBackContainer? = null, fragment: Fragment? = null, activity: FragmentActivity? = null): BaseDialogFragment {
        var baseDialogFragment: BaseDialogFragment

        val bundle = Bundle()
        val dialogType = dialogEM.dialogType
        bundle.putSerializable(BaseDialogFragment.TAG, dialogEM.dialogExchangeModelBuilder)

        // 单按钮框
        if (dialogType == DialogType.SINGLE) {
            baseDialogFragment = SingleInfoDialogFragment.getInstance(bundle)
        }
        // 双按钮框
        else if (dialogType == DialogType.EXCUTE) {
            baseDialogFragment = HandleInfoDialogFragment.getInstance(bundle)
        }
        // 自定义view框
        else if (dialogType == DialogType.CUSTOMER) {
            baseDialogFragment = CustomerDialogFragment.getInstance(bundle)
        }
        // 进度框，即菊花框
        else if (dialogType == DialogType.PROGRESS) {
            baseDialogFragment = ProcessDialogFragment.getInstance(bundle)
        }
        // 编辑输入
        else if (dialogType == DialogType.EDIT) {
            baseDialogFragment = EditDialogFragment.getInstance(bundle)
        }
        // 默认为双按钮框
        else {
            baseDialogFragment = HandleInfoDialogFragment.getInstance(bundle)
        }

        baseDialogFragment?.compatibilityListener = dialogEM.compatibilityListener
        baseDialogFragment?.compatibilityNegativeListener = dialogEM.compatibilityNegativeListener
        baseDialogFragment?.compatibilityPositiveListener = dialogEM.compatibilityPositiveListener

        if (callBackContainer != null) {
            baseDialogFragment?.singleClickCallBack = callBackContainer.singleClickCallBack
            baseDialogFragment?.positiveClickCallBack = callBackContainer.positiveClickCallBack
            baseDialogFragment?.negativeClickCallBack = callBackContainer.negativeClickCallBack
            baseDialogFragment?.dismissCallBack = callBackContainer.dismissCallBack
            baseDialogFragment?.onStopCallBack = callBackContainer.onStopCallBack
            baseDialogFragment?.onCancelCallBack = callBackContainer.onCancelCallBack
            baseDialogFragment?.containerSingleCallBack = callBackContainer.containerSingleCallBack

            if (baseDialogFragment is CustomerDialogFragment) {
                baseDialogFragment.customView = callBackContainer.customView
            }
        }

        try {
            baseDialogFragment?.let {
                if (fragment != null) {
                    baseDialogFragment.setTargetFragment(fragment, DIALOG_REQUEST_CODE)
                }

                if (activity is IBaseDialogFragment) {
                    activity.showDialogCallback(dialogEM.tag)
                }

                val ft = fragmentManager.beginTransaction()
                ft.add(baseDialogFragment, dialogEM.tag)
                ft.commitAllowingStateLoss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return baseDialogFragment
    }

}