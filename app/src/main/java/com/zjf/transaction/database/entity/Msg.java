package com.zjf.transaction.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

public class Msg {
    @ColumnInfo(name = "from_id")
    private String fromId;
    @ColumnInfo(name = "to_id")
    private String toId;
    @ColumnInfo(name = "message")
    private String message;
    @ColumnInfo(name = "timestamp")
    private long timestamp;

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
