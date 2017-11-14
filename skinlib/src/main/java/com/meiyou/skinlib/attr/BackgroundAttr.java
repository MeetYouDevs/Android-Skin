package com.meiyou.skinlib.attr;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.meiyou.skinlib.AndroidSkin;

/**
 * Author: meetyou
 * Date: 17/11/10 09:56.
 */

public class BackgroundAttr extends MutableAttr {

    public BackgroundAttr(String attrName, int attrValueRefId, String attrValueRefName, String typeName) {
        super(attrName, attrValueRefId, attrValueRefName, typeName);
        this.type = TYPE.BACKGROUND;
    }

    @Override
    public void apply(View view) {
        if (view==null){
            return;
        }
        if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
            view.setBackgroundColor(AndroidSkin.getInstance().getSkinColor(
                    attrValueTypeName, attrValueRefName, attrValueRefId));
        } else if (RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)) {
            Drawable bg = AndroidSkin.getInstance().getSkinDrawable(
                    attrValueTypeName, attrValueRefName, attrValueRefId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(bg);
            } else {
                view.setBackgroundDrawable(bg);
            }
        }
        //LogUtils.d(TAG, "view apply ,id "+attrValueRefId+" " + attrValueTypeName+" "+attrValueRefName );
    }
}
