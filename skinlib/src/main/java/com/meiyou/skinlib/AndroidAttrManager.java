package com.meiyou.skinlib;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.meiyou.skinlib.attr.MutableAttr;
import com.meiyou.skinlib.attr.MutableAttrFactory;
import com.meiyou.skinlib.util.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yangsq on 2017/11/24.
 */

public class AndroidAttrManager {
    // 支持的android原生属性最后数组
    private int[] mAndroidAttrs;
    private List<AttrBean> mAndroidAttrBeanList;
    private static AndroidAttrManager mInstance;

    public static AndroidAttrManager getInstance() {
        if (mInstance == null) {
            synchronized (AndroidAttrManager.class) {
                if (mInstance == null)
                    mInstance = new AndroidAttrManager();
            }
        }
        return mInstance;
    }

    /**
     * 通过传入的属性，获取支持的android属性MutableAttr列表
     * @param context
     * @param attrs
     * @return
     */
    public List<MutableAttr> obtainMutableAttrList(Context context, AttributeSet attrs) {
        List<MutableAttr> viewAttrs = new ArrayList<>();
        if (mAndroidAttrs == null) {
            mAndroidAttrBeanList = new ArrayList<>();
            for (MutableAttr.TYPE type : MutableAttr.TYPE.values()) {
                int attrId = Resources.getSystem().getIdentifier(type.getRealName(), "attr", "android");
                if (attrId == 0) {
                    LogUtils.d("can not find attr:" + type.getRealName() + " resId");
                    continue;
                }
                AttrBean attrBean = new AttrBean();
                attrBean.attrId = attrId;
                attrBean.type = type;
                mAndroidAttrBeanList.add(attrBean);
            }
            // 通过resId排序，因为obtainStyledAttributes需要保证传入id的顺序
            Collections.sort(mAndroidAttrBeanList);
            int length = mAndroidAttrBeanList.size();
            mAndroidAttrs = new int[length];
            for (int i = 0; i < length; i++) {
                mAndroidAttrs[i] = mAndroidAttrBeanList.get(i).attrId;
            }
        }
        TypedArray a = context.obtainStyledAttributes(attrs, mAndroidAttrs);
        int N = a.getIndexCount();
        for (int j = 0; j < N; j++) {
            int index = a.getIndex(j);
            int resId = a.getResourceId(index,0);
            MutableAttr mutableAttr = MutableAttrFactory.create(context, mAndroidAttrBeanList.get(index).type, resId);
            if (mutableAttr != null) {
                viewAttrs.add(mutableAttr);
            }
        }
        a.recycle();
        return viewAttrs;
    }

    private static class AttrBean implements Comparable<AttrBean> {
        int attrId;
        MutableAttr.TYPE type;

        @Override
        public int compareTo(@NonNull AttrBean o) {
            if (attrId < o.attrId) {
                return -1;
            } else if (attrId > o.attrId) {
                return 1;
            }
            return 0;
        }
    }
}
