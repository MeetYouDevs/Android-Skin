package com.meiyou.skinlib;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meiyou.skinlib.attr.ICustAttrApplyForColorListener;
import com.meiyou.skinlib.attr.ICustAttrApplyForDrawableListener;
import com.meiyou.skinlib.attr.MutableAttr;
import com.meiyou.skinlib.loader.AbstractSkinPathLoader;
import com.meiyou.skinlib.loader.AndroidSkinPathLoader;
import com.meiyou.skinlib.loader.SkinLoader;
import com.meiyou.skinlib.manager.AndroidSkinManager;
import com.meiyou.skinlib.util.SkinStringUtils;

public class AndroidSkin implements IAndroidSkin {

    private Context mContext;
    private AndroidSkinManager mAndroidSkinManager;
    private boolean isInited = false;

    public static AndroidSkin getInstance() {
        return Holder.instance;
    }

    static class Holder {
        static AndroidSkin instance = new AndroidSkin();
    }

    private AndroidSkin() {
    }

    /**
     * 初始化
     * 默认 如果有皮肤的话，立刻生效
     * @param application
     */
    public void init(Application application) {
        init(application, true);
    }

    /**
     * 初始化
     * @param application
     * @param isApplyImmediate 是否立刻生效
     */
    public void init(Application application, boolean isApplyImmediate) {
        if (application == null)
            return;
        isInited = true;
        mContext = application.getApplicationContext();
        mAndroidSkinManager = new AndroidSkinManager(application.getApplicationContext());
        if (isApplyImmediate)
            mAndroidSkinManager.loadSkinIfApply();
        AndroidSkinHook.getInstance().registerActivityLife(application);
    }

    @Override
    public void saveSkin(String skinFilePath) {
        saveSkin(skinFilePath, SkinLoader.SDCARD);

    }

    @Override
    public void saveSkin(String skinFilePath, SkinLoader skinLoader) {
        AndroidSkinPathLoader loader = new AndroidSkinPathLoader(mContext);
        final AbstractSkinPathLoader loaderSkinPathLoader = loader.getSkinPathLoader(skinFilePath, SkinLoader.SDCARD);
        new LoadSkinTask(loaderSkinPathLoader, null).execute();
    }

    @Override
    public void saveSkinAndApply(String skinFilePath, SkinListener listener) {
        saveSkinAndApply(skinFilePath, SkinLoader.SDCARD, listener);
    }

    @Override
    public void saveSkinAndApply(String skinFilePath, SkinLoader skinLoader, SkinListener listener) {
        if (skinLoader == null || skinFilePath == null)
            return;
        AndroidSkinPathLoader loader = new AndroidSkinPathLoader(mContext);
        final AbstractSkinPathLoader loaderSkinPathLoader = loader.getSkinPathLoader(skinFilePath, skinLoader);
        new LoadSkinTask(loaderSkinPathLoader, listener).execute();

    }

    @Override
    public void applySkin() {
        mAndroidSkinManager.setSkinApply(true);
        mAndroidSkinManager.loadSkinIfApply();
    }

    public void applySkin(SkinListener listener) {
        mAndroidSkinManager.setSkinApply(true);
        mAndroidSkinManager.loadSkinIfApply(listener);
    }

    @Override
    public void clearSkin() {
        mAndroidSkinManager.clearSkinInfo();
        mAndroidSkinManager.setSkinApply(false);
    }

    @Override
    public void clearSkinAndApply() {
        clearSkin();
        mAndroidSkinManager.applyView();
    }

    @Override
    public Drawable getSkinDrawable(int originId) {
        return getSkinDrawable(null, null, originId);
    }

    @Override
    public int getSkinColor(int originId) {
        return getSkinColor(null, null, originId);
    }

    @Override
    public ColorStateList getSkinColorStateList(int originId) {
        return getSkinColorStateList(null, null, originId);
    }

    @Override
    public boolean isSkinApply() {
        return mAndroidSkinManager.isSkinApply();
    }

    @Nullable
    public ColorStateList getSkinColorStateList(String type, String entryName, int originId) {
        int newId = mAndroidSkinManager.getAndroidSkinResources().obtainSkinId(type, entryName, originId);
        if (newId > 0) {
            return mAndroidSkinManager.getAndroidSkinResources().getColorStateList(newId);
        }
        return null;
    }

