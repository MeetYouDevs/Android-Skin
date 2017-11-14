package com.meiyou.skinlib.attr;

import android.view.View;
import android.widget.TextView;

import com.meiyou.skinlib.AndroidSkin;


/**
 * Author: meetyou
 * Date: 17/11/10 10:05.
 */
public class TextColorAttr extends MutableAttr {
    public static final String RES_TYPE_NAME_HINT_COLOR = "hintTextColor";

    public TextColorAttr(String attrName, int attrValueRefId, String attrValueRefName, String typeName) {
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
            if ((RES_TYPE_NAME_COLOR.equals(attrValueTypeName))
                    || (RES_TYPE_NAME_HINT_COLOR.equals(attrValueTypeName))) {
                tv.setTextColor(AndroidSkin.getInstance().getSkinColorStateList(
                        attrValueTypeName, attrValueRefName, attrValueRefId));

            }
        }
    }
}
