package com.tttqiu.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

/**
 * 加载图片工具类
 * 通过loadImageInto(String address, ImageView imageView)将网络图片加载到imageView
 * 二级缓存：先到内存中找图片，没找到再去外部存储中找，没找到再去网络上下载，并存储到内存和外部存储中
 */

public class TUtil {

    /**
     * 将网络图片加载到imageView
     * @param address 图片地址
     * @param imageView ImageView
     */
    public static void loadImageInto(Context context,String address, ImageView imageView) {
        Bitmap bitmap = getBitmapFromMemory(address);
        if (bitmap == null) {
//            bitmap=getBitmapFromDisk(context,address);
            if (bitmap == null) {
                getBitmapFromHttp(context,address,imageView);
                return;
            }
        }
        imageView.setImageBitmap(bitmap);
    }

    /**
     * 从网络获取图片
     */
    private static void getBitmapFromHttp(Context context,String address,ImageView imageView) {
        new GetBitmapFromHttpTask().execute(context,address,imageView);
    }

    /**
     * 从内存获取图片
     */
    private static Bitmap getBitmapFromMemory(String address) {
        return MemoryCacheUtil.getBitmapFromMemory(address);
    }

    /**
     * 从外部存储获取图片
     */
    private static Bitmap getBitmapFromDisk(String address) {
        return null;
    }
}
