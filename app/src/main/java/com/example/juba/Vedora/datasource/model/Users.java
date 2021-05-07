package com.example.juba.chatmessenger.datasource.model;

public class Users {

    private String name;
    private String image;
    private String status;
    private String id;
    private String tokens;

    public Users() {
    }

    public Users(String name, String image, String status, String id,String tokens) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.id = id;
        this.tokens = tokens;
    }

    public String getTokens() {
        return tokens;
    }

    public void setTokens(String tokens) {
        this.tokens = tokens;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
