package com.zhuang.badfilm.user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.zhuang.badfilm.R;
import com.zhuang.badfilm.ZActivity;
import com.zhuang.badfilm.databinding.ActivityUserMainBinding;
import com.zhuang.badfilm.login.LoginActivity;
import com.zhuang.badfilm.model.User;
import com.zhuang.badfilm.utils.SharedPreferencesUtil;

public class UserMainActivity extends ZActivity {

    ActivityUserMainBinding binding;
    private AlertDialog themeCheckDialog;
    private CheckBox[] checkBoxes = new CheckBox[3];
    private boolean hasChangeTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_main);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private boolean hasToken() {
        String token = SharedPreferencesUtil.getToken();
        return token != null;
    }

    public void checkLogin(View view) {
        if (!hasToken()) {
            gotoLogin();
        } else {
            gotoUserData();
        }
    }

    private void gotoUserData() {
        Intent intent = new Intent(this, UserDataActivity.class);
        startActivity(intent);
    }

    private void gotoLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void selectTheme(View view) {
        if (themeCheckDialog == null) {
            View themeView = View.inflate(this, R.layout.dialog_select_theme, null);
            checkBoxes[0] = (CheckBox) themeView.findViewById(R.id.redCheck);
            checkBoxes[1] = (CheckBox) themeView.findViewById(R.id.blueCheck);
            checkBoxes[2] = (CheckBox) themeView.findViewById(R.id.greenSeaCheck);
            changeCheck();
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("选择主题")
                    .setView(themeView)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            changeTheme();
                        }
                    });
            themeCheckDialog = builder.create();
        };
        int theme = SharedPreferencesUtil.getTheme();
        checkBoxes[theme].setChecked(true);
        themeCheckDialog.show();
    }

    private void changeTheme() {
        hasChangeTheme = true;
        int themeColor[] = {R.color.colorPrimary, R.color.blue, R.color.greenSea};
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()) {
                getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(themeColor[i]));
                binding.relativeLayout.setBackgroundResource(themeColor[i]);
                SharedPreferencesUtil.setTheme(this,i);
            }
        }

    }

    private void changeCheck() {
        for (int i = 0; i < checkBoxes.length; i++) {
            final int finalI = i;
            checkBoxes[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        clearCheck(finalI);
                    }
                }
            });
        }
    }

    private void clearCheck(int j) {
        for (int i = 0; i < checkBoxes.length; i++) {
            if (j != i) {
                checkBoxes[i].setChecked(false);
            }
        }
    }

    public void loginOut(View view) {
        SharedPreferences sp = getSharedPreferences(SharedPreferencesUtil.tag, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("token");
        editor.remove("refreshtoken");
        editor.commit();

        User user = SingleUser.getInstance();
        user.removeSelf();

        Toast.makeText(this, "已退出登录", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        if(hasChangeTheme){
            setResult(RESULT_OK);
        }
        super.finish();
    }
}
