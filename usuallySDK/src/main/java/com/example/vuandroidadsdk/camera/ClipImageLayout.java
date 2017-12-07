package com.example.vuandroidadsdk.camera;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.vuandroidadsdk.R;


/**
 * http://blog.csdn.net/lmj623565791/article/details/39761281
 * @author zhy
 *
 */
public class ClipImageLayout extends RelativeLayout
{

	public ClipZoomImageView mZoomImageView;
	public ClipImageBorderView mClipImageView;

	/**
	 * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
	 */
	private int mHorizontalPadding ;
	private int mBoardColor;

	private static final int DEFAULT_PADDING = 20;
	private static final int DEFAULT_COLOR = Color.WHITE;
	public ClipImageLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		mZoomImageView = new ClipZoomImageView(context);
		mClipImageView = new ClipImageBorderView(context);

		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);

		this.addView(mZoomImageView, lp);
		this.addView(mClipImageView, lp);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClipImageLayout);
		mHorizontalPadding = a.getDimensionPixelSize(R.styleable.ClipImageLayout_horizontal_padding,DEFAULT_PADDING);
		mBoardColor = a.getColor(R.styleable.ClipImageLayout_board_color,DEFAULT_COLOR);

		a.recycle();
		// 计算padding的px
//		mHorizontalPadding = (int) TypedValue.applyDimension(
//				TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
//						.getDisplayMetrics());
		mZoomImageView.setHorizontalPadding(mHorizontalPadding);
		mClipImageView.setHorizontalPadding(mHorizontalPadding);
		mClipImageView.setmBorderColor(mBoardColor);

	}

	/**
	 * 对外公布设置边距的方法,单位为dp
	 * 
	 * @param mHorizontalPadding
	 */
	public void setHorizontalPadding(int mHorizontalPadding)
	{
		this.mHorizontalPadding = mHorizontalPadding;
	}

	/**
	 * 裁切图片
	 * 
	 * @return
	 */
	public Bitmap clip()
	{
		return mZoomImageView.clip();
	}

}
