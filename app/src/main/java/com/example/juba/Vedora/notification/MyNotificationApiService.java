package com.example.juba.chatmessenger.notification;


import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static com.example.juba.chatmessenger.notification.NConstants.SERVER_Key;

public interface MyNotificationApiService {

    @Headers(

            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAABU3jGHs:APA91bFQMm2NU4mIhhDdpKlNICR-1YqoVuHLfCVLWhZtvogB7AdGSq2HGqUozzWnkBwYE_yGjkOhN-6-Ax9IPDQZBEoxfy3V08vf52NS9tIJ2ZkuAh-WvEKdxxJ-dZoJuDxcr0jBVEoC"


            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body MyNotificationSender sender);
}
