package com.tttqiu.library.network;

import com.tttqiu.library.request.Request;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 请求执行者
 */

class RequestExecutor {

    /**
     * 执行具体的请求操作，调用request的解析数据方法获得解析后的数据，并返回一个response
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
                // 解析数据
                response.setContent(request.parseResponse(is));
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
