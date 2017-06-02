package com.hw.hwdroid.dialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.hw.hwdroid.dialog.model.DialogType;
import com.hw.hwdroid.dialog.model.exchangeModel.DialogExchangeModel;


/**
 * Created by ChenJ on 16/4/5.
 */
public class HDialogManager {

    public final static int DIALOG_REQUEST_CODE = 0x002001;

    /**
     * 弹框方法
     * <p>
     * fragment与fragmentActivity不可同时为NULL
     *
     * @param fragmentManager     (必传字段)
     * @param dialogExchangeModel (必传字段)
     * @param fragment            (选传)
     * @param fragmentActivity    (选传)
     * @return BaseDialogFragmentV2对象
     */
    public static BaseDialogFragment showDialogFragment(FragmentManager fragmentManager, DialogExchangeModel dialogExchangeModel, Fragment fragment, FragmentActivity fragmentActivity) {
        if (fragmentManager == null) {
            throw new NullPointerException("FragmentManager can not be null!");
        }
        return showDialogFragment(fragmentManager, dialogExchangeModel, null, fragment, fragmentActivity);
    }

    /**
     * 弹框方法
     * <p>
     * fragment与fragmentActivity不可同时为NULL
     * DialogCallBackContainer,添加这个参数是因为，目前DialogFagment将DialogExchangeModel通过SetAguments的形式保存为Fragment的私有变量mArguments。
     * 而mArguments会在onSavedInstance被序列化，如果其中的几个ExcuteCallBack和CustomView被赋值，则会导致序列化失败，直接Crash掉。
     * 所以这几个callBack和view不能通过DialogExchangeModel来赋值，或者说，不能赋值给会被序列化的类。
     * *******************
     *
     * @param fragmentManager         FragmentManager
     * @param dialogExchangeModel     DialogExchangeModel
     * @param dialogCallBackContainer DialogCallBackContainer|一些CallBack可以通过这个容器传递
     * @param fragment                Fragment
     * @param fragmentActivity        FragmentActivity
     * @return BaseDialogFragment
     */
    public static BaseDialogFragment showDialogFragment(FragmentManager fragmentManager,
                                                        DialogExchangeModel dialogExchangeModel,
                                                        DialogCallBackContainer dialogCallBackContainer,
                                                        Fragment fragment, FragmentActivity fragmentActivity) {
        BaseDialogFragment baseDialogFragment = null;

        if (dialogExchangeModel != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(BaseDialogFragment.TAG, dialogExchangeModel.dialogExchangeModelBuilder);
            DialogType dialogType = dialogExchangeModel.getDialogType();

            // 单按钮框
            if (dialogType == DialogType.SINGLE) {
                baseDialogFragment = SingleInfoDialogFragment.getInstance(bundle);
            }
            // 双按钮框
            else if (dialogType == DialogType.EXCUTE) {
                baseDialogFragment = HandleInfoDialogFragment.getInstance(bundle);
            }
            // 自定义view框
            else if (dialogType == DialogType.CUSTOMER) {
                baseDialogFragment = CustomerDialogFragment.getInstance(bundle);
            }
            // 进度框，即菊花框
            else if (dialogType == DialogType.PROGRESS) {
                baseDialogFragment = ProcessDialogFragment.getInstance(bundle);
            }
            // 编辑输入
            else if (dialogType == DialogType.EDIT) {
                baseDialogFragment = EditDialogFragment.getInstance(bundle);
            }
        }

        if (baseDialogFragment != null) {
            baseDialogFragment.compatibilityListener = dialogExchangeModel.compatibilityListener;
            baseDialogFragment.compatibilityNegativeListener = dialogExchangeModel.getCompatibilityNegativeListener();
            baseDialogFragment.compatibilityPositiveListener = dialogExchangeModel.getCompatibilityPositiveListener();
            if (dialogCallBackContainer != null) {
                baseDialogFragment.singleClickCallBack = dialogCallBackContainer.singleClickCallBack;
                baseDialogFragment.positiveClickCallBack = dialogCallBackContainer.positiveClickCallBack;
                baseDialogFragment.negativeClickCallBack = dialogCallBackContainer.negativeClickCallBack;
                baseDialogFragment.dismissCallBack = dialogCallBackContainer.dismissCallBack;
                baseDialogFragment.onStopCallBack = dialogCallBackContainer.onStopCallBack;
                baseDialogFragment.onCancelCallBack = dialogCallBackContainer.onCancelCallBack;
                baseDialogFragment.containerSingleCallBack = dialogCallBackContainer.containerSingleCallBack;
                if (baseDialogFragment instanceof CustomerDialogFragment) {
                    ((CustomerDialogFragment) baseDialogFragment).customView = dialogCallBackContainer.customView;
                }
            }
        }
        try {
            if (baseDialogFragment != null) {
                if (fragment != null) {
                    baseDialogFragment.setTargetFragment(fragment, DIALOG_REQUEST_CODE);
                }
                if (fragmentActivity != null && fragmentActivity instanceof IBaseDialogFragment) {
                    ((IBaseDialogFragment) fragmentActivity).showCallback(dialogExchangeModel.getTag());
                }
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.add(baseDialogFragment, dialogExchangeModel.getTag());
                ft.commitAllowingStateLoss();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        return baseDialogFragment;
    }


}
