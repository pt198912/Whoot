package com.app.whoot.modle.http.response;

/**
 * Created by ruibing.han on 2018/3/26.
 */

public class BaseResponse<T> {

    private String message;

    private String code;

    private T data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
