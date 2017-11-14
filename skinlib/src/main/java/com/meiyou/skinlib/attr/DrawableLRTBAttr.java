package com.meiyou.skinlib.attr;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.meiyou.skinlib.AndroidSkin;

/**
 * Author: meetyou
 * Date: 17/11/10 09:58.
 */

public class DrawableLRTBAttr extends MutableAttr {

    public DrawableLRTBAttr(String supportName, String attrName,
                            int attrValueRefId, String attrValueRefName, String typeName) {
        super(attrName, attrValueRefId, attrValueRefName, typeName);
        for (TYPE type : TYPE.values()) {
            if (type.getRealName().equals(supportName)) {
                this.type = type;
                break;
            }
        }
    }

    @Override
    public void apply(View view) {
        if (view == null) {
            return;
        }
        if (view instanceof TextView) {//button extends TextView
            TextView textView = (TextView) view;
            Drawable bg = AndroidSkin.getInstance().getSkinDrawable(
                    attrValueTypeName, attrValueRefName, attrValueRefId);
            Drawable[] bound = {null, null, null, null};
            switch (type) {
                case DRAWABLE_LEFT:
                    bound[0] = bg;
                    break;
                case DRAWABLE_TOP:
                    bound[1] = bg;
                    break;
                case DRAWABLE_RIGHT:
                    bound[2] = bg;
                    break;
                case DRAWABLE_BOTTOM:
                    bound[3] = bg;
                    break;
            }
            //textView.setCompoundDrawablePadding(0);
            textView.setCompoundDrawables(null, null, null, null);
            /// 这一步必须要做,否则不会显示.
            if (bound[0] != null)
                bound[0].setBounds(0, 0, bound[0].getIntrinsicWidth(), bound[0].getIntrinsicHeight());
            if (bound[1] != null)
                bound[1].setBounds(0, 0, bound[1].getIntrinsicWidth(), bound[1].getIntrinsicHeight());
            if (bound[2] != null)
                bound[2].setBounds(0, 0, bound[2].getIntrinsicWidth(), bound[2].getIntrinsicHeight());
            if (bound[3] != null)
                bound[3].setBounds(0, 0, bound[3].getIntrinsicWidth(), bound[3].getIntrinsicHeight());

            textView.setCompoundDrawables(bound[0], bound[1], bound[2], bound[3]);
        }

    }
}
