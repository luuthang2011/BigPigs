package com.bigpigs;

import android.content.Context;


import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class NetworkUtils {

    OkHttpClient okHttpClient;

    public static String sendGetRequest(String url) throws Exception
    {
        String result="null";
        Request newsRequest = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okhttp3.Response newsResponse = okHttpClient.newCall(newsRequest).execute();
        if(newsResponse.isSuccessful()) result = newsResponse.body().string();
        return result;
    }
}
