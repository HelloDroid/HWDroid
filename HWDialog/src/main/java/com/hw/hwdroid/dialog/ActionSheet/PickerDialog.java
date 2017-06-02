package com.hw.hwdroid.dialog.ActionSheet;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hw.hwdroid.dialog.R;
import com.hw.hwdroid.dialog.utils.StringUtils;
import com.hw.hwdroid.dialog.wegit.CNumberPickerView;

import java.util.List;


/**
 * PickerView Dialog
 * <p>
 * Created by ChenJ on 2017/5/4.
 */

public class PickerDialog extends Dialog {

    private List<String> actions;
    private boolean isFromBottom;

    private TextView mTitleTv;
    private TextView mOkButton;
    private CNumberPickerView mPicker;

    private onClickOk onClickOk;

    public PickerDialog(@NonNull Context context, boolean isFromBottom, int index, String title, List<String> actions) {
        super(context, isFromBottom ? R.style.DialogTheme_Full : R.style.DialogTheme);

        this.index = index;
        this.actions = actions;
        this.isFromBottom = isFromBottom;

        setCancelable(false);
        setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(context).inflate(R.layout.c_number_picker, null);
        setContentView(view);

        if (this.isFromBottom) {
            Window window = getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setWindowAnimations(R.style.Slide_FromBottom);

            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.alpha = 0.9876f;
            layoutParams.dimAmount = 0.6789f;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        }

        mOkButton = (TextView) view.findViewById(R.id.ok_btn);
        mTitleTv = (TextView) view.findViewById(R.id.title_tv);
        mPicker = (CNumberPickerView) view.findViewById(R.id.c_picker);

        mTitleTv.setText(StringUtils.changeNullOrWhiteSpace(title));
        mTitleTv.setVisibility(StringUtils.isNullOrWhiteSpace(title) ? View.GONE : View.VISIBLE);

        view.findViewById(R.id.cancel_btn).setOnClickListener(v -> {
            dismiss();
            mPicker.setValue(this.index);
        });

        mOkButton.setOnClickListener(v -> {
            this.index = mPicker.getValue();

            if (null != onClickOk) {
                onClickOk.onClick(this.index, actions.get(this.index));
            }
        });

        init();
    }

    private int index;

    @Override
    public void show() {
        super.show();
        this.index = mPicker.getValue();
    }

    private void init() {
        mPicker.setDisplayedValues(actions.toArray(new String[]{}));
        mPicker.setMinValue(0);
        mPicker.setMaxValue(actions.size() - 1);
        mPicker.setValue(index);

        mPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            String newV = actions.get(newVal);
        });
    }

    public void setOnClickOk(PickerDialog.onClickOk onClickOk) {
        this.onClickOk = onClickOk;
    }

    public interface onClickOk {
        void onClick(int index, String item);
    }

}
