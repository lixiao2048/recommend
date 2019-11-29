package com.bingo.qa.model;

import lombok.Data;

import java.util.Date;

/**
 */
@Data
public class Message {
    private int id;
    private int fromId;
    private int toId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    private String content;
    private Date createdDate;
    private int hasRead;
    private String conversationId;

    /**
     * 保证无论在 from 方还是 to 方，conversation_id 都是一致的
     *
     * @return str
     */
    public String getConversationId() {
        if (fromId < toId) {
            return String.format("%d_%d", fromId, toId);
        }
        return String.format("%d_%d", toId, fromId);
    }

}
