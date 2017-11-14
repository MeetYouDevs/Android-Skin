package com.meiyou.skinlib.attr;

/**
 * Author: meetyou
 * Date: 17/11/10 10:03.
 */

public class MutableAttrFactory {
    public static MutableAttr create(String attrName, int attrValueRefId,
                                     String attrValueRefName, String typeName) {
        //LogUtils.d("MutableAttrFactory",attrName);
        MutableAttr.TYPE type = genType(attrName);
        if (type == null) {
            return null;
        }
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
                return new DrawableLRTBAttr(type.getRealName(), attrName, attrValueRefId, attrValueRefName, typeName);
        }
        return null;
    }

    public static MutableAttr.TYPE genType(String attrName) {
        for (MutableAttr.TYPE type : MutableAttr.TYPE.values()) {
            //if (StringUtils.equals(type.getRealName(), attrName)) {
            if (type.getRealName().equals(attrName)) {
                return type;
            }

        }
        return null;
    }
}
