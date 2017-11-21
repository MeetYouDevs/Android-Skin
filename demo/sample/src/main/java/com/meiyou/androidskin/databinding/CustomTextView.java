package com.meiyou.androidskin.databinding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.meiyou.skinlib.AndroidSkin;
import com.meiyou.skinlib.manager.AndroidSkinManager;

/**
 * Author: ice
 * Date: 17/11/21 15:26.
 */

@SuppressLint("AppCompatCustomView")
public class CustomTextView extends TextView {
    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTextColor(int color) {
        //int colorNew = AndroidSkin.getInstance().getSkinColor(color);
        super.setTextColor(color);
    }
}
