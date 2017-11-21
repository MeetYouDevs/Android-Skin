package com.meiyou.skinlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.support.annotation.NonNull;

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
    private HashMap<String, Class<? extends MutableAttr>> mCustomMutableAttrClassMap;

    public MutableAttrManager() {
        mCustomAttrList = new ArrayList<>();
        mCustomMutableAttrClassMap = new HashMap<>();
    }

    static MutableAttrManager getInstance() {
        if (mInstance == null) {
            synchronized (MutableAttrManager.class) {
                if (mInstance == null)
                    mInstance = new MutableAttrManager();
            }
        }
        return mInstance;
    }

    /**
     * 增加一个自定义属性    
     * @param attrName
     * @param mutableAttrClass
     */
    public void addCustomAttr(@NonNull String attrName, @NonNull Class<? extends MutableAttr> mutableAttrClass) {
        if (!mCustomAttrList.contains(attrName) && !mCustomMutableAttrClassMap.containsKey(attrName)) {
            mCustomAttrList.add(attrName);
            mCustomMutableAttrClassMap.put(attrName, mutableAttrClass);
        } else {
            LogUtils.d(sTAG, "add a customAttr:" + attrName + " which is already added");
        }
    }

    /**
     * 属性是否支持，要支持，需要先{@link #addCustomAttr(String, Class)}
     * @param attr
     * @return
     */
    public boolean isAttrSupport(@NonNull String attr) {
        return mCustomAttrList.contains(attr);
    }

    /**
     *  获取属性对应的MutableAttr
     * @param attr
     * @return
     */
    public Class<? extends MutableAttr> getMutableAttrByAttr(@NonNull String attr) {
        Class<? extends MutableAttr> mutableAttr = mCustomMutableAttrClassMap.get(attr);
        if (mutableAttr == null) {
            LogUtils.d(sTAG, "try to get MutableAttr by attr:" + attr + ",but it do not add before");
        }
        return mutableAttr;
    }
}
