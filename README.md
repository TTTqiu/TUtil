# TUtil
网络请求和缓存工具库

# 添加依赖：

## Step 1. Add the JitPack repository to your build file
**gradle:**

Add it in your root build.gradle at the end of repositories:
```
	allprojects {
	    repositories {
		...
		maven { url 'https://jitpack.io' }
	    }
	}
```

## Step 2. Add the dependency
```
	dependencies {
	    compile 'com.github.TTTqiu:TUtil:1.1'
	}
```

# 主要功能：
### 多种类型网络请求
包括 GET、POST 等请求方式，String、Bitmap、Gson 三种常用请求类型，还可继承 Request 类扩展其他类型的请求。
### 多线程
默认使用容量为 5 的线程池，阻塞队列使用 LinkedBlockingQueue，可更改线程池大小，开启、停止线程，清空请求列表。
### 二级缓存
如果已缓存，会先去内存中找，没找到再去文件中找，都没有才从网络上下载。
### 内存缓存
 * 使用 LruCache 实现最近最少使用算法。
 * 缓存空间默认为系统给应用分配的最大内存的 1/4 并可更改。
 * 当缓存总大小超过限制，会先删除掉最不常用的内容腾出空间。
### 文件缓存
 * 缓存文件名为对应 url 的 hashCode
 * 存放路径为 /storage/emulated/0/Android/data/<项目包名>/files/。该路径下缓存的文件在应用删除时会自动全部删除。
 * 默认最大缓存空间为 100MB，可更改。
 * 每次缓存前检查，当缓存文件总大小大于预设的最大缓存空间，或磁盘剩余空间不足 100MB 时，根据最后修改时间，删除掉 30% 最不常用的文件缓存。

# 使用方法：
### 简单使用：
```
        /**
         * 创建请求队列，并开启所有执行子线程
         *
         * @param context context
         * @param threadNum 执行线程数量
         */
        RequestQueue mRequestQueue = TUtil.startRequestQueue(this,RequestQueue.DEFAULT_THREAD_NUM);

    // String
        /**
         * 
         * @param method GET/POST
         * @param needMemoryCache 是否需要内存缓存
         * @param needDiskCache 是否需要文件缓存
         * @param url url
         * @param listener RequestListener
         */
        StringRequest request = new StringRequest(Request.GET, true,true, url,
                new Request.RequestListener<String>() {
                    @Override
                    public void onComplete(String result) {
                        
                    }

                    @Override
                    public void onError(Response response) {

                    }
                });
        mRequestQueue.addRequest(request);

      // Bitmap
        /**
         *
         * @param method GET/POST
         * @param needMemoryCache 是否需要内存缓存
         * @param needDiskCache 是否需要文件缓存
         * @param url url
         * @param listener RequestListener
         */
        BitmapRequest request=new BitmapRequest(Request.GET,true,true,url,
                new Request.RequestListener<Bitmap>() {
            @Override
            public void onComplete(Bitmap result) {
                
            }

            @Override
            public void onError(Response response) {

            }
        });
        mRequestQueue.addRequest(request);

      // JSON
        /**
         *
         * @param method GET/POST
         * @param needMemoryCache 是否需要内存缓存
         * @param needDiskCache 是否需要文件缓存
         * @param clazz GsonBean.class
         * @param url url
         * @param listener RequestListener
         */
        GsonRequest<TitleBean> request = new GsonRequest<>(Request.GET, true,true, 
                TitleBean.class,url,new Request.RequestListener<TitleBean>() {
                    @Override
                    public void onComplete(TitleBean result) {
                        
                    }

                    @Override
                    public void onError(Response response) {

                    }
                });
        mRequestQueue.addRequest(request);
```
### 扩展其他类型请求：
继承 Request 类，复写 parseResponse() 方法：
```
public class xxxRequest extends Request<xxx> {

    public xxxRequest(String method,Boolean needMemoryCache,Boolean needDiskCache,
                         String url,Request.RequestListener<xxx> listener) {
        super(method,needMemoryCache,needDiskCache,url,listener);
    }

    @Override
    protected xxx parseResponse(byte[] data) {
        // 处理数据返回xxx类型
    }
}
```
### 设定线程池大小：
```
RequestQueue mRequestQueue = TUtil.startRequestQueue(this,RequestQueue.DEFAULT_THREAD_NUM);
```
 * startRequestQueue() 方法的参数设定线程池大小。
 * 默认 RequestQueue.DEFAULT_THREAD_NUM=5
### 设置最大缓存空间：
```
 mRequestQueue.setCache(RequestQueue.CACHE_DEFAULT,RequestQueue.CACHE_DISABLE);
```
 * 设定内存、文件缓存空间大小，单位 MB。
 * RequestQueue.CACHE_DISABLE 为不使用。
 * RequestQueue.CACHE_DEFAULT 
内存默认为系统给应用分配的最大内存的 1/4。
文件默认为 100MB。
### 停止所有请求线程：
```
        mRequestQueue.stop();
```
### 清空请求列表：
```
        mRequestQueue.clear();
```
### 删除所有文件缓存：
```
        mRequestQueue.deleteAllDiskCache();
```
### request 添加 header：
```
        request.addHeader("xxx","xxx");
```
### request 添加请求参数：
```
        request.addParam("xxx","xxx");
```
