网络请求和缓存工具库
#使用方法：
####Step 1. Add the JitPack repository to your build file
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
####Step 2. Add the dependency
```
	dependencies {
	    compile 'com.github.TTTqiu:TUtil:1.1'
	}
```
#主要功能：
###1. 网络请求
1. 多线程异步网络请求
