package com.hw.hwdroid.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import com.hw.hwdroid.dialog.model.exchangeModel.DialogExchangeModel;

import java.util.HashMap;


public class BaseDialogFragment extends DialogFragment {

    public final static String TAG = "HDBaseDialogFragment";

    // Tag
    protected String mDialogTag;

    // 标题
    protected CharSequence mTitleTxt = "";

    // 确认操作
    protected CharSequence mPositiveBtnTxt = "";

    // 取消操作
    protected CharSequence mNegativeBtnTxt = "";

    // 单个button文字
    protected CharSequence mSingleBtnTxt = "";

    // 内容
    protected CharSequence mContentTxt = "";

    // 是否back取消
    public boolean isBackable;

    // 是否空白取消
    public boolean isSpaceable;

    public boolean mHasTitle;

    // 错误弹框 按键点击事件
    public OnClickListener compatibilityListener;
    public OnClickListener compatibilityPositiveListener;
    public OnClickListener compatibilityNegativeListener;

    public int gravity = Gravity.CENTER;

    /** 存在containerCallback中外部传入的回调 (带Tag区分) */
    public SingleDialogFragmentCallBack containerSingleCallBack;

    /** Fragment在onStop的时候，进行回调 */
    public DialogHandleEvent onStopCallBack;

    /** Fragment在onCancel的时候，进行回调 */
    public DialogHandleEvent onCancelCallBack;

    /** 单选按钮的点击回调 */
    public DialogHandleEvent singleClickCallBack;

    /** Positive按钮的点击回调 */
    public DialogHandleEvent positiveClickCallBack;
    /** Negative按钮的点击回调 */
    public DialogHandleEvent negativeClickCallBack;

    /** Fragment在Dismiss的时候，进行回调 */
    public DialogHandleEvent dismissCallBack;

    /**
     * 点击空白处 会调用onCancel
     */
    @Deprecated
    protected OnClickListener mSpaceClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!isSpaceable) {
                return;
            }

            Activity activity = getActivity();
            Fragment tarFragment = getTargetFragment();
            dismissSelf();

            //            if (tarFragment != null && tarFragment instanceof SpaceAndCancelCallBack) {
            //                ((SpaceAndCancelCallBack) tarFragment).onSpaceClick(mDialogTag);
            //            } else if (activity != null && activity instanceof SpaceAndCancelCallBack) {
            //                ((SpaceAndCancelCallBack) activity).onSpaceClick(mDialogTag);
            //            }
        }
    };

    public static BaseDialogFragment getInstance(Bundle bundle) {
        BaseDialogFragment baseDialogFragment = new BaseDialogFragment();
        baseDialogFragment.setArguments(bundle);
        return baseDialogFragment;
    }

    public BaseDialogFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.ThemeHolo);

        Bundle bundle = getArguments();

        if (null == bundle) {
            return;
        }

        DialogExchangeModel dialogExchangeModel = ((DialogExchangeModel.DialogExchangeModelBuilder) bundle.getSerializable(TAG)).create();
        if (dialogExchangeModel != null) {
            mDialogTag = dialogExchangeModel.getTag();
            isBackable = dialogExchangeModel.isBackable();
            isSpaceable = dialogExchangeModel.isSpaceable();
            mHasTitle = dialogExchangeModel.isHasTitle();
            mContentTxt = dialogExchangeModel.getDialogContext();

            if (null != mContentTxt && mContentTxt.toString().length() > 0) {
                HashMap<String, Object> params = new HashMap<>();
                params.put("content", mContentTxt);
            }

            setCancelable(isBackable);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);

        if (d != null) {
            d.setCanceledOnTouchOutside(isSpaceable);
        }

        return d;
    }

    @Override
    public void dismiss() {
        if (getActivity() != null && getActivity() instanceof IBaseDialogFragment) {
            ((IBaseDialogFragment) getActivity()).dismissDialogCallback(getTag());
        }

        super.dismissAllowingStateLoss();
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        return show(transaction, tag, true);
    }

    public int show(FragmentTransaction transaction, String tag, boolean allowStateLoss) {
        transaction.add(this, tag);
        final int backStackId = allowStateLoss ? transaction.commitAllowingStateLoss() : transaction.commit();
        return backStackId;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Activity activity = getActivity();
        Fragment targetFragment = getTargetFragment();

        if (targetFragment != null && targetFragment instanceof SpaceAndCancelCallBack) {
            ((SpaceAndCancelCallBack) targetFragment).onCanceled(mDialogTag);
        } else if (activity != null && activity instanceof SpaceAndCancelCallBack) {
            ((SpaceAndCancelCallBack) activity).onCanceled(mDialogTag);
        }

        if (onCancelCallBack != null) {
            onCancelCallBack.callBack();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (onStopCallBack != null) {
            onStopCallBack.callBack();
        }
    }

    @Override
    public void onDetach() {
        if (dismissCallBack != null) {
            dismissCallBack.callBack();
        }

        super.onDetach();
    }

    /**
     * @return false 滑动返回事件本函数不做处理，由系统调用通用返回方法处理
     */
    /*public boolean onInteruptSlidebackEvent() {
        return false;
    }*/
    public void dismissSelf() {
        if (getActivity() != null && getActivity() instanceof IBaseDialogFragment) {
            // ((CoBaseActivity) get()).dialogFragmentTags.remove(getTag());
            ((IBaseDialogFragment) getActivity()).dismissDialogCallback(getTag());
        }

        HFragmentExchangeController.removeFragment(getFragmentManager(), this);
    }
}
