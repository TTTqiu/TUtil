package com.tttqiu.library.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 返回String类型数据的请求
 */

public class StringRequest extends Request<String> {

    public StringRequest(String address,Request.RequestListener<String> listener) {
        super(address,listener);
    }

    @Override
    public String parseResponse(InputStream inputStream) {
        StringBuilder result=new StringBuilder();
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line=br.readLine())!=null){
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
