package com.hw.hwdroid.foundation.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by ChenJ on 2017/5/11.
 */

public class HPreviousPageView extends View {

    private View mView;

    public HPreviousPageView(Context context) {
        super(context);
    }

    public void cacheView(View view) {
        mView = view;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mView != null) {
            mView.draw(canvas);
            mView = null;
        }
    }

}
