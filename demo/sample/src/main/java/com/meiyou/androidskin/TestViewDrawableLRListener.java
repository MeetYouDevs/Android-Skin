package com.meiyou.androidskin;

import android.graphics.drawable.Drawable;

import com.meiyou.skinlib.attr.ICustAttrApplyForDrawableListener;

/**
 * Author: yangsq
 */

public class TestViewDrawableLRListener implements ICustAttrApplyForDrawableListener<TestView> {

    @Override
    public void applyDrawable(int attrId, TestView view, Drawable drawable) {
        view.setDrawableLeftAndRight(drawable);
    }
}
