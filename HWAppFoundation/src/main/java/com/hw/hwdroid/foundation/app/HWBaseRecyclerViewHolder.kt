package com.hw.hwdroid.foundation.app

import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.ButterKnife
import butterknife.Unbinder

/**
 * Base RecyclerView Holder
 * Created by ChenJ on 2016/10/9.
 */
open class HWBaseRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    init {
        initViews(itemView)
    }

    private var unbinder: Unbinder? = null
    open protected fun initViews(itemView: View) {
        unbinder?.let {
            unbinder = ButterKnife.bind(this, itemView)
        }
    }

}


