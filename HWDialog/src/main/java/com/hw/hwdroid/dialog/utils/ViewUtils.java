package com.hw.hwdroid.dialog.utils;

import android.graphics.Typeface;
import android.support.annotation.IntDef;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ChenJ on 16/4/5.
 */
public class ViewUtils {

    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    private @interface Visibility {
    }

    public static void setVisibility(View view, @Visibility int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    public static CharSequence getBlodString(CharSequence charSequence) {
        SpannableString spannableString = new SpannableString(charSequence);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 是否超出显示范围
     *
     * @param textView      TextView控件
     * @param text          写入的string
     * @param textViewWidth 控件宽度dip
     * @return
     */
    private boolean isOverTextView(TextView textView, String text, int textViewWidth) {
        if (null == textView || null == text) {
            return false;
        }

        TextPaint textPaint = textView.getPaint();
        int needWidth = (int) textPaint.measureText(text.toString());
        return needWidth >= textViewWidth;
    }

    /**
     * 在onCreate()即可获取View的宽高
     */
    public static int[] getViewMeasure(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
        return new int[]{view.getMeasuredWidth(), view.getMeasuredHeight()};
    }

    /**
     * ListView中提前测量View尺寸，如headerView
     */
    private void measureView(View view) {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int height;
        int tempHeight = p.height;
        if (tempHeight > 0) {
            height = View.MeasureSpec.makeMeasureSpec(tempHeight, View.MeasureSpec.EXACTLY);
        } else {
            height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        view.measure(width, height);
    }

}
