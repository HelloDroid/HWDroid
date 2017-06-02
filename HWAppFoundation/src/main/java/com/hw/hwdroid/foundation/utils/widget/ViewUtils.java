package com.hw.hwdroid.foundation.utils.widget;

import android.graphics.Typeface;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hw.hwdroid.foundation.utils.MatcherUtils;

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

    /**
     * 设置EditText只能输入中英文
     *
     * @param editText
     */
    public static void setEditTextFiltersChieseAndEnglish(@NonNull EditText editText) {
        if (null == editText) {
            return;
        }

        // 过滤匹配输入中英文
        editText.setFilters(new InputFilter[]{
                // source:新输入字符串
                // dstart、dend:原始字符串将被替换的起始和结束位置
                (source, start, end, dest, dstart, dend) -> {
                    String newSource = (source == null) ? "" : (source.toString()).substring(start, end);
                    int newSourceLen = (newSource == null) ? 0 : newSource.length();                    // 输入字符串长度
                    int destLen = (dest == null) ? 0 : dest.length();                                   // 原始字符串长度
                    String coverDest = (dest == null) ? "" : (dest.toString()).substring(dstart, dend); // 将会被覆盖的字符串
                    int coverLen = (coverDest == null) ? 0 : coverDest.length();                        // 将会被覆盖的字符串长度

                    // 匹配中英文
                    if (MatcherUtils.matches("^[\\u4e00-\\u9fa5a-zA-Z]+$", newSource)) {
                        return source;
                    } else {
                        if (null == coverDest || coverDest.length() == 0) {
                            return "";
                        } else {
                            return coverDest;
                        }
                    }
                }
        });
    }


}
