package com.creatrix.salessolution.Network;
import com.creatrix.salessolution.Setting.AppSetting;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static final String BASE_URL = AppSetting.newBASE_URL;
   // private static final String BASE_URL = AppSetting.testingBASE_URL;

    private static Retrofit retrofit = null;
    /*   OkHttpClient client= new OkHttpClient()
               .writeTimeoutMillis(TimeUnit.MINUTES)*/
    public static Retrofit getRetrofitInstance() {
        OkHttpClient clientz =new OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.MINUTES)
                .writeTimeout(10,TimeUnit.MINUTES)
                .readTimeout(10,TimeUnit.MINUTES)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(clientz)
                    .build();
        }
        return retrofit;
    }
}