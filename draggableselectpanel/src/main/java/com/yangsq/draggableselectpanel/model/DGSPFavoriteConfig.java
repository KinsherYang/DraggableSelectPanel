package com.yangsq.draggableselectpanel.model;

import com.yangsq.draggableselectpanel.adapter.DGSPItemAdapter;

/**
 * 收藏功能的配置文件
 * @author ysq 2016/10/9.
 */
public class DGSPFavoriteConfig {
    /**
     * 收藏tab使用的图标资源id
     */
    private int favoriteIcoResId;
    /**
     * 收藏tab显示的文字
     */
    private String favoriteTabName;
    /**
     *  是否使用收藏功能，如果为false，那么其他属性都无意义
     */
    private boolean isUseFavorite;
    /**
     * 收藏数据类型，用于区分唯一使用场景（如妆容等）
     */
    private String dataTypeName;
    /**
     * 用户id，用于区分不同用户的收藏内容
     */
    private String userId;
    /**
     * 如果有特殊ui逻辑，可以使用新的适配器，为null的时候使用默认adapter
     */
    private DGSPItemAdapter favoriteAdapter;

    public int getFavoriteIcoResId() {
        return favoriteIcoResId;
    }

    public DGSPFavoriteConfig setFavoriteIcoResId(int favoriteIcoResId) {
        this.favoriteIcoResId = favoriteIcoResId;
        return this;
    }

    public String getFavoriteTabName() {
        return favoriteTabName;
    }

    public DGSPFavoriteConfig setFavoriteTabName(String favoriteTabName) {
        this.favoriteTabName = favoriteTabName;
        return this;
    }

    public boolean isUseFavorite() {
        return isUseFavorite;
    }

    public DGSPFavoriteConfig setUseFavorite(boolean useFavorite) {
        isUseFavorite = useFavorite;
        return this;
    }

    public String getDataTypeName() {
        return dataTypeName;
    }

    public DGSPFavoriteConfig setDataTypeName(String dataTypeName) {
        this.dataTypeName = dataTypeName;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public DGSPFavoriteConfig setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public DGSPItemAdapter getFavoriteAdapter() {
        return favoriteAdapter;
    }

    public DGSPFavoriteConfig setFavoriteAdapter(DGSPItemAdapter favoriteAdapter) {
        this.favoriteAdapter = favoriteAdapter;
        return this;
    }
}
