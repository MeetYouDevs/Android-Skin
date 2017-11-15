package com.meiyou.skinlib;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;

import java.lang.reflect.Field;

/**
 * Author: meetyou
 * Date: 17/11/10 17:46.
 */

public class AndroidSkinHook {

    public static AndroidSkinHook getInstance() {
        return AndroidSkinHook.Holder.instance;
    }
    static class Holder {
        static AndroidSkinHook instance = new AndroidSkinHook();
    }
    private AndroidSkinHook() {
    }

    public void registerActivityLife(Application application){
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                hookLayoutInflater(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private void hookLayoutInflater(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        try {
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater, false);
            LayoutInflaterCompat.setFactory2(layoutInflater, AndroidSkinFactory.from(context,layoutInflater));
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
