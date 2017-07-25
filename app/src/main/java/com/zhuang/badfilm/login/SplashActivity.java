package com.zhuang.badfilm.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.zhuang.badfilm.R;
import com.zhuang.badfilm.ZActivity;
import com.zhuang.badfilm.film.FilmMainListActivity;
import com.zhuang.badfilm.model.User;
import com.zhuang.badfilm.network.BaseReturnMsg;
import com.zhuang.badfilm.network.RetrofitHelper;
import com.zhuang.badfilm.network.service.LoginService;
import com.zhuang.badfilm.user.SingleUser;
import com.zhuang.badfilm.utils.SharedPreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends ZActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        gotoHome();
    }

    /**
     * 跳转到首页
     */
    private void gotoHome() {
        Intent intent = new Intent(SplashActivity.this, FilmMainListActivity.class);
        startActivity(intent);
        finish();
    }
}
