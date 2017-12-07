package com.example.ckz.jizhang.view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.example.ckz.jizhang.R;

/**
 * Created by CKZ on 2017/12/2.
 */

public class TypeView extends View{
    private static final int DEFAULT_BG = Color.RED;
    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;
    private static final int DEFAULT_TEXT_SIZE = 15;
    private Context context;
    private Paint bgPaint;
    private Paint textPaint;
    private int bgColor;
    private int textColor;
    private int textSize;
    private String text = "";
    private float width ;
    private float height;

    public TypeView(Context context) {
        this(context,null);
    }

    public TypeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TypeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.TypeView,defStyleAttr,0);
        bgColor = array.getColor(R.styleable.TypeView_bg_color,DEFAULT_BG);
        textColor = array.getColor(R.styleable.TypeView_text_color,DEFAULT_TEXT_COLOR);
        textSize = array.getDimensionPixelSize(R.styleable.TypeView_text_size,DEFAULT_TEXT_SIZE);
        text = array.getString(R.styleable.TypeView_text);
        array.recycle();
        initPaint();
    }

    public void setBgColor(@ColorRes int resColor){
        int color = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = context.getResources().getColor(resColor,null);
        }else {
            color = context.getResources().getColor(resColor);
        }
        bgPaint.setColor(color);
        invalidate();
    }

    public void setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
        textPaint.setColor(textColor);
        invalidate();
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        invalidate();
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    private void initPaint(){
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setDither(true);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(bgColor);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        height = getHeight();
        width = getWidth();
        canvas.drawCircle(width/2,height/2,Math.min(width,height)/2,bgPaint);

        canvas.drawText(text,width/2-textPaint.measureText(text)/2,height/2+getFontHeight()/2-3,textPaint);
    }
    private int getFontHeight()
    {

        Paint.FontMetrics fm = textPaint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent)  ;
    }
}
