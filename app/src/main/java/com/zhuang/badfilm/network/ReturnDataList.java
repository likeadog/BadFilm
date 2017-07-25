package com.zhuang.badfilm.network;

import java.util.List;

/**
 * Created by zhuang on 2017/5/22.
 */

public class ReturnDataList<T> {
    private int code;
    private String detail;
    private List<T> data;

    public int getCode() {
        return code;
    }

    public List<T> getData() {
        return data;
    }

    public String getDetail() {
        return detail;
    }
}
