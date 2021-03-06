package com.zhuang.badfilm.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.zhuang.badfilm.network.service.LoginService;
import com.zhuang.badfilm.utils.SharedPreferencesUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhuang on 2016/12/30.
 */

public class RetrofitHelper {

    public static final String API_BASE_URL = "https://www.liguanjian.com/";
    private final static Gson gson = new Gson();
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());
    private static Context mContext;

    public static void initialize(Context context) {
        mContext = context.getApplicationContext();
    }

    public static <T> T createService(Class<T> serviceClass) {
        return createService(serviceClass, null);
    }

    public static <T> T createServiceWidthToken(Class<T> serviceClass) {
        return createService(serviceClass, SharedPreferencesUtil.getToken());
    }

    public static <T> T createService(Class<T> serviceClass, final String authToken) {
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        if (authToken != null) {
            httpBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    request = request
                            .newBuilder()
                            .header("token", authToken)
                            .build();
                    Response response = chain.proceed(request);
                    String json = response.body().string();
                    BaseReturnMsg baseReturnMsg = gson.fromJson(json, BaseReturnMsg.class);
                    int code = baseReturnMsg.getCode();
                    //token已过期(refresh未过期)
                    if (code == -23) {
                        getNewToken();
                        Request newRequest = chain.request();
                        newRequest = newRequest
                                .newBuilder()
                                .header("token", SharedPreferencesUtil.getToken())
                                .build();
                        return chain.proceed(newRequest);
                    } else {
                        //上面的response已经被关闭了，重新生成一个
                        response = response.newBuilder()
                                .body(ResponseBody.create(null, json))
                                .build();
                        return response;
                    }
                }
            });
        }
        Retrofit retrofit = builder.client(httpBuilder.build()).build();
        return retrofit.create(serviceClass);
    }

    /**
     * 同步请求方式，获取最新的Token
     *
     * @return
     */
    private static String getNewToken() throws IOException {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        LoginService service = builder.build().create(LoginService.class);
        Call<TokenMsg> call = service.refreshToken(SharedPreferencesUtil.getRefreshToken());
        retrofit2.Response<TokenMsg> response = call.execute();
        String token = response.body().getToken();
        String refreshToken = response.body().getRefreshtoken();
        SharedPreferencesUtil.setToken(token);
        SharedPreferencesUtil.setRefreshToken(refreshToken);
        return null;
    }
}
