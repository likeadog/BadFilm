package com.zhuang.badfilm;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.zhuang.badfilm.network.RetrofitHelper;
import com.zhuang.badfilm.utils.SharedPreferencesUtil;

import java.util.List;

/**
 * Created by zhuang on 2017/7/7.
 */

public class ZApplication extends Application {

    //小米推送 
    private static final String APP_ID = "2882303761517597678";
    private static final String APP_KEY = "5221759794678";

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        RetrofitHelper.initialize(this);
        SharedPreferencesUtil.initialize(this);
        initMiPush();
    }

    private void initMiPush() {
        // 注册push服务，注册成功后会向DemoMessageReceiver发送广播
        // 可以从DemoMessageReceiver的onCommandResult方法中MiPushCommandMessage对象参数中获取注册信息
        if (shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
