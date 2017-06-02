package com.hw.hwdroid.foundation.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

/**
 * 阴影
 * <p>
 * Created by ChenJ on 2017/5/11.
 */
public class HShadowView extends View {

    private Drawable mDrawable;

    public HShadowView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawable == null) {
            int colors[] = {0x00000000, 0x17000000, 0x43000000};    // 分别为开始颜色，中间夜色，结束颜色
            mDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        }

        mDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mDrawable.draw(canvas);
    }

}
