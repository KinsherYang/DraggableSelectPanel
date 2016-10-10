package com.yangsq.draggableselectpanel.event;

import java.util.List;

import com.yangsq.draggableselectpanel.model.IDGSPItem;
import com.yangsq.draggableselectpanel.view.DraggableGroupSelectPanelView;

/**
 * 收藏修改事件
 * @author ysq 2016/9/30.
 */
public class DGSPFavoriteGroupChangeEvent {
    /**
     * 添加收藏
     */
    public static final int FAVORITE_GROUP_CHANGE_ACTION_ADD = 1;
    /**
     * 取消收藏
     */
    public static final int FAVORITE_GROUP_CHANGE_ACTION_REMOVE = 2;
    /**
     * 给收藏重新排序
     */
    public static final int FAVORITE_GROUP_CHANGE_ACTION_SWAP = 3;
    /**
     * 改变收藏的动作{@link #FAVORITE_GROUP_CHANGE_ACTION_ADD},
     * {@link #FAVORITE_GROUP_CHANGE_ACTION_REMOVE},{@link #FAVORITE_GROUP_CHANGE_ACTION_SWAP}
     */
    private int action;
    private List<IDGSPItem> dgspItemList;
    /**
     * {@link DraggableGroupSelectPanelView#mDGSPFavoriteConfig#dataTypeName}
     */
    private String dataTypeName;

    /**
     * 收藏修改事件
     * @param action 改变收藏的动作{@link #FAVORITE_GROUP_CHANGE_ACTION_ADD},
     * {@link #FAVORITE_GROUP_CHANGE_ACTION_REMOVE},{@link #FAVORITE_GROUP_CHANGE_ACTION_SWAP}
     * @param idgspItemList 改变后的当前的itemList
     * @param dataTypeName {@link DraggableGroupSelectPanelView#mDGSPFavoriteConfig#dataTypeName}
     */
    public DGSPFavoriteGroupChangeEvent(int action, List<IDGSPItem> idgspItemList, String dataTypeName) {
        this.action = action;
        this.dgspItemList = idgspItemList;
        this.dataTypeName = dataTypeName;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public List<IDGSPItem> getDGSPItemList() {
        return dgspItemList;
    }

    public void setDGSPItemList(List<IDGSPItem> dgspItemList) {
        this.dgspItemList = dgspItemList;
    }

    public String getDataTypeName() {
        return dataTypeName;
    }

    public void setDataTypeName(String dataTypeName) {
        this.dataTypeName = dataTypeName;
    }
}
