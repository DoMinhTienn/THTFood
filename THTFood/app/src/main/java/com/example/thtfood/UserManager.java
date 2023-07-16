package com.example.thtfood;

public class UserManager {
    private static UserManager instance;
    private User user;

    private UserManager() {

    }

    public static  synchronized UserManager getInstance(){
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public User getUser(){
        return user;
    }
    public void setUser(User user){
        this.user = user;
    }
}
