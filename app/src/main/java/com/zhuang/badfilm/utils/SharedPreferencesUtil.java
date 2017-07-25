package com.zhuang.badfilm.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.zhuang.badfilm.model.User;
import com.zhuang.badfilm.user.SingleUser;

/**
 * Created by zhuang on 2017/6/13.
 */

public class SharedPreferencesUtil {

    public static String tag = "badFilm";
    private static Context mContext;

    public static void initialize(Context context) {
        mContext = context.getApplicationContext();
    }

    public static String getToken(){
        SharedPreferences sp = mContext.getSharedPreferences(tag, Context.MODE_PRIVATE);
        String token = sp.getString("token", null);
        return token;
    }

    public static String getRefreshToken(){
        SharedPreferences sp = mContext.getSharedPreferences(tag, Context.MODE_PRIVATE);
        String refreshtoken = sp.getString("refreshtoken", null);
        return refreshtoken;
    }

    public static void setToken(String token){
        SharedPreferences sp = mContext.getSharedPreferences(tag, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", token);
        editor.commit();
    }

    public static void setRefreshToken(String refreshtoken){
        SharedPreferences sp = mContext.getSharedPreferences(tag, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("refreshtoken", refreshtoken);
        editor.commit();
    }

    public static void setAllData(String uid,String name,
                                  String password,String avatar,String token,String refreshtoken){
        SharedPreferences sp = mContext.getSharedPreferences(tag, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name",name);
        editor.putString("password", password);
        editor.putString("token", token);
        editor.putString("refreshtoken", refreshtoken);
        editor.putString("avatar", avatar);
        editor.putString("uid", uid);
        editor.commit();
    }

    public static void setDownloadId(long downloadId){
        SharedPreferences sp = mContext.getSharedPreferences(tag, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("downloadId", downloadId);
        editor.commit();
    }

    public static long getDownloadId(){
        SharedPreferences sp = mContext.getSharedPreferences(tag, Context.MODE_PRIVATE);
        long downloadId = sp.getLong("downloadId", -1L);
        return downloadId;
    }

    public static void setAvatar(String avatar){
        SharedPreferences sp = mContext.getSharedPreferences(tag, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("avatar", avatar);
        editor.commit();
    }

    public static void setTheme(Context context,int theme){
        SharedPreferences sp = context.getSharedPreferences(tag, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("theme", theme);
        editor.commit();
    }

    public static int getTheme(){
        SharedPreferences sp = mContext.getSharedPreferences(tag, Context.MODE_PRIVATE);
        int theme = sp.getInt("theme", 0);
        return theme;
    }

    public static void initSingleUser( User user){
        SharedPreferences sp = mContext.getSharedPreferences(tag, Context.MODE_PRIVATE);
        String name = sp.getString("name", null);
        String password = sp.getString("password", null);
        String avatar = sp.getString("avatar", null);
        String uid = sp.getString("uid", null);
        user.setName(name);
        user.setPassword(password);
        user.setAvatar(avatar);
        user.setUid(uid);
    }
}
