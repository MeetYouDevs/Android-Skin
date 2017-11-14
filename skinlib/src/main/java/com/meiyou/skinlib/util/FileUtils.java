package com.meiyou.skinlib.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 * Author: meetyou
 * Date: 17/11/10 11:32.
 */

public class FileUtils {
    public static void copyFile(File src, File dest) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;

        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }

            inChannel = (new FileInputStream(src)).getChannel();
            outChannel = (new FileOutputStream(dest)).getChannel();
            inChannel.transferTo(0L, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }

            if (outChannel != null) {
                outChannel.close();
            }

        }

    }

    public static String copyAssetsFileToSd(Context context, String assetsFileName,String dirFolderName) {
        if(context==null || assetsFileName==null || dirFolderName==null)
            return "";
        String skinPath = new File(getDir(context,dirFolderName), assetsFileName).getAbsolutePath();
        try {
            File file = new File(skinPath);
            if(!file.exists()){
                file.createNewFile();
            }
            InputStream is = context.getAssets().open(assetsFileName);
            OutputStream os = new FileOutputStream(skinPath);
            int byteCount;
            byte[] bytes = new byte[1024];
            while ((byteCount = is.read(bytes)) != -1) {
                os.write(bytes, 0, byteCount);
            }
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return skinPath;
    }

    public static String getDir(Context context,String dirFolderName) {
        File file = new File(getCacheDir(context),dirFolderName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    private static String getCacheDir(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheDir = context.getExternalCacheDir();
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                return cacheDir.getAbsolutePath();
            }
        }

        return context.getCacheDir().getAbsolutePath();
    }


    public static boolean isFileExists(String path) {
        return !TextUtils.isEmpty(path) && new File(path).exists();
    }
}
