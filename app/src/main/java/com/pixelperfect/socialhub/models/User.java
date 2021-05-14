package com.pixelperfect.socialhub.models;

import java.util.UUID;

public class User {

    private String id, fullName, email;

    // Constructors
    public User() {}
    public User(String id, String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
        this.id = id;
    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
