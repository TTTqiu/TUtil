package com.tttqiu.library.cache;


import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.tttqiu.library.request.Request;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 文件缓存工具类
 * <p>
 * 文件名：url的hashCode
 * <p>
 * 存放路径：/storage/emulated/0/Android/data/com.tttqiu.tutil/files
 * <p>
 * 默认使用缓存空间100MB
 * <p>
 * 通过getExternalFilesDir()方法得到文件保存路径，这个路径是Android为每个App提供的私有文件缓存路径，
 * 当App被删除时，这个目录也会被自动删除
 */

public class DiskCacheUtil {

    // 磁盘最小空余空间，单位MB
    private static final int MIN_DISK_FREE_SPACE = 100;
    // 使用的最大文件存储空间，单位B
    private int maxSize = 100 * 1024 * 1024;
    private File dirPath;

    public DiskCacheUtil(Context context) {
        dirPath = context.getExternalFilesDir(null);

        /*
        getExternalFilesDir(null);
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Environment.getExternalStorageDirectory();
          分别返回：
        /storage/emulated/0/Android/data/com.tttqiu.tutil/files/Pictures
        /storage/emulated/0/Pictures
        /storage/emulated/0
        */
    }

    /**
     * 将数据存入文件
     * <p>
     * 每次存入前检查，
     * 当缓存文件总大小大于预设的最大文件存储空间，或磁盘剩余空间不足100MB时，
     * 根据最后修改时间，删除掉30%最不常用的文件
     */
    public void putByteToDisk(String url, byte[] data) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.d("TUtil_Cache", "DC：外部存储不可用");
            return;
        }
        if (maxSize==0){
            Log.d("TUtil_Cache", "DC：不使用文件缓存");
            return;
        }

        trimDiskCache();

        String fileName = url.hashCode() + "";

        File file = new File(dirPath, fileName);
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(data);
            bos.flush();
            Log.d("TUtil_Cache", "DC：存入文件："+fileName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从文件中获取数据
     */
    public byte[] getByteFromDisk(String url) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.d("TUtil_Cache", "DC：外部存储不可用");
            return null;
        }

        String fileName = url.hashCode() + "";
        File file=new File(dirPath,fileName);

        // 更新文件最后修改时间
        setFileModifiedTime(file);

        byte[] buffer=new byte[1024];
        byte[] data =null;
        int length;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis=null;
        try {
            fis=new FileInputStream(file);
            while ((length=fis.read(buffer))!=-1){
                baos.write(buffer,0,length);
            }
            data=baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("TUtil_Cache", "DC：从文件中获取数据："+fileName);
        return data;
    }

    /**
     * 检查请求是否已被缓存
     */
    public Boolean isDiskCached(Request<?> request) {
        File file = new File(dirPath, request.getUrl().hashCode() + "");
        return file.exists();
    }

    /**
     * 设置最大文件缓存空间，单位MB
     */
    public void setDiskCacheSpace(int maxSpace) {
        if (maxSpace >= 0) {
            maxSize = maxSpace * 1024 * 1024;
        }
    }

    /**
     * 清理文件缓存
     * <p>
     * 当缓存文件总大小大于预设的最大文件存储空间，或磁盘剩余空间不足100MB时，
     * 根据最后修改时间，删除掉30%最不常用的文件
     */
    private void trimDiskCache() {
        File[] files = dirPath.listFiles();

        if (files == null) {
            return;
        }

        // 计算缓存文件总大小
        int dirSize = 0;
        for (File file : files) {
            dirSize += file.length();
        }

        Log.d("TUtil_Cache", "DC：文件存储空间：" + maxSize);
        Log.d("TUtil_Cache", "DC：文件总大小：" + dirSize);

        // 当缓存文件总大小大于预设的最大文件存储空间，或磁盘剩余空间不足100MB时，删除掉30%最不常用的文件
        if (dirSize > maxSize || MIN_DISK_FREE_SPACE > DiskFreeSpace()) {
            int removeCount = (int) (0.3 * files.length);
            // 按文件最后修改时间重新排列files
            Arrays.sort(files, new FileLastModifiedSort());
            for (int i = 0; i < removeCount; i++) {
                Log.d("TUtil_Cache", "DC：删除文件：" + files[i].getName() + "(" + files[i].lastModified() + ")");
                files[i].delete();
            }
        }
    }

    /**
     * 设置文件最后修改时间
     */
    private void setFileModifiedTime(File file) {
        long newModifiedTime = System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }

    /**
     * 删除所有文件缓存
     */
    public void deleteAllCache() {
        File[] files=dirPath.listFiles();
        for (File file:files){
            file.delete();
        }
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
        return (int) (diskFree / (1024 * 1024));
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
