package com.hw.hwdroid.foundation.app.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.hw.hwdroid.foundation.R;
import com.hw.hwdroid.foundation.app.FoundationContext;
import com.hw.hwdroid.foundation.utils.ScreenUtils;
import com.hw.hwdroid.foundation.utils.StringUtils;
import com.hw.hwdroid.foundation.utils.UnitConverterUtils;
import com.hw.hwdroid.foundation.utils.widget.ViewUtils;
import com.orhanobut.logger.Logger;

import common.android.foundation.app.HActivityStack;


/**
 * TitleBar
 * <p>
 * Created by ChenJ on 2017/2/16.
 */

public class HTitleBarView extends LinearLayoutCompat {

    private final int MAX_WIDTH_DP = 100;

    private AppCompatTextView titleTv;
    private AppCompatTextView subTitleTv;

    private AppCompatImageView backBtn;
    private AppCompatTextView backTv;

    private AppCompatTextView menuTv;
    private AppCompatImageView menuBtn;

    private View dividerView;
    private LinearLayoutCompat titleBarBackView;
    private LinearLayoutCompat titleBarMenuView;
    private LinearLayoutCompat titleBarTitleView;

    private String initLeftText;
    private String initRightText;
    private final String titleFromAttribute;

    public final boolean iosMode;

    public final int MODE_BACK_TITLE = 0;
    public final int MODE_BACK = 1;
    public final int MODE_TITLE = 2;
    public final int MODE_BACK_MENU = 3;
    public final int MODE_BACK_MENUTEXT = 4;
    public final int MODE_BACKTEXT_MENUTEXT = 5;

    public int titleBarMode = MODE_BACK_TITLE;

