package com.yangsq.draggableselectpanel.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yangsq.draggableselectpanel.interfaces.OnItemViewClickListener;
import com.yangsq.draggableselectpanel.model.IDGSPItem;

/**
 * 包含显示图片、名字、收藏的定义，供子类继承
 * @author ysq 2016/10/9.
 */
public abstract class DraggableSelectViewHolder extends BGViewHolder {
    public DraggableSelectViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * 改变选中状态
     * @param checked
     */
    public abstract void setChecked(boolean checked);

    /**
     * 把名字显示到ui上
     * @param name
     */
    public abstract void setName(String name);

    /**
     * 设置item需要的监听
     * @param groupCode 组code
     * @param itemModel item的实体
     * @param onItemClickListener 点击事件，包括单击和长按
     * @param adapter 所使用的adapter，用于传递下去，让监听处方便操作数据与ui
     */
    public abstract void setListener(final String groupCode, final IDGSPItem itemModel,
        final OnItemViewClickListener onItemClickListener, final RecyclerView.Adapter adapter);

    /**
     * 加载本地资源图片到ui
     * @param idImage  本地资源id
     */
    public abstract void setImage(int idImage);
    /**
     * 加载图片到ui
     * @param imageUri url或者picasso可以识别的uri
     */
    public abstract void setImage(String imageUri);

    /**
     * 结束拖拽，并且拖拽项经过动画回到他的位置上的时候做的ui改变
     */
    public abstract void startDrag();

    /**
     * 结束拖拽，并且拖拽项经过动画回到他的位置上的时候做的ui改变
     */
    public abstract void endDrag();

    /**
     * 是否加入收藏时ui表现,如果不使用收藏功能，将不生效
     * @param isFavorite
     */
    public abstract void setIsFavorite(boolean isFavorite) ;
}
