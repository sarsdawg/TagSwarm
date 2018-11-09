package com.eriksarson.tagswarm;

// This is a singleton pattern to be able to access User across all activities/
public class User {

    private static User user = null;
    private String userName;

    protected User() {}

    public static synchronized User getInstance() {
        if (user == null){
            user = new User();
        }
        return user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