    public HTitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HTitleBarView);
        iosMode = typedArray == null ? FoundationContext.getUseIosModeForTitleBar() : typedArray.getBoolean(R.styleable.HTitleBarView_htitlebar_ios, FoundationContext.getUseIosModeForTitleBar());
        LayoutInflater.from(context).inflate(iosMode ? R.layout.common_title_bar_ios : R.layout.common_title_bar, this);

        titleBarBackView = (LinearLayoutCompat) findViewById(R.id.titleBarBackView);
        titleBarTitleView = (LinearLayoutCompat) findViewById(R.id.titleBarTitleView);
        titleBarMenuView = (LinearLayoutCompat) findViewById(R.id.titleBarMenuView);

        backBtn = (AppCompatImageView) findViewById(R.id.titleBarBackBtn);
        backTv = (AppCompatTextView) findViewById(R.id.titleBarBackTv);
        dividerView = findViewById(R.id.titleBarDivider);

        titleTv = (AppCompatTextView) findViewById(R.id.titleBarTitleTv);
        subTitleTv = (AppCompatTextView) findViewById(R.id.titleBarSubtitleTv);

        menuBtn = (AppCompatImageView) findViewById(R.id.titleBarMenuBtn);
        menuTv = (AppCompatTextView) findViewById(R.id.titleBarMenuTv);

        if (iosMode && dividerView != null) {
            dividerView.setVisibility(View.GONE);
        }

        if (null != typedArray) {
            titleBarMode = typedArray.getInt(R.styleable.HTitleBarView_htitlebar_mode, MODE_BACK_TITLE);

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

            if (typedArray.hasValue(R.styleable.HTitleBarView_htitlebar_backtext_text)) {
                initLeftText = typedArray.getString(R.styleable.HTitleBarView_htitlebar_backtext_text);
            }

            if (typedArray.hasValue(R.styleable.HTitleBarView_htitlebar_menutext_text)) {
                initRightText = typedArray.getString(R.styleable.HTitleBarView_htitlebar_menutext_text);
            }

            backTv.setText(StringUtils.changeNull(initLeftText));
            menuTv.setText(StringUtils.changeNull(initRightText));

            typedArray.recycle();
        } else {
            titleFromAttribute = "";
        }

        titleBarBackView.setOnClickListener(v -> {
            if (getContext() instanceof Activity) {
                HActivityStack.INSTANCE.pop((Activity) getContext());
            } else {
                Activity activity = HActivityStack.INSTANCE.curr();
                if (null != activity) {
                    HActivityStack.INSTANCE.pop();
                }
            }
        });

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
     * @param isLightTheme
     */
    public void updateStyle(boolean isLightTheme) {
        if (iosMode) {
            backBtn.setImageResource(isLightTheme ? R.drawable.icon_back_vector_dark : R.drawable.icon_back_vector);
        } else {
            ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor(isLightTheme ? "#333333" : "#ffffff"));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                backBtn.setImageTintList(colorStateList);
            } else {
                ViewCompat.setBackgroundTintList(backBtn, colorStateList);
                ViewCompat.setBackgroundTintMode(backBtn, PorterDuff.Mode.SRC_IN);
            }
        }

        subTitleTv.setTextColor(getContext().getResources().getColor(isLightTheme ? R.color.gray_6e : R.color.gray_white_b8e1f4));
        titleTv.setTextColor(getContext().getResources().getColor(isLightTheme ? R.color.textColorPrimary : android.R.color.white));
        ColorStateList csl = getContext().getResources().getColorStateList(isLightTheme ? R.color.text_selector : R.color.text_white_selector);
        backTv.setTextColor(csl);
        menuTv.setTextColor(csl);
    }

    private void setVisibility4Model() {
        // 只有Title
        if (titleBarMode == MODE_TITLE) {
            titleBarBackView.removeAllViews();
            titleBarMenuView.removeAllViews();

            if (dividerView != null) {
                dividerView.setVisibility(View.GONE);
            }

            titleBarBackView.setVisibility(View.GONE);
            titleBarMenuView.setVisibility(View.GONE);
        }
        // 只有back
        else if (titleBarMode == MODE_BACK) {
            titleBarBackView.removeView(backTv);
            backBtn.setVisibility(View.VISIBLE);
            titleBarBackView.setVisibility(View.VISIBLE);

            titleBarMenuView.removeAllViews();
            titleBarMenuView.setVisibility(View.GONE);
        }
        // back + menu
        else if (titleBarMode == MODE_BACK_MENU) {
            titleBarBackView.removeView(backTv);
            backBtn.setVisibility(View.VISIBLE);
            titleBarBackView.setVisibility(View.VISIBLE);

            if (dividerView != null) {
                dividerView.setVisibility(View.GONE);
            }

            titleBarMenuView.removeView(menuTv);
            menuBtn.setVisibility(View.VISIBLE);
            titleBarMenuView.setVisibility(View.VISIBLE);
        }
        // back + menuText
        else if (titleBarMode == MODE_BACK_MENUTEXT) {
            titleBarBackView.removeView(backTv);
            backBtn.setVisibility(View.VISIBLE);
            titleBarBackView.setVisibility(View.VISIBLE);

            if (dividerView != null) {
                dividerView.setVisibility(View.GONE);
            }

            titleBarMenuView.removeView(menuBtn);
            menuTv.setVisibility(View.VISIBLE);
            titleBarMenuView.setVisibility(View.VISIBLE);
        }
        // cancel + menuText
        else if (titleBarMode == MODE_BACKTEXT_MENUTEXT) {
            titleBarBackView.removeView(backBtn);
            backTv.setVisibility(View.VISIBLE);
            titleBarBackView.setVisibility(View.VISIBLE);

            titleBarMenuView.removeView(menuBtn);
            menuTv.setVisibility(View.VISIBLE);
            titleBarMenuView.setVisibility(View.VISIBLE);
        }
        // MODE_BACK_TITLE
        else {
            titleBarBackView.removeView(backTv);
            backBtn.setVisibility(View.VISIBLE);
            titleBarBackView.setVisibility(View.VISIBLE);

            titleBarMenuView.removeAllViews();
            titleBarMenuView.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(backTv.getText())) {
            backBtn.setVisibility(View.GONE);
            backTv.setVisibility(View.VISIBLE);
            titleBarBackView.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(menuTv.getText())) {
            menuTv.setVisibility(View.VISIBLE);
            titleBarMenuView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 重新计算View的位置
     *
     * @param context
     */
    public void computeViewsWH(Context context) {
        if (iosMode) {
            return;
        }

        int sw = ScreenUtils.getScreenWH(context)[0] / 4;
        int maxW = UnitConverterUtils.getPixelFromDip(context, MAX_WIDTH_DP);
        if (maxW > (sw)) {
            maxW = sw;
        }

        int[] leftLayoutWH = ViewUtils.getViewMeasure(titleBarBackView);
        int[] rightLayoutWH = ViewUtils.getViewMeasure(titleBarMenuView);

        int centerPaddingL = leftLayoutWH[0];
        int centerPaddingR = rightLayoutWH[0];

        int w = leftLayoutWH[0] > rightLayoutWH[0] ? leftLayoutWH[0] : rightLayoutWH[0];

        if (w > maxW) {
            titleBarBackView.getLayoutParams().width = maxW;
            titleBarBackView.requestLayout();
            centerPaddingL = maxW;

            titleBarMenuView.getLayoutParams().width = maxW;
            titleBarMenuView.requestLayout();
            centerPaddingR = maxW;

        }

        centerPaddingL += UnitConverterUtils.getPixelFromDip(context, 5);
        centerPaddingR += UnitConverterUtils.getPixelFromDip(context, 5);

        titleBarTitleView.setPadding(centerPaddingL, 0, centerPaddingR, 0);
    }

    public AppCompatTextView getTitleTextView() {
        return titleTv;
    }

    public AppCompatTextView getMenuTextView() {
        return menuTv;
    }

    public AppCompatTextView getBackTextView() {
        return backTv;
    }

    public LinearLayoutCompat getTitleBarBackView() {
        return titleBarBackView;
    }

    public LinearLayoutCompat getTitleBarTitleView() {
        return titleBarTitleView;
    }

    public LinearLayoutCompat getTitleBarMenuView() {
        return titleBarMenuView;
    }

    public AppCompatImageView getBackButton() {
        return backBtn;
    }

    public AppCompatImageView getMenuButton() {
        return menuBtn;
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
        titleBarBackView.removeAllViews();
        titleBarMenuView.removeAllViews();

        titleBarBackView.setVisibility(View.GONE);
        titleBarMenuView.setVisibility(View.GONE);
        titleBarTitleView.setVisibility(View.VISIBLE);
    }

    /**
     * Done-Cancel模式
     */
    public void setCancelDoneModel() {
        setCancelDoneModel(R.string.save);
    }

    /**
     * Done-Cancel模式
     *
     * @param doneRes
     */
    public void setCancelDoneModel(@StringRes int doneRes) {
        setCancelDoneModel(R.string.cancel, doneRes);
    }

    /**
     * Done-Cancel模式
     *
     * @param cancelRes
     * @param doneRes
     */
    public void setCancelDoneModel(@StringRes int cancelRes, @StringRes int doneRes) {
        setCancelDoneModel(getContext().getString(cancelRes), getContext().getString(doneRes));
    }

    /**
     * Done-Cancel模式
     *
     * @param cancel
     * @param done
     */
    public void setCancelDoneModel(String cancel, String done) {
        menuBtn.setVisibility(View.GONE);
        ViewUtils.removeView(titleBarMenuView, menuBtn);

        setBackButtonVisibility(false);

        setBackText(cancel);
        setMenuText(done);
    }

    public void setBackText(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            backTv.setVisibility(View.GONE);

            if (titleBarBackView.getVisibility() != View.VISIBLE) {
                return;
            }

            final int childCount = titleBarBackView.getChildCount();
            if (childCount <= 0) {
                titleBarBackView.setVisibility(View.GONE);
                return;
            }

            boolean needShow = false;
            for (int index = 0; index < childCount; index++) {
                View child = titleBarBackView.getChildAt(index);
                if (child == null) {
                    continue;
                }

                if (child.getVisibility() == View.VISIBLE) {
                    needShow = true;
                    break;
                }
            }

            titleBarBackView.setVisibility(needShow ? View.VISIBLE : View.GONE);
        } else {
            ViewUtils.addView(titleBarBackView, backTv, true);

            backTv.setText(text);
            backTv.setVisibility(View.VISIBLE);
            titleBarBackView.setVisibility(View.VISIBLE);
        }

        computeViewsWH(getContext());
    }

    /**
     * 设置右边Text
     *
     * @param textResId
     */
    public void setMenuText(@StringRes int textResId) {
        try {
            setMenuText(StringUtils.changeNull(getContext().getString(textResId)));
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    /**
     * 设置右边Text
     *
     * @param text
     */
    public void setMenuText(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            menuTv.setVisibility(View.GONE);

            if (titleBarMenuView.getVisibility() != View.VISIBLE) {
                return;
            }

            int childCount = titleBarMenuView.getChildCount();
            if (childCount <= 0) {
                return;
            }

            boolean needShow = false;
            for (int index = 0; index < childCount; index++) {
                View child = titleBarMenuView.getChildAt(index);
                if (null == child) {
                    continue;
                }

                if (child.getVisibility() == View.VISIBLE) {
                    needShow = true;
                    break;
                }
            }

            titleBarMenuView.setVisibility(needShow ? View.VISIBLE : View.GONE);
        } else {
            ViewUtils.addView(titleBarMenuView, menuTv, true);

            menuTv.setText(text);
            menuTv.setVisibility(View.VISIBLE);
            titleBarMenuView.setVisibility(View.VISIBLE);
        }

        computeViewsWH(getContext());
    }

    public void setMenuImage(Drawable drawable, boolean cleanOtherViews) {
        if (menuBtn == null) {
            return;
        }

        if (cleanOtherViews) {
            titleBarMenuView.removeAllViews();
            titleBarMenuView.addView(menuBtn);
        } else if (!ViewUtils.containView(titleBarMenuView, menuBtn)) {
            titleBarMenuView.addView(menuBtn);
        }

        menuBtn.setImageDrawable(drawable);
        menuBtn.setVisibility(View.VISIBLE);
        titleBarMenuView.setVisibility(View.VISIBLE);

        computeViewsWH(getContext());
    }

    public void setMenuImage(@DrawableRes int resId, boolean cleanOtherViews) {
        if (null == menuBtn) {
            return;
        }

        if (cleanOtherViews) {
            titleBarMenuView.removeAllViews();
            titleBarMenuView.addView(menuBtn);
        } else if (!ViewUtils.containView(titleBarMenuView, menuBtn)) {
            titleBarMenuView.addView(menuBtn);
        }

        menuBtn.setImageResource(resId);
        menuBtn.setVisibility(View.VISIBLE);
        titleBarMenuView.setVisibility(View.VISIBLE);

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

}
