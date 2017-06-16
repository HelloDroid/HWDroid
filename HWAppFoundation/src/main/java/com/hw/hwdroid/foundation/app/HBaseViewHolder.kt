package com.hw.hwdroid.foundation.app

import android.view.View

import butterknife.ButterKnife
import butterknife.Unbinder

/**
 * Adapter View Holder
 *
 *
 * Created by ChenJ on 16/8/4.
 */
open class HBaseViewHolder(view: View) {

    private var unbinder: Unbinder? = null

    init {
        initViews(view)
    }

    open protected fun initViews(view: View) {
        unbinder?.let {
            unbinder = ButterKnife.bind(this, view)
        }
    }

}
