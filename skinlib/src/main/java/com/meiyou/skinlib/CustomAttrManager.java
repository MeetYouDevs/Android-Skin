package com.meiyou.skinlib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.meiyou.skinlib.attr.ICustAttrApplyForColorListener;
import com.meiyou.skinlib.attr.ICustAttrApplyForDrawableListener;
import com.meiyou.skinlib.attr.MutableAttr;
import com.meiyou.skinlib.attr.MutableAttrFactory;
import com.meiyou.skinlib.util.LogUtils;

/**
 * 属性的管理类
 * Created by yangsq on 2017/11/21.
 */

public class CustomAttrManager {
    private static final String sTAG = "MutableAttrManager";
    private static CustomAttrManager mInstance;
    private List<Integer> mCustomAttrList;
    private Map<Integer, ICustAttrApplyForColorListener> mColorListenerMap;
    private Map<Integer, ICustAttrApplyForDrawableListener> mDrawablerListenerMap;

    public CustomAttrManager() {
        mCustomAttrList = new ArrayList<>();
        mColorListenerMap = new HashMap<>();
        mDrawablerListenerMap = new HashMap<>();
    }

    public static CustomAttrManager getInstance() {
        if (mInstance == null) {
            synchronized (CustomAttrManager.class) {
                if (mInstance == null)
                    mInstance = new CustomAttrManager();
            }
        }
        return mInstance;
    }

    /**
     * 增加一个自定义属性,并接受颜色改变的监听回调
     * @param attrId 自定义属性id，如R.attr.tv_border_color
     * @param colorListener
     */
    public void addCustomAttr(int attrId, @NonNull ICustAttrApplyForColorListener colorListener) {
        if (attrId == 0) {
            return;
        }
        if (!mCustomAttrList.contains(attrId) && !mColorListenerMap.containsKey(attrId)) {
            mCustomAttrList.add(attrId);
            mColorListenerMap.put(attrId, colorListener);
        } else {
            LogUtils.d(sTAG, "add a customAttr:" + attrId + " for apply color which is already added");
        }
    }

    /**
     * 增加一个自定义属性,并接受drawable改变的监听回调
     * @param attrId 自定义属性id，如R.attr.tv_border_color
     * @param drawableListener
     */
    public void addCustomAttr(int attrId, @NonNull ICustAttrApplyForDrawableListener drawableListener) {
        if (attrId == 0) {
            return;
        }
        if (!mCustomAttrList.contains(attrId) && !mDrawablerListenerMap.containsKey(attrId)) {
            mCustomAttrList.add(attrId);
            mDrawablerListenerMap.put(attrId, drawableListener);
        } else {
            LogUtils.d(sTAG, "add a customAttr:" + attrId + " for apply drawable which is already added");
        }
    }

    /**
     * 属性是否支持，要支持，需要先{@link #addCustomAttr}
     * @param attrId
     * @return
     */
    public boolean isAttrSupport(@NonNull int attrId) {
        return mCustomAttrList.contains(attrId);
    }

    /**
     *  获取属性对应的监听
     * @param attrId
     * @return
     */
    public ICustAttrApplyForColorListener getApplyColorListener(int attrId) {
        ICustAttrApplyForColorListener custAttrApplyForColorListener = mColorListenerMap.get(attrId);
        if (custAttrApplyForColorListener == null) {
            LogUtils.d(sTAG, "try to get color listener by attr:" + attrId + ",but it do not add before");
        }
        return custAttrApplyForColorListener;
    }

    /**
     *  获取属性对应的监听
     * @param attrId
     * @return
     */
    public ICustAttrApplyForDrawableListener getApplyDrawableListener(int attrId) {
        ICustAttrApplyForDrawableListener custAttrApplyForDrawableListener = mDrawablerListenerMap.get(attrId);
        if (custAttrApplyForDrawableListener == null) {
            LogUtils.d(sTAG, "try to get drawable listener by attr:" + attrId + ",but it do not add before");
        }
        return custAttrApplyForDrawableListener;
    }

    /**
     * 通过传入的属性，获取支持的自定义属性MutableAttr列表
     * @param context
     * @param attrs
     * @return
     */
    public List<MutableAttr> obtainMutableAttrList(Context context, AttributeSet attrs, List<MutableAttr> viewAttrs) {
        if (mCustomAttrList == null || mCustomAttrList.isEmpty()) {
            return new ArrayList<>();
        }
        if (viewAttrs == null) {
            viewAttrs = new ArrayList<>();
        }
        Collections.sort(mCustomAttrList);
        int size = mCustomAttrList.size();
        int[] customAttrs = new int[size];
        for (int i = 0; i < size; i++) {
            customAttrs[i] = mCustomAttrList.get(i);
        }
        TypedArray a = context.obtainStyledAttributes(attrs, customAttrs);
        int N = a.getIndexCount();
        for (int j = 0; j < N; j++) {
            int index = a.getIndex(j);
            int resId = a.getResourceId(index, 0);
            MutableAttr mutableAttr = MutableAttrFactory.create(context, customAttrs[j], resId);
            if (mutableAttr != null) {
                viewAttrs.add(mutableAttr);
            }
        }
        return viewAttrs;
    }
}
