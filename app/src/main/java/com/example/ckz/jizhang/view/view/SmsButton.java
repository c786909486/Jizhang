package com.example.ckz.jizhang.view.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.Button;


/**
 * Created by CKZ on 2017/11/28.
 */

public class SmsButton extends Button{
    private static final int DEFAULT_TIME = 60;
    private static final String DEFAULT_TEXT = "获取验证码";
    public static final int CLICKABLE = 0x123;
    public static final int DISABLE = 0x124;
    private int current = CLICKABLE;
    private String text = DEFAULT_TEXT;
    private int time = DEFAULT_TIME;
    public SmsButton(Context context) {
        this(context,null);
    }

    public SmsButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SmsButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public int getCurrent() {
        return current;
    }

    public void start(){
        ValueAnimator animator = ValueAnimator.ofInt(time,0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                current = DISABLE;
                int s = (int) valueAnimator.getAnimatedValue();
                setText(s+"s后获取");
                postInvalidate();
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.play(animator);
        set.setDuration(time*1000);
        set.setInterpolator(new LinearInterpolator());
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                current = CLICKABLE;
                clearAnimation();
                setText("重新获取");
                postInvalidate();
            }
        });
    }
}
