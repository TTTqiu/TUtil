package com.tttqiu.library.network;

/**
 * 网络响应类
 * <p>
 * 包括响应内容、状态码、异常信息
 */

public class Response {

    private Object content;
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

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
