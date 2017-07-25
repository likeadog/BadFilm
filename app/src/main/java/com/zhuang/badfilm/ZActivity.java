package com.zhuang.badfilm;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.zhuang.badfilm.utils.SharedPreferencesUtil;

/**
 * Created by zhuang on 2017/7/18.
 */

public class ZActivity extends AppCompatActivity {

    public String TAG = getClass().getSimpleName();
    public static final int Theme_Red = 0;//红色主题
    public static final int Theme_Blue = 1;//蓝色主题
    public static final int Theme_Green = 2;//绿色主题

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setTheme() {
        int theme = SharedPreferencesUtil.getTheme();
        switch (theme) {
            case Theme_Red:
                getTheme().applyStyle(R.style.RedTheme, true);
                break;
            case Theme_Blue:
                getTheme().applyStyle(R.style.BlueTheme, true);
                break;
            case Theme_Green:
                getTheme().applyStyle(R.style.GreenTheme, true);
                break;
            default:
                getTheme().applyStyle(R.style.RedTheme, true);
                break;
        }
    }

}
