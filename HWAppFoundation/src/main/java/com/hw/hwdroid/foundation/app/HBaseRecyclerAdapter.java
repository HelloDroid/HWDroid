package com.hw.hwdroid.foundation.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hw.hwdroid.foundation.app.annotation.HAtomicData;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * Base Recycler Adapter
 * <p>
 * Created by ChenJ on 2016/10/9.
 */
public abstract class HBaseRecyclerAdapter<Data, Holder extends HBaseRecyclerViewHolder> extends RecyclerView.Adapter<Holder> {

    protected Context context;
    protected List<Data> data = new ArrayList<>();

    public HBaseRecyclerAdapter(Context context) {
        this(context, null);
    }

    public HBaseRecyclerAdapter(Context context, List<Data> data) {
        this.context = context;
        setData(data);
    }

    public Context getContext() {
        return context;
    }

    /**
     * reset
     *
     * @param data
     */
    public void setData(List<Data> data) {
        if (getClass().isAnnotationPresent(HAtomicData.class)) {
            HAtomicData annotation = getClass().getAnnotation(HAtomicData.class);
            if (annotation.value()) {
                this.data = data;
                return;
            }
        }

        clear();
        addAll(data);
    }

    /**
     * add all
     *
     * @param data
     */
    public void addAll(List<Data> data) {
        if (null == data || data.isEmpty()) {
            return;
        }

        if (null == this.data) {
            this.data = new ArrayList<>();
        }

        this.data.addAll(data);
    }

    /**
     * 清除
     */
    public void clear() {
        if (null == this.data) {
            return;
        }

        this.data.clear();
    }

    /**
     * 是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return null == data || data.isEmpty();
    }

    public Data getItem(int position) {
        try {
            if (null == data || position < 0 || position >= data.size()) {
                return null;
            }

            return data.get(position);
        } catch (Exception e) {
            Logger.e(e);
            return null;
        }
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return null == data ? 0 : data.size();
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if (null != holder && null != holder.itemView) {
            holder.itemView.setOnClickListener(v -> {
                if (null != mOnRecyclerViewItemClickListener) {
                    mOnRecyclerViewItemClickListener.onItemClick(v, position);
                }
            });
        }
    }

    protected OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.mOnRecyclerViewItemClickListener = recyclerViewItemClickListener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }


}
