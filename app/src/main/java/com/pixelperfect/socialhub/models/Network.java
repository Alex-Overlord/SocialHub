package com.pixelperfect.socialhub.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Network {

    private String id, name, description;
    private Map<String, User> users;
    private Map<String, User> admins;
    private Map<String, Message> messages;

    // Constructors
    public Network() {}
    public Network(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.users = new HashMap<>();
        this.admins = new HashMap<>();
        this.messages = new HashMap<>();
    }

    // Getters and Setters
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Map<String, User> getUsers() {
        return users;
    }
    public void setUsers(Map<String, User> users) {
        this.users = users;
    }
    public Map<String, User> getAdmins() {
        return admins;
    }
    public void setAdmins(Map<String, User> admins) {
        this.admins = admins;
    }
    public Map<String, Message> getMessages() {
        return messages;
    }
    public void setMessages(Map<String, Message> messages) {
        this.messages = messages;
    }

    // Others methods
    public void addUser(String key, User user) {
        users.put(key, user);
    }
    public void addAdmin(String key, User user) {
        admins.put(key, user);
    }
    public boolean isMember(User user){
        return this.users.containsKey(user.getId());
    }
}