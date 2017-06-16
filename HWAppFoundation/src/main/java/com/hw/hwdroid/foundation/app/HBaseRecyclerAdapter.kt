package com.hw.hwdroid.foundation.app

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.hw.hwdroid.foundation.app.annotation.HAtomicData
import com.orhanobut.logger.Logger
import java.util.*


/**
 * Base Recycler Adapter
 * Created by ChenJ on 2016/10/9.
 */
abstract class HBaseRecyclerAdapter<Data, Holder : HBaseRecyclerViewHolder> @JvmOverloads constructor(_Context: Context, _Data: MutableList<Data>? = null) : RecyclerView.Adapter<Holder>() {

    val context: Context = _Context
    protected var dataList: MutableList<Data> = ArrayList()

    init {
        setData(_Data)
    }

    /**
     * set data
     */
    var data: MutableList<Data>
        get() = dataList
        set(value) = setData(value, false)

    fun setData(data: MutableList<Data>?, notifyDataSetChanged: Boolean = false) {
        if (javaClass.isAnnotationPresent(HAtomicData::class.java) && javaClass.getAnnotation(HAtomicData::class.java).value) {
            dataList = data ?: ArrayList()

            if (notifyDataSetChanged) {
                notifyDataSetChanged()
            }

            return
        }

        clear()
        addAll(data)
    }

    /**
     * add all
     * @param data
     */
    fun addAll(data: List<Data>?) {
        if (data == null || data.isEmpty()) {
            return
        }

        dataList.addAll(data)
    }

    /**
     * 清除
     */
    fun clear() {
        dataList.clear()
    }

    /**
     * 是否为空
     * @return
     */
    val isEmpty: Boolean
        get() = dataList.isEmpty()

    open fun getItem(position: Int): Data? {
        try {
            return dataList[position]
        } catch (e: Exception) {
            Logger.e(e)
            return null
        }
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener { v -> onRecyclerViewItemClickListener?.onItemClick(v, position) }
    }

    override fun onBindViewHolder(holder: Holder, position: Int, payloads: MutableList<Any>?) {
        super.onBindViewHolder(holder, position, payloads)
    }

    protected var onRecyclerViewItemClickListener: OnRecyclerViewItemClickListener? = null

    interface OnRecyclerViewItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

}
