package com.meiyou.skinlib.attr;

import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: meetyou
 * Date: 17/11/10 10:03.
 */
public class MutableAttrHolder {

    public WeakReference<View> viewRef;
    public List<MutableAttr> mutableAttrList = new ArrayList<>();

    public MutableAttrHolder(View view, List<MutableAttr> list) {
        viewRef = new WeakReference<>(view);
        this.mutableAttrList = list;
    }
}
