package com.tttqiu.library.imageCache;


import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * 内存缓存工具类
 * 使用LruCache实现内存缓存
 * LruCache的maxSize默认为系统给应用分配的最大缓存的 1/4
 * <p>
 * LruCache：基于LinkedHashMap的LRU顺序结构
 * 当插入或访问某一entry，同时会把这一entry移到链表的头端
 * 当内存缓存总大小超过预设的maxSize，会自动从链表尾端开始移除数据，腾出空间
 */

class MemoryCacheUtil {

    private LruCache<String, Bitmap> mLruCache;
    private int maxSize=0;

    /**
     * 将图片存入内存
     */
    void putBitmapToMemory(String key, Bitmap value) {
        createLruCache();
        Bitmap oldValue = mLruCache.put(key, value);
        if (oldValue == null) {
            Log.d("TUtil_Image", "MC：存入内存：" + value + "(" + key + ")");
            Log.d("TUtil_Image", "MC：内存缓存总大小：" + mLruCache.size() / 1024 + "KB");
        }
    }

    /**
     * 将图片从内存中取出
     */
    Bitmap getBitmapFromMemory(String key) {
        createLruCache();
        Bitmap bitmap = mLruCache.get(key);
        if (bitmap != null) {
            Log.d("TUtil_Image", "MC：从内存读取：" + bitmap + "(" + key + ")");
        }
        return bitmap;
    }

    /**
     * 创建LruCache
     * 创建LruCache需要设定一个最大缓存容量maxSize，一般为系统给应用分配的最大缓存的 1/8
     * sizeOf()方法，不复写返回的是条目个数（这时maxSize就设定为最大条目数目），一般要复写来返回条目大小
     * 这里复写了sizeOf()方法，计算并返回每个Bitmap的大小
     */
    private void createLruCache() {
        if (mLruCache == null) {
            if (maxSize == 0) {
                // 如果用户没自定义最大空间，设置LruCache最大缓存为系统给应用分配的最大缓存的 1/4
                maxSize = (int) (Runtime.getRuntime().maxMemory() / 4);
            }
            Log.d("TUtil_Image", "MC：创建 LruCache：maxSize=" + maxSize / 1024 + "KB");
            mLruCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                // 不复写返回的是条目个数，复写返回条目大小
                protected int sizeOf(String key, Bitmap value) {
                    int size;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        // API 19
                        size = value.getAllocationByteCount();
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                            // API 12
                            size = value.getByteCount();
                        } else {
                            // 更早版本
                            size = value.getRowBytes() * value.getHeight();
                        }
                    }
                    Log.d("TUtil_Image", "size=" + size / 1024 + "KB");
                    return size;
                }

                // entry被移除时调用
                // evicted为true表示超过最大缓存，为腾出空间自动移除了entry
                // evicted为false表示由put()或remove()导致的移除
                @Override
                protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                    super.entryRemoved(evicted, key, oldValue, newValue);
                    if (evicted) {
                        Log.d("TUtil_Image", "MC：移出缓存：" + key);
                    }
                }
            };
        }
    }

    /**
     * 设置最大内存缓存空间
     */
    void setMemoryCacheSpace(int maxSpace) {
        if (maxSpace > 0) {
            maxSize = maxSpace * 1024 * 1024;
        }
    }
}
