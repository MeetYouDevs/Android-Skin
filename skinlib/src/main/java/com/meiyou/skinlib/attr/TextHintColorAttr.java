package com.meiyou.skinlib.attr;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.TextView;

import com.meiyou.skinlib.AndroidSkin;

/**
 * Author: meetyou
 * Date: 17/11/10 10:05.
 */
public class TextHintColorAttr extends MutableAttr {
    public static final String RES_TYPE_NAME_HINT_COLOR = "hintTextColor";

    public TextHintColorAttr(String attrName, int attrValueRefId, String attrValueRefName, String typeName) {
        super(attrName, attrValueRefId, attrValueRefName, typeName);
        this.type = TYPE.TEXT_COLOR;
    }

    @Override
    public void apply(View view) {
        if (view == null) {
            return;
        }
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            if ((RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) || (RES_TYPE_NAME_HINT_COLOR.equals(attrValueTypeName))) {
                ColorStateList color =
                    AndroidSkin.getInstance()
                        .getSkinColorStateList(attrValueTypeName, attrValueRefName, attrValueRefId);
                tv.setHintTextColor(color);

            }
        }
    }
}
