package com.team_red.melody.models;



public class User {

    private  String userName;
    private  int ID;

    public User(String userName, int ID) {
        this.userName = userName;
        this.ID = ID;
    }

    public User(String userName) {
        this.userName = userName;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
