package com.nolmax.database.clientDatabase;

import java.time.LocalDateTime;

public class Participant {
    private Long conversationId;
    private Long userId;
    private int role; // 0: member, 1: admin
    private LocalDateTime joined_at;
    private LocalDateTime left_at;
    private Long lastReadMessageId;

    //Constructor
    public Participant(Long conversationId, Long userId, int role, LocalDateTime joined_at, LocalDateTime left_at, Long lastReadMessageId) {
        this.conversationId = conversationId;
        this.userId = userId;
        this.role = role;
        this.joined_at = joined_at;
        this.left_at = left_at;
        this.lastReadMessageId = lastReadMessageId;
    }

    //Getter and Setter

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Long getLastReadMessageId() {
        return lastReadMessageId;
    }

    public void setLastReadMessageId(Long lastReadMessageId) {
        this.lastReadMessageId = lastReadMessageId;
    }

    public LocalDateTime getJoined_at() {
        return joined_at;
    }

    public void setJoined_at(LocalDateTime joined_at) {
        this.joined_at = joined_at;
    }

    public LocalDateTime getLeft_at() {
        return left_at;
    }

    public void setLeft_at(LocalDateTime left_at) {
        this.left_at = left_at;
    }
}
