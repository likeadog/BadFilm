package com.zhuang.badfilm.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.zhuang.badfilm.model.User;
import com.zhuang.badfilm.utils.SharedPreferencesUtil;

/**
 * Created by zhuang on 2017/5/31.
 */

public class SingleUser{
    private static final User ourInstance = new User();
    public static User getInstance() {
        //系统异常时，会清空数据，这里把数据重新设置进去
        if(ourInstance.getName()==null){
            SharedPreferencesUtil.initSingleUser(ourInstance);
        }
        return ourInstance;
    }

}
