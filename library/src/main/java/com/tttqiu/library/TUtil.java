package com.tttqiu.library;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.tttqiu.library.http.RequestQueue;
import com.tttqiu.library.imageCache.ImageLoader;

import java.util.concurrent.ExecutorService;

public class TUtil {

    public static final int CACHE_DEFAULT = -1;
    public static final int CACHE_DISABLE = 0;
    private static ImageLoader mImageLoader;
    private static RequestQueue mRequestQueue;

    /**
     * 将网络图片加载到imageView
     *
     * @param context        context
     * @param address        图片url
     * @param imageView      ImageView
     * @param maxMemorySpace 最大内存缓存空间，单位MB TUtil.CACHE_DEFAULT：默认大小 TUtil.CACHE_DISABLE：不使用该缓存
     * @param maxDiskSpace   最大文件缓存空间，单位MB TUtil.CACHE_DEFAULT：默认大小 TUtil.CACHE_DISABLE：不使用该缓存
     */
    public static void loadImageInto(Context context, String address, ImageView imageView,
                                     int maxMemorySpace, int maxDiskSpace) {
        if (mImageLoader==null){
            mImageLoader=new ImageLoader();
        }
        mImageLoader.loadImageInto(context,address,imageView,maxMemorySpace,maxDiskSpace);
    }

    public static RequestQueue startRequestQueue(int threadNum){
        if (mRequestQueue==null){
            Log.d("TUtil_Network","创建队列并开启所有线程。 "+"线程池容量："+threadNum);
            mRequestQueue=new RequestQueue(threadNum);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }
}
