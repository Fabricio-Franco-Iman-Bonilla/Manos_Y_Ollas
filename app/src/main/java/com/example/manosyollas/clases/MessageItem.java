package com.example.manosyollas.clases;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ChatMessages")
public class MessageItem {
    @PrimaryKey
    @NonNull
    private String messageId;

    private int forumId;
    private String content;
    private String userId;
    private boolean isPending;
    private String timestamp;
    private String userProfileImage;
    private String userName;  // Nuevo campo para el nombre del usuario

    public MessageItem(String messageId, Integer forumId, String content, String userId, String timestamp, String userProfileImage, String userName) {
        this.messageId = messageId;
        this.forumId = forumId;
        this.content = content;
        this.userId = userId;
        this.timestamp = timestamp;
        this.userProfileImage = userProfileImage;
        this.userName = userName;
    }

    public String getMessageId() {
        return messageId;
    }

    public Integer getForumId() {
        return forumId;
    }

    public String getContent() {
        return content;
    }

    public String getUserId() {
        return userId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public String getUserName() {
        return userName;
    }


    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }
}

