package com.pixelperfect.socialhub.models;

public class Message {
    private String id, idSender, date, text, type;

    public Message() {}
    public Message(String id, String idSender, String date, String text, String type) {
        this.id = id;
        this.idSender = idSender;
        this.date = date;
        this.text = text;
        this.type = type;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
