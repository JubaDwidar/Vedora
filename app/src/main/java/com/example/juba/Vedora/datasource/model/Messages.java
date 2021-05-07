package com.example.juba.chatmessenger.datasource.model;

public class Messages {

    private String message;
    private String MessageType;
    private String senderId;
    private String receiverId;

    public Messages() {
    }

    public Messages(String messageType, String message, String senderId) {
        this.message = message;
        MessageType = messageType;
        this.senderId = senderId;
    }




    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return MessageType;
    }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
}
