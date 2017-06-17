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
open class HWBaseViewHolder(itemView: View) {

    // Create ViewHolder时的View，用于初始化
    // 建议：itemView只在Create时使用
    val itemView: View = itemView
    private var unbinder: Unbinder? = null

    init {
        initViews(itemView)
    }

    open protected fun initViews(itemView: View) {
        itemView.tag = this
        unbinder?.let {
            unbinder = ButterKnife.bind(this, itemView)
        }
    }

}
