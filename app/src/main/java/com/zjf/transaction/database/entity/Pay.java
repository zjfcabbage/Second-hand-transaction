package com.zjf.transaction.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Pay {
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    private String userId;
    @ColumnInfo(name = "commodity_id")
    private String commodityId;
    @ColumnInfo(name = "timestamp")
    private long timestamp;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
