package com.yangsq.draggableselectpanel.event;

import com.yangsq.draggableselectpanel.adapter.DraggableSelectViewHolder;
import com.yangsq.draggableselectpanel.model.IDGSPItem;
import com.yangsq.draggableselectpanel.view.DraggableGroupSelectPanelView;

/**
 * Created by Administrator on 2016/9/30 0030.
 */

public class DGSPFavoriteEvent {


    /**
     * 取消收藏
     */
    public static final int DGSP_ITEM_FAVORITE_ACTION_CANCEL = 1;
    /**
     * 添加收藏
     */
    public static final int DGSP_ITEM_FAVORITE_ACTION_SET = 2;

    /**
     * 构造改变收藏事件
     * @param action 操作类型
     * @param code item唯一标识
     */
    public DGSPFavoriteEvent(int action, String code,String dataTypeName) {
        this.action = action;
        this.code = code;
        this.dataTypeName = dataTypeName;
    }

    /**
     * 操作类型{@link #DGSP_ITEM_FAVORITE_ACTION_CANCEL}{@link #DGSP_ITEM_FAVORITE_ACTION_SET}
     */
    private int action;
    /**
     * 妆容id
     */
    private String code;
    /**
     * {@link DraggableGroupSelectPanelView#mDGSPFavoriteConfig#dataTypeName}
     */
    private String dataTypeName;
    private DraggableSelectViewHolder viewHolder;
    private IDGSPItem dgspItem;


    public IDGSPItem getDGSPItem() {
        return dgspItem;
    }

    public void setDGSPItem(IDGSPItem idgspItem) {
        this.dgspItem = idgspItem;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DraggableSelectViewHolder getViewHolder() {
        return viewHolder;
    }

    public void setViewHolder(DraggableSelectViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    public String getDataTypeName() {
        return dataTypeName;
    }

    public void setDataTypeName(String dataTypeName) {
        this.dataTypeName = dataTypeName;
    }
}
