package com.tttqiu.library;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2016/11/1.
 */


public class DiskCacheUtil {

    /**
     * getExternalFilesDir(Environment.DIRECTORY_PICTURES));
     * Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
     * Environment.getExternalStorageDirectory());
     * 分别返回：
     * /storage/emulated/0/Android/data/com.tttqiu.tutil/files/Pictures
     * /storage/emulated/0/Pictures
     * /storage/emulated/0
     */
    static void putBitmapToDisk(Context context, String address,Bitmap bitmap) {
        String fileName=address.hashCode()+"";
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),fileName);
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(file));
            Log.d("TUtil","存入文件："+bitmap+ "(" + fileName + ")");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
