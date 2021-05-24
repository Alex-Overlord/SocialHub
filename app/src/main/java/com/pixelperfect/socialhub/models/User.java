package com.pixelperfect.socialhub.models;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String id, fullName, email;
    private Map<String, Network> networks;

    // Constructors
    public User() {}
    public User(String id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.networks = new HashMap<>();
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
    public Map<String, Network> getNetworks() {
        return networks;
    }
    public void setNetworks(Map<String, Network> networks) {
        this.networks = networks;
    }
}
