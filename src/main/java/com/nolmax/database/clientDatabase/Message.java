package com.nolmax.database.clientDatabase;

import java.time.LocalDateTime;

public class Message {
    private Long id;
    private Long conversationid;
    private Long senderid;
    private String content;
    private LocalDateTime sentAt;

    //Constructor
    public Message(Long id, Long conversationid, Long senderid, String content, LocalDateTime sentAt) {
        this.id = id;
        this.conversationid = conversationid;
        this.senderid = senderid;
        this.content = content;
        this.sentAt = sentAt;
    }

    //Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getConversationid() {
        return conversationid;
    }

    public void setConversationid(Long conversationid) {
        this.conversationid = conversationid;
    }


    public Long getSenderid() {
        return senderid;
    }

    public void setSenderid(Long senderid) {
        this.senderid = senderid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }


}
