package com.meitu.makeuppicker.app.makeuppicker.widget.makeuppanel.controller;

import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.meitu.makeuppicker.R;
import com.meitu.makeuppicker.app.makeuppicker.widget.makeuppanel.data.MakeupProvider;
import com.meitu.makeuppicker.util.MyDialog;
import com.yangsq.draggableselectpanel.helper.DGSPFavoriteHelper;
import com.yangsq.draggableselectpanel.helper.DGSPItemHelper;
import com.yangsq.draggableselectpanel.view.DraggableGroupSelectPanelView;
import com.yangsq.draggableselectpanel.adapter.DraggableSelectViewHolder;
import com.yangsq.draggableselectpanel.view.DGSPViewAdapter;
import com.yangsq.draggableselectpanel.interfaces.OnDragToTitleBtnListener;
import com.yangsq.draggableselectpanel.interfaces.OnItemViewClickListener;
import com.yangsq.draggableselectpanel.model.IDGSPItem;
import com.yangsq.draggableselectpanel.model.ItemGroupModel;

/**
 * 对{@link DraggableGroupSelectPanelView}进行选择妆容逻辑处理的Controller
 *
 * @author ysq 2016/9/29.
 */
public class MakeupListPanelController {
    private static final String TEST_USER_ID = "testUser";// 测试用户id
    private static final String TAG = "PanelController";
    private Activity mActivity;
    private DraggableGroupSelectPanelView mPanelView;
    private MakeupProvider mMakeupProvider;
    private DGSPViewAdapter mPanelAdapter;

    public MakeupListPanelController(Activity activity, DraggableGroupSelectPanelView panelView) {
        if (activity == null || panelView == null) {
            throw new IllegalArgumentException("can not create MakeupListPanelController,arguments is null");
        }

        mActivity = activity;
        mPanelView = panelView;

    }

    /**
     * 主要处理获取妆容数据的逻辑，分别为收藏组，正式妆容组，特殊管理中心组
     */
    public void init() {
        mPanelView.setFragmentManager(mActivity.getFragmentManager());
        // mPanelView.setOnDragItemListener(mOnDragItemListener);
        mPanelView.setOnGroupSelectListener(mOnGroupSelectListener);
        mPanelView.setOnMakeupClickListener(mOnItemViewClickListener);
        initCancelFavoriteBtn();
        mMakeupProvider = new MakeupProvider();
        mPanelView.getFavoriteConfig()
                .setDataTypeName("makeup")
                .setFavoriteIcoResId(R.drawable.makeup_favorite_tab_ic)
                .setUseFavorite(true)
                .setUserId(TEST_USER_ID);
        getMakeupList();
    }

    /**
     * 初始化panel里顶部按钮,设为取消收藏
     */
    private void initCancelFavoriteBtn() {
        TextView cancelView = mPanelView.getTitleBtn();
        if (cancelView != null) {
            cancelView.setText(R.string.makeup_cancel_favorite);
            Drawable cancelFavoriteIco = ContextCompat.getDrawable(mActivity, R.drawable.makeup_cancel_favorite_ic);
            cancelFavoriteIco.setBounds(0, 0, cancelFavoriteIco.getMinimumWidth(),
                cancelFavoriteIco.getMinimumHeight());
            cancelView.setCompoundDrawables(cancelFavoriteIco, null, null, null);
        }
        mPanelView.setOnTitleBtnClick(mOnCancelFavoriteRelease);
    }

