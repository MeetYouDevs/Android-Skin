package com.meiyou.skinlib.loader;

import android.content.Context;

import com.meiyou.skinlib.util.PackageUtil;
import com.meiyou.skinlib.util.SkinStringUtils;

/**
 * Author: meetyou
 * Date: 17/11/13 16:08.
 */

public class HttpSkinPathLoader extends AbstractSkinPathLoader {


    public HttpSkinPathLoader(Context context, String originPath){
        super(context,originPath);
    }

    @Override
    public String getSkinPath() {
        return mSkinPath;
    }

    @Override
    public String getSkinPackage() {
        if(SkinStringUtils.isNull(mPacakgeName)){
            mPacakgeName = PackageUtil.getApkPackageName(mContext,mSkinPath);
        }
        return mPacakgeName;
    }
}