package com.quizapp;

public class UserSession {
    private int id;           // user ID from database
    private String username;
    private String role;

    public UserSession(int id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
