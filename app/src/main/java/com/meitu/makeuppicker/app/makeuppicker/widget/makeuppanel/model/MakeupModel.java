package com.meitu.makeuppicker.app.makeuppicker.widget.makeuppanel.model;

import java.io.Serializable;

import com.yangsq.draggableselectpanel.model.IDGSPItem;

/**
 * Created by Administrator on 2016/9/27 0027.
 */

public class MakeupModel implements Serializable, IDGSPItem {
    /**
     * 图片uri
     */
    private String makeupUri;
    /**
     * 名字
     */
    private String makeupName;
    /**
     * 排序号码
     */
    private int sort;
    /**
     * 是否已加入收藏
     */
    private boolean isFavourite;
    /**
     * 图片资源id，用于取消等固定图片的选项
     */
    private int icoResId;

    public int getIcoResId() {
        return icoResId;
    }

    public void setIcoResId(int icoResId) {
        this.icoResId = icoResId;
    }

    private String makeupId;

    public String getMakeupUri() {
        return makeupUri;
    }

    public void setMakeupUri(String makeupUri) {
        this.makeupUri = makeupUri;
    }

    public String getMakeupName() {
        return makeupName;
    }

    public void setMakeupName(String makeupName) {
        this.makeupName = makeupName;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String getMakeupId() {
        return makeupId;
    }

    public void setMakeupId(String makeId) {
        this.makeupId = makeId;
    }

    @Override
    public int getImageDrawableId() {
        return icoResId;
    }

    @Override
    public String getImageUri() {
        return makeupUri;
    }

    @Override
    public String getName() {
        return makeupName;
    }

    @Override
    public String getCode() {
        return makeupId;
    }

    @Override
    public boolean isFavorite() {
        return isFavourite;
    }

    @Override
    public void setFavorite(boolean isFavorite) {
        this.isFavourite = isFavorite;
    }
}
