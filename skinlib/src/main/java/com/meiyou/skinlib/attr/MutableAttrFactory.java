package com.meiyou.skinlib.attr;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.meiyou.skinlib.MutableAttrManager;

/**
 * Author: meetyou
 * Date: 17/11/10 10:03.
 */

public class MutableAttrFactory {
    public static MutableAttr create(String attrName, int attrValueRefId, String attrValueRefName, String typeName,
                                     MutableAttrManager mutableAttrManager) {
        // LogUtils.d("MutableAttrFactory",attrName);
        MutableAttr.TYPE type = genType(attrName);
        if (type != null) {
            switch (type) {
                case BACKGROUND:
                    return new BackgroundAttr(attrName, attrValueRefId, attrValueRefName, typeName);
                case TEXT_COLOR:
                case HINT_TEXT_COLOR:
                    return new TextColorAttr(attrName, attrValueRefId, attrValueRefName, typeName);
                case SRC:
                    return new ImageSrcAttr(attrName, attrValueRefId, attrValueRefName, typeName);
                case DRAWABLE_LEFT:
                case DRAWABLE_TOP:
                case DRAWABLE_RIGHT:
                case DRAWABLE_BOTTOM:
                    return new DrawableLRTBAttr(type.getRealName(), attrName, attrValueRefId, attrValueRefName,
                        typeName);
            }
        }
        if (mutableAttrManager != null) {
            Class<? extends MutableAttr> mutableAttrClass = mutableAttrManager.getMutableAttrByAttr(attrName);
            try {
                Constructor<? extends MutableAttr> constructor =
                    mutableAttrClass.getConstructor(String.class, int.class, String.class, String.class);
                return constructor.newInstance(attrName, attrValueRefId, attrValueRefName, typeName);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static MutableAttr.TYPE genType(String attrName) {
        for (MutableAttr.TYPE type : MutableAttr.TYPE.values()) {
            // if (StringUtils.equals(type.getRealName(), attrName)) {
            if (type.getRealName().equals(attrName)) {
                return type;
            }

        }
        return null;
    }
}
