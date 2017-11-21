package com.meiyou.androidskin;

import android.view.View;

import com.meiyou.skinlib.AndroidSkin;
import com.meiyou.skinlib.attr.MutableAttr;
import com.meiyou.skinlib.util.LogUtils;

/**
 * Author: yangsq
 */

public class TestViewBorderAttr extends MutableAttr {
    public TestViewBorderAttr(String attrName, int attrValueRefId, String attrValueRefName, String typeName) {
        super(attrName, attrValueRefId, attrValueRefName, typeName);
    }

    @Override
    public void apply(View view) {
        if (view instanceof TestView) {
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)){
                int color = AndroidSkin.getInstance().
                        getSkinColor(attrValueTypeName, attrValueRefName, attrValueRefId);
                ((TestView) view).setBorderColor(color);
            }
        } else {
            LogUtils.d(TAG, "TestViewBorderAttr apply border not TestView! ");
        }

    }
}
