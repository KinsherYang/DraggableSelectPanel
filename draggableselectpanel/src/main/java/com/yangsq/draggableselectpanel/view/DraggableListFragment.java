package com.yangsq.draggableselectpanel.view;

import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yangsq.draggableselectpanel.R;
import com.yangsq.draggableselectpanel.adapter.DGSPItemAdapter;
import com.yangsq.draggableselectpanel.controller.DraggableListController;
import com.yangsq.draggableselectpanel.event.DGSPSelectItemEvent;
import com.yangsq.draggableselectpanel.interfaces.OnDragItemListener;
import com.yangsq.draggableselectpanel.interfaces.OnItemViewClickListener;
import com.yangsq.draggableselectpanel.model.DGSPFavoriteConfig;
import com.yangsq.draggableselectpanel.model.IDGSPItem;

/**
 * 放置可选择列表的fragment
 *
 * @author ysq 2016/9/27.
 */
public class DraggableListFragment extends Fragment {
    /**
     * 显示数据源
     */
    private List<IDGSPItem> mItemModelList;
    private BGAnimRecyclerView mBGAnimRecyclerView;
    private DraggableListController mDraggableListController;
    private float mBGHeight;
    private float mBGWidth;
    private int mBGResId;
    /**
     * 组的唯一标识
     */
    private String mGroupCode;
    /**
     * 是否可以拖拽
     */
    private boolean mIsCanDrag;
    /**
     * 点击监听
     */
    private OnItemViewClickListener mOnItemViewClickListener;
    /**
     * 拖拽监听
     */
    private OnDragItemListener mOnDragItemListener;
    /**
     * 不能通过拖拽重新排序的序列限制，在{@link #mLockPositionStart}
     * 和{@link #mLockPositionEnd}之间的item不能拖动也不能被重新排序
     */
    private int mLockPositionStart = -1;
    /**
     * 不能通过拖拽重新排序的序列限制，在{@link #mLockPositionStart}
     * 和{@link #mLockPositionEnd}之间的item不能拖动也不能被重新排序
     */
    private int mLockPositionEnd = -1;
    private DGSPItemAdapter mItemAdapter;
    /**
     * 收藏功能的配置
     */
    private DGSPFavoriteConfig mDGSPFavoriteConfig;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mBGAnimRecyclerView = new BGAnimRecyclerView(getActivity());
        if (mBGWidth>0&&mBGHeight>0) {
            mBGAnimRecyclerView.setBGSize(mBGWidth, mBGHeight);
        }
        if (mBGResId > 0) {
            mBGAnimRecyclerView.setBGResId(mBGResId);
        }
        mDraggableListController = new DraggableListController(getActivity(), mGroupCode, mItemModelList,
            mBGAnimRecyclerView, mItemAdapter, mDGSPFavoriteConfig);
        if (mOnItemViewClickListener != null) {
            mDraggableListController.setOnItemClickListener(mOnItemViewClickListener);
        }
        if (mOnDragItemListener != null) {
            mDraggableListController.setOnDragItemListener(mOnDragItemListener);
        }
        mDraggableListController.setLockItemRange(mLockPositionStart, mLockPositionEnd);
        mDraggableListController.setLongPressToDrag(mIsCanDrag);
        return mBGAnimRecyclerView;
    }

    public void setBGResId(int resId) {
        mBGResId = resId;
        if (mBGAnimRecyclerView != null) {
            mBGAnimRecyclerView.setBGResId(mBGResId);
        }
    }

    public void setBGSize(int width, int height) {
        mBGHeight = height;
        mBGWidth = width;
        if (mBGAnimRecyclerView != null) {
            mBGAnimRecyclerView.setBGSize(width, height);
        }
    }

    /**
     * 设置显示数据源
     *
     * @param itemModelList
     */
    public void setItemModelList(List<IDGSPItem> itemModelList) {
        mItemModelList = itemModelList;
    }

    /**
     * 分组的唯一标示
     *
     * @param groupCode
     */
    public void setGroupCode(String groupCode) {
        mGroupCode = groupCode;
    }

    /**
     * 设置特殊ui处理的adapter
     *
     * @param adapter 特殊ui处理的adapter,传null为默认
     */
    public void setItemAdapter(DGSPItemAdapter adapter) {
        mItemAdapter = adapter;
    }

    /**
     * 设置收藏的配置
     * @param DGSPFavoriteConfig
     */
    public void setDGSPFavoriteConfig(DGSPFavoriteConfig DGSPFavoriteConfig) {
        this.mDGSPFavoriteConfig = DGSPFavoriteConfig;
    }

    /**
     * 设置item点击监听
     *
     * @param onItemViewClickListener
     */
    public void setOnItemClickListener(OnItemViewClickListener onItemViewClickListener) {
        mOnItemViewClickListener = onItemViewClickListener;
        if (mDraggableListController != null) {
            mDraggableListController.setOnItemClickListener(mOnItemViewClickListener);
        }
    }

    /**
     * 拖拽的监听
     *
     * @param onDragItemListener
     */
    public void setOnDragItemListener(OnDragItemListener onDragItemListener) {
        mOnDragItemListener = onDragItemListener;
        if (mDraggableListController != null) {
            mDraggableListController.setOnDragItemListener(mOnDragItemListener);
        }
    }

    /**
     * 是否可以拖拽
     *
     * @param isCanDrag
     */
    public void setIsCanDrag(boolean isCanDrag) {
        mIsCanDrag = isCanDrag;
        if (mDraggableListController != null) {
            mDraggableListController.setLongPressToDrag(isCanDrag);
        }
    }

    /**
     * 不可拖拽的限制区域
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     */
    public void setLockItemRange(int startPosition, int endPosition) {
        mLockPositionStart = startPosition;
        mLockPositionEnd = endPosition;
        if (mDraggableListController != null) {
            mDraggableListController.setLockItemRange(startPosition, endPosition);
        }
    }

    /**
     * 创建一个fragment
     *
     * @param groupCode     组的唯一标示
     * @param itemModelList 数据列表
     * @param adapter       特殊ui逻辑处理的adapter，如果没有特殊ui逻辑可传null
     *                      @param favoriteConfig
     * @return
     */
    public static DraggableListFragment createMakeupListFragment(String groupCode, List<IDGSPItem> itemModelList,
        DGSPItemAdapter adapter, DGSPFavoriteConfig favoriteConfig) {
        DraggableListFragment fragment = new DraggableListFragment();
        fragment.setItemModelList(itemModelList);
        fragment.setGroupCode(groupCode);
        fragment.setItemAdapter(adapter);
        fragment.setDGSPFavoriteConfig(favoriteConfig);
        return fragment;
    }

    @Subscribe
    public void onEventMainThread(DGSPSelectItemEvent event) {
        if (!event.getGroupCode().equals(mGroupCode)) {
            // 取消除了选中组的其他组当前所选择的项
            mDraggableListController.clearSelect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mItemAdapter != null)
            mItemAdapter.onDestroy();
        if (mDraggableListController != null) {
            mDraggableListController.onDestroy();
        }
    }
}
