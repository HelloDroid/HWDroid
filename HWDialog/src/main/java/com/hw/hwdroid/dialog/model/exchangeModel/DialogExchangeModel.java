package com.hw.hwdroid.dialog.model.exchangeModel;

import android.view.Gravity;
import android.view.View;

import com.hw.hwdroid.dialog.model.DialogType;

import java.io.Serializable;


/**
 * Created by ChenJ on 16/4/5.
 */
public class DialogExchangeModel implements Serializable {


    public DialogExchangeModelBuilder dialogExchangeModelBuilder;

    public View.OnClickListener compatibilityListener;// 错误弹框 按键点击事件

    public DialogExchangeModel(DialogExchangeModelBuilder dialogExchangeModelBuilder) {
        this.dialogExchangeModelBuilder = dialogExchangeModelBuilder;
    }

    public DialogType getDialogType() {
        return dialogExchangeModelBuilder.dialogType;
    }

    public CharSequence getDialogTitle() {
        return dialogExchangeModelBuilder.dialogTitle;
    }

    public CharSequence getDialogContext() {
        return dialogExchangeModelBuilder.dialogContext;
    }

    public boolean isHasTitle() {
        return dialogExchangeModelBuilder.hasTitle;
    }

    public CharSequence getPostiveText() {
        return dialogExchangeModelBuilder.postiveText;
    }

    public CharSequence getNegativeText() {
        return dialogExchangeModelBuilder.negativeText;
    }

    public String getTag() {
        return dialogExchangeModelBuilder.tag;
    }

    public CharSequence getSingleText() {
        return dialogExchangeModelBuilder.singleText;
    }

    public boolean isBussinessCancleable() {
        return dialogExchangeModelBuilder.isBussinessCancleable;
    }

    public boolean isBackable() {
        return dialogExchangeModelBuilder.isBackable;
    }

    public boolean isSpaceable() {
        return dialogExchangeModelBuilder.isSpaceable;
    }

    public int getGravity() {
        return dialogExchangeModelBuilder.gravity;
    }

    public boolean isSingleLine() {
        return dialogExchangeModelBuilder.isSingleLine;
    }

    public int getWidth() {
        return dialogExchangeModelBuilder.iWidth;
    }

    public int getHeight() {
        return dialogExchangeModelBuilder.iHeight;
    }

    public String getEditHint() {
        return dialogExchangeModelBuilder.editHint;
    }

    public View.OnClickListener getCompatibilityNegativeListener() {
        return dialogExchangeModelBuilder.compatibilityNegativeListener;
    }

    public View.OnClickListener getCompatibilityPositiveListener() {
        return dialogExchangeModelBuilder.compatibilityPositiveListener;
    }

    public static class DialogExchangeModelBuilder implements Serializable {
        private static final long serialVersionUID = -3548316127754197414L;
        /**
         * 弹出框类型
         */
        private DialogType dialogType = DialogType.SINGLE;
        /**
         * 弹出框标题
         */
        private CharSequence dialogTitle = "";
        /**
         * 弹出框内容
         */
        private CharSequence dialogContext = "";
        /**
         * 是否显示标题
         */
        private boolean hasTitle = false;
        /**
         * 确认按键
         */
        private CharSequence postiveText = "";
        /**
         * 取消按键
         */
        private CharSequence negativeText = "";
        /**
         * 单按键
         */
        private CharSequence singleText = "";
        /**
         * tag
         */
        private String tag = "";
        /**
         * back可点（默认可点）
         */
        private boolean isBackable = true;
        /**
         * 空白可点（默认可点）
         */
        private boolean isSpaceable = true;
        /**
         * 服务可取消(默认可取消)
         */
        private boolean isBussinessCancleable = true;

        private int gravity = Gravity.CENTER;

        /**
         * 是否单行显示用于多行显示提示信息:火车票 yechen add
         */
        private boolean isSingleLine = true;

        /**
         * edit dialog hint
         */
        private String editHint;

