package com.tttqiu.library;

import android.content.Context;
import android.util.Log;

import com.tttqiu.library.network.RequestQueue;

/**
 * 网络请求和缓存工具
 * <p>
 * 二级缓存机制：先到内存中找，没找到再去文件中找，没找到再去网络上下载，并可存储到内存和文件中
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
public class TUtil {

    private static RequestQueue mRequestQueue;

    /**
     * 创建请求队列，并开启所有执行子线程
     *
     * @param threadNum 执行线程数量
     * @return 请求队列
     */
    public static RequestQueue startRequestQueue(Context context,int threadNum){
        if (mRequestQueue==null){
            Log.d("TUtil_Network","创建队列并开启所有线程。 "+"线程池容量："+threadNum);
            mRequestQueue=new RequestQueue(context,threadNum);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }
}
