package com.yangsq.draggableselectpanel.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yangsq.draggableselectpanel.R;
import com.yangsq.draggableselectpanel.adapter.BGViewHolder;
import com.yangsq.draggableselectpanel.util.BitmapUtils;

/**
 * 包含可移动背景的RecyclerView
 * 采用两种形式结合表现背景图片
 * 1：当拖动的项是已选中的项，采用drawBitmap形式，用{@link #moveBgToLocation(RectF, boolean, long)}的形式控制动画；
 * 2：当拖动的项不是已选中项，采用在viewHolder里添加一个view的形式表示背景图片，这样当拖动的项改变到了已选中项的
 * 位置，背景图片可以跟着已选中项移动。
 *
 * @author ysq 2016/9/26.
 */
public class BGAnimRecyclerView extends RecyclerView {
    private static final String TAG = "BGAnimRecyclerView";
    /**
     * 移动背景框动画的默认时长
     */
    public static final int DEFAULT_ANIMATOR_DURATION = 250;
    private static final int DEFAULT_BG_RESOURCE = R.drawable.dgsp_selected_bg;
    /**
     * 在{@link #mAnimatorDuration}毫秒的时间里有多少帧动画
     */
    private int ANIMATOR_FRAME = 50;
    private Bitmap mBGBitmap;
    private RectF mBGLocation;
    private RectF mTempRect;
    private float mBGHeight;
    private float mBGWidth;
    private boolean isInited;
    private boolean mIsDrawBitmap = true;
    private ValueAnimator mAnimator;
    /**
     * 是否正在动画中，是的话则不能立即设置背景位置
     */
    private boolean mIsMoving;
    /**
     * 当前选中的index
     */
    private int mCurrentSelectedIndex = -1;
    /**
     * 背景图片的view形式，当{@link #addBGViewToItem(ViewHolder)}后，这个view会出现在当前选中的viewholder里
     */
    private ImageView mBGView;
    /**
     * 当前选中的ViewHolder
     */
    private ViewHolder mCurrentSelectedViewHolder;
    /**
     * 移动背景框动画的时长
     */
    private int mAnimatorDuration;

    public BGAnimRecyclerView(Context context) {
        this(context, null);
    }

