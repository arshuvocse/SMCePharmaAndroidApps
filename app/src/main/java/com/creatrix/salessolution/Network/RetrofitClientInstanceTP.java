package com.creatrix.salessolution.Network;

import com.creatrix.salessolution.Model.SafeNumberTypeAdapter;
import com.creatrix.salessolution.Setting.AppSetting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstanceTP {
    private static final String BASE_URL = AppSetting.TPBASE_URL;
    private static Retrofit retrofit = null;
    private static OkHttpClient client = null;

    private static synchronized OkHttpClient getOkHttpClient() {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))
                    .retryOnConnectionFailure(true)
                    .build();
        }
        return client;
    }

    public static synchronized Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Create a custom Gson instance
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Number.class, new SafeNumberTypeAdapter())
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson)) // Use custom Gson instance
                    .client(getOkHttpClient())
                    .build();
        }
        return retrofit;
    }
}