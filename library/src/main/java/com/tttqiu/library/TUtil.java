package com.tttqiu.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

/**
 * 加载图片工具类
 * 通过loadImageInto(String address, ImageView imageView)将网络图片加载到imageView
 * 二级缓存：先到内存中找图片，没找到再去外部存储中找，没找到再去网络上下载，并存储到内存和外部存储中
 * 可设置最大内存缓存空间和最大文件缓存空间
 *
 * 内存缓存：
 * 使用LruCache实现内存缓存
 * LruCache的maxSize默认为系统给应用分配的最大缓存的 1/4
 * LruCache：基于LinkedHashMap的LRU顺序结构
 * 当插入或访问某一entry，同时会把这一entry移到链表的头端
 * 当内存缓存总大小超过预设的maxSize，会自动从链表尾端开始移除数据，腾出空间
 *
 * 文件缓存：
 * 文件名：图片url的hashCode
 * 存放路径：/storage/emulated/0/Android/data/com.tttqiu.tutil/files/Pictures
 * 默认使用文件缓存空间100MB
 * 通过getExternalFilesDir()方法得到文件保存路径
 * 这个路径是Android为每个App提供的私有文件缓存路径
 * 当App被删除时，这个目录也会被自动删除
 * 每次存入文件前检查
 * 当缓存文件总大小大于预设的最大文件存储空间，或磁盘剩余空间不足100MB时
 * 根据最后修改时间，删除掉30%最不常用的文件
 */

public class TUtil {

    public static final int DEFAULT = -1;
    public static final int DISABLE = 0;

    /**
     * 将网络图片加载到imageView
     *
     * @param context        context
     * @param address        图片地址
     * @param imageView      ImageView
     * @param maxMemorySpace 最大内存缓存空间，单位MB TUtil.DEFAULT：默认大小 TUtil.DISABLE：不使用该缓存
     * @param maxDiskSpace   最大文件缓存空间，单位MB TUtil.DEFAULT：默认大小 TUtil.DISABLE：不使用该缓存
     */
    public static void loadImageInto(Context context, String address, ImageView imageView,
                                     int maxMemorySpace, int maxDiskSpace) {
        Bitmap bitmap = null;
        setCacheSpace(maxMemorySpace,maxDiskSpace);
        if (maxMemorySpace != DISABLE) {
            bitmap = getBitmapFromMemory(address);
        }
        if (bitmap == null && maxDiskSpace != DISABLE) {
            bitmap = getBitmapFromDisk(context, address,maxMemorySpace);
        }
        if (bitmap == null) {
            getBitmapFromHttp(context, address, imageView,maxMemorySpace,maxDiskSpace);
            return;
        }
        imageView.setImageBitmap(bitmap);
    }

    /**
     * 从网络获取图片
     */
    private static void getBitmapFromHttp(Context context, String address, ImageView imageView,
                                          int maxMemorySpace, int maxDiskSpace) {
        new GetBitmapFromHttpTask().execute(context, address, imageView,maxMemorySpace,maxDiskSpace);
    }

    /**
     * 从内存获取图片
     */
    private static Bitmap getBitmapFromMemory(String address) {
        return MemoryCacheUtil.getBitmapFromMemory(address);
    }

    /**
     * 从文件获取图片
     */
    private static Bitmap getBitmapFromDisk(Context context, String address,int maxMemorySpace) {
            return DiskCacheUtil.getBitmapFromDisk(context, address,maxMemorySpace!=0);

    }

    /**
     * 设置缓存空间
     */
    private static void setCacheSpace(int maxMemorySpace, int maxDiskSpace) {
        MemoryCacheUtil.setMemoryCacheSpace(maxMemorySpace);
        DiskCacheUtil.setDiskCacheSpace(maxDiskSpace);
    }
}
