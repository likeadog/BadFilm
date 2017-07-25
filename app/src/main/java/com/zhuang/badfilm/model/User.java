package com.zhuang.badfilm.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.zhuang.badfilm.BR;

/**
 * Created by zhuang on 2017/5/22.
 */

public class User extends BaseObservable {
    private String uid;
    private String name;
    private String password;
    private String avatar;//头像url
    private String messageNumStr = "10";

    @Bindable
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
        notifyPropertyChanged(BR.uid);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        notifyPropertyChanged(BR.avatar);
    }

    @Bindable
    public String getMessageNumStr() {
        return messageNumStr;
    }

    public void setMessageNumStr(String messageNumStr) {
        this.messageNumStr = messageNumStr;
        notifyPropertyChanged(BR.messageNumStr);
    }

    public void removeSelf() {
        setUid(null);
        setName(null);
        setPassword(null);
        setAvatar(null);
        setMessageNumStr(null);
    }
}
