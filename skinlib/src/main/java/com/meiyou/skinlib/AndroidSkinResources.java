package com.meiyou.skinlib;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.meiyou.skinlib.util.LogUtils;

import dalvik.system.DexClassLoader;

public class AndroidSkinResources extends Resources {

    private Resources originResources;
    private String skinPackageName;
    private DexClassLoader classLoader;
    private Map<String, Integer> mResourceMap = new HashMap<>();
    private Map<String, Class> mClassMap = new HashMap<>();
    private Map<String, Field> mFieldMap = new HashMap<>();


    public AndroidSkinResources(Context context,AssetManager assets, String packageName) {
        super(assets, context.getResources().getDisplayMetrics(),  context.getResources().getConfiguration());
        this.originResources =context.getResources();
        this.skinPackageName = packageName;
    }


    /**
     * @param typeName  type
     * @param entryName entryName
     * @return id
     * @throws NotFoundException
     */
    public int obtainSkinId(String typeName, String entryName, int originId) throws NotFoundException {
        if (originId > 0) {
            entryName = originResources.getResourceEntryName(originId);
            typeName = originResources.getResourceTypeName(originId);
        }
        return obtainSkinId(typeName, entryName);
    }

    /**
     * 只获取包含color的
     * @param typeName
     * @param entryName
     * @param originId
     * @return
     * @throws NotFoundException
     */
    public int obtainSkinColorId(String typeName, String entryName, int originId) throws NotFoundException {
        if (originId > 0) {
            entryName = originResources.getResourceEntryName(originId);
            typeName = originResources.getResourceTypeName(originId);
        }
        if(typeName.toLowerCase().contains("color"))
            return obtainSkinId(typeName, entryName);
        return -1;
    }

    public int obtainSkinId(String typeName, String entryName) throws NotFoundException {
        int newId = 0;
        if (classLoader != null) {
            try {
                String clazzName = skinPackageName + ".R$" + typeName;
                String key = clazzName + "." + entryName;
                if (mResourceMap.get(key) != null) {
                    return mResourceMap.get(key);
                }

                Class resClazz = mClassMap.get(clazzName);
                if (resClazz == null) {
                    resClazz = classLoader.loadClass(clazzName);
                    mClassMap.put(clazzName, resClazz);
                }

                Field field = mFieldMap.get(entryName);
                if (field == null) {
                    field = resClazz.getDeclaredField(entryName);
                    mFieldMap.put(entryName, field);
                }
                newId = field.getInt(null);
                mResourceMap.put(key, newId);
            } catch (Exception e) {
                // nothing
                LogUtils.d(e.getMessage());
            }

        }else{
            String clazzName = skinPackageName + ".R$" + typeName;
            String key = clazzName + "." + entryName;
            if (mResourceMap.get(key) != null) {
                return mResourceMap.get(key);
            }

            Class resClazz = mClassMap.get(clazzName);
            if (resClazz == null) {
                try {
                    resClazz = Class.forName(clazzName);//classLoader.loadClass(clazzName);
                    mClassMap.put(clazzName, resClazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }

            Field field = mFieldMap.get(entryName);
            if (field == null) {
                try {
                    field = resClazz.getDeclaredField(entryName);
                    mFieldMap.put(entryName, field);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }

            }
            try {
                newId = field.getInt(null);
                mResourceMap.put(key, newId);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return newId;
    }


    @Override
    public String getResourceEntryName(int resid) throws NotFoundException {
        return originResources.getResourceEntryName(resid);
    }

    @Override
    public String getResourceTypeName(int resid) throws NotFoundException {
        return originResources.getResourceTypeName(resid);
    }

    public void setClassLoader(DexClassLoader classLoader) {
        this.classLoader = classLoader;
    }

     /*int obtainSkinIdForColor(String typeName, String entryName, int originId) throws NotFoundException {
        if (originId > 0) {
            entryName = originResources.getResourceEntryName(originId);
            typeName = originResources.getResourceTypeName(originId);
        }
        if(typeName.contains("drawable"))
            return -1;
        return obtainSkinId(typeName, entryName);
    }*/

}
