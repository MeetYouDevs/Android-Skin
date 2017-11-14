package com.meiyou.skinlib;

import android.app.Application;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

import com.meiyou.skinlib.loader.SkinLoader;

/**
 * Author: meetyou
 * Date: 17/11/10 11:10.
 */

public interface IAndroidSkin  {

    /**
     * 初始化
     * @param application
     */
    public void init(Application application);

    /**
     * 保存皮肤，默认从sd卡加载
     * @param skinFilePath  支持assets,sdk卡，网络皮肤包
     * 可以是：assets文件名
     * 或者 http://xxxxx.apk，暂时不支持；
     * 或者 /storage/xxxx/xx.apk
     *
     *
     */
    public void saveSkin(String skinFilePath);

    /**
     * 保存皮肤
     * @param skinFilePath  支持assets,sdk卡，网络皮肤包
     * 可以是：assets文件名
     * 或者 http://xxxxx.apk，暂时不支持；
     * 或者 /storage/xxxx/xx.apk
     *
     * @param skinFilePath
     * @param skinLoader
     * SkinLoader.ASSETS:从assets加载
     * SkinLoader.SDCARD：从SDCard加载
     */
    public void saveSkin(String skinFilePath,SkinLoader skinLoader);


    /**
     * 保存皮肤并应用
     * @param skinFilePath
     */
    public void saveSkinAndApply(String skinFilePath,SkinListener listener);

    /**
     * 默认从sd卡加载
     * @param skinFilePath
     * @param skinLoader
     */
    public void saveSkinAndApply(String skinFilePath,SkinLoader skinLoader,SkinListener listener);

    /**
     * 应用换肤
     */
    public void applySkin();

    /**
     * 清除换肤
     */
    public void clearSkin();

    /**
     * 清除换肤并应用
     */
    public void clearSkinAndApply();

    /**
     * 获取对应drawable
     * @param originId
     * @return
     */
    public Drawable getSkinDrawable(int originId);

    /**
     * 获取对应ColorDrawable
     * @param originId
     * @return
     */
    public Drawable getSkinColorDrawable(int originId);

    /**
     * 获取对应的color资源
     * @param originId
     * @return
     */
    public int getSkinColor(int originId);
    /**
     * 对于带有status的color，如。R.color.text_color_selector;获取对应的资源ID
     * @param originId
     * @return
     */
    public ColorStateList getSkinColorStateList(int originId);


    public boolean isSkinApply();
}
