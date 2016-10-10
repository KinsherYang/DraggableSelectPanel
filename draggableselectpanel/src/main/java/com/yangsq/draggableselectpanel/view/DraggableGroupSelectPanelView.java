package com.yangsq.draggableselectpanel.view;

import java.util.List;

import org.greenrobot.eventbus.EventBus;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yangsq.draggableselectpanel.R;
import com.yangsq.draggableselectpanel.adapter.DraggableSelectViewHolder;
import com.yangsq.draggableselectpanel.adapter.DGSPItemAdapter;
import com.yangsq.draggableselectpanel.adapter.DGSPViewAdapter;
import com.yangsq.draggableselectpanel.event.DGSPFavoriteEvent;
import com.yangsq.draggableselectpanel.event.DGSPFavoriteGroupChangeEvent;
import com.yangsq.draggableselectpanel.event.DGSPSelectItemEvent;
import com.yangsq.draggableselectpanel.helper.DGSPFavoriteHelper;
import com.yangsq.draggableselectpanel.interfaces.OnDragItemListener;
import com.yangsq.draggableselectpanel.interfaces.OnDragToTitleBtnListener;
import com.yangsq.draggableselectpanel.interfaces.OnItemViewClickListener;
import com.yangsq.draggableselectpanel.model.DGSPFavoriteConfig;
import com.yangsq.draggableselectpanel.model.IDGSPItem;

/**
 * 可拖动item的横向选择列表，对列表进行分组
 *
 * @author ysq 2016/9/27.
 */
public class DraggableGroupSelectPanelView extends LinearLayout {
    private static final String TAG = "DraggablePanelView";
    /**
     * 所有分组标题卡的父view
     */
    private View mVGroupParent;
    /**
     * 拖动item时会显示的操作按钮,标题按钮
     */
    private View mVTitleBtn;
    /**
     * 顶部按钮
     */
    private TextView mTVTitleBtn;
    /**
     * 分组的父View
     */
    private LinearLayout mLLItemGroup;
    /**
     * 当前显示的fragment
     */
    private DraggableListFragment currentFragment;
    private FragmentManager mFragmentManager;
    /**
     * 当前所选中的item里的CheckedTextView
     */
    private DraggableSelectViewHolder currentSelectViewHolder;
    /**
     * 列表里的item点击监听
     */
    private OnItemViewClickListener mOnItemViewClickListener;
    /**
     * 拖动item监听
     */
    private OnDragItemListener mOnDragItemListener;
    /**
     * 拖拽item到{@link #mVTitleBtn}后释放的事件监听
     */
    private OnDragToTitleBtnListener mOnTitleBtnTrigger;
    /**
     * 切换分组监听
     */
    private OnGroupSelectListener mOnGroupSelectListener;
    /**
     * 收藏功能的配置
     */
    private DGSPFavoriteConfig mDGSPFavoriteConfig = new DGSPFavoriteConfig();

    /**
     * 适配器
     */
    private DGSPViewAdapter mAdapter;
    private FavoriteManager mFavoriteManager = new FavoriteManager();

    /**
     * 切换分组监听
     */
    public interface OnGroupSelectListener {
        /**
         * 切换分组时的事件
         *
         * @param groupCode 分组code
         * @return 返回是否截断view里本身的切换事件, true的话将不执行切换动作
         */
        boolean onGroupSelect(String groupCode);
    }

    public DraggableGroupSelectPanelView(Context context) {
        this(context, null);
    }

