package com.creatrix.salessolution.Network;

import com.creatrix.salessolution.Model.SafeNumberTypeAdapter;
import com.creatrix.salessolution.Setting.AppSetting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstanceTP {
    private static final String BASE_URL = AppSetting.TPBASE_URL;
    private static Retrofit retrofit = null;
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Create a custom Gson instance
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Number.class, new SafeNumberTypeAdapter())
                    .create();

            OkHttpClient clientz = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(10, TimeUnit.MINUTES)
                    .readTimeout(10, TimeUnit.MINUTES)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson)) // Use custom Gson instance
                    .client(clientz)
                    .build();
        }
        return retrofit;
    }
}