package com.hw.hwdroid.foundation.utils.widget;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.provider.Browser;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.hw.hwdroid.foundation.utils.StringUtils;

import java.util.List;

/**
 * TextView Span Util
 *
 * @author chenj
 * @date 2014-1-13
 */
public class TextViewSpanUtils {

    /**
     * 渲染字体前景色
     *
     * @param tv    TextView
     * @param color 颜色
     */
    public static void foregroundColor(TextView tv, int color) {
        if (tv == null) {
            return;
        }

        if (tv.getText() == null || StringUtils.isNullOrWhiteSpace(tv.getText())) {
            return;
        }

        int end = tv.getText().length() - 1;

        SpannableStringBuilder spannableSB = new SpannableStringBuilder(tv.getText());
        spannableSB.setSpan(new ForegroundColorSpan(color), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spannableSB);
    }

    /**
     * 渲染字体前景色
     *
     * @param tv    TextView
     * @param start 起始位置
     * @param end   结束位置
     * @param color 颜色
     */
    public static void foregroundColor(TextView tv, int start, int end, int color) {
        if (tv == null) {
            return;
        }

        if (tv.getText() == null || StringUtils.isNullOrWhiteSpace(tv.getText())) {
            return;
        }

        SpannableStringBuilder spannableSB = new SpannableStringBuilder(tv.getText());
        spannableSB.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spannableSB);
    }

    /**
     * 渲染字体前景色
     *
     * @param tv
     * @param indexList
     * @param color
     */
    public static void foregroundColor(TextView tv, List<Integer[]> indexList, int color) {
        if (tv == null || null == indexList || indexList.isEmpty()) {
            return;
        }

        if (tv.getText() == null || StringUtils.isNullOrWhiteSpace(tv.getText())) {
            return;
        }

        SpannableStringBuilder spannableStr = new SpannableStringBuilder(tv.getText());

        for (int i = 0; i < indexList.size(); i++) {
            Integer[] index = indexList.get(i);
            if (index != null && index.length == 2 && index[0] != index[1]) {
                spannableStr.setSpan(new ForegroundColorSpan(color), index[0], index[1], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        tv.setText(spannableStr);
    }

    /**
     * 渲染字体前景色
     *
     * @param context
     * @param tv         TextView
     * @param start      起始位置
     * @param end        结束位置
     * @param appearance TextAppearanceSpan,用于new TextAppearanceSpan(context, appearance)
     */
    public static void foregroundColorSpan(Context context, TextView tv, int start, int end, int appearance) {
        if (tv == null) {
            return;
        }

        if (tv.getText() == null || StringUtils.isEmptyOrNull(tv.getText())) {
            return;
        }

        if (start < 0) {
            return;
        }

        if (end > tv.getText().length()) {
            return;
        }

        Spannable span = (Spannable) tv.getText();
        TextAppearanceSpan textappearancespan = new TextAppearanceSpan(context, appearance);
        span.setSpan(textappearancespan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 渲染字体前景色
     *
     * @param context
     * @param tv         TextView
     * @param appearance R.style.TextColor_60D4FD
     */
    public static void foregroundColorSpan(Context context, TextView tv, int appearance) {
        if (tv == null) {
            return;
        }

        if (tv.getText() == null || StringUtils.isEmptyOrNull(tv.getText())) {
            return;
        }

        Spannable span = (Spannable) tv.getText();
        TextAppearanceSpan textappearancespan = new TextAppearanceSpan(context, appearance);
        span.setSpan(textappearancespan, 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 设置带有前景色的TextView
     *
     * @param context
     * @param tv         TextView
     * @param text       字符串
     * @param appearance TextAppearanceSpan属性
     */
    public static void setTextPaintForegroundColor(Context context, TextView tv, String text, int appearance) {
        if (tv == null) {
            return;
        }

        if (StringUtils.isEmptyOrNull(text)) {
            return;
        }

        Spannable span = (Spannable) tv.getText();
        TextAppearanceSpan textappearancespan = new TextAppearanceSpan(context, appearance);
        span.setSpan(textappearancespan, 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 设置带有前景色的TextView
     *
     * @param context
     * @param tv         TextView
     * @param text       字符串
     * @param start      起始位置
     * @param end        结束位置
     * @param appearance TextAppearanceSpan属性
     */
    public static void setTextPaintForegroundColor(Context context, TextView tv, String text, int start, int end, int appearance) {
        if (tv == null) {
            return;
        }

        if (StringUtils.isEmptyOrNull(text)) {
            return;
        }

        if (start < 0) {
            return;
        }

        if (end > text.length()) {
            return;
        }

        Spannable span = (Spannable) tv.getText();
        TextAppearanceSpan textappearancespan = new TextAppearanceSpan(context, appearance);
        span.setSpan(textappearancespan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 添加下划线
     *
     * @param context
     * @param tv         TextView
     * @param start      起始位置
     * @param end        结束位置
     * @param appearance TextAppearanceSpan属性
     */
    public static void setUnderline(Context context, TextView tv, int start, int end, int appearance) {
        if (tv == null) {
            return;
        }

        if (tv.getText() == null || StringUtils.isEmptyOrNull(tv.getText())) {
            return;
        }

        if (start < 0) {
            return;
        }

        if (end > tv.getText().length()) {
            return;
        }

        Spannable span = (Spannable) tv.getText();
        TextAppearanceSpan textappearancespan = new TextAppearanceSpan(context, appearance);
        span.setSpan(textappearancespan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 设置TextView部分字符可点击
     *
     * @param context
     * @param tv              TextView
     * @param start           起始位置
     * @param end             结束位置
     * @param appearance      TextAppearanceSpan属性
     * @param onClickListener onClick Listener
     */
    public static void setTextClickableSpan(Context context, TextView tv, int start, int end, int appearance, ClickableSpan onClickListener) {
        if (tv == null) {
            return;
        }

        if (tv.getText() == null || StringUtils.isEmptyOrNull(tv.getText())) {
            return;
        }

        if (start < 0) {
            return;
        }

        if (end > tv.getText().length()) {
            return;
        }

        Spannable span = (Spannable) tv.getText();
        TextAppearanceSpan textappearancespan = new TextAppearanceSpan(context, appearance);
        if (onClickListener != null) {
            span.setSpan(onClickListener, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        span.setSpan(textappearancespan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setMovementMethod(LinkMovementMethod.getInstance());// 可点击
        tv.setFocusable(false);
        tv.setFocusableInTouchMode(false);
    }

    /**
     * TextView链接
     *
     * @param tv         TextView
     * @param start      url起始位置
     * @param end        url结束位置
     * @param appearance TextAppearanceSpan属性
     */
    public static void urlLinkMovementSpan(TextView tv, int start, int end, int appearance) {
        if (tv == null || tv.getText() == null || StringUtils.isEmptyOrNull(tv.getText())) {
            return;
        }

        if (start < 0) {
            return;
        }

        if (end > tv.getText().length()) {
            return;
        }

        String url = tv.getText().subSequence(start, end).toString();

        Spannable span = (Spannable) tv.getText();
        TextAppearanceSpan textappearancespan = new TextAppearanceSpan(tv.getContext(), appearance);
        span.setSpan(new MyURLSpan(url), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(textappearancespan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setMovementMethod(LinkMovementMethod.getInstance());// 可点击
        tv.setFocusable(false);
        tv.setFocusableInTouchMode(false);
    }

    /**
     * TextView链接
     *
     * @param tv         TextView
     * @param start      超链接起始位置
     * @param end        超链接结束位置
     * @param appearance TextAppearanceSpan属性
     * @param url        链接
     */
    public static void urlLinkMovementSpan(TextView tv, int start, int end, int appearance, String url) {
        if (tv == null || tv.getText() == null || StringUtils.isEmptyOrNull(url) || StringUtils.isEmptyOrNull(tv.getText()))
            return;

        if (!(url.toLowerCase().startsWith("http:"))) {
            return;
        }

        if (start < 0)
            return;

        if (end > tv.getText().length())
            return;

        Spannable span = (Spannable) tv.getText();
        TextAppearanceSpan textappearancespan = new TextAppearanceSpan(tv.getContext(), appearance);
        span.setSpan(new MyURLSpan(url), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(textappearancespan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setMovementMethod(LinkMovementMethod.getInstance());// 可点击
        tv.setFocusable(false);
        tv.setFocusableInTouchMode(false);
    }

    /**
     * 设置TextViewBitmap
     *
     * @param tv     TextView
     * @param start  Bitmap起始位置
     * @param end    Bitmap结束位置
     * @param bitmap 位图
     */
    public static void setImgSpan(TextView tv, int start, int end, Bitmap bitmap) {
        if (tv == null || tv.getText() == null || StringUtils.isEmptyOrNull(tv.getText()) || bitmap == null)
            return;

        if (start < 0 || end > tv.getText().length())
            return;

        Spannable span = (Spannable) tv.getText();
        ImageSpan imgSpan = new ImageSpan(bitmap);
        span.setSpan(imgSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * Url Span
     *
     * @author chenj
     */
    public static class MyURLSpan extends URLSpan {

        public MyURLSpan(Parcel src) {
            super(src);
        }

        public MyURLSpan(String url) {
            super(url);
        }

        @Override
        public void onClick(View widget) {
            String mURL = getURL();
            if (!StringUtils.isEmptyOrNull(mURL) && mURL.startsWith("www.")) {
                mURL = "http://" + mURL;
            }

            Uri uri = Uri.parse(mURL);
            Context context = widget.getContext();
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
            }
        }
    }

    /**
     *
     *
     */
    public static class MyLinkMovementMethod extends LinkMovementMethod {

        private static MyLinkMovementMethod sInstance;

        public static MyLinkMovementMethod getInstance() {
            if (sInstance == null)
                sInstance = new MyLinkMovementMethod();

            return sInstance;
        }
    }

}
