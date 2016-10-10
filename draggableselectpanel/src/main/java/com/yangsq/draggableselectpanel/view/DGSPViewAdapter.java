package com.yangsq.draggableselectpanel.view;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import android.content.Context;

import com.yangsq.draggableselectpanel.helper.DGSPFavoriteHelper;
import com.yangsq.draggableselectpanel.helper.DGSPItemHelper;
import com.yangsq.draggableselectpanel.event.DGSPFavoriteGroupChangeEvent;
import com.yangsq.draggableselectpanel.model.DGSPFavoriteConfig;
import com.yangsq.draggableselectpanel.model.IDGSPItem;
import com.yangsq.draggableselectpanel.model.ItemGroupModel;

/**
 * {@link DraggableGroupSelectPanelView}的适配器
 *
 * @author ysq 2016/10/9.
 */
public class DGSPViewAdapter {
    private Context mContext;
    private List<ItemGroupModel> mItemGroupModelList;
    private DraggableGroupSelectPanelView mPanelView;
    private DGSPFavoriteConfig mFavoriteConfig;
    private List<IDGSPItem> mFavoriteList;

    public DGSPViewAdapter(Context context, List<ItemGroupModel> itemGroupModelList) {
        EventBus.getDefault().register(this);
        mContext = context;
        mItemGroupModelList = itemGroupModelList;
        if (mItemGroupModelList == null) {
            mItemGroupModelList = new ArrayList<>();
        }
    }

    // region 数据的改变,调用后将直接改变ui
    public void addItemGroup(ItemGroupModel itemGroupModel) {
        mItemGroupModelList.add(itemGroupModel);
        addTabToView(itemGroupModel, -1);
    }

    public void addItemGroup(int index, ItemGroupModel itemGroupModel) {
        mItemGroupModelList.add(index, itemGroupModel);
        addTabToView(itemGroupModel, index);
    }

    public void addItemGroupAll(int index, List<ItemGroupModel> itemGroupModelList) {
        if (itemGroupModelList != null) {
            mItemGroupModelList.addAll(index, itemGroupModelList);
            for (int i = 0; i < itemGroupModelList.size(); i++) {
                addTabToView(itemGroupModelList.get(i), index + i);
            }
        }
    }

    public void addItemGroupAll(List<ItemGroupModel> itemGroupModelList) {
        mItemGroupModelList.addAll(itemGroupModelList);
        for (int i = 0; i < itemGroupModelList.size(); i++) {
            addTabToView(itemGroupModelList.get(i), -1);
        }
    }
    // endregion

    /**
     * 设置当前控制的DraggableGroupSelectPanelView对象，构造的时候可以不用设，当调用{@link DraggableGroupSelectPanelView#setAdapter(DGSPViewAdapter)}时会自动调用
     *
     * @param panelView
     */
    void setPanelView(DraggableGroupSelectPanelView panelView) {
        this.mPanelView = panelView;
        mFavoriteConfig = panelView.getFavoriteConfig();
    }

    /**
     * 初始化ui
     */
     void initViews() {
        // 先判断是否使用收藏
        if (mFavoriteConfig.isUseFavorite()) {
            DGSPFavoriteHelper.getFavoriteItemList(mContext, mFavoriteConfig.getUserId(),
                mFavoriteConfig.getDataTypeName(), new DGSPFavoriteHelper.GetItemListCallback() {
                    @Override
                    public void onGetResult(List<IDGSPItem> dgspItemList) {
                        if (dgspItemList == null)
                            dgspItemList = new ArrayList<IDGSPItem>();
                        mFavoriteList = dgspItemList;
                        // 添加收藏数据
                        ItemGroupModel favoriteItemGroup = new ItemGroupModel();
                        favoriteItemGroup.setCanDrag(true);
                        favoriteItemGroup.setGroupCode(DGSPFavoriteHelper.DGSP_FAVORITE_GROUP_CODE);
                        favoriteItemGroup.setItemList(dgspItemList);
                        favoriteItemGroup.setLockStartPosition(0);
                        favoriteItemGroup.setLockEndPosition(0);
                        favoriteItemGroup.setItemAdapter(mFavoriteConfig.getFavoriteAdapter());
                        favoriteItemGroup.setIconResId(mFavoriteConfig.getFavoriteIcoResId());
                        favoriteItemGroup.setGroupName(mFavoriteConfig.getFavoriteTabName());
                        favoriteItemGroup.setAddCancelItemOnTop(true);
                        addTabToView(favoriteItemGroup, -1);
                        for (ItemGroupModel itemGroupModel : mItemGroupModelList) {
                            // 检查收藏
                            DGSPFavoriteHelper.checkItemListFavorite(dgspItemList, itemGroupModel.getItemList());
                            addTabToView(itemGroupModel, -1);
                        }
                    }
                });
        } else {
            for (ItemGroupModel itemGroupModel : mItemGroupModelList) {
                addTabToView(itemGroupModel, -1);
            }
        }

    }

    /**
     * 添加view
     *
     * @param itemGroupModel
     * @param position       标题所在位置,小于0默认添加到队列最后
     */
    private void addTabToView(ItemGroupModel itemGroupModel, int position) {
        if (itemGroupModel.getItemList() == null)
            itemGroupModel.setItemList(new ArrayList<IDGSPItem>());
        if (itemGroupModel.isAddCancelItemOnTop()) {
            // 在头部加入取消选项
            itemGroupModel.getItemList().add(0, DGSPItemHelper.getCancelSelectModel());
        }
        mPanelView.generateItemListFragment(itemGroupModel.getGroupCode(), itemGroupModel.getIconResId(),
            itemGroupModel.getGroupName(), itemGroupModel.getItemList(), itemGroupModel.isCanDrag(),
            itemGroupModel.getLockStartPosition(), itemGroupModel.getLockEndPosition(), itemGroupModel.getItemAdapter(),
            position);
    }

    @Subscribe
    public void onEventAsync(DGSPFavoriteGroupChangeEvent DGSPFavoriteGroupChangeEvent) {
        if (DGSPFavoriteGroupChangeEvent != null) {
            // 只要有改动，都保存最新
            DGSPFavoriteHelper.saveFavoriteList(mContext, DGSPFavoriteGroupChangeEvent.getDGSPItemList(),
                mFavoriteConfig.getUserId(), mFavoriteConfig.getDataTypeName());
        }
    }

    /**
     *  获取收藏列表
     * @return
     */
    public List<IDGSPItem> getFavoriteList() {
        return mFavoriteList;
    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }
}
