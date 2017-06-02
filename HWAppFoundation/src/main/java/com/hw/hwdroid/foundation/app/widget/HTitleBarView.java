package com.hw.hwdroid.foundation.app.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.hwdroid.foundation.R;
import com.hw.hwdroid.foundation.utils.ScreenUtils;
import com.hw.hwdroid.foundation.utils.StringUtils;
import com.hw.hwdroid.foundation.utils.UnitConverterUtils;
import com.hw.hwdroid.foundation.utils.widget.ViewUtils;
import com.orhanobut.logger.Logger;


/**
 * TitleBar
 * <p>
 * Created by ChenJ on 2017/2/16.
 */

public class HTitleBarView extends LinearLayout {

    private final int MAX_WIDTH_DP = 100;

    private TextView titleTv;
    private TextView subTitleTv;

    private TextView leftTv;
    private HBackButton backBtn;

    private TextView rightTv;
    private AppCompatImageView btnImage;

    private RelativeLayout leftLayout;
    private LinearLayout rightLayout;
    private LinearLayout centerLayout;

    private boolean showOnlyTitle;
    private boolean showOnlyBack;
    private boolean showBackAndHome;
    private boolean showBackAndRightText;
    private boolean showLeftAndRightText;

    private String initLeftText;
    private String initRightText;
    private final String titleFromAttribute;

