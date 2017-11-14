package com.meiyou.skinlib.attr;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.meiyou.skinlib.AndroidSkin;
import com.meiyou.skinlib.util.LogUtils;

/**
 * Author: meetyou
 * Date: 17/11/10 10:01.
 */

public class ImageSrcAttr extends MutableAttr {
    public ImageSrcAttr(String attrName, int attrValueRefId, String attrValueRefName, String typeName) {
        super(attrName, attrValueRefId, attrValueRefName, typeName);
        this.type = TYPE.SRC;
    }

    @Override
    public void apply(View view) {
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            if (RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)) {
                Drawable bg = AndroidSkin.getInstance().getSkinDrawable(
                        attrValueTypeName, attrValueRefName, attrValueRefId);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    imageView.setImageDrawable(bg);
                } else {
                    imageView.setImageDrawable(bg);
                }
            }else if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)){
                int color = AndroidSkin.getInstance().
                        getSkinColor(attrValueTypeName, attrValueRefName, attrValueRefId);
                imageView.setImageDrawable(new ColorDrawable(color));
            }
        }else {
            LogUtils.d(TAG,"ImageSrcAttr apply src not ImageView! ");
        }

    }
}
