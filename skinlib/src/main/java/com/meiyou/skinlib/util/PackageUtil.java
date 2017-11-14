package com.meiyou.skinlib.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Author: meetyou
 * Date: 17/11/10 11:06.
 */

public class PackageUtil {
    public static PackageInfo getPackageInfo(Context context){
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return  new PackageInfo();
    }

    public static String getApkPackageName(Context context,String apkPath) {
        try {
            if(context==null)
                return null;
            PackageManager pm = context.getPackageManager();
            if(SkinStringUtils.isNull(apkPath)){
                pm = context.getPackageManager();
            }
            PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
            ApplicationInfo appInfo = null;
            if (info != null) {
                appInfo = info.applicationInfo;
                String packageName = appInfo.packageName;
                return packageName;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
