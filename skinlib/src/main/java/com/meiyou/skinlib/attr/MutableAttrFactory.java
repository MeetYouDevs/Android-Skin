package com.meiyou.skinlib.attr;

import android.content.Context;
import android.support.annotation.NonNull;

import com.meiyou.skinlib.CustomAttrManager;

/**
 * Author: meetyou
 * Date: 17/11/10 10:03.
 */

public class MutableAttrFactory {
    private static final String sTAG = "MutableAttrFactory";

    public static MutableAttr create(String attrName, int attrValueRefId, String attrValueRefName, String typeName) {
        MutableAttr.TYPE type = genType(attrName);
        if (type == null) {
            return null;
        }
        return create(type, attrValueRefId, attrValueRefName, typeName);
    }

    public static MutableAttr create(Context context, @NonNull MutableAttr.TYPE type, int attrValueRefId) {
        String entryName = context.getResources().getResourceEntryName(attrValueRefId);
        // 防止被Theme.Light的颜色覆盖,导致换肤失败
        if (entryName != null
            && (entryName.equals("hint_foreground_light")
                || entryName.equals("bright_foreground_disabled_material_light") || entryName.contains("_foreground_") || entryName.contains("_material_"))) {
            return null;
        }
        String typeName = context.getResources().getResourceTypeName(attrValueRefId);
        return create(type, attrValueRefId, entryName, typeName);
    }

    /**
     * 自定义属性用
     * @param attrId
     * @param attrValueRefId
     * @return
     */
    public static MutableAttr create(Context context, int attrId, int attrValueRefId){
        String entryName = context.getResources().getResourceEntryName(attrValueRefId);
        // 防止被Theme.Light的颜色覆盖,导致换肤失败
        if (entryName != null
                && (entryName.equals("hint_foreground_light")
                || entryName.equals("bright_foreground_disabled_material_light") || entryName.contains("_foreground_") || entryName.contains("_material_"))) {
            return null;
        }
        String typeName = context.getResources().getResourceTypeName(attrValueRefId);
        // 自定义属性
        if (attrId != 0 && CustomAttrManager.getInstance().isAttrSupport(attrId)) {
            return new CustomAttr(null, attrValueRefId, entryName, typeName, attrId);
        }
        return null;
    }

    private static MutableAttr create(@NonNull MutableAttr.TYPE type, int attrValueRefId, String attrValueRefName,
                                      String typeName) {
        String attrName = type.getRealName();
        switch (type) {
            case BACKGROUND:
                return new BackgroundAttr(attrName, attrValueRefId, attrValueRefName, typeName);
            case TEXT_COLOR:
                return new TextColorAttr(attrName, attrValueRefId, attrValueRefName, typeName);
            case HINT_TEXT_COLOR:
                return new TextHintColorAttr(attrName, attrValueRefId, attrValueRefName, typeName);
            case SRC:
                return new ImageSrcAttr(attrName, attrValueRefId, attrValueRefName, typeName);
            case DRAWABLE_LEFT:
            case DRAWABLE_TOP:
            case DRAWABLE_RIGHT:
            case DRAWABLE_BOTTOM:
                return new DrawableLRTBAttr(type.getRealName(), attrName, attrValueRefId, attrValueRefName, typeName);
            case FONTFAMILY:
                return new FontAttr(attrName, attrValueRefId, attrValueRefName, typeName);
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
