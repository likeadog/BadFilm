package com.zhuang.badfilm.network;

import java.util.List;

/**
 * Created by zhuang on 2017/5/22.
 */

public class ReturnData<T> {
    private int code;
    private String detail;
    private T obj;

    public int getCode() {
        return code;
    }

    public T getObj() {
        return obj;
    }

    public String getDetail() {
        return detail;
    }
}
