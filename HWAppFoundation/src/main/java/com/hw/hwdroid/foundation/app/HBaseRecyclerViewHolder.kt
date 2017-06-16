package com.hw.hwdroid.foundation.app

import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.ButterKnife

/**
 * Base RecyclerView Holder
 *
 *
 * Created by ChenJ on 2016/10/9.
 */
open class HBaseRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    init {
        ButterKnife.bind(this, itemView)
    }

}


