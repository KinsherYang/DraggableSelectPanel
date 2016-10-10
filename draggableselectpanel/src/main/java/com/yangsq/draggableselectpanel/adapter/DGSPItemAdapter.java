package com.yangsq.draggableselectpanel.adapter;

import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yangsq.draggableselectpanel.R;
import com.yangsq.draggableselectpanel.view.BGAnimRecyclerView;
import com.yangsq.draggableselectpanel.helper.DGSPFavoriteHelper;
import com.yangsq.draggableselectpanel.event.DGSPFavoriteEvent;
import com.yangsq.draggableselectpanel.event.DGSPFavoriteGroupChangeEvent;
import com.yangsq.draggableselectpanel.interfaces.OnItemViewClickListener;
import com.yangsq.draggableselectpanel.model.DGSPFavoriteConfig;
import com.yangsq.draggableselectpanel.model.IDGSPItem;

/**
 * 适用于{@link BGAnimRecyclerView}的adapter,
 * 有图片显示，name显示，切换选中等基础业务逻辑，如果有具体业务有其他ui要求，继承此类
 */
public class DGSPItemAdapter extends RecyclerView.Adapter<DGSPItemViewHolder> {
    protected List<IDGSPItem> mItemList;
    protected String mGroupCode;
    protected OnItemViewClickListener mOnItemClickListener;
    protected DGSPFavoriteConfig mFavoriteConfig;
    private int mItemLayoutId;

    /**
     *
     * @param groupCode 分组code
     * @param mList 数据组
     */
    public DGSPItemAdapter(String groupCode, List<IDGSPItem> mList, DGSPFavoriteConfig favoriteConfig) {
        this(groupCode, mList, favoriteConfig, 0);
    }

    /**
     * 构造函数
     * @param groupCode 分组code
     * @param mList 数据组
     * @param favoriteConfig 收藏功能的配置
     * @param itemLayoutId 自定义的itemLayoutId
     */
    public DGSPItemAdapter(String groupCode, List<IDGSPItem> mList, DGSPFavoriteConfig favoriteConfig, int itemLayoutId) {
        this.mItemList = mList;
        mGroupCode = groupCode;
        mFavoriteConfig = favoriteConfig;
        if (itemLayoutId <= 0) {
            mItemLayoutId = R.layout.dsgp_list_item;
        } else {
            mItemLayoutId = itemLayoutId;
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public DGSPItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutId(), parent, false);
        DGSPItemViewHolder itemViewHolder = new DGSPItemViewHolder(view);
        itemViewHolder.initViews();
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(DGSPItemViewHolder holder, int position) {
        IDGSPItem itemModel = mItemList.get(position);
        holder.setName(itemModel.getName());
        if (itemModel.getImageDrawableId() > 0) {
            holder.setImage(itemModel.getImageDrawableId());
        } else {
            holder.setImage(itemModel.getImageUri());
        }
        holder.setListener(mGroupCode, itemModel, mOnItemClickListener, this);
        if (mFavoriteConfig.isUseFavorite()) {
            holder.setIsFavorite(itemModel.isFavorite());
        }
    }

    @Override
    public int getItemCount() {
        if (mItemList == null) {
            return 0;
        } else {
            return mItemList.size();
        }
    }

    public void setOnItemClickListener(OnItemViewClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public int getItemLayoutId() {
        return mItemLayoutId;
    }

    /**
     * 处理添加、取消收藏的ui
     * @param event
     */
    @Subscribe
    public void onEventMainThread(DGSPFavoriteEvent event) {
        if (mFavoriteConfig.isUseFavorite() && mFavoriteConfig.getDataTypeName().equals(event.getDataTypeName())) {
            if (mItemList != null) {
                for (int i = 0; i < mItemList.size(); i++) {
                    IDGSPItem item = mItemList.get(i);
                    if (event.getCode().equals(item.getCode())) {
                        if (event.getAction() == DGSPFavoriteEvent.DGSP_ITEM_FAVORITE_ACTION_CANCEL) {
                            // 取消收藏
                            if (DGSPFavoriteHelper.DGSP_FAVORITE_GROUP_CODE.equals(mGroupCode)) {
                                // 收藏组，从列表移除
                                cancelFavorite(event.getViewHolder(), i, true);
                                EventBus.getDefault()
                                    .post(new DGSPFavoriteGroupChangeEvent(
                                        DGSPFavoriteGroupChangeEvent.FAVORITE_GROUP_CHANGE_ACTION_ADD, mItemList,
                                        mFavoriteConfig.getDataTypeName()));
                            } else {
                                // 普通组，不从列表移除
                                cancelFavorite(event.getViewHolder(), i, false);
                            }
                        } else if (event.getAction() == DGSPFavoriteEvent.DGSP_ITEM_FAVORITE_ACTION_SET) {
                            if (!DGSPFavoriteHelper.DGSP_FAVORITE_GROUP_CODE.equals(mGroupCode)) {
                                // 普通组，设为收藏的ui
                                setFavorite(event.getViewHolder(), i);
                            }
                        }
                        break;
                    }
                }
            }
            if (event.getAction() == DGSPFavoriteEvent.DGSP_ITEM_FAVORITE_ACTION_SET
                && DGSPFavoriteHelper.DGSP_FAVORITE_GROUP_CODE.equals(mGroupCode)) {
                // 如果是添加收藏、并且本组为收藏分组，则在头部加入列表
                addModelToTop(event);
            }
        }
    }

    /**
     * 如果是添加收藏、并且本组为收藏分组，则在头部加入列表
     * @param event
     */
    private void addModelToTop(DGSPFavoriteEvent event) {
        if (event.getDGSPItem() != null) {
            event.getDGSPItem().setFavorite(true);
            boolean isExist = false;
            for (IDGSPItem idgspItem : mItemList) {
                if (idgspItem.getCode().equals(event.getCode())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                mItemList.add(DGSPFavoriteHelper.DGSP_FAVORITE_CANNOT_MOVE_ITEM_COUNT, event.getDGSPItem());
            }
            EventBus.getDefault()
                .post(new DGSPFavoriteGroupChangeEvent(DGSPFavoriteGroupChangeEvent.FAVORITE_GROUP_CHANGE_ACTION_ADD,
                    mItemList, mFavoriteConfig.getDataTypeName()));
            notifyDataSetChanged();
        }
    }

    /**
     * 取消收藏
     *
     * @param draggableSelectViewHolder viewHolder,如果有动画可以再viewholder里定义，在这边调用
     * @param position           位置
     * @param isRemoveFromList   是否从列表移除
     */
    private void cancelFavorite(DraggableSelectViewHolder draggableSelectViewHolder, int position,
        boolean isRemoveFromList) {
        mItemList.get(position).setFavorite(false);
        if (isRemoveFromList) {
            mItemList.remove(position);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加为收藏
     *
     * @param draggableSelectViewHolder ,如果有动画可以再viewholder里定义，在这边调用
     * @param position
     */
    public void setFavorite(DraggableSelectViewHolder draggableSelectViewHolder, int position) {
        mItemList.get(position).setFavorite(true);
        notifyDataSetChanged();
    }

    /**
     * 用于给子类重写
     */
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    public IDGSPItem getItem(int position) {
        return mItemList.get(position);
    }
}
