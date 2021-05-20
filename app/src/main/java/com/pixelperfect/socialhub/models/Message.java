package com.pixelperfect.socialhub.models;

import java.util.Date;

public class Message {
    private String idSender, date, content, type;

    public Message() {}
    public Message(String idSender, String date, String content, String type) {
        this.idSender = idSender;
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
