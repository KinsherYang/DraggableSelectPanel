package com.yangsq.draggableselectpanel.interfaces;

import android.support.v7.widget.RecyclerView;

import com.yangsq.draggableselectpanel.adapter.DraggableSelectViewHolder;
import com.yangsq.draggableselectpanel.model.IDGSPItem;

/**
 * 拖拽到顶部按钮后释放的监听
 *
 * @author ysq 2016/9/29.
 */
public interface OnDragToTitleBtnListener {
    /**
     * 拖拽到顶部按钮后释放
     *
     * @param recyclerView 当前操作的 RecyclerView
     * @param dgspItem    当前拖拽的item
     * @param viewHolder   拖拽的viewholder
     */
    void OnDragToTitleBtn(RecyclerView recyclerView, IDGSPItem dgspItem, DraggableSelectViewHolder viewHolder);
}
