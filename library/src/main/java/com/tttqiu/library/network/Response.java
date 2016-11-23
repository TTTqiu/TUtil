package com.tttqiu.library.network;

import java.io.ByteArrayOutputStream;

/**
 * 网络响应类
 * <p>
 * 包括响应内容、状态码、异常信息
 */

public class Response {

    private ByteArrayOutputStream byteArrayOutputStream;
    private int responseCode;
    private String exceptionMessage;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public ByteArrayOutputStream getByteArrayOutputStream() {
        return byteArrayOutputStream;
    }

    public void setByteArrayOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
        this.byteArrayOutputStream = byteArrayOutputStream;
    }
}