        private int iWidth;
        private int iHeight;

        public View.OnClickListener compatibilityListener;// 错误弹框 按键点击事件

        public View.OnClickListener compatibilityPositiveListener;
        public View.OnClickListener compatibilityNegativeListener;
        public View.OnClickListener compatibilityCancelListener;

        public DialogExchangeModelBuilder(DialogType hdDialogType, String tag) {
            this.dialogType = hdDialogType;
            this.tag = tag;
        }

        public DialogExchangeModelBuilder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public DialogExchangeModelBuilder setDialogType(DialogType hdDialogType) {
            this.dialogType = hdDialogType;
            return this;
        }

        public DialogExchangeModelBuilder setDialogTitle(CharSequence dialogTitle) {
            this.dialogTitle = dialogTitle;
            if (this.dialogTitle == null) {
                this.dialogTitle = "";
            }
            return this;
        }

        public DialogExchangeModelBuilder setDialogContext(CharSequence dialogContext) {
            this.dialogContext = dialogContext;
            if (this.dialogTitle == null) {
                this.dialogTitle = "";
            }
            return this;
        }

        public DialogExchangeModelBuilder setHasTitle(boolean hasTitle) {
            this.hasTitle = hasTitle;
            return this;
        }

        public DialogExchangeModelBuilder setPostiveText(CharSequence postiveText) {
            this.postiveText = postiveText;
            if (this.postiveText == null) {
                this.postiveText = "";
            }
            return this;
        }

        public DialogExchangeModelBuilder setNegativeText(CharSequence negativeText) {
            this.negativeText = negativeText;
            if (this.negativeText == null) {
                this.negativeText = "";
            }
            return this;
        }

        public DialogExchangeModelBuilder setSingleText(CharSequence singleText) {
            this.singleText = singleText;
            if (this.singleText == null) {
                this.singleText = "";
            }
            return this;
        }

        public DialogExchangeModelBuilder setBackable(boolean isBackable) {
            this.isBackable = isBackable;
            return this;
        }

        public DialogExchangeModelBuilder setBussinessCancleable(boolean isBussinessCancleable) {
            this.isBussinessCancleable = isBussinessCancleable;
            return this;
        }

        public DialogExchangeModelBuilder setSpaceable(boolean isSpaceable) {
            this.isSpaceable = isSpaceable;
            return this;
        }

        public DialogExchangeModelBuilder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public DialogExchangeModelBuilder setCompatibilityListener(View.OnClickListener compatibilityListener) {
            this.compatibilityListener = compatibilityListener;
            return this;
        }

        public DialogExchangeModelBuilder setCompatibilityPositiveListener(View.OnClickListener compatibilityPositiveListener) {
            this.compatibilityPositiveListener = compatibilityPositiveListener;
            return this;
        }

        public DialogExchangeModelBuilder setCompatibilityNegativeListener(View.OnClickListener compatibilityNegativeListener) {
            this.compatibilityNegativeListener = compatibilityNegativeListener;
            return this;
        }

        public DialogExchangeModelBuilder setCompatibilityCancelListener(View.OnClickListener compatibilityCancelListener) {
            this.compatibilityCancelListener = compatibilityCancelListener;
            return this;
        }

        public DialogExchangeModelBuilder setEditHint(String editHint) {
            this.editHint = editHint;
            return this;
        }

        /**
         * 方法描述 提示文本是否换行
         *
         * @param isSingleLine
         */
        public DialogExchangeModelBuilder setIsSingleLine(boolean isSingleLine) {
            this.isSingleLine = isSingleLine;
            return this;
        }

        /**
         * 方法描述 设置对话框大小
         *
         * @param width
         * @param height
         */
        public DialogExchangeModelBuilder setLayoutParams(int width, int height) {
            this.iWidth = width;
            this.iHeight = height;
            return this;
        }

        public DialogExchangeModel create() {
            return new DialogExchangeModel(this);
        }
    }

}
