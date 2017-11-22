package com.meiyou.skinlib.attr;

import android.view.View;

import com.meiyou.skinlib.MutableAttrManager;

/**
 * Author: meetyou
 * Date: 17/11/10 09:57.
 */

public abstract class MutableAttr {
    protected static final String TAG = "MutableAttr";
    public static final String RES_TYPE_NAME_COLOR = "color";
    public static final String RES_TYPE_NAME_DRAWABLE = "drawable";

    public enum TYPE {
        BACKGROUND("background"), SRC("src"), HINT_TEXT_COLOR("hintTextColor"), TEXT_COLOR("textColor"), STYLE("style"), DRAWABLE_LEFT(
            "drawableLeft"), DRAWABLE_RIGHT("drawableRight"), DRAWABLE_TOP("drawableTop"), DRAWABLE_BOTTOM(
            "drawableBottom"), ;
        private String realName;

        private TYPE(String realName) {
            this.realName = realName;
        }

        public String getRealName() {
            return realName;
        }
    }

    public MutableAttr(String attrName, int attrValueRefId, String attrValueRefName, String typeName) {
        this.attrName = attrName;
        this.attrValueRefId = attrValueRefId;
        this.attrValueRefName = attrValueRefName;
        this.attrValueTypeName = typeName;
    }

    /**
     * name of the attr, ex: background or textSize or textColor
     */
    public String attrName;

    /**
     * id of the attr value refered to, normally is [2130745655]
     */
    public int attrValueRefId;

    /**
     * entry name of the value , such as [app_exit_btn_background]
     */
    public String attrValueRefName;

    /**
     * type of the value , such as color or drawable
     */
    public String attrValueTypeName;

    public TYPE type;

    abstract public void apply(View view);

    public static boolean support(String attrName, MutableAttrManager mutableAttrManager) {
        if (attrName == null)
            return false;
        for (MutableAttr.TYPE type : MutableAttr.TYPE.values()) {
            if (type != null && type.getRealName() != null && type.getRealName().equals(attrName)) {
                return true;
            }
        }
        // 判断自定义属性
        if (mutableAttrManager != null && mutableAttrManager.isAttrSupport(attrName)) {
            return true;
        }
        return false;
    }
}
