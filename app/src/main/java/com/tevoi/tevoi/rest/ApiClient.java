package com.tevoi.tevoi.rest;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tevoi.tevoi.Utils.Global;
import com.tevoi.tevoi.model.InternetConnectionListener;
import com.tevoi.tevoi.model.NetworkConnectionInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient
{
    private static InternetConnectionListener mInternetConnectionListener;

    public static void setmInternetConnectionListener(InternetConnectionListener mInternetConnectionListener) {
        mInternetConnectionListener = mInternetConnectionListener;
    }

    private static Retrofit retrofit = null;

    public static Retrofit getClient(InternetConnectionListener listener)
    {
        if(listener != null)
            mInternetConnectionListener = listener;
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(new NetworkConnectionInterceptor()
                {
                    @Override
                    public boolean isInternetAvailable()
                    {
                        if(mInternetConnectionListener!= null)
                            return mInternetConnectionListener.isInternetAvailable();
                        else
                            return true;
                    }

                    @Override
                    public void onInternetUnavailable()
                    {
                        // we can broadcast this event to activity/fragment/service
                        // through LocalBroadcastReceiver or
                        // RxBus/EventBus
                        // also we can call our own interface method
                        // like this.
                        if(mInternetConnectionListener!= null)
                            mInternetConnectionListener.onInternetUnavailable();
                    }
                })
                //.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                                Global.UserToken).addHeader("Content-Language", Global.UserUILanguage)
                                .addHeader("License", "TevoiMobileApp");

                        Request newRequest = builder.build();
                        okhttp3.Response response = chain.proceed(newRequest);

                        // todo deal with the issues the way you need to
                        if (response.code() == 500)
                        {
                            /*startActivity(
                                    new Intent(
                                            ErrorHandlingActivity.this,
                                            ServerIsBrokenActivity.class
                                    )
                            );*/

                            return response;
                        }

                        return response;

                        //return chain.proceed(newRequest);
                    }
                })
                /*.addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        okhttp3.Response response = chain.proceed(request);

                        // todo deal with the issues the way you need to
                        if (response.code() == 500) {
                            startActivity(
                                    new Intent(
                                            ErrorHandlingActivity.this,
                                            ServerIsBrokenActivity.class
                                    )
                            );

                            return response;
                        }

                        return response;
                    }
                })*/
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        /*OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();*/
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Global.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /*public boolean isInternetAvailableV() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }*/
}

