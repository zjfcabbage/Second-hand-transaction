package com.zjf.transaction.pages.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 订单信息查询时，后台返回的对象
 */
public class OrderInfo {
    @SerializedName("orderId")
    private String orderId;
    @SerializedName("userId")
    private String userId;
    @SerializedName("contentList")
    private List<Content> contentList;
    @SerializedName("timestamp")
    private long orderTime;
    @SerializedName("money")
    private String orderMoney;

    public static class Content {
        @SerializedName("userId")
        private String userId;
        @SerializedName("userName")
        private String userName;
        @SerializedName("userPicUrl")
        private String userPicUrl;
        @SerializedName("commodityId")
        private String commodityId;
        @SerializedName("imageUrl")
        private String commodityUrl;
        @SerializedName("msg")
        private String commodityMsg;
        @SerializedName("price")
        private String price;

        public Content(String userId, String userName, String userPicUrl, String commodityId,
                       String commodityUrl, String commodityMsg, String price) {
            this.userId = userId;
            this.userName = userName;
            this.userPicUrl = userPicUrl;
            this.commodityId = commodityId;
            this.commodityUrl = commodityUrl;
            this.commodityMsg = commodityMsg;
            this.price = price;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPicUrl() {
            return userPicUrl;
        }

        public void setUserPicUrl(String userPicUrl) {
            this.userPicUrl = userPicUrl;
        }


        public String getCommodityUrl() {
            return commodityUrl;
        }

        public void setCommodityUrl(String commodityUrl) {
            this.commodityUrl = commodityUrl;
        }

        public String getCommodityMsg() {
            return commodityMsg;
        }

        public void setCommodityMsg(String commodityMsg) {
            this.commodityMsg = commodityMsg;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "Content{" +
                    "userId='" + userId + '\'' +
                    ", userName='" + userName + '\'' +
                    ", userPicUrl='" + userPicUrl + '\'' +
                    ", commodityId='" + commodityId + '\'' +
                    ", commodityUrl='" + commodityUrl + '\'' +
                    ", commodityMsg='" + commodityMsg + '\'' +
                    ", price='" + price + '\'' +
                    '}';
        }
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Content> getContentList() {
        return contentList;
    }

    public void setContentList(List<Content> contentList) {
        this.contentList = contentList;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }
}
