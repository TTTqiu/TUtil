package com.tttqiu.library.imageCache;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 文件缓存工具类
 * 文件名：图片url的hashCode
 * 存放路径：/storage/emulated/0/Android/data/com.tttqiu.tutil/files/Pictures
 * 默认使用缓存空间100MB
 * <p>
 * 通过getExternalFilesDir()方法得到文件保存路径
 * 这个路径是Android为每个App提供的私有文件缓存路径
 * 当App被删除时，这个目录也会被自动删除
 */


class DiskCacheUtil {

    // 磁盘最小空余空间，单位MB
    private static final int MIN_DISK_FREE_SPACE = 100;
    // 使用的最大文件存储空间，单位B
    private int maxSize = 100 * 1024 * 1024;

    /**
     * 将图片存入文件
     * <p>
     * 每次存入前检查
     * 当缓存文件总大小大于预设的最大文件存储空间，或磁盘剩余空间不足100MB时
     * 根据最后修改时间，删除掉30%最不常用的文件
     */
    void putBitmapToDisk(Context context, String address, Bitmap bitmap) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.d("TUtil_Image", "DC：外部存储不可用");
            return;
        }

        trimDiskCache(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "");

        String fileName = address.hashCode() + "";
        /*
        getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
        Environment.getExternalStorageDirectory());
          分别返回：
        /storage/emulated/0/Android/data/com.tttqiu.tutil/files/Pictures
        /storage/emulated/0/Pictures
        /storage/emulated/0
        */
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
            Log.d("TUtil_Image", "DC：存入文件：" + bitmap + "(" + fileName + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 从文件中获取图片
     * 如果获取到，并且putToMemory为true，就在内存中存一份
     */
    Bitmap getBitmapFromDisk(Context context, String address, MemoryCacheUtil mMemoryCacheUtil) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.d("TUtil_Image", "DC：外部存储不可用");
            return null;
        }

        String fileName = address.hashCode() + "";

        // 更新文件最后修改时间
        setFileModifiedTime(context, fileName);

        Bitmap bitmap = BitmapFactory.decodeFile(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + fileName);
        if (bitmap != null) {
            Log.d("TUtil_Image", "DC：从文件读取：" + bitmap + "(" + address + ")");
            if (mMemoryCacheUtil!=null) {
                mMemoryCacheUtil.putBitmapToMemory(address, bitmap);
            }
        }
        return bitmap;
    }

    /**
     * 设置最大内存缓存空间
     */
    void setDiskCacheSpace(int maxSpace) {
        if (maxSpace > 0) {
            maxSize = maxSpace * 1024 * 1024;
        }
    }

    /**
     * 清理文件缓存
     * 当缓存文件总大小大于预设的最大文件存储空间，或磁盘剩余空间不足100MB时
     * 根据最后修改时间，删除掉30%最不常用的文件
     */
    private void trimDiskCache(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();

        if (files == null) {
            return;
        }

        // 计算缓存文件总大小
        int dirSize = 0;
        for (File file : files) {
            dirSize += file.length();
        }

        Log.d("TUtil_Image", "DC：文件存储空间：" + maxSize);
        Log.d("TUtil_Image", "DC：文件总大小：" + dirSize);

        // 当缓存文件总大小大于预设的最大文件存储空间，或磁盘剩余空间不足100MB时，删除掉30%最不常用的文件
        if (dirSize > maxSize || MIN_DISK_FREE_SPACE > DiskFreeSpace()) {
            int removeCount = (int) (0.3 * files.length);
            Arrays.sort(files, new FileLastModifiedSort());
            for (int i = 0; i < removeCount; i++) {
                Log.d("TUtil_Image", "DC：删除文件：" + files[i].getName() + "(" + files[i].lastModified() + ")");
                files[i].delete();
            }
        }
    }


    /**
     * 设置文件最后修改时间
     */
    private void setFileModifiedTime(Context context, String fileName) {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
        long newModifiedTime = System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }

    /**
     * 计算Disk上的剩余空间
     */
    private int DiskFreeSpace() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long diskFree;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            diskFree = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        } else {
            diskFree = stat.getAvailableBlocks() * stat.getBlockSize();
        }
        return (int) diskFree / (1024 * 1024);
    }

    /**
     * 根据文件的最后修改时间进行排序
     */
    private class FileLastModifiedSort implements Comparator<File> {
        public int compare(File file0, File file1) {
            if (file0.lastModified() > file1.lastModified()) {
                return 1;
            } else if (file0.lastModified() == file1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}
