package com.hw.hwdroid.dialog.permission;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hw.hwdroid.dialog.R;


/**
 * Created by peng_j on 2015/12/24.
 */
public class HPermissionSettingDialog extends Dialog {


    public HPermissionSettingDialog(Context context) {
        super(context, R.style.PermissionDialog);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.permission_setting_dialog_layout);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            if (this.title == null) {
                this.title = "";
            }
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            if (this.title == null) {
                this.title = "";
            }
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public HPermissionSettingDialog create() {
            // instantiate the dialog with the custom Theme
            final HPermissionSettingDialog dialog = new HPermissionSettingDialog(context);

            // set the dialog title
            if (TextUtils.isEmpty(title)) {
                (dialog.findViewById(R.id.titel_text)).setVisibility(View.GONE);
                dialog.findViewById(R.id.content_text).setMinimumHeight(getPixelFromDip(context, 90f));
            } else {
                ((TextView) dialog.findViewById(R.id.titel_text)).setText(title);
                (dialog.findViewById(R.id.titel_text)).setVisibility(View.VISIBLE);
            }

            // set the confirm button
            if (positiveButtonText != null) {
                ((TextView) dialog.findViewById(R.id.lef_btn)).setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    dialog.findViewById(R.id.lef_btn).setOnClickListener(v -> {
                        positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                dialog.findViewById(R.id.lef_btn).setVisibility(View.GONE);
            }

            // set the cancel button
            if (negativeButtonText != null) {
                ((TextView) dialog.findViewById(R.id.right_btn)).setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    (dialog.findViewById(R.id.right_btn)).setOnClickListener(v -> {
                        negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                    });
                }
            } else {
                dialog.findViewById(R.id.right_btn).setVisibility(View.GONE);
            }

            // set the content message
            if (message != null) {
                ((TextView) dialog.findViewById(R.id.content_text)).setText(message);
            } else {
                dialog.findViewById(R.id.content_text).setVisibility(View.GONE);
            }

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            HPermissionsDispatcher.setPermissionSettingDialog(dialog);

            return dialog;
        }

        /**
         * Dip转换为实际屏幕的像素值
         *
         * @param context
         * @param dip
         * @return
         */
        private int getPixelFromDip(@NonNull Context context, float dip) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, dm) + 0.5f * (dip >= 0 ? 1 : -1));
        }
    }

}
