package com.example.juba.chatmessenger.notification;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.juba.chatmessenger.notification.NConstants.BASE_URL;

public class MyClient {

    public static Retrofit retrofit = null;

    public static Retrofit getClient() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }
}
