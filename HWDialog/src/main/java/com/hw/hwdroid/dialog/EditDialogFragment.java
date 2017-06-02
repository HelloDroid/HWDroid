package com.hw.hwdroid.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hw.hwdroid.dialog.model.exchangeModel.DialogExchangeModel;
import com.hw.hwdroid.dialog.utils.ViewUtils;


/**
 */
public class EditDialogFragment extends BaseDialogFragment {

    private TextView titleView;
    private TextView contentView;
    private EditText editView;
    private TextView positiveView;
    private TextView negativeView;
    private View btnLine;
    private View containerView;

    private String editHint;

    private View.OnClickListener onTouchOutsideListener;

    public EditDialogFragment() {
    }

    public static EditDialogFragment getInstance(Bundle bundle) {
        EditDialogFragment baseDialogFragment = new EditDialogFragment();
        baseDialogFragment.setArguments(bundle);
        return baseDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_edit_dialog_layout, container, false);
        titleView = (TextView) view.findViewById(R.id.titel_text);
        contentView = (TextView) view.findViewById(R.id.content_text);
        editView = (EditText) view.findViewById(R.id.edit_text);
        negativeView = (TextView) view.findViewById(R.id.lef_btn);
        btnLine = view.findViewById(R.id.btn_line);
        positiveView = (TextView) view.findViewById(R.id.right_btn);
        containerView = view.findViewById(R.id.container);
        return view;
    }

    public void hideInputSoft() {
        hideIme(editView);
    }

    /**
     * hide ime
     *
     * @param v
     */
    public static void hideIme(View v) {
        if (v == null)
            return;

        Context context = v.getContext();
        if (context != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // title
        ViewUtils.setVisibility(titleView, TextUtils.isEmpty(mTitleTxt) ? View.GONE : View.VISIBLE);
        titleView.setText(mTitleTxt);

        // content
        contentView.setText(mContentTxt);

        // pos btn
        CharSequence positiveTxt = ViewUtils.getBlodString(mPositiveBtnTxt);

        if (TextUtils.isEmpty(positiveTxt)) {
            ViewUtils.setVisibility(positiveView, View.GONE);
            ViewUtils.setVisibility(btnLine, View.GONE);
        } else {
            positiveView.setText(positiveTxt);
            positiveView.setOnClickListener((v) -> {
                if (compatibilityPositiveListener != null) {
                    compatibilityPositiveListener.onClick(v);
                }
            });
        }
        // neg btn
        if (TextUtils.isEmpty(mNegativeBtnTxt)) {
            ViewUtils.setVisibility(negativeView, View.GONE);
        } else {
            negativeView.setText(mNegativeBtnTxt);
            negativeView.setOnClickListener(v -> {
                if (compatibilityNegativeListener != null) {
                    compatibilityNegativeListener.onClick(v);
                }
                dismiss();
            });
        }
        // edit
        editView.setHint(editHint);

        setCancelable(false);

        containerView.setOnTouchListener((v, event) -> {
            if (onTouchOutsideListener != null) {
                onTouchOutsideListener.onClick(containerView);
            }
            return false;
        });
    }

    public void setPositiveListener(View.OnClickListener listener) {
        compatibilityPositiveListener = listener;
        if (positiveView != null) {
            positiveView.setOnClickListener(listener);
        }
    }

    public void setNegativeListener(View.OnClickListener listener) {
        compatibilityNegativeListener = listener;
        if (negativeView != null) {
            negativeView.setOnClickListener(listener);
        }
    }

    public void setOnTouchOutsideListener(View.OnClickListener onTouchOutsideListener) {
        this.onTouchOutsideListener = onTouchOutsideListener;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (onTouchOutsideListener != null) {
            onTouchOutsideListener.onClick(null);
        }
    }

    public String getEditContent() {
        return editView.getText().toString();
    }

    private void parseArguments() {
        if (getArguments() != null) {
            Bundle bundle = getArguments();

            DialogExchangeModel dialogExchangeModel = ((DialogExchangeModel.DialogExchangeModelBuilder) bundle.getSerializable(TAG)).create();

            if (dialogExchangeModel != null) {
                mDialogTag = dialogExchangeModel.getTag();
                mTitleTxt = dialogExchangeModel.getDialogTitle();
                mPositiveBtnTxt = dialogExchangeModel.getPostiveText();
                mNegativeBtnTxt = dialogExchangeModel.getNegativeText();
                mContentTxt = dialogExchangeModel.getDialogContext();
                gravity = dialogExchangeModel.getGravity();
                editHint = dialogExchangeModel.getEditHint();
            }
        }
    }

}

