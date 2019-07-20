package com.tevoi.tevoi.model;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.rest.ApiInterface;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomApp extends Application {
    private ApiInterface apiService;
    private InternetConnectionListener mInternetConnectionListener;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setInternetConnectionListener(InternetConnectionListener listener) {
        mInternetConnectionListener = listener;
    }

    public void removeInternetConnectionListener() {
        mInternetConnectionListener = null;
    }

    public ApiInterface getApiService() {
        if (apiService == null) {
            apiService = provideRetrofit(Global.BASE_URL).create(ApiInterface.class);
        }
        return apiService;
    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private Retrofit provideRetrofit(String url)
    {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    private OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        okhttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);

        okhttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                        Global.UserToken).addHeader("Content-Language", Global.DefaultUILanguage)
                        .addHeader("License", "TevoiMobileApp");

                Request newRequest = builder.build();
                okhttp3.Response response = chain.proceed(newRequest);

                // todo deal with the issues the way you need to

                return response;

                //return chain.proceed(newRequest);
            }
        });

        okhttpClientBuilder.addInterceptor(new NetworkConnectionInterceptor() {
            @Override
            public boolean isInternetAvailable() {
                return CustomApp.this.isInternetAvailable();
            }

            @Override
            public void onInternetUnavailable() {
                if (mInternetConnectionListener != null) {
                    mInternetConnectionListener.onInternetUnavailable();
                }
            }
        });

        return okhttpClientBuilder.build();
    }
}