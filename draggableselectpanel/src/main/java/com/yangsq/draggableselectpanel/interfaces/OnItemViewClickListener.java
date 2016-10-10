package com.yangsq.draggableselectpanel.interfaces;

import android.support.v7.widget.RecyclerView;

import com.yangsq.draggableselectpanel.adapter.DraggableSelectViewHolder;
import com.yangsq.draggableselectpanel.model.IDGSPItem;

/**
 * 列表中的item点击监听
 * Created by Administrator on 2016/9/28 0028.
 */

public interface OnItemViewClickListener {
    /**
     * 点击列表里的item
     *
     * @param groupCode  组code
     * @param itemModel  选中的妆容
     * @param viewHolder 当前选中的viewHolder，位置可以通过viewHolder.getAdapterPosition获取
     * @param adapter    当前tab处理适配的adapter，可以用来做一些数据的处理
     */
    void onClick(String groupCode, IDGSPItem itemModel, DraggableSelectViewHolder viewHolder, RecyclerView.Adapter adapter);

    /**
     * 长按列表里的item，当可以长按拖拽排序的时候不触发
     *
     * @param groupCode  组code
     * @param itemModel  选中的妆容
     * @param viewHolder 当前选中的viewHolder，位置可以通过viewHolder.getAdapterPosition获取
     * @param adapter    当前tab处理适配的adapter，可以用来做一些数据的处理
     */
    void onLongClick(String groupCode, IDGSPItem itemModel, DraggableSelectViewHolder viewHolder, RecyclerView.Adapter adapter);
}
