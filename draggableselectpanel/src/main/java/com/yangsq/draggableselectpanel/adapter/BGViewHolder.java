package com.yangsq.draggableselectpanel.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.yangsq.draggableselectpanel.view.BGAnimRecyclerView;

/**
 * 抽象了需要加背景的view的viewholer类，与{@link BGAnimRecyclerView}一起使用
 * Created by Administrator on 2016/9/28 0028.
 */

public abstract class BGViewHolder extends RecyclerView.ViewHolder {
    public BGViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * 获取需要加背景的view，如果获取不到，则以整个item为背景
     * @return
     */
    public abstract View getBGBelongView();

    /**
     * 获取根view，必须是RelativeLayout
     * @return
     */
    public abstract RelativeLayout getRootView();


}
