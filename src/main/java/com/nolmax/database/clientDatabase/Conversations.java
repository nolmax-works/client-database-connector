package com.nolmax.database.clientDatabase;

public class Conversations {
    private Long id;
    private int type; // 0: private, 1: group
    private String name;
    private String avatarUrl;
    private Long lastMessageId;

    //com.nolmax.database.clientDatabase.Conversations
    public Conversations(Long id, int type, String name, String avatarUrl, Long lastMessageId) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.lastMessageId = lastMessageId;
    }

    // Getters and Setters...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Long getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(Long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }
}
