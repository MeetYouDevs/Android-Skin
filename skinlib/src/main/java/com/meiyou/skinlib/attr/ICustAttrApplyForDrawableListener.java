package com.meiyou.skinlib.attr;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by yangsq on 2017/11/22.
 */

public interface ICustAttrApplyForDrawableListener<T extends View> {

    void applyDrawable(int attrId, T view, Drawable drawable);

}
