package com.example.ckz.jizhang.view.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by CKZ on 2017/10/23.
 */

public class SkipView extends View{
    private static final int DEFAULT_BOARD_COLOR = Color.parseColor("#FFFFFF");
    private static final int DEFAULT_TIME = 3;
    private static final int DEFAULT_CIRCLE_COLOR = Color.parseColor("#40000000");
    private Paint boardPaint;
    private Paint circlePaint;
    private Paint textPaint;
    private int boardColor = DEFAULT_BOARD_COLOR;

    private float width;
    private float height;
    private float cicleWidth;

    private float startAngle = -90;//开始角度
    private float mmSweepAngleStart = 0f;//起点
    private float mmSweepAngleEnd = 360f;//终点
    private float mSweepAngle;//扫过的角度

    private int mLoadingTime = DEFAULT_TIME;

    private String showTime = "跳过";

    private boolean intented = false;

    private void setText(String text){
        showTime = text;
        invalidate();
    }

    public SkipView(Context context) {
        this(context,null);
    }

    public SkipView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SkipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.onFinish();
                    intented = true;
                }
            }
        });
    }

    private void initCirclePaint(){
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(DEFAULT_CIRCLE_COLOR);
        circlePaint.setStyle(Paint.Style.FILL);
    }

    private void initBoardPaint(){
        resetParams();
        boardPaint = new Paint();
        boardPaint.setAntiAlias(true);
        boardPaint.setStyle(Paint.Style.STROKE);
        boardPaint.setColor(boardColor);
        boardPaint.setStrokeWidth(cicleWidth);
    }

    private float textSize;
    private void initTextPaint(){
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(boardColor);
        textSize = width/4;
        textPaint.setTextSize(textSize);
    }
    private void resetParams(){
        width = getWidth();
        height = getHeight();
        cicleWidth = (float) (0.05*width);
    }

    //    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (width > height) {
            setMeasuredDimension(height, height);
        } else {
            setMeasuredDimension(width, width);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initBoardPaint();
        initCirclePaint();
        initTextPaint();
        canvas.drawCircle(width/2,height/2,Math.min(width/2,height/2),circlePaint);
        RectF rectF = new RectF(cicleWidth,cicleWidth,width-cicleWidth,height-cicleWidth);
        canvas.drawArc(rectF,startAngle,mSweepAngle,false,boardPaint);
        canvas.drawText(showTime, (float) (width/2-textPaint.measureText(showTime)/2), (float) (height/2+getFontHeight()/2-1),textPaint);
    }

    private int getFontHeight()
    {

        Paint.FontMetrics fm = textPaint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent)  ;
    }

    public void start(final OnSkipListener listener){
        this.listener = listener;
        ValueAnimator animator = ValueAnimator.ofFloat(mmSweepAngleStart, mmSweepAngleEnd);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSweepAngle = (float) valueAnimator.getAnimatedValue();
                //获取到需要绘制的角度，重新绘制
                invalidate();
            }
        });
        //这里是时间获取和赋值
        ValueAnimator animator1 = ValueAnimator.ofInt(mLoadingTime, 0);
        animator1.setInterpolator(new LinearInterpolator());
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int time = (int) valueAnimator.getAnimatedValue();
//                setText(time+"s");
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator, animator1);
        set.setDuration(mLoadingTime * 1000);
        set.setInterpolator(new LinearInterpolator());
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                clearAnimation();
                postInvalidate();
                if (listener!=null && !intented){
                    listener.onFinish();
                }
            }
        });

    }
    private OnSkipListener listener;


    public interface OnSkipListener{
        void onClick(View view);
        void onFinish();
    }
}
