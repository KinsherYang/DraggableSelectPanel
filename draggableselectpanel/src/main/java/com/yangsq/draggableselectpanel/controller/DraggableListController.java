package com.yangsq.draggableselectpanel.controller;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.yangsq.draggableselectpanel.view.BGAnimRecyclerView;
import com.yangsq.draggableselectpanel.adapter.DraggableSelectViewHolder;
import com.yangsq.draggableselectpanel.adapter.DGSPItemAdapter;
import com.yangsq.draggableselectpanel.interfaces.OnDragItemListener;
import com.yangsq.draggableselectpanel.interfaces.OnItemViewClickListener;
import com.yangsq.draggableselectpanel.model.DGSPFavoriteConfig;
import com.yangsq.draggableselectpanel.model.IDGSPItem;

/**
 * 处理实现BGAnimRecyclerView的拖拽逻辑
 *
 * @author ysq 2016/9/27.
 */
public class DraggableListController {
    private Context mContext;
    /**
     * 数据源列表
     */
    private List<IDGSPItem> mItemModelList;
    private BGAnimRecyclerView mBgAnimRecyclerView;
    private ItemTouchHelper mTouchHelper;
    private DGSPItemAdapter mAdapter;
    /**
     * 当前组的唯一标识
     */
    private String mGroupCode;
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
    /**
     * 是否可以长按拖拽改变位置
     */
    private boolean mIsLongPressToDrag;
    /**
     * 选择列表里的item监听，分为点击和长按
     */
    private OnItemViewClickListener mOnItemViewClickListener;
    /**
     * 拖拽的监听
     */
    private OnDragItemListener mOnDragItemListener;
    /**
     * 当前选中的viewholder
     */
    private DraggableSelectViewHolder mCurrentSelectViewHolder;

