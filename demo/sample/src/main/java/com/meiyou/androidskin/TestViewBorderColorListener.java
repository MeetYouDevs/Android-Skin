package com.meiyou.androidskin;

import com.meiyou.skinlib.attr.ICustAttrApplyForColorListener;

/**
 * Author: yangsq
 */

public class TestViewBorderColorListener implements ICustAttrApplyForColorListener<TestView> {

    @Override
    public void applyColor(String attrName, TestView view, int color) {
        view.setBorderColor(color);
    }
}
