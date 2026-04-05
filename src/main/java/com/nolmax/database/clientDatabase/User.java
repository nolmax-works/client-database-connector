package com.nolmax.database.clientDatabase;

public class User {
    private Long id;
    private String username;
    private String avatarurl;

    //Constructor
    public User(Long id, String username, String avatarurl) {
        this.id = id;
        this.username = username;
        this.avatarurl = avatarurl;
    }

    //Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarurl() {
        return avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }


}
