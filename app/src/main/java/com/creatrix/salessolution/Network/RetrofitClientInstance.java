package com.creatrix.salessolution.Network;

import com.creatrix.salessolution.Setting.AppSetting;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClientInstance {

    private static final String BASE_URL = AppSetting.newBASE_URL;
   // private static final String BASE_URL = AppSetting.testingBASE_URL;

    private static Retrofit retrofit = null;
    /*   OkHttpClient client= new OkHttpClient()
               .writeTimeoutMillis(TimeUnit.MINUTES)*/
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            // 🟡 1. Create the logging interceptor
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC); // Changed to BASIC to prevent OOM

            // 🟡 2. Attach it to the OkHttpClient
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .build();

            // 🟡 3. Build the Retrofit instance with the client
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return retrofit;
    }
}