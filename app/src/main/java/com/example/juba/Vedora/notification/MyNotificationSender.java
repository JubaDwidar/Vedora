package com.example.juba.chatmessenger.notification;

public class MyNotificationSender {
    private Data data;
    private String to;

    public MyNotificationSender() {
    }

    public MyNotificationSender(Data data, String to) {
        this.data = data;
        this.to = to;
    }
}
