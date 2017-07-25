package com.zhuang.badfilm.model;

import java.util.List;

/**
 * Created by zhuang on 2017/7/12.
 */

public class FilmList {

    int code;
    List<Film> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Film> getData() {
        return data;
    }

    public void setData(List<Film> data) {
        this.data = data;
    }
}