    /**
     * 拖拽到顶部按钮之后释放的事件
     */
    private OnDragToTitleBtnListener mOnCancelFavoriteRelease = new OnDragToTitleBtnListener() {
        @Override
        public void OnDragToTitleBtn(final RecyclerView recyclerView, IDGSPItem dgspItem,
            final DraggableSelectViewHolder viewHolder) {
            if (DGSPFavoriteHelper.DGSP_FAVORITE_GROUP_CODE.equals(dgspItem.getCode())) {
                // 取消收藏

                MyDialog.showConfirmDialog(mActivity, mActivity.getString(R.string.makeup_cancel_favorite),
                    mActivity.getString(R.string.makeup_msg_confirm_cancel_favorite),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 取消收藏
                        }
                    });

            }
        }
    };

    /**
     * 获取妆容分组数据
     */
    private void getMakeupList() {
        mMakeupProvider.getMakeupGroup(new MakeupProvider.GetMakeupGroupCallback() {
            @Override
            public void onGetResult(List<ItemGroupModel> makeupGroupModelList) {
                if (makeupGroupModelList != null) {
                    for (ItemGroupModel makeupGroupModel : makeupGroupModelList) {
                        makeupGroupModel.setAddCancelItemOnTop(true);
                    }
                    // 插入管理空组
                    ItemGroupModel manageGroup = new ItemGroupModel();
                    manageGroup.setIconResId(R.drawable.makeup_panel_manage_center_ic);
                    manageGroup.setGroupCode(MakeupProvider.MANAGE_CENTER_GROUP_CODE);
                    makeupGroupModelList.add(manageGroup);
                    mPanelAdapter = new DGSPViewAdapter(mActivity, makeupGroupModelList);
                    mPanelView.setAdapter(mPanelAdapter);
                }
            }
        });
    }

    private OnItemViewClickListener mOnItemViewClickListener = new OnItemViewClickListener() {
        @Override
        public void onClick(String groupCode, IDGSPItem itemModel, DraggableSelectViewHolder viewHolder,
            RecyclerView.Adapter adapter) {
            // TODO: 2016/9/29 选中妆容之后的逻辑处理
            if (DGSPItemHelper.CANCEL_MODEL_CODE.equals(itemModel.getCode())) {
                // 选择“无”
                Toast.makeText(mActivity, "取消选择", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mActivity, "groupCode:" + groupCode + ",name:" + itemModel.getName(), Toast.LENGTH_SHORT)
                    .show();
            }

        }

        @Override
        public void onLongClick(String groupCode, final IDGSPItem itemModel, final DraggableSelectViewHolder viewHolder,
            RecyclerView.Adapter adapter) {
            if (DGSPItemHelper.CANCEL_MODEL_CODE.equals(itemModel.getCode())) {
                // 选择“无”
            } else {
                if (itemModel.isFavorite()) {
                    // 弹出取消收藏
                    String[] items = {mActivity.getString(R.string.makeup_cancel_favorite)};
                    MyDialog.showSelectDialog(mActivity, mActivity.getString(R.string.operate), items,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    mPanelView.getFavoriteManager().cancelFavorite(itemModel, viewHolder);
                                }
                            }
                        });
                } else {
                    // 弹出选择收藏
                    String[] items = {mActivity.getString(R.string.makeup_set_favorite)};
                    MyDialog.showSelectDialog(mActivity, mActivity.getString(R.string.operate), items,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    mPanelView.getFavoriteManager().addFavorite(itemModel, viewHolder);
                                }
                            }
                        });
                }
            }

        }
    };

    /**
     * 切换组监听，当切换到收藏时检查收藏内容，如果没有收藏弹出提示框;切换到管理中心时进行管理中心逻辑处理
     */
    private DraggableGroupSelectPanelView.OnGroupSelectListener mOnGroupSelectListener =
        new DraggableGroupSelectPanelView.OnGroupSelectListener() {
            @Override
            public boolean onGroupSelect(String groupCode) {
                boolean isIntercept = false;
                if (DGSPFavoriteHelper.DGSP_FAVORITE_GROUP_CODE.equals(groupCode)) {
                    List<IDGSPItem> favoriteList = mPanelView.getFavoriteManager().getFavoriteList();
                    if (favoriteList != null && favoriteList.size() > 0) {
                        if (favoriteList.size() == 1
                            && DGSPItemHelper.CANCEL_MODEL_CODE.equals(favoriteList.get(0).getCode())) {
                            // 只有一项取消
                            isIntercept = true;
                        }
                    } else {
                        isIntercept = true;
                    }
                    if (isIntercept) {
                        // 弹出提醒
                        noFavorite();
                    }
                } else if (MakeupProvider.MANAGE_CENTER_GROUP_CODE.equals(groupCode)) {
                    // 管理中心
                    isIntercept = true;
                    manageCenter();
                }

                return isIntercept;
            }

            private void manageCenter() {
                Toast.makeText(mActivity, "管理中心", Toast.LENGTH_SHORT).show();
            }

            private void noFavorite() {
                MyDialog.showAlertDialog(mActivity, mActivity.getString(R.string.hint),
                    mActivity.getString(R.string.makeup_msg_no_favorite));
            }
        };


    public void onDestroy() {
        mPanelAdapter.onDestroy();
    }
}
