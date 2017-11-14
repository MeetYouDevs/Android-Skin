package com.meiyou.skinlib.loader;

import android.content.Context;

import com.meiyou.skinlib.util.SkinStringUtils;

/**
 * Author: meetyou
 * Date: 17/11/13 16:03.
 */

public class AndroidSkinPathLoader {

    private Context mContext;
    public AndroidSkinPathLoader(Context context){
        mContext = context;
    }

    public AbstractSkinPathLoader getSkinPathLoader(String path,SkinLoader skinLoader){
        if(skinLoader ==null)
            return null;
        if(skinLoader.value()==SkinLoader.ASSETS.value()){
            return new AssetsSkinPathLoader(mContext,path);
        }else  if(skinLoader.value()==SkinLoader.SDCARD.value()){
            return new SDSkinPathLoader(mContext,path);
        }
        //return new HttpSkinPathLoader(mContext,path);
        return new SDSkinPathLoader(mContext,path);
    }


    private boolean isAssets(String path){
        if(SkinStringUtils.isNull(path))
            return false;
        // String path = "file:///android_asset/文件名";
        return  path.contains("android_asset/");
    }

    private boolean isHttpFile(String path){
        if(SkinStringUtils.isNull(path))
            return false;
        return  path.startsWith("http://") || path.startsWith("https://");
    }




}
