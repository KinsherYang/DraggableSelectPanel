package com.yangsq.draggableselectpanel.model;

import java.util.List;

import com.yangsq.draggableselectpanel.adapter.DGSPItemAdapter;
import com.yangsq.draggableselectpanel.helper.DGSPItemHelper;

/**
 * itemList的组，用来做整组的操作
 * @author ysq 2016/9/29.
 */
public class ItemGroupModel {


    /**
     * 组code
     */
    private String groupCode;
    /**
     *  组名
     */
    private String groupName;
    /**
     * 显示在tab上的图标
     */
    private int iconResId = -1;
    /**
     * 是否可以拖拽
     */
    private boolean isCanDrag = false;
    /**
     * 不可拖拽也不可被排序的item范围,开始
     */
    private int lockStartPosition = -1;
    /**
     * 不可拖拽也不可被排序的item范围,结束
     */
    private int lockEndPosition = -1;
    /**
     *  特殊ui逻辑处理的adapter，如果没有特殊ui逻辑可为null
     */
    private DGSPItemAdapter itemAdapter;
    /**
     * 是否在组开始添加code为{@link DGSPItemHelper#CANCEL_MODEL_CODE}的取消选择的项
     * 将对{@link #itemList}进行直接添加，所以处理原数据的时候注意进行code的区分
     */
    private boolean isAddCancelItemOnTop;
    private List<IDGSPItem> itemList;

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<IDGSPItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<IDGSPItem> itemList) {
        this.itemList = itemList;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public boolean isCanDrag() {
        return isCanDrag;
    }

    public void setCanDrag(boolean canDrag) {
        isCanDrag = canDrag;
    }

    public int getLockStartPosition() {
        return lockStartPosition;
    }

    public void setLockStartPosition(int lockStartPosition) {
        this.lockStartPosition = lockStartPosition;
    }

    public int getLockEndPosition() {
        return lockEndPosition;
    }

    public void setLockEndPosition(int lockEndPosition) {
        this.lockEndPosition = lockEndPosition;
    }

    public DGSPItemAdapter getItemAdapter() {
        return itemAdapter;
    }

    public void setItemAdapter(DGSPItemAdapter itemAdapter) {
        this.itemAdapter = itemAdapter;
    }

    public boolean isAddCancelItemOnTop() {
        return isAddCancelItemOnTop;
    }

    public void setAddCancelItemOnTop(boolean addCancelItemOnTop) {
        isAddCancelItemOnTop = addCancelItemOnTop;
    }
}
