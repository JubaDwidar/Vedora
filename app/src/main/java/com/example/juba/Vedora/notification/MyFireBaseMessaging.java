package com.example.juba.chatmessenger.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.juba.chatmessenger.R;
import com.example.juba.chatmessenger.ui.main.MainActivity;
import com.example.juba.chatmessenger.ui.main.users.UsersFragment;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyFireBaseMessaging extends FirebaseMessagingService {
    public static String TAG = "MyFireBase";
    String title, message;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
title = remoteMessage.getData().get("Title");
        message = remoteMessage.getData().get("Message");
        String CHANNEL_NAME = "MESSAGE";
        String CHANNEL_ID = "MESSAGE";
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            managerCompat.createNotificationChannel(channel);

        }


        Intent intent=new Intent(this,MainActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);


        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_chat2)
                .setContentText(message)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .build();
        managerCompat.notify(getRandomNumber(), notification);

    }

    private static int getRandomNumber() {

        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("mmssSS");
        String s = ft.format(date);
        return Integer.parseInt(s);

    }

}