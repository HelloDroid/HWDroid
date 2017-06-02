package com.hw.hwdroid.foundation.app;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Base RecyclerView Holder
 * <p>
 * Created by ChenJ on 2016/10/9.
 */
public class HBaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    public HBaseRecyclerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
