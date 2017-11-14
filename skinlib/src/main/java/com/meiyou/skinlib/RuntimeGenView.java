package com.meiyou.skinlib;

import android.view.View;

import com.meiyou.skinlib.attr.MutableAttr;

import java.util.List;


public interface RuntimeGenView {

    public void addRuntimeView(View view, List<MutableAttr> mutableAttrList);

    public MutableAttr createMutableAttr(String attrName, int attrValueRefId);
}
