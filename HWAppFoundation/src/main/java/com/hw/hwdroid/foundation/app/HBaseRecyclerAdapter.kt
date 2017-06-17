package com.hw.hwdroid.foundation.app

import android.content.Context
import android.view.ViewGroup


/**
 * Base Recycler Adapter
 * Created by ChenJ on 2016/10/9.
 */
abstract class HBaseRecyclerAdapter<Data, Holder : HWBaseRecyclerViewHolder> @JvmOverloads constructor(_Context: Context, _Data: MutableList<Data>? = null) : HWBaseRecyclerAdapter<Data, Holder>(_Context, _Data) {

    var onRecyclerViewItemClickListener: HWBaseRecyclerAdapter.OnRecyclerViewItemClickListener? = null

    override abstract fun onCreateViewHolder(parent: ViewGroup?, position: Int): Holder
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener { v -> onRecyclerViewItemClickListener?.onItemClick(v, position) }
    }

}