    public BGAnimRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mAnimatorDuration = DEFAULT_ANIMATOR_DURATION;
        mBGLocation = null;
        mTempRect = new RectF();
        mAnimator = ValueAnimator.ofInt(0, ANIMATOR_FRAME);
        mAnimator.setDuration(mAnimatorDuration);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsMoving = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsMoving = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mIsMoving = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mBGView = new ImageView(getContext());
    }

    /**
     * 点亮指定item的背景
     *
     * @param index  位置
     * @param isAnim 是否需要动画
     */
    public void selectItem(int index, boolean isAnim) {
        selectItem(index, isAnim, -1, true);
    }

    /**
     * 点亮指定item的背景
     *
     * @param index                     位置
     * @param isAnim                    是否需要动画
     * @param duration                  动画时间，小于0将取{@link #mAnimatorDuration}
     * @param isChangeCurrentViewHolder 是否要重新计算mCurrentSelectedViewHolder,因为 adapter.notifyItemMoved
     *                                  会改变viewHolder的顺序，所有当调用 mAdapter.notifyItemMoved的时候就不用
     *                                  是否要重新计算mCurrentSelectedViewHolder
     */
    public void selectItem(int index, boolean isAnim, long duration, boolean isChangeCurrentViewHolder) {
        if (index >= 0) {
            mCurrentSelectedIndex = index;
            if (isChangeCurrentViewHolder) {
                mCurrentSelectedViewHolder = setBGLocation(index);
            } else {
                setBGLocation(index);
            }
            moveBgToLocation(mTempRect, isAnim, duration);
        }
    }

    /**
     * 根据位置生成背景图片的位置,设置到{@link #mTempRect}里
     *
     * @param index 在adapter里的位置
     * @return 当前设定的位置对应的ViewHolder, 当位置不可见时返回null
     */
    public ViewHolder setBGLocation(int index) {
        // int firstVisiblePosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        // 当某次滚动没有释放时，getChildCount的值可能大于可见的子view，所以findFirstVisibleItemPosition不准
        View firstChild = getChildAt(0);
        int firstVisiblePosition = getChildViewHolder(firstChild).getAdapterPosition();
        index = index - firstVisiblePosition;
        View childView = getChildAt(index);
        ViewHolder indexViewHolder = null;//当前index对应的viewHolder
        if (childView != null) {
            // 找到背景图片的中心
            float middleX, middleY;
            ViewHolder viewHolder = getChildViewHolder(childView);
            indexViewHolder = viewHolder;
            View bgBelongView;
            float parentDx = 0, parentDy = 0;
            if (viewHolder != null && viewHolder instanceof BGViewHolder) {
                bgBelongView = ((BGViewHolder) viewHolder).getBGBelongView();
                // getLeft和getTop都是获得相对父控件坐标，所以需要不断找到父控件，累计计算偏移，
                // 直到计算到childView，最后的left和right加上相对应的偏移量才是最后的坐标
                View parentView = (View) bgBelongView.getParent();
                while (parentView != childView) {
                    parentDx += parentView.getLeft();
                    parentDy += parentView.getTop();
                    parentView = (View) parentView.getParent();
                }
            } else {
                bgBelongView = childView;
            }
            middleX = (bgBelongView.getLeft() + bgBelongView.getRight()) / 2f + childView.getLeft() + parentDx;
            middleY = (bgBelongView.getTop() + bgBelongView.getBottom()) / 2f + childView.getTop() + parentDy;
            float left = middleX - mBGWidth / 2f;
            float right = middleX + mBGWidth / 2f;
            float top = middleY - mBGHeight / 2f;
            float bottom = middleY + mBGHeight / 2f;
            mTempRect.set(left, top, right, bottom);
        } else {
            // 选中的不可见
            if (index < 0) {
                // 背景框在前面(左边、上面)
                if (((LinearLayoutManager) getLayoutManager()).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    // 水平
                    mTempRect.set(0 - mBGWidth, 0, 0, mBGHeight);
                } else {
                    mTempRect.set(0, 0 - mBGHeight, mBGWidth, 0);
                }
            } else if (index >= getChildCount()) {
                // 背景框在后面（右边、下面);
                if (((LinearLayoutManager) getLayoutManager()).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    // 水平
                    mTempRect.set(getWidth(), 0, getWidth() + mBGWidth, mBGHeight);
                } else {
                    mTempRect.set(0, getHeight(), mBGWidth, getHeight() + mBGHeight);
                }
            }

        }
        return indexViewHolder;
    }

    /**
     * 把背景移动到指定的位置
     *
     * @param location 指定位置
     * @param isAnim   是否需要动画
     */
    private void moveBgToLocation(final RectF location, boolean isAnim, long duration) {
        duration = duration > 0 ? duration : mAnimatorDuration;
        if (!mIsMoving) {
            if (mBGLocation == null || !isAnim) {
                // 没有初始位置不能做动画
                if (mBGLocation == null)
                    mBGLocation = new RectF();
                mBGLocation.set(location);
                invalidate();
            } else {
                // 需要动画
                if (!location.equals(mBGLocation)) {
                    mAnimator.setDuration(duration);
                    mAnimator.removeAllUpdateListeners();
                    mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float dx = location.left - mBGLocation.left;
                            float dy = location.top - mBGLocation.top;
                            float onceDx = (Integer) animation.getAnimatedValue() / (float) ANIMATOR_FRAME * dx;
                            float onceDy = (Integer) animation.getAnimatedValue() / (float) ANIMATOR_FRAME * dy;
                            mBGLocation.set(mBGLocation.left + onceDx, mBGLocation.top + onceDy,
                                    mBGLocation.right + onceDx, mBGLocation.bottom + onceDy);
                            invalidate();
                        }
                    });
                    mAnimator.start();
                }
            }
        }
    }

    @Override
    public void onScrolled(int dx, int dy) {
        if (dx != 0 || dy != 0) {
            selectItem(mCurrentSelectedIndex, false);
        }
        super.onScrolled(dx, dy);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInited) {
            // 初始化
            isInited = true;
            View childView = getChildAt(0);
            if (childView != null) {
                if (mBGHeight <= 0 && mBGWidth <= 0) {
                    mBGHeight = childView.getHeight();
                    mBGWidth = childView.getWidth();
                }
                Drawable drawableIc = ContextCompat.getDrawable(getContext(), DEFAULT_BG_RESOURCE);
                int size = (int) Math.min(mBGHeight, mBGWidth);
                mBGHeight = size;
                mBGWidth = size;
                mBGBitmap = BitmapUtils.drawableToBitmap(drawableIc, size, size);
                mBGView.setLayoutParams(new RelativeLayout.LayoutParams((int) mBGWidth, (int) mBGHeight));
                mBGView.setImageBitmap(mBGBitmap);
            }
        }
        if (mBGBitmap != null && !mBGBitmap.isRecycled() && mBGLocation != null && mIsDrawBitmap) {
            canvas.drawBitmap(mBGBitmap, null, mBGLocation, null);
        }
    }

    /**
     * 设置背景移动动画的时长
     *
     * @param animatorDuration
     */
    public void setAnimatorDuration(int animatorDuration) {
        mAnimatorDuration = animatorDuration;
        if (mAnimator != null) {
            mAnimator.setDuration(mAnimatorDuration);
        }
    }

    /**
     * 设置背景图片
     *
     * @param bg
     */
    public void setBGBitmap(Bitmap bg) {
        mBGBitmap = bg;
        if (mBGView != null)
            mBGView.setImageBitmap(mBGBitmap);
    }

    /**
     * 设置背景图的尺寸
     *
     * @param width
     * @param height
     */
    public void setBGSize(float width, float height) {
        mBGWidth = width;
        mBGHeight = height;
    }

    /**
     * 清除选中背景
     */
    public void clearSelect() {
        mCurrentSelectedIndex = -1;
        mBGLocation = null;
        mCurrentSelectedViewHolder = null;
        invalidate();
    }

    /**
     * 显示背景图片为view形式
     *
     * @param viewHolder 当前操作的viewHolder
     */
    private void addBGViewToItem(ViewHolder viewHolder) {
        if (viewHolder != null && viewHolder instanceof BGViewHolder) {
            View bgBelongView;
            float parentDx = 0, parentDy = 0;
            bgBelongView = ((BGViewHolder) viewHolder).getBGBelongView();
            RelativeLayout rootView = ((BGViewHolder) viewHolder).getRootView();
            // getLeft和getTop都是获得相对父控件坐标，所以需要不断找到父控件，累计计算偏移，
            // 直到计算到childView，最后的left和right加上相对应的偏移量才是最后的坐标
            View parentView = (View) bgBelongView.getParent();
            while (parentView != rootView) {
                parentDx += parentView.getLeft();
                parentDy += parentView.getTop();
                parentView = (View) parentView.getParent();
            }
            float middleX = (bgBelongView.getLeft() + bgBelongView.getRight()) / 2f + parentDx;
            float middleY = (bgBelongView.getTop() + bgBelongView.getBottom()) / 2f + parentDy;
            float marginTop = middleY - mBGHeight / 2f;
            float marginLeft = middleX - mBGWidth / 2f;
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mBGView.getLayoutParams();
            lp.leftMargin = (int) marginLeft;
            lp.topMargin = (int) marginTop;
            mBGView.setLayoutParams(lp);
            if (mBGView.getParent() != null) {
                Log.e(TAG, "mBGView is not removed from parent");
                return;
            }
            if (rootView != null)
                rootView.addView(mBGView);
        }
    }

    /**
     * 移除viewHolder里作为背景图片的view
     *
     * @param viewHolder 当前操作的viewHolder
     */
    private void removeBGViewFromItem(ViewHolder viewHolder) {
        if (viewHolder != null && viewHolder instanceof BGViewHolder) {
            RelativeLayout rootView = ((BGViewHolder) viewHolder).getRootView();
            if (mBGView.getParent() != null) {
                rootView.removeView(mBGView);
            } else {
                Log.e(TAG, "mBGView is not added to parent");
            }
        }
    }

    /**
     * 改变位置完后，刷新当前选中项的相关值
     * @param oldPosition 改变前位置
     * @param newPosition 改变后位置
     */
    public void moveItemRefreshPosition(int oldPosition, int newPosition) {
        if (mCurrentSelectedIndex >= 0) {
            if ((oldPosition < mCurrentSelectedIndex && newPosition < mCurrentSelectedIndex) ||
                    (oldPosition > mCurrentSelectedIndex && newPosition > mCurrentSelectedIndex)) {
                return;
            }
            if (newPosition > mCurrentSelectedIndex || (newPosition == mCurrentSelectedIndex && oldPosition < mCurrentSelectedIndex)) {
                mCurrentSelectedIndex--;
            } else if (newPosition < mCurrentSelectedIndex || (newPosition == mCurrentSelectedIndex && oldPosition > mCurrentSelectedIndex)) {
                mCurrentSelectedIndex++;
            }
        }
    }

    /**
     * 当前选中的viewHolder和拖动的viewHolder不是同一个的时候
     * 背景图片表示为View,否则不变（表示为drawBitmap）
     *
     * @param dragViewHolder 当前拖拽的ViewHolder
     */
    public void showBgViewIfNecessary(ViewHolder dragViewHolder) {
        if (dragViewHolder != mCurrentSelectedViewHolder && mIsDrawBitmap) {
            addBGViewToItem(mCurrentSelectedViewHolder);
            //隐藏drawBitmap
            mIsDrawBitmap = false;
        }
    }

    /**
     * 把背景图片恢复成drawBitmap模式
     */
    public void resetBgView() {
        setBGLocation(mCurrentSelectedIndex);
        if (!mIsDrawBitmap) {
            removeBGViewFromItem(mCurrentSelectedViewHolder);
            //显示drawBitmap
            if (mBGLocation == null) {
                mBGLocation = new RectF();
            }
            mBGLocation.set(mTempRect);
            mIsDrawBitmap = true;
        }
    }

    public void recycle() {
        if (mBGBitmap != null) {
            if (!mBGBitmap.isRecycled()) {
                mBGBitmap.recycle();
            }
            mBGBitmap = null;
        }
        if (mAnimator != null) {
            if (mAnimator.isRunning()) {
                mAnimator.end();
            }
            mAnimator = null;
        }
    }
}
