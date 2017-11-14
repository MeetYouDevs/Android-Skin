package com.meiyou.androidskin;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author: meetyou
 * Date: 17/11/13 09:29.
 */

public class SpInstance {
    private String mSpFileName = "";
    private boolean bSync = false;
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSharedPreferences;



    private static SpInstance manager;
    public static synchronized SpInstance getInstance() {
        if(manager == null) {
            manager = new SpInstance();
        }

        return manager;
    }
    public SpInstance() {

    }
    public void init(Context context, String filename, boolean sync) {
        this.mSpFileName = filename;
        this.mContext = context.getApplicationContext();
        this.bSync = sync;
        this.mSharedPreferences = context.getSharedPreferences(this.mSpFileName, 0);
        this.mEditor = this.mSharedPreferences.edit();
    }

    public void init(Context context, String filename) {
        this.mSpFileName = filename;
        this.mContext = context.getApplicationContext();
        this.mSharedPreferences = context.getSharedPreferences(this.mSpFileName, 0);
        this.mEditor = this.mSharedPreferences.edit();
    }

    public void SpInstance(String key, String content) {
        if(this.mSpFileName!=null) {
            this.mEditor.putString(key, content);
            this.mEditor.apply();
        }
    }

    public void saveString(String key, String content) {
        if(this.mSpFileName!=null) {
            this.mEditor.putString(key, content);
            this.mEditor.apply();
        }
    }

    public String getString(String key, String defaultValue) {
        return this.mSpFileName==null?defaultValue:this.mSharedPreferences.getString(key, defaultValue);
    }

    public void saveInt(String name, int content) {
        this.mEditor.putInt(name, content);
        if(this.bSync) {
            this.mEditor.commit();
        } else {
            this.mEditor.apply();
        }

    }

    public int getInt(String name, int def) {
        return this.mSharedPreferences.getInt(name, def);
    }

    public long getLong(String name, Context context, long def) {
        return this.mSharedPreferences.getLong(name, def);
    }

    public void saveLong(String name, long value) {
        this.mEditor.putLong(name, value);
        if(this.bSync) {
            this.mEditor.commit();
        } else {
            this.mEditor.apply();
        }

    }

    public void saveBoolean(String name, boolean vaule) {
        this.mEditor.putBoolean(name, vaule);
        if(this.bSync) {
            this.mEditor.commit();
        } else {
            this.mEditor.apply();
        }

    }

    public boolean getBoolean(String key, boolean def) {
        return this.mSharedPreferences.getBoolean(key, def);
    }

    public boolean containKey(Context context, String key) {
        SharedPreferences shared = context.getSharedPreferences(this.mSpFileName, 0);
        return shared.contains(key);
    }

    public SharedPreferences getSharedPreference() {
        return this.mSharedPreferences;
    }
}
