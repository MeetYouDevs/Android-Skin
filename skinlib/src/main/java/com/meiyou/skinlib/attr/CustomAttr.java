package com.meiyou.skinlib.attr;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.meiyou.skinlib.AndroidSkin;
import com.meiyou.skinlib.MutableAttrManager;

/**
 * Created by yangsq on 2017/11/22.
 */

public class CustomAttr extends MutableAttr {
    public CustomAttr(String attrName, int attrValueRefId, String attrValueRefName, String typeName) {
        super(attrName, attrValueRefId, attrValueRefName, typeName);
    }

    @Override
    public void apply(View view) {
        if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
            // 颜色
            ICustAttrApplyForColorListener colorListener =
                MutableAttrManager.getInstance().getApplyColorListener(attrName);
            if (colorListener != null) {
                int color = AndroidSkin.getInstance().getSkinColor(attrValueTypeName, attrValueRefName, attrValueRefId);
                colorListener.applyColor(attrName, view, color);
            }

        } else if (RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)) {
            // drawable
            ICustAttrApplyForDrawableListener drawableListener =
                MutableAttrManager.getInstance().getApplyDrawableListener(attrName);
            if (drawableListener != null) {
                Drawable drawable =
                    AndroidSkin.getInstance().getSkinDrawable(attrValueTypeName, attrValueRefName, attrValueRefId);
                drawableListener.applyDrawable(attrName, view, drawable);
            }
        }
    }

}
