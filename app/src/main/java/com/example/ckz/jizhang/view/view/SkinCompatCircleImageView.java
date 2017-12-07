package com.example.ckz.jizhang.view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

import com.example.ckz.jizhang.R;

import de.hdodenhof.circleimageview.CircleImageView;
import skin.support.content.res.SkinCompatResources;
import skin.support.widget.SkinCompatHelper;
import skin.support.widget.SkinCompatImageHelper;
import skin.support.widget.SkinCompatSupportable;

import static skin.support.widget.SkinCompatHelper.INVALID_ID;

/**
 * Created by CKZ on 2017/11/28.
 */

public class SkinCompatCircleImageView extends CircleImageView implements SkinCompatSupportable {
    private SkinCompatImageHelper mImageHelper;
    private int mFillColorResId = INVALID_ID;
    private int mBorderColorResId = INVALID_ID;

    public SkinCompatCircleImageView(Context context) {
        this(context, null);
    }

    public SkinCompatCircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinCompatCircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mImageHelper = new SkinCompatImageHelper(this);
        mImageHelper.loadFromAttributes(attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs,  R.styleable.CircleImageView, defStyle, 0);
        mBorderColorResId = a.getResourceId(R.styleable.CircleImageView_civ_border_color, INVALID_ID);
        mFillColorResId = a.getResourceId(R.styleable.CircleImageView_civ_fill_color, INVALID_ID);
        a.recycle();
        applyBorderColorResource();
        applyFillColorResource();
    }

    private void applyFillColorResource() {
        mFillColorResId = SkinCompatHelper.checkResourceId(mFillColorResId);
        if (mFillColorResId != INVALID_ID) {
            int color = SkinCompatResources.getInstance().getColor(mFillColorResId);
            setFillColor(color);
        }
    }

    private void applyBorderColorResource() {
        mBorderColorResId = SkinCompatHelper.checkResourceId(mBorderColorResId);
        if (mBorderColorResId != INVALID_ID) {
            int color = SkinCompatResources.getInstance().getColor(mBorderColorResId);
            setBorderColor(color);
        }
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        if (mImageHelper != null) {
            mImageHelper.setImageResource(resId);
        }
    }

    @Override
    public void setBorderColorResource(@ColorRes int borderColorRes) {
        super.setBorderColorResource(borderColorRes);
        mBorderColorResId = borderColorRes;
        applySkin();
    }

    @Override
    public void setFillColorResource(@ColorRes int fillColorRes) {
        super.setFillColorResource(fillColorRes);
        mFillColorResId = fillColorRes;
        applySkin();
    }

    @Override
    public void applySkin() {
        if (mImageHelper != null) {
            mImageHelper.applySkin();
        }
        applyBorderColorResource();
        applyFillColorResource();
    }

}