    public DraggableGroupSelectPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    private void initViews() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dsgp_view, this);
        mVGroupParent = view.findViewById(R.id.hsv_item_group);
        mVTitleBtn = view.findViewById(R.id.rl_title_btn);
        mLLItemGroup = (LinearLayout) view.findViewById(R.id.ll_item_group);
        mTVTitleBtn = (TextView) view.findViewById(R.id.tv_title_btn);
    }

    /**
     * 生成顶部tab和fragment
     *
     * @param groupCode         组的唯一标示
     * @param iconResId         分组的图标,小于0将不显示
     * @param groupName         组的名字,为空将不显示
     * @param itemModelList     列表
     * @param isCanDrag         列表是否可以拖拽重新排序
     * @param lockStartPosition 不可拖拽也不可被排序的item范围,开始
     * @param lockEndPosition   不可拖拽也不可被排序的item范围,开始
     * @param itemAdapter       特殊ui逻辑处理的adapter，如果没有特殊ui逻辑可传null
     * @param position          拖拽列表标题所在位置,小于0默认添加到队列最后
     */
    public void generateItemListFragment(String groupCode, int iconResId, String groupName,
                                         List<IDGSPItem> itemModelList, boolean isCanDrag, int lockStartPosition, int lockEndPosition,
                                         DGSPItemAdapter itemAdapter, int position) {
        if (mLLItemGroup != null) {
            if (mFragmentManager == null) {
                throw new IllegalArgumentException("mFragmentManager is null");
            }
            DraggableListFragment fragment = null;
            if (itemModelList != null) {
                fragment = DraggableListFragment.createMakeupListFragment(groupCode, itemModelList, itemAdapter,
                    mDGSPFavoriteConfig);
                fragment.setOnItemClickListener(mItemClickListener);
                fragment.setOnDragItemListener(mOnAllDragItem);
                fragment.setIsCanDrag(isCanDrag);
                fragment.setLockItemRange(lockStartPosition, lockEndPosition);
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.add(R.id.fl_makeup_list, fragment);
                transaction.hide(fragment);
                transaction.commit();
            }
            if (position >= 0) {
                mLLItemGroup.addView(getTabView(iconResId, groupCode, groupName, fragment), position);
            } else {
                mLLItemGroup.addView(getTabView(iconResId, groupCode, groupName, fragment));
            }
        }
    }

    public void clearDataView() {
        mLLItemGroup.removeAllViews();
        // TODO: 2016/10/9 移除fragmenManager里的所有fragment
    }

    /**
     * 获取分组的标题
     *
     * @param resId     分组的图标,小于0将不显示
     * @param groupName 组的名字,为空将不显示
     * @param groupCode 组的code
     * @param fragment  点击需要显示的fragment
     * @return 标题view
     */
    private View getTabView(int resId, final String groupCode, String groupName, final DraggableListFragment fragment) {
        View tabView = LayoutInflater.from(getContext()).inflate(R.layout.dsgp_tab, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            getResources().getDimensionPixelOffset(R.dimen.dsgp_tab_title_width), LayoutParams.MATCH_PARENT);
        tabView.setLayoutParams(layoutParams);
        CheckedTextView textView = (CheckedTextView) tabView.findViewById(R.id.tv_panel_tab);
        textView.setText(groupName);
        if (resId > 0) {
            Drawable drawableLeft = ContextCompat.getDrawable(getContext(), resId);
            if (drawableLeft != null) {
                drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
                textView.setCompoundDrawables(drawableLeft, null, null, null);
            }
        }
        tabView.setOnClickListener(getOnTabClickListener(groupCode, fragment));
        return tabView;
    }

    private View mCurrentSelectTab;

    /**
     * 点击tab
     *
     * @param groupCode 组code
     * @param fragment  点击要显示的fragment
     * @return 点击事件
     */
    private OnClickListener getOnTabClickListener(final String groupCode, final DraggableListFragment fragment) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnGroupSelectListener != null) {
                    if (mOnGroupSelectListener.onGroupSelect(groupCode)) {
                        // 如果返回true则不执行切换动作
                        return;
                    }
                }
                if (fragment == null || (currentFragment != null && currentFragment.equals(fragment))) {
                    return;
                }
                // 改变上一次选中的tab的ui
                if (mCurrentSelectTab != null) {
                    changTabUi(mCurrentSelectTab, false);
                }
                changTabUi(v, true);
                mCurrentSelectTab = v;
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                if (currentFragment != null) {
                    transaction.hide(currentFragment);
                }
                currentFragment = fragment;
                transaction.show(fragment);
                transaction.commit();
            }

            /**
             * 改变tab的ui
             * @param mCurrentSelectTab 需要改变ui的tab
             * @param isCheck 是否选中状态
             */
            private void changTabUi(View mCurrentSelectTab, boolean isCheck) {
                CheckedTextView textView = (CheckedTextView) mCurrentSelectTab.findViewById(R.id.tv_panel_tab);
                if (isCheck) {
                    mCurrentSelectTab.setBackgroundResource(R.drawable.dgsp_title_bg_checked);
                    textView.setChecked(true);
                } else {
                    mCurrentSelectTab.setBackgroundResource(R.drawable.dgsp_title_bg_normal);
                    textView.setChecked(false);
                }
            }
        };
    }

    /**
     * 选中列表项
     */
    private OnItemViewClickListener mItemClickListener = new OnItemViewClickListener() {
        @Override
        public void onClick(String groupCode, IDGSPItem itemModel, DraggableSelectViewHolder viewHolder,
            RecyclerView.Adapter adapter) {
            // 取消上一次选中项
            if (currentSelectViewHolder != null)
                currentSelectViewHolder.setChecked(false);
            viewHolder.setChecked(true);
            currentSelectViewHolder = viewHolder;
            // 发通知通知其他fragment取消当前选项
            EventBus.getDefault().post(new DGSPSelectItemEvent(itemModel.getCode(), groupCode));
            if (mOnItemViewClickListener != null) {
                mOnItemViewClickListener.onClick(groupCode, itemModel, viewHolder, adapter);
            }
        }

        @Override
        public void onLongClick(String groupCode, IDGSPItem itemModel, DraggableSelectViewHolder viewHolder,
            RecyclerView.Adapter adapter) {
            if (mOnItemViewClickListener != null) {
                mOnItemViewClickListener.onLongClick(groupCode, itemModel, viewHolder, adapter);
            }
        }
    };

    /**
     * 拖拽的时候先做是否拖拽到按钮上的判断,如果拖拽到按钮区域并且放开，触发事件
     */
    private OnDragItemListener mOnAllDragItem = new OnDragItemListener() {
        /**
         * 标题按钮在屏幕上的位置
         */
        private Rect mTitleBtnLocation;
        /**
         * 开始拖拽的时候item在屏幕上的位置，真正位置要根据拖拽的偏移得出
         */
        private Rect mItemStartLocation;
        /**
         * 拖拽的item是否在标题按钮上
         */
        private boolean mIsItemOnTitleBtn = false;

        @Override
        public void onStartDrag(RecyclerView recyclerView, String groupCode, DraggableSelectViewHolder viewHolder) {
            mVGroupParent.setVisibility(GONE);
            mVTitleBtn.setVisibility(VISIBLE);
            initItemStartLocation(viewHolder);
            if (mOnDragItemListener != null) {
                mOnDragItemListener.onStartDrag(recyclerView, groupCode, viewHolder);
            }
        }

        @Override
        public void onDragging(RecyclerView recyclerView, String groupCode, DraggableSelectViewHolder viewHolder,
            float dx, float dy) {
            if (mTitleBtnLocation == null) {
                mTitleBtnLocation = new Rect();
                mVTitleBtn.getGlobalVisibleRect(mTitleBtnLocation);
            }
            initItemStartLocation(viewHolder);
            mIsItemOnTitleBtn = checkFocusOnTitleBtn(dx, dy);
            if (mOnDragItemListener != null) {
                mOnDragItemListener.onDragging(recyclerView, groupCode, viewHolder, dx, dy);
            }
        }

        @Override
        public void onReleaseItem(RecyclerView recyclerView, String groupCode, DraggableSelectViewHolder viewHolder) {
            mVGroupParent.setVisibility(VISIBLE);
            mVTitleBtn.setVisibility(GONE);
            if (mOnDragItemListener != null) {
                mOnDragItemListener.onReleaseItem(recyclerView, groupCode, viewHolder);
            }
            if (mIsItemOnTitleBtn && mOnTitleBtnTrigger != null) {
                // 触发标题按钮事件
                if (recyclerView.getAdapter() instanceof DGSPItemAdapter) {
                    IDGSPItem item = ((DGSPItemAdapter) recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());
                    mOnTitleBtnTrigger.OnDragToTitleBtn(recyclerView, item, viewHolder);
                }
            }
        }

        @Override
        public void onDragFinish(RecyclerView recyclerView, String groupCode, DraggableSelectViewHolder viewHolder) {
            mItemStartLocation = null;
            if (mOnDragItemListener != null) {
                mOnDragItemListener.onDragFinish(recyclerView, groupCode, viewHolder);
            }
        }

        @Override
        public void onItemMoved(RecyclerView recyclerView, String groupCode, int oldPosition, int newPosition,
            List<IDGSPItem> resultList) {
            // 处理收藏逻辑
            if (DGSPFavoriteHelper.DGSP_FAVORITE_GROUP_CODE.equals(groupCode)) {
                // 收藏重新排序
                EventBus.getDefault().post(
                    new DGSPFavoriteGroupChangeEvent(DGSPFavoriteGroupChangeEvent.FAVORITE_GROUP_CHANGE_ACTION_SWAP,
                        resultList, mDGSPFavoriteConfig.getDataTypeName()));
            }
            if (mOnDragItemListener != null) {
                mOnDragItemListener.onItemMoved(recyclerView, groupCode, oldPosition, newPosition, resultList);
            }
        }

        /**
         * 检查当前拖拽的item是否在标题按钮上，如果是则改变状态和改变焦点
         * 拖动时，不能根据getGlobalVisibleRect直接获取item的位置，因为超过这个view的范围后可能出现left和top都是0的情况
         * 不能和真实的top和left为0的情况做区分，所以只能根据初始位置在加上当前位移dx，dy
         * @param dx 拖拽之后的偏移x
         * @param dy 拖拽之后的偏移y
         * @return 是否在标题按钮上
         */
        private boolean checkFocusOnTitleBtn(float dx, float dy) {
            if (mTitleBtnLocation == null || mItemStartLocation == null) {
                Log.e(TAG, "checkFocusOnTitleBtn error,location is null");
                mVTitleBtn.setFocusable(false);
                return false;
            }
            if (mItemStartLocation.left + dx > mTitleBtnLocation.right
                || mItemStartLocation.right + dx < mTitleBtnLocation.left
                || mItemStartLocation.top + dy > mTitleBtnLocation.bottom
                || mItemStartLocation.bottom + dy < mTitleBtnLocation.top) {
                mVTitleBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dsgp_title_btn_bg_normal));
                return false;
            }
            mVTitleBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dsgp_title_btn_bg_focus));
            return true;

        }

        /**
         * 初始化item的开始位置
         * @param viewHolder
         */
        private void initItemStartLocation(DraggableSelectViewHolder viewHolder) {
            if (mItemStartLocation == null) {
                mItemStartLocation = new Rect();
                View view = viewHolder.getBGBelongView();
                if (view == null) {
                    view = viewHolder.itemView;
                }
                view.getGlobalVisibleRect(mItemStartLocation);
            }
        }
    };

    /**
     * 收藏的处理
     */
    public class FavoriteManager {
        /**
         * 加入收藏
         *
         * @param dgspItem   当前选中项
         * @param viewHolder 当前选中ViewHolder,如果viewholder里有定义收藏动画将有用
         */
        public void addFavorite(IDGSPItem dgspItem, DraggableSelectViewHolder viewHolder) {
            setIsFavorite(true, dgspItem, viewHolder);
        }

        /**
         * 取消收藏
         *
         * @param dgspItem   当前选中项
         * @param viewHolder 当前选中ViewHolder,如果viewholder里有定义收藏动画将有用
         */
        public void cancelFavorite(IDGSPItem dgspItem, DraggableSelectViewHolder viewHolder) {
            setIsFavorite(false, dgspItem, viewHolder);
        }

        /**
         * 获取当前收藏列表，包含取消选项
         *
         * @return
         */
        public List<IDGSPItem> getFavoriteList() {
            return mAdapter.getFavoriteList();
        }

        /**
         * 加入收藏或者取消收藏
         *
         * @param isFavorite 是否加入收藏
         * @param viewHolder 转换得来的viewHolder,后面可以用来做动画的效果
         */
        private void setIsFavorite(boolean isFavorite, IDGSPItem dgspItem, DraggableSelectViewHolder viewHolder) {
            int action = isFavorite ? DGSPFavoriteEvent.DGSP_ITEM_FAVORITE_ACTION_SET
                : DGSPFavoriteEvent.DGSP_ITEM_FAVORITE_ACTION_CANCEL;
            DGSPFavoriteEvent dgspFavoriteEvent =
                new DGSPFavoriteEvent(action, dgspItem.getCode(), mDGSPFavoriteConfig.getDataTypeName());
            dgspFavoriteEvent.setViewHolder(viewHolder);
            dgspFavoriteEvent.setDGSPItem(dgspItem);
            EventBus.getDefault().post(dgspFavoriteEvent);
        }
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    /**
     * 设置点击选项监听
     *
     * @param onItemViewClickListener 点击监听
     */
    public void setOnMakeupClickListener(OnItemViewClickListener onItemViewClickListener) {
        mOnItemViewClickListener = onItemViewClickListener;
    }

    /**
     * 设置拖拽的监听事件
     *
     * @param onDragItemListener 拖拽监听
     */
    public void setOnDragItemListener(OnDragItemListener onDragItemListener) {
        mOnDragItemListener = onDragItemListener;
    }

    /**
     * 设置标题处按钮的触发事件
     *
     * @param onTitleBtnClick
     */
    public void setOnTitleBtnClick(OnDragToTitleBtnListener onTitleBtnClick) {
        mOnTitleBtnTrigger = onTitleBtnClick;
    }

    /**
     * 获取标题按钮的文字，可以用来设置文字的内容、图标
     *
     * @return
     */
    public TextView getTitleBtn() {
        return mTVTitleBtn;
    }

    /**
     * 设置切换组的监听事件
     *
     * @param onGroupSelectListener
     */
    public void setOnGroupSelectListener(OnGroupSelectListener onGroupSelectListener) {
        mOnGroupSelectListener = onGroupSelectListener;
    }

    public DGSPFavoriteConfig getFavoriteConfig() {
        return mDGSPFavoriteConfig;
    }

    public DGSPViewAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(DGSPViewAdapter adapter) {
        this.mAdapter = adapter;
        mAdapter.setPanelView(this);
        mAdapter.initViews();
    }

    /**
     * 获取收藏操作相关的功能类
     *
     * @return
     */
    public FavoriteManager getFavoriteManager() {
        return mFavoriteManager;
    }
}
