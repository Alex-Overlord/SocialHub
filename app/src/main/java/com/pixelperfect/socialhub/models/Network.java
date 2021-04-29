package com.pixelperfect.socialhub.models;

import java.util.ArrayList;

public class Network {

    private String name, description;
    private ArrayList<User> users;

    public void addUser(User user) {
        users.add(user);
    }

    // Constructors
    public Network() {}
    public Network(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
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
    public ArrayList<User> getUsers() {
        return users;
    }
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
