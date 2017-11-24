package com.meiyou.skinlib.attr;

import android.content.res.ColorStateList;
import android.view.View;

/**
 * Created by yangsq on 2017/11/22.
 */

public interface ICustAttrApplyForColorListener<T extends View> {

    void applyColor(int attrId, T view, ColorStateList color);

}
