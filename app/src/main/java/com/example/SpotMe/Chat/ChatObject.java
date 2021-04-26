package com.example.SpotMe.Chat;

public class ChatObject {
    private String message;
    private Boolean CurrentUser;


    public ChatObject( String message,Boolean CurrentUser) {

        this.message = message;
        this.CurrentUser = CurrentUser;


    }

    public String getMessage(){
        return message;
    }
    public Boolean getCurrentUser(){
        return CurrentUser;
    }








}
