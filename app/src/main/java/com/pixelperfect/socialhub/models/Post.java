package com.pixelperfect.socialhub.models;

import java.util.Map;

public class Post {
    private String idSender, username, date, content, type;

    public Post() {}
    public Post(String idSender, String username, String date, String content, String type) {
        this.idSender = idSender;
        this.username = username;
        this.date = date;
        this.content = content;
        this.type = type;
    }

    public String getIdSender() {
        return idSender;
    }
    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String text) {
        this.content = text;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