    public HTitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.common_title_bar, this);

        backBtn = (HBackButton) findViewById(R.id.titleBar_back);
        leftTv = (TextView) findViewById(R.id.titleBar_left_tv);

        btnImage = (AppCompatImageView) findViewById(R.id.titleBar_btnImage);
        rightTv = (TextView) findViewById(R.id.titleBar_right_tv);

        titleTv = (TextView) findViewById(R.id.titleBar_title_tv);
        subTitleTv = (TextView) findViewById(R.id.titleBar_subTitle_tv);

        leftLayout = (RelativeLayout) findViewById(R.id.titleBar_left);
        centerLayout = (LinearLayout) findViewById(R.id.titleBar_center);
        rightLayout = (LinearLayout) findViewById(R.id.titleBar_right);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HTitleBarView);
        if (null != typedArray) {
            showOnlyTitle = typedArray.getBoolean(R.styleable.HTitleBarView_htitlebar_onlytitle, false);
            showOnlyBack = typedArray.getBoolean(R.styleable.HTitleBarView_htitlebar_onlyback, false);
            showBackAndHome = typedArray.getBoolean(R.styleable.HTitleBarView_htitlebar_back_home, false);
            showBackAndRightText = typedArray.getBoolean(R.styleable.HTitleBarView_htitlebar_back_righttext, false);
            showLeftAndRightText = typedArray.getBoolean(R.styleable.HTitleBarView_htitlebar_left_righttext, false);

            if (typedArray.hasValue(R.styleable.HTitleBarView_htitlebar_title_text)) {
                String title = typedArray.getString(R.styleable.HTitleBarView_htitlebar_title_text);
                titleFromAttribute = StringUtils.changeNull(title);
                if (!StringUtils.isNull(titleFromAttribute)) {
                    titleTv.setText(titleFromAttribute);
                }
            } else {
                titleFromAttribute = "";
            }

            if (typedArray.hasValue(R.styleable.HTitleBarView_htitlebar_subtitle_text)) {
                String subTitle = typedArray.getString(R.styleable.HTitleBarView_htitlebar_subtitle_text);
                if (!StringUtils.isNull(subTitle)) {
                    subTitleTv.setText(StringUtils.changeNull(subTitle));
                }
            }

            if (typedArray.hasValue(R.styleable.HTitleBarView_htitlebar_left_text)) {
                initLeftText = typedArray.getString(R.styleable.HTitleBarView_htitlebar_left_text);
            }

            if (typedArray.hasValue(R.styleable.HTitleBarView_htitlebar_right_text)) {
                initRightText = typedArray.getString(R.styleable.HTitleBarView_htitlebar_right_text);
            }

            leftTv.setText(StringUtils.changeNull(initLeftText));
            rightTv.setText(StringUtils.changeNull(initRightText));

            typedArray.recycle();
        } else {
            titleFromAttribute = "";
        }

        setVisibility4Model();
        computeViewsWH(context);
    }

    /**
     * xml设置的title
     *
     * @return
     */
    public String getTitleFromAttribute() {
        return titleFromAttribute;
    }

    /**
     * updateViews Style
     *
     * @param isLight
     */
    @SuppressWarnings("deprecation")
    public void updateStyle(boolean isLight) {
        if (isLight) {
            backBtn.setImageResource(isLight);
        }
        subTitleTv.setTextColor(getContext().getResources().getColor(isLight ? R.color.gray_6e : R.color.gray_white_b8e1f4));
        titleTv.setTextColor(getContext().getResources().getColor(isLight ? R.color.textColorPrimary : android.R.color.white));
        ColorStateList csl = getContext().getResources().getColorStateList(isLight ? R.color.text_selector : R.color.text_white_selector);
        leftTv.setTextColor(csl);
        rightTv.setTextColor(csl);
    }


    private void setVisibility4Model() {
        // 只有Title
        if (showOnlyTitle) {
            leftLayout.removeAllViews();
            rightLayout.removeAllViews();

            leftLayout.setVisibility(View.GONE);
            rightLayout.setVisibility(View.GONE);
        }
        // 只有back
        else if (showOnlyBack) {
            leftLayout.removeView(leftTv);
            backBtn.setVisibility(View.VISIBLE);
            leftLayout.setVisibility(View.VISIBLE);

            rightLayout.removeAllViews();
            rightLayout.setVisibility(View.GONE);
        }
        // back + home
        else if (showBackAndHome) {
            leftLayout.removeView(leftTv);
            backBtn.setVisibility(View.VISIBLE);
            leftLayout.setVisibility(View.VISIBLE);

            rightLayout.removeView(rightTv);
            btnImage.setVisibility(View.VISIBLE);
            rightLayout.setVisibility(View.VISIBLE);
        }
        // back + rightText
        else if (showBackAndRightText) {
            leftLayout.removeView(leftTv);
            backBtn.setVisibility(View.VISIBLE);
            leftLayout.setVisibility(View.VISIBLE);

            rightLayout.removeView(btnImage);
            rightTv.setVisibility(View.VISIBLE);
            rightLayout.setVisibility(View.VISIBLE);
        }
        // cancel + rightText
        else if (showLeftAndRightText) {
            leftLayout.removeView(backBtn);
            leftTv.setVisibility(View.VISIBLE);
            leftLayout.setVisibility(View.VISIBLE);

            rightLayout.removeView(btnImage);
            rightTv.setVisibility(View.VISIBLE);
            rightLayout.setVisibility(View.VISIBLE);
        } else {
            // rightLayout.removeAllViews();
            rightLayout.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(leftTv.getText())) {
            backBtn.setVisibility(View.GONE);
            leftTv.setVisibility(View.VISIBLE);
            leftLayout.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(rightTv.getText())) {
            rightTv.setVisibility(View.VISIBLE);
            rightLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 重新计算View的位置
     *
     * @param context
     */
    public void computeViewsWH(Context context) {
        // back + text
        if (backBtn.getVisibility() == View.VISIBLE && leftTv.getVisibility() == View.VISIBLE) {
            backBtn.setPadding(UnitConverterUtils.getPixelFromDip(context, 6), 0, 0, 0);
        }
        // only text
        if (backBtn.getVisibility() == View.GONE && leftTv.getVisibility() == View.VISIBLE) {
            backBtn.setPadding(UnitConverterUtils.getPixelFromDip(context, 10), 0, UnitConverterUtils.getPixelFromDip(context, 10), 0);
        }

        int sw = ScreenUtils.getScreenWH(context)[0] / 4;
        int maxW = UnitConverterUtils.getPixelFromDip(context, MAX_WIDTH_DP);
        if (maxW > (sw)) {
            maxW = sw;
        }

        int[] leftLayoutWH = ViewUtils.getViewMeasure(leftLayout);
        int[] rightLayoutWH = ViewUtils.getViewMeasure(rightLayout);

        int centerPaddingL = leftLayoutWH[0];
        int centerPaddingR = rightLayoutWH[0];

        int w = leftLayoutWH[0] > rightLayoutWH[0] ? leftLayoutWH[0] : rightLayoutWH[0];

        if (w > maxW) {
            leftLayout.getLayoutParams().width = maxW;
            leftLayout.requestLayout();
            centerPaddingL = maxW;

            rightLayout.getLayoutParams().width = maxW;
            rightLayout.requestLayout();
            centerPaddingR = maxW;

        }

        centerPaddingL += UnitConverterUtils.getPixelFromDip(context, 5);
        centerPaddingR += UnitConverterUtils.getPixelFromDip(context, 5);

        centerLayout.setPadding(centerPaddingL, 0, centerPaddingR, 0);
    }

    public TextView getTitleTextView() {
        return titleTv;
    }

    public TextView getRightTextView() {
        return rightTv;
    }

    public TextView getLeftTextView() {
        return leftTv;
    }

    public RelativeLayout getLeftLayout() {
        return leftLayout;
    }

    public LinearLayout getCenterLayout() {
        return centerLayout;
    }

    public LinearLayout getRightLayout() {
        return rightLayout;
    }

    public HBackButton getBackButton() {
        return backBtn;
    }

    public AppCompatImageView getButtonImage() {
        return btnImage;
    }

    public void setBackButtonVisibility(boolean visibility) {
        if (null == backBtn) {
            return;
        }

        backBtn.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置只显示Title
     */
    public void setShowOnlyTitle() {
        leftLayout.removeAllViews();
        rightLayout.removeAllViews();

        leftLayout.setVisibility(View.GONE);
        rightLayout.setVisibility(View.GONE);
        centerLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Done-Cancel模式
     *
     * @param activity
     */
    public void setCancelDoneModel(@NonNull Activity activity) {
        setCancelDoneModel(activity, R.string.save);
    }

    /**
     * Done-Cancel模式
     *
     * @param activity
     * @param doneRes
     */
    public void setCancelDoneModel(@NonNull Activity activity, @StringRes int doneRes) {
        setCancelDoneModel(activity, R.string.cancel, doneRes);
    }

    /**
     * Done-Cancel模式
     *
     * @param activity
     * @param cancelRes
     * @param doneRes
     */
    public void setCancelDoneModel(@NonNull Activity activity, @StringRes int cancelRes, @StringRes int doneRes) {
        setCancelDoneModel(activity, getContext().getString(cancelRes), getContext().getString(doneRes));
    }

    /**
     * Done-Cancel模式
     *
     * @param activity
     * @param cancel
     * @param done
     */
    public void setCancelDoneModel(@NonNull Activity activity, String cancel, String done) {
        btnImage.setVisibility(View.GONE);
        setBackButtonVisibility(false);
        setLeftText(cancel);
        setRightText(done);

        getLeftLayout().setOnClickListener(v -> activity.finish());
    }

    public void setLeftText(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            leftTv.setVisibility(View.GONE);

            if (leftLayout.getVisibility() != View.VISIBLE) {
                return;
            }

            int childCount = leftLayout.getChildCount();
            if (childCount <= 0) {
                leftLayout.setVisibility(View.GONE);
                return;
            }

            boolean needShow = false;
            for (int index = 0; index < childCount; index++) {
                View child = leftLayout.getChildAt(index);
                if (null == child) {
                    continue;
                }

                if (child.getVisibility() == View.VISIBLE) {
                    needShow = true;
                    break;
                }
            }

            leftLayout.setVisibility(needShow ? View.VISIBLE : View.GONE);

        } else {
            if (!containView(leftLayout, leftTv)) {
                leftLayout.addView(leftTv);
            }

            leftTv.setText(text);
            leftTv.setVisibility(View.VISIBLE);
            leftLayout.setVisibility(View.VISIBLE);
        }

        computeViewsWH(getContext());
    }

    /**
     * 设置右边Text
     *
     * @param textResId
     */
    public void setRightText(@StringRes int textResId) {
        try {
            String text = getContext().getString(textResId);
            setRightText(text);
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    /**
     * 设置右边Text
     *
     * @param text
     */
    public void setRightText(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            rightTv.setVisibility(View.GONE);

            if (rightLayout.getVisibility() != View.VISIBLE) {
                return;
            }

            int childCount = rightLayout.getChildCount();
            if (childCount <= 0) {
                return;
            }

            boolean needShow = false;
            for (int index = 0; index < childCount; index++) {
                View child = rightLayout.getChildAt(index);
                if (null == child) {
                    continue;
                }

                if (child.getVisibility() == View.VISIBLE) {
                    needShow = true;
                    break;
                }
            }

            rightLayout.setVisibility(needShow ? View.VISIBLE : View.GONE);
        } else {
            if (!containView(rightLayout, rightTv)) {
                rightLayout.addView(rightTv);
            }

            rightTv.setText(text);
            rightTv.setVisibility(View.VISIBLE);
            rightLayout.setVisibility(View.VISIBLE);
        }

        computeViewsWH(getContext());
    }

    public void setRightImage(Drawable drawable, boolean cleanOtherViews) {
        if (null == btnImage) {
            return;
        }

        if (cleanOtherViews) {
            rightLayout.removeAllViews();
            rightLayout.addView(btnImage);
        } else if (!containView(rightLayout, btnImage)) {
            rightLayout.addView(btnImage);
        }

        btnImage.setImageDrawable(drawable);
        btnImage.setVisibility(View.VISIBLE);
        rightLayout.setVisibility(View.VISIBLE);

        computeViewsWH(getContext());
    }

    public void setRightImage(@DrawableRes int resId, boolean cleanOtherViews) {
        if (null == btnImage) {
            return;
        }

        if (cleanOtherViews) {
            rightLayout.removeAllViews();
            rightLayout.addView(btnImage);
        } else if (!containView(rightLayout, btnImage)) {
            rightLayout.addView(btnImage);
        }

        btnImage.setImageResource(resId);
        btnImage.setVisibility(View.VISIBLE);
        rightLayout.setVisibility(View.VISIBLE);

        computeViewsWH(getContext());
    }

    /**
     * 设置Title
     *
     * @param text
     */
    public void setTitle(CharSequence text) {
        titleTv.setText(text);
    }

    /**
     * 设置SubTitle
     *
     * @param text
     */
    public void setSubTitleTv(CharSequence text) {
        subTitleTv.setText(text);
        subTitleTv.setVisibility(!StringUtils.isEmptyOrNull(text) ? View.VISIBLE : View.GONE);
    }

    public boolean containView(ViewGroup viewGroup, View view) {
        if (null == viewGroup || null == view) {
            return false;
        }

        return viewGroup.indexOfChild(view) != -1;
    }

}
