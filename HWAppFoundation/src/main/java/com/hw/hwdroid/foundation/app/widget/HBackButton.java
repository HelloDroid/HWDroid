package com.hw.hwdroid.foundation.app.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.hw.hwdroid.foundation.R;
import com.orhanobut.logger.Logger;

import common.android.foundation.app.HActivityStack;

/**
 * Back Button
 * <p>
 * Created by ChenJ on 2017/4/21.
 */
public class HBackButton extends AppCompatImageView {

    public HBackButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnClickListener(v -> backListener());
    }


    @Override
    public final void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
    }

    public final void setImageResource(boolean iosMode, boolean isLightTheme) {
        if (iosMode) {
            setImageResource(isLightTheme ? R.drawable.icon_back_vector_dark : R.drawable.icon_back_vector);
            return;
        }

        ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor(isLightTheme ? "#333333" : "#ffffff"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setImageTintList(colorStateList);
        } else {
            ViewCompat.setBackgroundTintList(this, colorStateList);
            ViewCompat.setBackgroundTintMode(this, PorterDuff.Mode.SRC_IN);
        }
    }


    private void backListener() {
        try {
            Context context = getContext();
            if (context instanceof Activity) {
                HActivityStack.INSTANCE.pop((Activity) context);
            } else {
                Activity activity = HActivityStack.INSTANCE.curr();
                if (null != activity) {
                    HActivityStack.INSTANCE.pop();
                }
            }
        } catch (Exception e) {
            Logger.e(e);
        }
    }

}
