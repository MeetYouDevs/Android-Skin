package com.meiyou.androidskin;

import android.content.res.ColorStateList;

import com.meiyou.skinlib.attr.ICustAttrApplyForColorListener;

/**
 * Author: yangsq
 */

public class TestViewBorderColorListener implements ICustAttrApplyForColorListener<TestView> {

    @Override
    public void applyColor(int attrId, TestView view, ColorStateList color) {
        view.setBorderColor(color.getDefaultColor());
    }
}
