package com.hw.hwdroid.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.hwdroid.dialog.model.exchangeModel.DialogExchangeModel;
import com.hw.hwdroid.dialog.utils.StringUtils;
import com.hw.hwdroid.dialog.utils.ThreadStateEnum;
import com.hw.hwdroid.dialog.utils.ThreadStateManager;


/**
 * 实现显示加载进度框效果。
 */
public class ProcessDialogFragment extends BaseDialogFragment {

    private TextView mDlgContent;
    private View mDlgButton;

    public String mTag;

    private boolean isCancelable;

    private RelativeLayout mMainLayout;


    private int iWidth;
    private int iHeight;

    /** 提示文本是否多行显示 */
    private boolean bIsSingleLine = true;

    public static ProcessDialogFragment getInstance(Bundle bundle) {
        ProcessDialogFragment baseDialogFragment = new ProcessDialogFragment();
        baseDialogFragment.setArguments(bundle);
        return baseDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null) {
            DialogExchangeModel dialogExchangeModel = ((DialogExchangeModel.DialogExchangeModelBuilder) bundle.getSerializable(TAG)).create();
            if (dialogExchangeModel != null) {
                mDialogTag = dialogExchangeModel.getTag();
                mTag = dialogExchangeModel.getTag();
                mContentTxt = dialogExchangeModel.getDialogContext();
                isCancelable = dialogExchangeModel.isBussinessCancleable();
                bIsSingleLine = dialogExchangeModel.isSingleLine();
                iWidth = dialogExchangeModel.getWidth();
                iHeight = dialogExchangeModel.getHeight();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.common_process_load_data_layout, container, false);
        layoutView.setOnClickListener(mSpaceClickListener);
        mMainLayout = (RelativeLayout) layoutView.findViewById(R.id.process_main_layout);
        mDlgContent = (TextView) layoutView.findViewById(R.id.tip);

        if (!StringUtils.isEmptyOrNull(mContentTxt.toString())) {
            mDlgContent.setText(mContentTxt);
        }

        // 设置对话框大小
        if (iWidth != 0 && iHeight != 0) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(iWidth, iHeight);
            layoutParams.gravity = Gravity.CENTER;
            mMainLayout.setLayoutParams(layoutParams);
        }

        // 设置是否单行显示
        mDlgContent.setSingleLine(bIsSingleLine);

        mDlgButton = layoutView.findViewById(R.id.btn_cancel);
        mDlgButton.setOnClickListener(v -> {
            Fragment tarFragment = getTargetFragment();
            Activity activity = getActivity();
            dismissSelf();
            try {
                if (singleClickCallBack != null) {
                    singleClickCallBack.callBack();
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

        if (!isCancelable) {
            mDlgButton.setVisibility(View.GONE);
        } else {
            mDlgButton.setVisibility(View.VISIBLE);
        }

        return layoutView;
    }

    /**
     * 方法描述 动态显示文本
     *
     * @param content
     */
    public void setContentText(String content) {
        this.mContentTxt = content;

        if (mDlgContent != null) {
            mDlgContent.setText(this.mContentTxt);
        }
    }

    @Override
    public void dismiss() {
        if (!StringUtils.isEmptyOrNull(mTag)) {
            ThreadStateManager.setThreadState(mTag, ThreadStateEnum.cancel);
        }

        super.dismiss();
    }

    @Override
    public void dismissSelf() {
        if (!StringUtils.isEmptyOrNull(mTag)) {
            ThreadStateManager.setThreadState(mTag, ThreadStateEnum.cancel);
        }
        super.dismissSelf();
    }
}
