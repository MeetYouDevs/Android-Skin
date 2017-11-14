package com.meiyou.skinlib.loader;

import android.content.Context;

import com.meiyou.skinlib.util.FileUtils;
import com.meiyou.skinlib.util.PackageUtil;
import com.meiyou.skinlib.util.SkinStringUtils;

import java.io.File;

/**
 * Author: meetyou
 * Date: 17/11/13 16:08.
 */

public class AssetsSkinPathLoader extends AbstractSkinPathLoader {


    private String mSdPath="";
    public AssetsSkinPathLoader(Context context, String originPath){
        super(context,originPath);
    }

    @Override
    public String getSkinPath() {
        //file:///android_asset/xxx.apk;
        try {
            File file = new File(mSkinPath);
            /*if(!file.exists()){
                return null;
            }*/
             mSdPath = FileUtils.copyAssetsFileToSd(mContext,file.getName(),"AndroidSkin");
            return mSdPath;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public String getSkinPackage() {
        if(!SkinStringUtils.isNull(mSdPath)){
            mPacakgeName = PackageUtil.getApkPackageName(mContext,mSdPath);
        }
        return mPacakgeName;
    }
}