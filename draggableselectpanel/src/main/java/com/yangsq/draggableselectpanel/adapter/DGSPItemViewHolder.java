package com.yangsq.draggableselectpanel.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yangsq.draggableselectpanel.R;
import com.yangsq.draggableselectpanel.interfaces.OnItemViewClickListener;
import com.yangsq.draggableselectpanel.model.IDGSPItem;
import com.squareup.picasso.Picasso;
import com.yangsq.draggableselectpanel.util.CircleTransform;

/**
 * 继承{@link #BGViewHolder}的ViewHolder,如果具体业务有其他ui要求，继承这个类
 */

public class DGSPItemViewHolder extends DraggableSelectViewHolder {
    /**
     * 显示name的控件
     */
    protected CheckedTextView mCtvName;
    /**
     * 图标
     */
    protected ImageView mIvIco;

    /**
     * 收藏图标
     */
    protected ImageView mIvFavorite;
    /**
     * itemView
     */
    protected View mItemView;
    /**
     * 根view，用来addViews，必须是relativeLayout
     */
    protected RelativeLayout mRLItemRoot;
    private int mIcoSize;

    public DGSPItemViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
    }

    public void initViews() {
        mCtvName = (CheckedTextView) itemView.findViewById(R.id.ctv_name);
        mIvIco = (ImageView) itemView.findViewById(R.id.iv_image_ico);
        mIvFavorite = (ImageView) itemView.findViewById(R.id.iv_favorite_ico);
        mRLItemRoot = (RelativeLayout) itemView.findViewById(R.id.rl_item_root);
        if (mCtvName == null) {
            throw new IllegalArgumentException(
                "Can not find a CheckedTextView which the id is ctv_name,if you use a custom item layout and named a different id,you need make a new ViewHolder and extends DraggableSelectViewHolder");
        }
        if (mIvIco == null) {
            throw new IllegalArgumentException(
                    "Can not find a ImageView which the id is iv_image_ico,if you use a custom item layout and named a different id,you need make a new ViewHolder and extends DraggableSelectViewHolder");
        }
        if (mIvFavorite == null) {
            throw new IllegalArgumentException(
                    "Can not find a ImageView which the id is iv_favorite_ico,if you use a custom item layout and named a different id,you need make a new ViewHolder and extends DraggableSelectViewHolder");
        }
        if (mRLItemRoot == null) {
            throw new IllegalArgumentException(
                    "Can not find a RelativeLayout which the id is rl_item_root,if you use a custom item layout and named a different id,you need make a new ViewHolder and extends DraggableSelectViewHolder");
        }
        mIcoSize = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.dsgp_item_pic_size);
    }

    @Override
    public View getBGBelongView() {
        return mIvIco;
    }

    @Override
    public RelativeLayout getRootView() {
        return mRLItemRoot;
    }

    @Override
    public void setName(String name) {
        mCtvName.setText(name);
    }

    @Override
    public void setListener(final String groupCode, final IDGSPItem itemModel,
        final OnItemViewClickListener onItemClickListener, final RecyclerView.Adapter adapter) {
        mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(groupCode, itemModel, DGSPItemViewHolder.this, adapter);
                }
            }
        });
        mItemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onLongClick(groupCode, itemModel, DGSPItemViewHolder.this, adapter);
                }
                return false;
            }
        });
    }

    @Override
    public void setImage(int idImage) {
        Picasso.with(mIvIco.getContext())
            .load(idImage)
            .centerCrop()
            .resize(mIcoSize, mIcoSize)
            .transform(new CircleTransform())
            .into(mIvIco);
    }

    @Override
    public void setImage(String imageUri) {
        Picasso.with(mIvIco.getContext())
            .load(imageUri)
            .centerCrop()
            .resize(mIcoSize, mIcoSize)
            .transform(new CircleTransform())
            .into(mIvIco);
    }

    /**
     * 开始拖动
     */
    @Override
    public void startDrag() {
        // 开始拖动的ui处理，这边设置名字不显示
        mCtvName.setVisibility(View.INVISIBLE);
    }

    /**
     * 结束拖拽，并且拖拽项经过动画回到他的位置上的时候做的ui改变
     */
    @Override
    public void endDrag() {
        mCtvName.setVisibility(View.VISIBLE);
    }

    @Override
    public void setIsFavorite(boolean isFavorite) {
        mIvFavorite.setVisibility(isFavorite ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setChecked(boolean checked) {
        mCtvName.setChecked(checked);
    }
}
