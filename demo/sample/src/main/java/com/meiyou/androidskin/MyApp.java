package com.meiyou.androidskin;

import android.app.Application;

import com.meiyou.skinlib.AndroidSkin;
import com.meiyou.skinlib.attr.ICustAttrApplyForColorListener;

/**
 * Author: meetyou
 * Date: 17/11/10 16:32.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // SpInstance.getInstance().init(getApplicationContext(),"skin_sp_file",false);
        AndroidSkin.getInstance().init(this);
        AndroidSkin.getInstance().addCustomAttrSupport("tv_border_color",
            new ICustAttrApplyForColorListener<TestView>() {
                @Override
                public void applyColor(String attrName, TestView view, int color) {
                    view.setBorderColor(color);
                }
            });
        AndroidSkin.getInstance().addCustomAttrSupport("tv_drawable_left_and_right", new TestViewDrawableLRListener());
    }
}
