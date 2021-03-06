package com.zhuang.badfilm.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zhuang.badfilm.R;
import com.zhuang.badfilm.ZActivity;
import com.zhuang.badfilm.network.RetrofitHelper;
import com.zhuang.badfilm.network.service.UserService;
import com.zhuang.badfilm.utils.PictureUtil;
import com.zhuang.badfilm.utils.SharedPreferencesUtil;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvatarPreviewActivity extends ZActivity {

    private SimpleDraweeView imageView;
    private String mCurrentPhotoPath;//图片path

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        init();
    }

    private void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mCurrentPhotoPath = getIntent().getStringExtra("mCurrentPhotoPath");
        File file = new File(mCurrentPhotoPath);
        imageView = (SimpleDraweeView) findViewById(R.id.imageView);
        imageView.setImageURI(Uri.fromFile(file));
    }

    public void submitPhoto(View view) {
        final String compressFilePath = PictureUtil.compressBitmap(getApplicationContext(), mCurrentPhotoPath);
        File file = new File(compressFilePath);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("fileUp", "android.jpg", RequestBody.create(MediaType.parse("image/jpeg"), file));
        MultipartBody requestBody = builder.build();

        UserService userService = RetrofitHelper.createServiceWidthToken(UserService.class);
        Call<Map> call = userService.saveAvatar(requestBody);
        call.enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {
                PictureUtil.deleteTempFile(compressFilePath);
                Map map = response.body();
                if (map.get("avatar") != null) {
                    SingleUser.getInstance().setAvatar(map.get("avatar").toString());
                    SharedPreferencesUtil.setAvatar(map.get("avatar").toString());
                }
            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
        //结束该activity，返回
        setResult(RESULT_OK);
        finish();
    }

    public void cancel(View view){
        finish();
    }
}
