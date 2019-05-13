package com.zjf.transaction.database.entity;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Msg {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("fromId")
    @ColumnInfo(name = "from_id")
    private String fromId;
    @SerializedName("toId")
    @ColumnInfo(name = "to_id")
    private String toId;
    @SerializedName("message")
    @ColumnInfo(name = "message")
    private String message;
    @SerializedName("timestamp")
    @ColumnInfo(name = "timestamp")
    private long timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return "Msg{" +
                "id=" + id +
                ", fromId='" + fromId + '\'' +
                ", toId='" + toId + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
