package com.yangsq.draggableselectpanel.model;

import com.yangsq.draggableselectpanel.view.DraggableGroupSelectPanelView;

/**
 * {@link DraggableGroupSelectPanelView}可以识别的数据源接口定义
 * @author ysq 2016/10/8.
 */
public interface IDGSPItem {
    /**
     * 如果item的图片为资源文件，可在此返回,显示优先级大于{@link #getImageUri()}
     * @return item的图片为资源文件Id
     */
    int getImageDrawableId();

    /**
     * item的图片uri，可以是网络地址，或者其他任何picasso库可以识别的uri
     * @return
     */
    String getImageUri();

    /**
     * item上显示的名字
     * @return
     */
    String getName();

    /**
     * 每个item的唯一标识
     * @return
     */
    String getCode();

    /**
     * 是否已加入收藏，如果要使用收藏功能，请返回有意义的值
     * @return
     */
    boolean isFavorite();

    /**
     * 改变收藏属性。需要控件编辑改变值的都应该有set
     * @param isFavorite
     */
    void setFavorite(boolean isFavorite);
}