    public Drawable getSkinDrawable(String type, String entryName, int originId) {
        int newId = mAndroidSkinManager.getAndroidSkinResources().obtainSkinId(type, entryName, originId);
        if (newId > 0) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                return mAndroidSkinManager.getAndroidSkinResources().getDrawable(newId);
            } else {
                return mAndroidSkinManager.getAndroidSkinResources().getDrawable(newId, null);
            }
        }
        return null;
    }

    public int getSkinId(String type, String entryName, int originId) {
        int newId = mAndroidSkinManager.getAndroidSkinResources().obtainSkinId(type, entryName, originId);
        return newId;
    }

    public int getSkinId(int originId) {
        int newId = mAndroidSkinManager.getAndroidSkinResources().obtainSkinId(null, null, originId);
        return newId;
    }

    public int getSkinColor(String type, String entryName, int originId) {
        int newId = mAndroidSkinManager.getAndroidSkinResources().obtainSkinId(type, entryName, originId);
        if (newId > 0) {
            return mAndroidSkinManager.getAndroidSkinResources().getColor(newId);
        }
        return -1;
    }

    @Override
    public Drawable getSkinColorDrawable(int originId) {
        return getSkinColorDrawable(null, null, originId);
    }

    private ColorDrawable getSkinColorDrawable(String type, String entryName, int originId) {
        int newId = mAndroidSkinManager.getAndroidSkinResources().obtainSkinColorId(type, entryName, originId);
        if (newId > 0) {
            return new ColorDrawable(newId);
            // return mAndroidSkinManager.getAndroidSkinResources().getColor(newId);
        }
        return null;
    }

    public AndroidSkin setBackgroundDrawable(View view, int resId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(AndroidSkin.getInstance().getSkinDrawable(resId));
            } else {
                view.setBackgroundDrawable(AndroidSkin.getInstance().getSkinDrawable(resId));
            }
            addMutableAttrs(view, MutableAttr.TYPE.BACKGROUND.getRealName(), resId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public AndroidSkin setImageDrawable(ImageView view, int resId) {
        try {
            view.setImageDrawable(AndroidSkin.getInstance().getSkinDrawable(resId));
            addMutableAttrs(view, MutableAttr.TYPE.SRC.getRealName(), resId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public AndroidSkin setTextColor(TextView view, int colorId) {
        try {
            view.setTextColor(AndroidSkin.getInstance().getSkinColor(colorId));
            addMutableAttrs(view, MutableAttr.TYPE.TEXT_COLOR.getRealName(), colorId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }

    private AndroidSkin addMutableAttrs(View view, String attrName, int resId) {
        List<MutableAttr> mutableAttrList = new ArrayList<>();
        AndroidSkinFactory factory = AndroidSkinFactory.from(mContext);
        mutableAttrList.add(factory.createMutableAttr(attrName, resId));
        factory.addRuntimeView(view, mutableAttrList);
        return this;
    }

    public AndroidSkin registerIgnoreSkinActivity(Activity activity) {
        AndroidSkinFactory.from(activity.getApplicationContext()).registerIgnoreSkinActivity(activity);
        return this;
    }

    public AndroidSkin unRegisterIgnoreSkinActivity(Activity activity) {
        AndroidSkinFactory.from(activity.getApplicationContext()).registerIgnoreSkinActivity(activity);
        return this;
    }

    public AndroidSkin registerIgnoreSkinView(View view) {
        AndroidSkinFactory.from(view.getContext()).registerIgnoreSkinView(view);
        return this;
    }

    public AndroidSkin registerIgnoreSkinViewAttrs(View view, String... attrs) {
        AndroidSkinFactory.from(view.getContext()).registerIgnoreSkinViewAttrs(view, attrs);
        return this;
    }

    public boolean isInited() {
        return isInited;
    }

    public AndroidSkinManager getAndroidSkinManager() {
        return mAndroidSkinManager;
    }

    private class LoadSkinTask extends AsyncTask<String, Void, String> {
        private final AbstractSkinPathLoader mAbstractSkinPathLoader;
        private final SkinListener mListener;

        LoadSkinTask(AbstractSkinPathLoader loader, SkinListener listener) {
            mAbstractSkinPathLoader = loader;
            mListener = listener;
        }

        protected void onPreExecute() {
            if (mListener != null) {
                mListener.onStart();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String path = mAbstractSkinPathLoader.getSkinPath();
                String packageName = mAbstractSkinPathLoader.getSkinPackage();
                mAndroidSkinManager.saveSkinInfo(path, packageName);
                return path;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            if (SkinStringUtils.isNull(result)) {
                AndroidSkin.getInstance().clearSkin();
                if (mListener != null) {
                    mListener.onFail("换肤失败");
                }
                return;
            }
            applySkin(mListener);

        }
    }

    /**
     * 增加自定义属性支持（即将修改）
     * @param attr 属性名，如：tv_border_color
     * @param colorListener 当皮肤切换时的回调，回调里包含需要替换的color值
     */
    public void addCustomAttrSupport(@NonNull String attr,
        @NonNull ICustAttrApplyForColorListener colorListener) {
        MutableAttrManager.getInstance().addCustomAttr(attr, colorListener);
    }

    /**
     * 增加自定义属性支持
     * @param attr 属性名，如：tv_border_color
     * @param drawableListener 当皮肤切换时的回调，回调里包含需要替换的drawable值
     */
    public void addCustomAttrSupport(@NonNull String attr,
        @NonNull ICustAttrApplyForDrawableListener drawableListener) {
        MutableAttrManager.getInstance().addCustomAttr(attr, drawableListener);

    }
}
