package com.example.garbagecollectionpoints;

public class User {
    private int id;
    private String
            name,
            email,
            password,
            role;
    private boolean isAdmin;
    private boolean isLogged;

    public User(String role, boolean isAdmin) {
        this.role = role;
        this.isAdmin = isAdmin;
    }

    public User() {
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
