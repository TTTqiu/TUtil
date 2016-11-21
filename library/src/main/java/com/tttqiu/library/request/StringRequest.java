package com.tttqiu.library.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2016/11/17.
 */

public class StringRequest extends Request<String> {

    public StringRequest(String address,Request.RequestListener<String> listener) {
        super(address,listener);
    }

    @Override
    protected String parseResponse(InputStream is) {
        StringBuilder response=new StringBuilder();
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line=br.readLine())!=null){
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }
}
