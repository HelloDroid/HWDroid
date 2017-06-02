package com.hw.hwdroid.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hw.hwdroid.dialog.model.exchangeModel.DialogExchangeModel;


public class SingleInfoDialogFragment extends BaseDialogFragment {

    private TextView mDlgContent;
    private TextView mDlgButton;
    private TextView mDlgTitle;

    public static SingleInfoDialogFragment getInstance(Bundle bundle) {
        SingleInfoDialogFragment baseDialogFragment = new SingleInfoDialogFragment();
        baseDialogFragment.setArguments(bundle);
        return baseDialogFragment;
    }

    public SingleInfoDialogFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if (null == bundle) {
            return;
        }

        DialogExchangeModel dialogExchangeModel = ((DialogExchangeModel.DialogExchangeModelBuilder) bundle.getSerializable(TAG)).create();

        if (dialogExchangeModel != null) {
            mDialogTag = dialogExchangeModel.getTag();
            mTitleTxt = dialogExchangeModel.getDialogTitle();
            mSingleBtnTxt = dialogExchangeModel.getSingleText();
            mContentTxt = dialogExchangeModel.getDialogContext();
            gravity = dialogExchangeModel.getGravity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.common_dialog_single_layout, container, false);
        contentView.setOnClickListener(mSpaceClickListener);

        mDlgTitle = (TextView) contentView.findViewById(R.id.titel_text);

        boolean showTitle = mHasTitle;
        if (!TextUtils.isEmpty(mTitleTxt) && mHasTitle) {
            mDlgTitle.setText(mTitleTxt);

            showTitle = true;
            mDlgTitle.setVisibility(View.VISIBLE);
        } else {
            showTitle = false;

            mDlgTitle.setVisibility(View.GONE);
        }

        mDlgContent = (TextView) contentView.findViewById(R.id.content_text);

        int contentGravity = gravity;
        if (!TextUtils.isEmpty(mContentTxt)) {
            mDlgContent.setText(mContentTxt);
            // mDlgContent.setGravity(gravity);

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

        mDlgButton = (TextView) contentView.findViewById(R.id.single_btn);
        mDlgButton.setClickable(true);
        if (!TextUtils.isEmpty(mSingleBtnTxt)) {
            mDlgButton.setText(mSingleBtnTxt);
        }

        mDlgButton.setOnClickListener(v -> {
            Fragment tarFragment = getTargetFragment();
            Activity activity = getActivity();

            dismissSelf();

            try {
                if (singleClickCallBack != null) {
                    singleClickCallBack.callBack();
                }

                if (containerSingleCallBack != null) {
                    containerSingleCallBack.onSingleBtnClick(mDialogTag);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (tarFragment != null && tarFragment instanceof SingleDialogFragmentCallBack) {
                ((SingleDialogFragmentCallBack) tarFragment).onSingleBtnClick(mDialogTag);
            } else if (activity != null && activity instanceof SingleDialogFragmentCallBack) {
                ((SingleDialogFragmentCallBack) activity).onSingleBtnClick(mDialogTag);
            }
        });
        return contentView;
    }
}
