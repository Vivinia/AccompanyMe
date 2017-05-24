package com.example.weixu.helper;

import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by weixu on 2017/4/1.
 */

public class AnimationHelper {
    //动画类
    public Animation getRotateAnimation(int fromPosition, int toPosition) {
        //动画旋转（从多少角度开始，到多少角度，后边参数为旋转中心位置）
        RotateAnimation animation = new RotateAnimation(fromPosition, toPosition,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new DecelerateInterpolator());
        //animation.setFillAfter(true);  //动画旋转结束保持现在状态
        animation.setDuration(400); //旋转时间
        return animation;
    }

    public Animation getTranslateAnimation(int left, int top) {
        //动画位移
        TranslateAnimation animation = new TranslateAnimation(0, left,0 , top);
        animation.setInterpolator(new DecelerateInterpolator());
        //animation.setFillAfter(true);  //动画位移结束保持现在状态
        animation.setDuration(400); //位移时间
        return animation;
    }
}
