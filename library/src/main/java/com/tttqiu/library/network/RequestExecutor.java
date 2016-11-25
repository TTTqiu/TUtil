package com.tttqiu.library.network;

import com.tttqiu.library.request.Request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        Response response;
        if (request.getMethod().equals("GET")){
            response=executeGetRequest(request);
        }else if (request.getMethod().equals("POST")){
            response=executePostRequest(request);
        }else{
            response=new Response();
            response.setExceptionMessage("method 错误");
        }
        return response;
    }

    /**
     * 执行GET请求，返回response
     */
    private Response executeGetRequest(Request<?> request){
        Response response=new Response();
        HttpURLConnection connection = null;
        try {
            URL url = new URL(request.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            response.setResponseCode(connection.getResponseCode());
            if (response.getResponseCode() == HttpURLConnection.HTTP_OK) {
                response.setByteArrayOutputStream(IsToByte(connection.getInputStream()));
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

    /**
     * 执行POST请求，返回response
     */
    private Response executePostRequest(Request<?> request){
        Response response=new Response();
        HttpURLConnection connection = null;
        try {
            URL url = new URL(request.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStream outputStream=connection.getOutputStream();
            outputStream.write(request.getBody());
            response.setResponseCode(connection.getResponseCode());
            if (response.getResponseCode()  == HttpURLConnection.HTTP_OK) {
                response.setByteArrayOutputStream(IsToByte(connection.getInputStream()));
            }
            outputStream.close();
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

    /**
     * 将inputStream转为字符数组
     */
    private ByteArrayOutputStream IsToByte(InputStream inputStream){
        byte[] buffer=new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        int length;
        try {
            while ((length=inputStream.read(buffer))!=-1){
                byteArrayOutputStream.write(buffer,0,length);
            }
            buffer=null;
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream;
    }
}
