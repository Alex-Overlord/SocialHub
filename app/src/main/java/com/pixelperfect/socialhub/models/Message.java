package com.pixelperfect.socialhub.models;

import java.util.Date;

public class Message {
    private String idSender, content, type;
    private Date date;

    public Message() {}
    public Message(String idSender, Date date, String content, String type) {
        this.idSender = idSender;
        this.content = content;
        this.type = type;
    }

    public String getIdSender() {
        return idSender;
    }
    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
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
