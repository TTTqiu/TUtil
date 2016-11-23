package com.tttqiu.library.network;

import com.tttqiu.library.request.Request;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 请求执行者
 */

class RequestExecutor {

    /**
     * 执行具体的请求操作，将响应数据转化为字符数组，并返回一个response
     */
    Response executeRequest(Request<?> request) {
        Response response = new Response();
        HttpURLConnection connection = null;
        try {
            URL url = new URL(request.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            response.setResponseCode(connection.getResponseCode());
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                // 将inputStream转为字符数组
                byte[] buffer=new byte[1024];
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                int length;
                while ((length=is.read(buffer))!=-1){
                    byteArrayOutputStream.write(buffer,0,length);
                }
                response.setByteArrayOutputStream(byteArrayOutputStream);
                buffer=null;
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setExceptionMessage(e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response;
    }
}
