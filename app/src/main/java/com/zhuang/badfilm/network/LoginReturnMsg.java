package com.zhuang.badfilm.network;

/**
 * Created by zhuang on 2017/5/26.
 */

public class LoginReturnMsg {
    private int code;
    private String detail;
    private String token;
    private String refreshtoken;
    private String avatar;
    private String uid;

    public int getCode() {
        return code;
    }

    public String getDetail() {
        return detail;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshtoken() {
        return refreshtoken;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getUid() {
        return uid;
    }
}
