package com.hw.hwdroid.foundation.app;

import android.support.annotation.NonNull;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Adapter View Holder
 * <p>
 * Created by ChenJ on 16/8/4.
 */
public class HBaseViewHolder {

    private boolean isBindButterKnife;

    public HBaseViewHolder(@NonNull View view) {
        initViews(view);
    }

    protected void initViews(@NonNull View view) {
        if (null != view && !isBindButterKnife) {
            ButterKnife.bind(this, view);
            isBindButterKnife = true;
        }
    }

}
