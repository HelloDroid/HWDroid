package com.hw.hwdroid.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hw.hwdroid.dialog.model.exchangeModel.DialogExchangeModel;
import com.hw.hwdroid.dialog.utils.StringUtils;
import com.orhanobut.logger.Logger;



public class HandleInfoDialogFragment extends BaseDialogFragment {

    private Button mLeftBtn;
    private Button mRightBtn;
    private TextView mDlgTitle;
    private TextView mDlgContent;

    private OnClickListener mExcuitePositiveListener;
    private OnClickListener mExcuiteNegativeListener;

    public static HandleInfoDialogFragment getInstance(Bundle bundle) {
        HandleInfoDialogFragment baseDialogFragment = new HandleInfoDialogFragment();
        baseDialogFragment.setArguments(bundle);
        return baseDialogFragment;
    }

    public HandleInfoDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null) {
            DialogExchangeModel dialogExchangeModel = ((DialogExchangeModel.DialogExchangeModelBuilder) bundle.getSerializable(TAG)).create();
            if (dialogExchangeModel != null) {
                mDialogTag = dialogExchangeModel.getTag();
                mTitleTxt = dialogExchangeModel.getDialogTitle();
                mPositiveBtnTxt = dialogExchangeModel.getPostiveText();
                mNegativeBtnTxt = dialogExchangeModel.getNegativeText();
                mContentTxt = dialogExchangeModel.getDialogContext();
                gravity = dialogExchangeModel.getGravity();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_dialog_excute_layout, container, false);

        view.setOnClickListener(mSpaceClickListener);
        mDlgTitle = (TextView) view.findViewById(R.id.titel_text);
        mDlgContent = (TextView) view.findViewById(R.id.content_text);

        mLeftBtn = (Button) view.findViewById(R.id.lef_btn);
        mRightBtn = (Button) view.findViewById(R.id.right_btn);

        boolean showTitle = mHasTitle;
        if (!StringUtils.isEmptyOrNull(mTitleTxt.toString()) && mHasTitle) {
            showTitle = true;

            mDlgTitle.setText(mTitleTxt);
            mDlgTitle.setVisibility(View.VISIBLE);
            //mDlgTitle.getPaint().setFakeBoldText(true);
            //mRightBtn.getPaint().setFakeBoldText(true);
            //	         int paddingTop = DeviceInfoUtil.getPixelFromDip(8);
            //	         mDlgContent.setMinHeight(DeviceInfoUtil.getPixelFromDip(40));
            //	         mDlgContent.setPadding(mDlgContent.getPaddingLeft(),
            //	                 paddingTop, mDlgContent.getPaddingRight(), mDlgContent.getPaddingBottom());
            //	         mDlgContent.requestLayout();
        } else {
            showTitle = false;

            mDlgTitle.setVisibility(View.GONE);
        }

        int contentGravity = gravity;
        if (!StringUtils.isEmptyOrNull(mContentTxt.toString())) {
            mDlgContent.setText(mContentTxt);

            if (!showTitle && contentGravity == -1) {
                contentGravity = Gravity.CENTER;
            }

            if (contentGravity != -1) {
                mDlgContent.setGravity(contentGravity);
            }

            if (!showTitle) {
                int minH = getContext().getResources().getDimensionPixelSize(R.dimen.dialog_content_mini_h_hastitle);
                mDlgContent.setMinHeight(minH);
            }
        }

        mExcuitePositiveListener = v -> {
            Fragment tarFragment = getTargetFragment();
            Activity activity = getActivity();
            try {
                if (positiveClickCallBack != null) {
                    positiveClickCallBack.callBack();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            dismissSelf();

            // fragment
            if (tarFragment != null && tarFragment instanceof HandleDialogFragmentEvent) {
                ((HandleDialogFragmentEvent) tarFragment).onPositiveBtnClick(mDialogTag);
            }
            // activity
            else if (activity != null && activity instanceof HandleDialogFragmentEvent) {
                ((HandleDialogFragmentEvent) activity).onPositiveBtnClick(mDialogTag);
            }
        };

        mExcuiteNegativeListener = v -> {
            try {
                if (negativeClickCallBack != null) {
                    negativeClickCallBack.callBack();
                }
            } catch (Exception e) {
                Logger.e(e);
            }

            Fragment tarFragment = getTargetFragment();
            Activity activity = getActivity();

            dismissSelf();

            if (tarFragment != null && tarFragment instanceof HandleDialogFragmentEvent) {
                ((HandleDialogFragmentEvent) tarFragment).onNegtiveBtnClick(mDialogTag);
            } else if (activity != null && activity instanceof HandleDialogFragmentEvent) {
                ((HandleDialogFragmentEvent) activity).onNegtiveBtnClick(mDialogTag);
            }
        };

        if (android.os.Build.VERSION.SDK_INT >= 14) {
            if (!StringUtils.isEmptyOrNull(mPositiveBtnTxt.toString())) {
                mRightBtn.setText(mPositiveBtnTxt);
            } else {
                mRightBtn.setText(R.string.ok);
            }
            mRightBtn.setOnClickListener(mExcuitePositiveListener);
            if (!StringUtils.isEmptyOrNull(mNegativeBtnTxt.toString())) {
                mLeftBtn.setText(mNegativeBtnTxt);
            } else {
                mLeftBtn.setText(R.string.cancel);
            }
            mLeftBtn.setOnClickListener(mExcuiteNegativeListener);
        } else {
            if (!StringUtils.isEmptyOrNull(mPositiveBtnTxt.toString())) {
                mLeftBtn.setText(mPositiveBtnTxt);
            } else {
                mLeftBtn.setText(R.string.ok);
            }
            mLeftBtn.setOnClickListener(mExcuitePositiveListener);
            if (!StringUtils.isEmptyOrNull(mNegativeBtnTxt.toString())) {
                mRightBtn.setText(mNegativeBtnTxt);
            } else {
                mRightBtn.setText(R.string.cancel);
            }
            mRightBtn.setOnClickListener(mExcuiteNegativeListener);
        }
        // ColorDrawable colorDrawable = new
        // ColorDrawable(getResources().getColor(R.color.background_dialog));
        // view.setBackgroundDrawable(colorDrawable);
        return view;
    }

}
