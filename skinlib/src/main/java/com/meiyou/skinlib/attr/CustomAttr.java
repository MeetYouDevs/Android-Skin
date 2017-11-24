package com.meiyou.skinlib.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.meiyou.skinlib.AndroidSkin;
import com.meiyou.skinlib.CustomAttrManager;

/**
 * Created by yangsq on 2017/11/22.
 */

public class CustomAttr extends MutableAttr {
    /**
     * 自定义属性的attrId
     */
    private int attrId;

    public CustomAttr(String attrName, int attrValueRefId, String attrValueRefName, String typeName, int attrId) {
        super(attrName, attrValueRefId, attrValueRefName, typeName);
        this.attrId = attrId;
    }

    @Override
    public void apply(View view) {
        if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
            // 颜色
            ICustAttrApplyForColorListener colorListener =
                CustomAttrManager.getInstance().getApplyColorListener(attrId);
            if (colorListener != null) {
                ColorStateList color = AndroidSkin.getInstance().getSkinColorStateList(attrValueTypeName, attrValueRefName, attrValueRefId);
                colorListener.applyColor(attrId, view, color);
            }

        } else if (RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)) {
            // drawable
            ICustAttrApplyForDrawableListener drawableListener =
                CustomAttrManager.getInstance().getApplyDrawableListener(attrId);
            if (drawableListener != null) {
                Drawable drawable =
                    AndroidSkin.getInstance().getSkinDrawable(attrValueTypeName, attrValueRefName, attrValueRefId);
                drawableListener.applyDrawable(attrId, view, drawable);
            }
        }
    }

}
