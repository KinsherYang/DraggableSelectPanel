package com.meitu.makeuppicker.util;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimationUtil {

    /**
     * 从控件所在位置移动到控件的底部
     * @param durationMillis 动画时长
     * @return
     */
    public static TranslateAnimation moveToViewBottom(long durationMillis) {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(durationMillis);
        return mHiddenAction;
    }

    /**
     * 从控件的底部移动到控件所在位置
     * @param durationMillis 动画时长
     * @return
     */
    public static TranslateAnimation moveToViewLocation(long durationMillis) {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(durationMillis);
        return mHiddenAction;
    }
}
