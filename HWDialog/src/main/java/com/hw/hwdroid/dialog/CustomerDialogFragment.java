package com.hw.hwdroid.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hw.hwdroid.dialog.model.exchangeModel.DialogExchangeModel;


public class CustomerDialogFragment extends BaseDialogFragment {
    public View customView;

    public static CustomerDialogFragment getInstance(Bundle bundle) {
        CustomerDialogFragment baseDialogFragment = new CustomerDialogFragment();
        baseDialogFragment.setArguments(bundle);
        return baseDialogFragment;
    }

    public CustomerDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null) {
            DialogExchangeModel dialogExchangeModel = ((DialogExchangeModel.DialogExchangeModelBuilder) bundle.getSerializable(TAG)).create();
            if (dialogExchangeModel != null) {
                mDialogTag = dialogExchangeModel.getTag();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = null;

        if (customView != null) {
            contentView = customView;
        } else {
            if (getTargetFragment() != null && getTargetFragment() instanceof CustomerFragmentCallBack) {
                contentView = ((CustomerFragmentCallBack) getTargetFragment()).getCustomerView(mDialogTag);
            } else if (getActivity() != null && getActivity() instanceof CustomerFragmentCallBack) {
                contentView = ((CustomerFragmentCallBack) getActivity()).getCustomerView(mDialogTag);
            }
        }

        FrameLayout layout = new FrameLayout(getActivity());
        layout.setClickable(true);
        layout.setOnClickListener(mSpaceClickListener);
        if (contentView != null && contentView.getLayoutParams() != null) {
            if (null == contentView.getParent()) {
                layout.addView(contentView, contentView.getLayoutParams());
            }
            contentView.setClickable(true);
        }

        return layout;
    }

}
