package com.example.SpotMe.Matches;

public class MatchObject {



    private String userId;
    private String name;
    private String profileImageUrl;


    public MatchObject(String userId,String name,String profileImageUrl) {
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }


    public String getUserId(){return userId;}

    public String getName(){
        return name;
    }

    public String getProfileImageUrl(){
        return profileImageUrl;
    }

}
