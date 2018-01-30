package com.meiyou.skinlib.attr;

import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;

import com.meiyou.skinlib.AndroidSkin;

/**
 * Author: ice
 * Date: 1/30/18 09:01.
 */

public class FontAttr  extends MutableAttr {

    public FontAttr(String attrName, int attrValueRefId, String attrValueRefName, String typeName) {
        super(attrName, attrValueRefId, attrValueRefName, typeName);
        this.type = TYPE.FONTFAMILY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void apply(View view) {
        if (view==null){
            return;
        }
        if (RES_FONT.equals(attrValueTypeName)) {
            if(view instanceof TextView){
                Typeface typeface = AndroidSkin.getInstance().getAndroidSkinManager().getAndroidSkinResources().getFont(attrValueRefId);
                ((TextView)view).setTypeface(typeface);
            }
        }
        //LogUtils.d(TAG, "view apply ,id "+attrValueRefId+" " + attrValueTypeName+" "+attrValueRefName );
    }
}
