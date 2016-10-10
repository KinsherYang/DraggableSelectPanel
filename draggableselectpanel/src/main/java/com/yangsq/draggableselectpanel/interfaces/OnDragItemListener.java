package com.yangsq.draggableselectpanel.interfaces;

import android.support.v7.widget.RecyclerView;

import com.yangsq.draggableselectpanel.adapter.DraggableSelectViewHolder;
import com.yangsq.draggableselectpanel.model.IDGSPItem;

import java.util.List;

/**
 * 拖动recycleview里面item重新排序的监听
 * @author ysq 2016/9/29.
 */
public interface OnDragItemListener {
    /**
     * 拖拽开始
     * @param recyclerView 当前处理的 RecyclerView
     * @param groupCode       组的唯一标示
     * @param viewHolder 所拖动的viewHolder
     */
    void onStartDrag(RecyclerView recyclerView, String groupCode,DraggableSelectViewHolder viewHolder);

    /**
     * 拖拽中的监听
     * @param recyclerView 当前处理的 RecyclerView
     * @param groupCode       组的唯一标示
     * @param viewHolder 所拖动的viewHolder
     * @param dx 相对于item初始位置的x轴偏移
     * @param dy 相对于item初始位置的y轴偏移
     */
    void onDragging(RecyclerView recyclerView, String groupCode,DraggableSelectViewHolder viewHolder, float dx, float dy);

    /**
     * 松开拖拽，还没有归位，需要等动画结束回调{@link #onDragFinish}的时候才算归为
     * @param recyclerView 当前处理的 RecyclerView
     * @param groupCode       组的唯一标示
     * @param viewHolder 所拖动的viewHolder
     */
    void onReleaseItem(RecyclerView recyclerView, String groupCode,DraggableSelectViewHolder viewHolder);

    /**
     * 松开拖拽后当item回到列表上时回调
     * @param recyclerView 当前处理的 RecyclerView
     * @param groupCode       组的唯一标示
     * @param viewHolder
     */
    void onDragFinish(RecyclerView recyclerView, String groupCode,DraggableSelectViewHolder viewHolder);

    /**
     * 当item被拖拽完重新排序之后触发
     * @param recyclerView 当前处理的 RecyclerView
     * @param groupCode   组的唯一标示
     * @param oldPosition 拖拽前的位置
     * @param newPosition 拖拽后的位置
     * @param resultList 重新排序之后的list
     */
    void onItemMoved(RecyclerView recyclerView, String groupCode,int oldPosition, int newPosition, List<IDGSPItem> resultList);
}
