package com.meiyou.androidskin;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by yangsq on 2017/11/21.
 */

public class TestView extends android.support.v7.widget.AppCompatTextView {
    private int mBorderColor;
    private int mBorderWidth;
    private Paint mPaint;
    private Drawable mDrawableLeftAndRight;

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TestView, defStyleAttr, 0);
        mBorderColor = a.getColor(R.styleable.TestView_tv_border_color, Color.RED);
        mBorderWidth = a.getDimensionPixelOffset(R.styleable.TestView_tv_border_width, 2);
        mDrawableLeftAndRight = a.getDrawable(R.styleable.TestView_tv_drawable_left_and_right);
        setDrawableLeftAndRight(mDrawableLeftAndRight);
        a.recycle();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
    }

    public void setBorderColor(int borderColor) {
        mBorderColor = borderColor;
        invalidate();
    }

    public void setBorderWidth(int borderWidth) {
        mBorderWidth = borderWidth;
        invalidate();
    }

    public void setDrawableLeftAndRight(Drawable drawableLeftAndRight) {
        mDrawableLeftAndRight = drawableLeftAndRight;
        if (mDrawableLeftAndRight != null) {
            mDrawableLeftAndRight.setBounds(0, 0, mDrawableLeftAndRight.getIntrinsicWidth(),
                mDrawableLeftAndRight.getIntrinsicHeight());
            setCompoundDrawables(mDrawableLeftAndRight, null, mDrawableLeftAndRight, null);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mBorderColor);
        mPaint.setStrokeWidth(mBorderWidth);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
    }
}
