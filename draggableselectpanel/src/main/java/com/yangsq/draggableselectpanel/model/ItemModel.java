package com.yangsq.draggableselectpanel.model;

/**
 * Created by Administrator on 2016/9/27 0027.
 */

public class ItemModel implements IDGSPItem {
    /**
     * 图片的resId，如果大于0优先于{@link #imageUri}显示
     */
    private int imageDrawableId;
    /**
     * 图片uri
     */
    private String imageUri;
    /**
     * 名字
     */
    private String name;
    /**
     * 唯一标识
     */
    private String code;

    /**
     * 是否已加入收藏
     */
    private boolean isFavourite;

    @Override
    public int getImageDrawableId() {
        return imageDrawableId;
    }

    public void setImageDrawableId(int imageDrawableId) {
        this.imageDrawableId = imageDrawableId;
    }

    @Override
    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean isFavorite() {
        return isFavourite;
    }
    @Override
    public void setFavorite(boolean favourite) {
        isFavourite = favourite;
    }
}
