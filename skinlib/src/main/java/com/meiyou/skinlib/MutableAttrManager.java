package com.meiyou.skinlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.annotation.NonNull;

import com.meiyou.skinlib.attr.ICustAttrApplyForColorListener;
import com.meiyou.skinlib.attr.ICustAttrApplyForDrawableListener;
import com.meiyou.skinlib.attr.MutableAttr;
import com.meiyou.skinlib.util.LogUtils;

/**
 * 自定义MutableAttr的管理类
 * Created by yangsq on 2017/11/21.
 */

public class MutableAttrManager {
    private static final String sTAG = "MutableAttrManager";
    private static MutableAttrManager mInstance;
    private List<String> mCustomAttrList;
    private Map<String, ICustAttrApplyForColorListener> mColorListenerMap;
    private Map<String, ICustAttrApplyForDrawableListener> mDrawablerListenerMap;

    public MutableAttrManager() {
        mCustomAttrList = new ArrayList<>();
        mColorListenerMap = new HashMap<>();
        mDrawablerListenerMap = new HashMap<>();
    }

    public static MutableAttrManager getInstance() {
        if (mInstance == null) {
            synchronized (MutableAttrManager.class) {
                if (mInstance == null)
                    mInstance = new MutableAttrManager();
            }
        }
        return mInstance;
    }

    /**
     * 增加一个自定义属性,并接受颜色改变的监听回调 
     * @param attrName
     * @param colorListener
     */
    public void addCustomAttr(@NonNull String attrName,
        @NonNull ICustAttrApplyForColorListener colorListener) {
        if (!mCustomAttrList.contains(attrName) && !mColorListenerMap.containsKey(attrName)) {
            mCustomAttrList.add(attrName);
            mColorListenerMap.put(attrName, colorListener);
        } else {
            LogUtils.d(sTAG, "add a customAttr:" + attrName + " for apply color which is already added");
        }
    }

    /**
     * 增加一个自定义属性,并接受drawable改变的监听回调 
     * @param attrName
     * @param drawableListener
     */
    public void addCustomAttr(@NonNull String attrName,
        @NonNull ICustAttrApplyForDrawableListener drawableListener) {
        if (!mCustomAttrList.contains(attrName) && !mDrawablerListenerMap.containsKey(attrName)) {
            mCustomAttrList.add(attrName);
            mDrawablerListenerMap.put(attrName, drawableListener);
        } else {
            LogUtils.d(sTAG, "add a customAttr:" + attrName + " for apply drawable which is already added");
        }
    }

    /**
     * 属性是否支持，要支持，需要先{@link #addCustomAttr(String, ICustAttrApplyForColorListener)} (String, Class)}或者{@link #addCustomAttr(String, ICustAttrApplyForDrawableListener)}
     * @param attr
     * @return
     */
    public boolean isAttrSupport(@NonNull String attr) {
        return mCustomAttrList.contains(attr);
    }

    /**
     *  获取属性对应的监听
     * @param attr
     * @return
     */
    public ICustAttrApplyForColorListener getApplyColorListener(@NonNull String attr) {
        ICustAttrApplyForColorListener custAttrApplyForColorListener = mColorListenerMap.get(attr);
        if (custAttrApplyForColorListener == null) {
            LogUtils.d(sTAG, "try to get color listener by attr:" + attr + ",but it do not add before");
        }
        return custAttrApplyForColorListener;
    }

    /**
     *  获取属性对应的监听
     * @param attr
     * @return
     */
    public ICustAttrApplyForDrawableListener getApplyDrawableListener(@NonNull String attr) {
        ICustAttrApplyForDrawableListener custAttrApplyForDrawableListener = mDrawablerListenerMap.get(attr);
        if (custAttrApplyForDrawableListener == null) {
            LogUtils.d(sTAG, "try to get drawable listener by attr:" + attr + ",but it do not add before");
        }
        return custAttrApplyForDrawableListener;
    }
}
