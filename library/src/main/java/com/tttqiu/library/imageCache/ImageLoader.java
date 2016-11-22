package com.tttqiu.library.imageCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * 加载图片工具类
 * <p>
 * 通过loadImageInto(String address, ImageView imageView)将网络图片加载到imageView
 * <p>
 * 二级缓存：先到内存中找图片，没找到再去外部存储中找，没找到再去网络上下载，并存储到内存和外部存储中
 * <p>
 * 可设置最大内存缓存空间和最大文件缓存空间
 * <p>
 * 内存缓存：
 * <p>
 * 使用LruCache实现内存缓存
 * <p>
 * LruCache的maxSize默认为系统给应用分配的最大缓存的 1/4
 * <p>
 * LruCache：基于LinkedHashMap的LRU顺序结构，
 * 当插入或访问某一entry，同时会把这一entry移到链表的头端，
 * 当内存缓存总大小超过预设的maxSize，会自动从链表尾端开始移除数据，腾出空间
 * <p>
 * 文件缓存：
 * <p>
 * 文件名：图片url的hashCode
 * <p>
 * 存放路径：/storage/emulated/0/Android/data/com.tttqiu.tutil/files/Pictures
 * <p>
 * 默认使用文件缓存空间100MB
 * <p>
 * 通过getExternalFilesDir()方法得到文件保存路径，
 * 这个路径是Android为每个App提供的私有文件缓存路径，
 * 当App被删除时，这个目录也会被自动删除
 * <p>
 * 每次存入文件前检查，
 * 当缓存文件总大小大于预设的最大文件存储空间，或磁盘剩余空间不足100MB时，
 * 根据最后修改时间，删除掉30%最不常用的文件
 */

public class ImageLoader {

    private static final int CACHE_DEFAULT = -1;
    private static final int CACHE_DISABLE = 0;
    private MemoryCacheUtil mMemoryCacheUtil;
    private DiskCacheUtil mDiskCacheUtil;

    /**
     * 将网络图片加载到imageView
     *
     * @param context        context
     * @param address        图片url
     * @param imageView      ImageView
     * @param maxMemorySpace 最大内存缓存空间，单位MB CACHE_DEFAULT：默认大小 CACHE_DISABLE：不使用该缓存
     * @param maxDiskSpace   最大文件缓存空间，单位MB CACHE_DEFAULT：默认大小 CACHE_DISABLE：不使用该缓存
     */
    public void loadImageInto(Context context, String address, ImageView imageView,
                              int maxMemorySpace, int maxDiskSpace) {
        // 透明占位图
        imageView.setImageDrawable(new ColorDrawable(Color.WHITE));
        imageView.setImageAlpha(0);

        Bitmap bitmap = null;
        initCacheSpace(maxMemorySpace, maxDiskSpace);

        if (maxMemorySpace != CACHE_DISABLE) {
            bitmap = getBitmapFromMemory(address);
        }
        if (bitmap == null && maxDiskSpace != CACHE_DISABLE) {
            bitmap = getBitmapFromDisk(context, address, mMemoryCacheUtil);
        }
        if (bitmap == null) {
            getBitmapFromHttp(context, address, imageView, maxMemorySpace, maxDiskSpace);
//            getBitmapFromHttp(address, imageView,requestListener);
            return;
        }
        imageView.setImageAlpha(255);
        imageView.setImageBitmap(bitmap);
    }

    /**
     * 初始化缓存，设置缓存空间
     */
    private void initCacheSpace(int maxMemorySpace, int maxDiskSpace) {
        if (maxMemorySpace != CACHE_DISABLE) {
            mMemoryCacheUtil = new MemoryCacheUtil();
            mMemoryCacheUtil.setMemoryCacheSpace(maxMemorySpace);
        }
        if (maxDiskSpace != CACHE_DISABLE) {
            mDiskCacheUtil = new DiskCacheUtil();
            mDiskCacheUtil.setDiskCacheSpace(maxDiskSpace);
        }
    }

    /**
     * 从网络获取图片
     */
    private void getBitmapFromHttp(Context context, String address, ImageView imageView,
                                   int maxMemorySpace, int maxDiskSpace) {
        new GetBitmapFromHttpTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                context, address, imageView, maxMemorySpace, maxDiskSpace);
    }

    /**
     * 从内存获取图片
     */
    private Bitmap getBitmapFromMemory(String address) {
        return mMemoryCacheUtil.getBitmapFromMemory(address);
    }

    /**
     * 从文件获取图片
     */
    private Bitmap getBitmapFromDisk(Context context, String address, MemoryCacheUtil mMemoryCacheUtil) {
        return mDiskCacheUtil.getBitmapFromDisk(context, address, mMemoryCacheUtil);
    }
}
