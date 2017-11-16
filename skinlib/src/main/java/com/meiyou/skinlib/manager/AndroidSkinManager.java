package com.meiyou.skinlib.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;

import com.meiyou.skinlib.AndroidSkinFactory;
import com.meiyou.skinlib.AndroidSkinResources;

import com.meiyou.skinlib.SkinListener;
import com.meiyou.skinlib.util.FileUtils;
import com.meiyou.skinlib.util.LogUtils;
import com.meiyou.skinlib.util.PackageUtil;
import com.meiyou.skinlib.util.SkinStringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Author: meetyou
 * Date: 17/11/10 10:55.
 */

public class AndroidSkinManager {
    private static final String TAG = "AndroidSkinManager";
    private static final String SP_SKIN_FILE = "android-skin-sp";
    private static final String SP_SKIN_FILE_PATH = "android-skin-file-path";
    private static final String SP_SKIN_FILE_PACKAGE = "android-skin-package";
    private static final String SP_IS_SKIN_APPLY = "is-skin-apply";

    private Context mContext;
    private AssetManager mAssetManager;
    private AndroidSkinResources mAndroidSkinResources;
    private AndroidSkinResources mAndroidSkinOriginResources;
    private String skinPackage;
    private String skinPath;
    private Handler mSkinHandler;
    private static final int APPLY_VIEW=1;

    public AndroidSkinManager(Context context){
        mContext = context;
        initSkinHandler();
    }
    private void initSkinHandler(){
        if(mSkinHandler==null) {
            mSkinHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what){
                        case APPLY_VIEW:{
                            applyView();
                            break;
                        }
                        default:
                            break;
                    }
                    super.handleMessage(msg);

                }
            };
        }
    }
    public void saveSkinInfo(String skinApkPath,String skinApkPackage) {
        if(SkinStringUtils.isNull(skinApkPackage) || SkinStringUtils.isNull(skinApkPath))
            return;
        SharedPreferences sp = mContext.getSharedPreferences(SP_SKIN_FILE, Context.MODE_PRIVATE);
        sp.edit().putString(SP_SKIN_FILE_PATH, skinApkPath)
                .putString(SP_SKIN_FILE_PACKAGE, skinApkPackage).commit();
    }

    public void clearSkinInfo() {
        SharedPreferences sp = mContext.getSharedPreferences(SP_SKIN_FILE, Context.MODE_PRIVATE);
        sp.edit().putString(SP_SKIN_FILE_PATH, "")
                .putString(SP_SKIN_FILE_PACKAGE, "").commit();
    }

    private String getSkinPath() {
        return mContext.getSharedPreferences(SP_SKIN_FILE, Context.MODE_PRIVATE).getString(SP_SKIN_FILE_PATH, "");
    }

    private String getSkinPackage() {
        return mContext.getSharedPreferences(SP_SKIN_FILE, Context.MODE_PRIVATE).getString(SP_SKIN_FILE_PACKAGE, "");
    }

    public void setSkinApply(boolean flag) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_SKIN_FILE, Context.MODE_PRIVATE);
        sp.edit().putBoolean(SP_IS_SKIN_APPLY, flag).commit();
    }

    public boolean isSkinApply() {
        if(mContext==null){
            LogUtils.d(TAG,"you must call init first");
            return false;
        }
        return mContext.getSharedPreferences(SP_SKIN_FILE, Context.MODE_PRIVATE).getBoolean(SP_IS_SKIN_APPLY, false);
    }
    public void loadSkinIfApply(final SkinListener listener) {
        if(!isSkinApply()){
            if(listener!=null){
                listener.onFail("skin no apply");
            }
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //close last
                    closeLastSkin();
                    //clear info if null
                    skinPath = getSkinPath();
                    skinPackage = getSkinPackage();
                    if(skinPackage==null || skinPath==null){
                        clearSkinInfo();
                        setSkinApply(false);
                        mSkinHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(listener!=null){
                                    listener.onFail("skinPackage or skinPath null");
                                }
                            }
                        });
                        return;
                    }
                    LogUtils.d("loadSkinIfApply  apk " + skinPath);

                    //get AssetManager
                    mAssetManager = AssetManager.class.newInstance();
                    Method addAssetPath = mAssetManager.getClass().getMethod("addAssetPath", String.class);
                    int add = (Integer) addAssetPath.invoke(mAssetManager, skinPath);
                    LogUtils.d("loadSkinIfApply add resources apk result " + add);
                    if (add <= 0) {
                        mSkinHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(listener!=null){
                                    listener.onFail("load skin file failed");
                                }
                            }
                        });
                        throw new RuntimeException("load res apk failed !!----" + skinPath);
                    }
                    //getResource
                    mAndroidSkinResources = new AndroidSkinResources(mContext, mAssetManager, skinPackage);
                    mAndroidSkinResources.setClassLoader(getSkinApkDexClassLoader());

                    //apply
                    mSkinHandler.sendEmptyMessage(APPLY_VIEW);

                    mSkinHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(listener!=null){
                                listener.onSuccess();
                            }
                        }
                    });

                }catch (Exception ex){
                    ex.printStackTrace();
                    if(listener!=null){
                        listener.onFail("skin exception:"+ex.getMessage());
                    }
                }

            }
        }).start();
    }
    /**
     * 加载皮肤资源
     */
    public void loadSkinIfApply() {
        loadSkinIfApply(null);
    }


    private void closeLastSkin() {
        if (mAssetManager != null) {
            mAssetManager.close();
        }
        if (mAndroidSkinResources != null) {
            mAndroidSkinResources = null;
        }
    }

    public AndroidSkinResources getAndroidSkinResources() {
        if(!isSkinApply()){
            //原始资源
            if(mAndroidSkinOriginResources==null)
                mAndroidSkinOriginResources = new AndroidSkinResources(mContext, mContext.getAssets(), PackageUtil.getPackageInfo(mContext).packageName);
            return mAndroidSkinOriginResources;
        }
        //换肤资源
        return mAndroidSkinResources;

    }

    private DexClassLoader getSkinApkDexClassLoader() {

        //TODO 未进行data拷贝
        //拷贝apk文件
        String newFile = skinPath+".apk";//skinPath.contains(".apk")?skinPath:(skinPath + ".apk");
        File file = new File(newFile);
        try {
            if (!file.exists()) {
                file.createNewFile();
                FileUtils.copyFile(new File(skinPath), file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        DexClassLoader dexClassLoader = new DexClassLoader(newFile,
                mContext.getCacheDir().getAbsolutePath(), null, this.getClass().getClassLoader());
        try {
            Class skinR = dexClassLoader.loadClass(skinPackage + ".R");
            return dexClassLoader;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }



    public boolean applyView() {
        return AndroidSkinFactory.from(mContext).apply();
        /*
        if (this.isSkinApply()) {
            return ViewFactory.from(mContext).apply();
        }
        return false;*/
    }

    public boolean applyView(ViewGroup viewGroup) {
       /* if (this.isSkinApply()) {
            return ViewFactory.from(mContext).apply(viewGroup);
        }
        return false;*/
        return AndroidSkinFactory.from(mContext).apply(viewGroup);
    }

    public boolean applyView(View view) {
      /*  if (this.isSkinApply()) {
            return ViewFactory.from(mContext).apply(view);
        }
        return false;*/
        return AndroidSkinFactory.from(mContext).apply(view);
    }

}
