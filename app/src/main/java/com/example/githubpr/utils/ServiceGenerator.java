package com.example.githubpr.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {


    private static final String BASE_URL = "https:/api.github.com/";

    private static Context mContext;

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);
    private static Dispatcher dispatcher = new Dispatcher();
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .method(original.method(), original.body())
                        .build();
                Response response = chain.proceed(request);

                //Unauthorised access. Indicates expired token.
                if (response.code() == 401) {
                    return response;
                }

                return response;
            })
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .dispatcher(dispatcher);

    private static Gson gson = new GsonBuilder()
            .create();
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson));
    private static Retrofit retrofit = builder.client(httpClient.build()).build();

    private static Context getmContext() {
        return mContext;
    }

    public static void setmContext(Context mContext) {
        ServiceGenerator.mContext = mContext;
    }

    /**
     * Function to get dispatcher.
     *
     * @return Dispatcher, helps to cancel calls when fragment ends.
     */
    public static Dispatcher getDispatcher() {
        return dispatcher;
    }


    /* package */
    static Retrofit getRetrofit() {
        return retrofit;
    }

    public static <S> S createService(Class<S> retrofitAPI) {
        return retrofit.create(retrofitAPI);
    }

}
