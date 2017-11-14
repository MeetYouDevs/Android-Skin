package com.meiyou.androidskin;

import android.app.Application;

import com.meiyou.skinlib.AndroidSkin;

/**
 * Author: meetyou
 * Date: 17/11/10 16:32.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //SpInstance.getInstance().init(getApplicationContext(),"skin_sp_file",false);
        AndroidSkin.getInstance().init(this);
    }
}
