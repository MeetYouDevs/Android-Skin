package com.meiyou.skinlib.loader;

import android.content.Context;

/**
 * Author: meetyou
 * Date: 17/11/13 16:22.
 */

public abstract class AbstractSkinPathLoader {

    protected Context mContext;
    protected String mPacakgeName;
    protected String mSkinPath;
    public AbstractSkinPathLoader(Context context,String originPath){
        mContext = context.getApplicationContext();
        mSkinPath = originPath;
    }



    public abstract String getSkinPath();

    public abstract String getSkinPackage();

}
