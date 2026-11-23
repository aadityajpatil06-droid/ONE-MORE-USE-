package com.onemoreuse.dto;

public class AuthResponse {

    private boolean success;
    private String message;
    private Long id;
    private String name;
    private String email;
    private String location;
    private String token;

    public AuthResponse() {
    }

    public static AuthResponse failure(String message) {
        AuthResponse res = new AuthResponse();
        res.success = false;
        res.message = message;
        return res;
    }

    public static AuthResponse success(Long id, String name, String email, String location, String token) {
        AuthResponse res = new AuthResponse();
        res.success = true;
        res.message = "Authentication successful";
        res.id = id;
        res.name = name;
        res.email = email;
        res.location = location;
        res.token = token;
        return res;
    }

    // Getters and Setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