    public DraggableListController(Context context, String groupCode, List<IDGSPItem> itemModelList,
                                   final BGAnimRecyclerView bgAnimRecyclerView, DGSPItemAdapter adapter, DGSPFavoriteConfig favoriteConfig) {
        mContext = context;
        mItemModelList = itemModelList;
        mBgAnimRecyclerView = bgAnimRecyclerView;
        mGroupCode = groupCode;
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);// 设为水平
        bgAnimRecyclerView.setLayoutManager(manager);
        if (adapter == null) {
            mAdapter = new DGSPItemAdapter(groupCode, itemModelList, favoriteConfig);
        } else {
            mAdapter = adapter;
        }
        mAdapter.setOnItemClickListener(mOnSelect);
        bgAnimRecyclerView.setAdapter(mAdapter);
        mTouchHelper = new ItemTouchHelper(touchCallback);
        mTouchHelper.attachToRecyclerView(bgAnimRecyclerView);
    }

    /**
     * 处理拖动需要实现的监听
     */
    private ItemTouchHelper.Callback touchCallback = new ItemTouchHelper.Callback() {
        /**
         * 正在拖拽的viewholder,因为松开时回调的viewHolder为空，所以记录开始拖拽的viewholder
         */
        private DraggableSelectViewHolder draggingViewHolder;

        @Override
        public boolean isLongPressDragEnabled() {
            // 通过长按后判断是否被限制不能拖动，手动调用startDrag实现
            return false;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            // 上下左右都可以移动
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, dragFlags);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (viewHolder instanceof DraggableSelectViewHolder) {
                DraggableSelectViewHolder itemViewHolder = (DraggableSelectViewHolder) viewHolder;
                draggingViewHolder = itemViewHolder;
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    // 开始拖拽,背景使用view
                    mBgAnimRecyclerView.showBgViewIfNecessary(viewHolder);
                    if (mOnDragItemListener != null) {
                        mOnDragItemListener.onStartDrag(mBgAnimRecyclerView, mGroupCode, itemViewHolder);
                    }
                    itemViewHolder.startDrag();
                }
            } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
                if (mOnDragItemListener != null) {
                    // 释放拖拽，背景图片恢复成drawBitmap
                    mBgAnimRecyclerView.resetBgView();
                    // 释放的时候viewHolder为null，可以用开始拖拽记录的viewholder
                    mOnDragItemListener.onReleaseItem(mBgAnimRecyclerView, mGroupCode, draggingViewHolder);
                }
            }
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            if (viewHolder instanceof DraggableSelectViewHolder) {
                DraggableSelectViewHolder itemViewHolder = (DraggableSelectViewHolder) viewHolder;
                if (mOnDragItemListener != null) {
                    mOnDragItemListener.onDragFinish(mBgAnimRecyclerView, mGroupCode, itemViewHolder);
                }
                // 经过归位的动画后，item回到列表里
                if (draggingViewHolder != null) {
                    draggingViewHolder.endDrag();
                    draggingViewHolder = null;
                }
            }
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX,
            float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            if (viewHolder instanceof DraggableSelectViewHolder) {
                DraggableSelectViewHolder itemViewHolder = (DraggableSelectViewHolder) viewHolder;
                if (mOnDragItemListener != null) {
                    mOnDragItemListener.onDragging(mBgAnimRecyclerView, mGroupCode, itemViewHolder, dX, dY);
                }
            }
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
            RecyclerView.ViewHolder target) {
            int oldPosition = viewHolder.getAdapterPosition();
            int newPosition = target.getAdapterPosition();
            if (mLockPositionStart >= 0 && mLockPositionEnd >= 0 && newPosition >= mLockPositionStart
                && newPosition <= mLockPositionEnd) {
                // 限制范围内，不可以移动
                return true;
            }
            Collections.swap(mItemModelList, oldPosition, newPosition);
            boolean isMoveCurrentSelected =
                mCurrentSelectViewHolder != null && oldPosition == mCurrentSelectViewHolder.getAdapterPosition();
            if (isMoveCurrentSelected) {
                // 不是移动当前选中项背景不动
                mBgAnimRecyclerView.selectItem(newPosition, true, -1, false);
            }
            mAdapter.notifyItemMoved(oldPosition, newPosition);
            if (!isMoveCurrentSelected) {
                // 刷新当前选中的值，由于isMoveCurrentSelected为true的时候已经在selectItem里处理过，就不重复处理
                mBgAnimRecyclerView.moveItemRefreshPosition(oldPosition, newPosition);
            }
            if (mOnDragItemListener != null) {
                mOnDragItemListener.onItemMoved(mBgAnimRecyclerView, mGroupCode, oldPosition, newPosition,
                    mItemModelList);
            }
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            // disabled
        }
    };

    /**
     * 点击和长按的事件
     */
    private OnItemViewClickListener mOnSelect = new OnItemViewClickListener() {
        @Override
        public void onClick(String groupName, IDGSPItem itemModel, DraggableSelectViewHolder viewHolder,
            RecyclerView.Adapter adapter) {
            mCurrentSelectViewHolder = viewHolder;
            mBgAnimRecyclerView.selectItem(viewHolder.getAdapterPosition(), false);
            if (mOnItemViewClickListener != null) {
                mOnItemViewClickListener.onClick(groupName, itemModel, viewHolder, adapter);
            }
        }

        @Override
        public void onLongClick(String groupName, IDGSPItem itemModel, DraggableSelectViewHolder viewHolder,
            RecyclerView.Adapter adapter) {
            // 长按时判断是否被锁住不能拖拽
            int position = viewHolder.getAdapterPosition();
            if (mIsLongPressToDrag) {
                if (mLockPositionStart >= 0 && mLockPositionEnd >= 0 && position >= mLockPositionStart
                    && position <= mLockPositionEnd) {
                    return;
                }
                mTouchHelper.startDrag(viewHolder);
            } else if (mOnItemViewClickListener != null) {
                mOnItemViewClickListener.onLongClick(groupName, itemModel, viewHolder, adapter);
            }

        }
    };

    /**
     * 是否可以长按拖拽改变位置
     *
     * @param isLongPressToDrag
     */
    public void setLongPressToDrag(boolean isLongPressToDrag) {
        mIsLongPressToDrag = isLongPressToDrag;
    }

    public void setOnItemClickListener(OnItemViewClickListener onItemViewClickListener) {
        mOnItemViewClickListener = onItemViewClickListener;
    }

    /**
     * 设置拖拽监听
     *
     * @param onDragItemListener
     */
    public void setOnDragItemListener(OnDragItemListener onDragItemListener) {
        mOnDragItemListener = onDragItemListener;
    }

    /**
     * 设置不可拖拽也不可被排序的item范围，必须是开头的一段或者结束的一段
     * 都小于0则不限制
     *
     * @param startPosition 开始的position
     * @param endPosition   结束的position
     */
    public void setLockItemRange(int startPosition, int endPosition) {
        if (startPosition > endPosition) {
            throw new IllegalArgumentException("end position is bigger than start");
        }
        mLockPositionStart = startPosition;
        mLockPositionEnd = endPosition;
    }

    /**
     * 清除选中背景
     */
    public void clearSelect() {
        mBgAnimRecyclerView.clearSelect();
    }

    public void onDestroy() {
        if (mBgAnimRecyclerView != null) {
            mBgAnimRecyclerView.recycle();
        }
    }
}
