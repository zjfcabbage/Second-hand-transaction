package com.zjf.transaction.pages.model;

/**
 *用户展示的数据类型
 */
public class OrderShow {
    //根据不同的type判断不同的view类型
    private transient int type;
    private String orderId;
    private long orderTime;
    private String orderMoney;  //订单总金额
    private String userName;
    private String userPicUrl;
    private String commodityUrl;
    private String commodityMsg;
    private String price;   //每件商品的价格

    public OrderShow(int type, String orderId, String orderMoney) {
        this.type = type;
        this.orderId = orderId;
        this.orderMoney = orderMoney;
    }

    public OrderShow(int type, String orderId, String userName, String userPicUrl, String commodityUrl, String commodityMsg, String price) {
        this.type = type;
        this.orderId = orderId;
        this.userName = userName;
        this.userPicUrl = userPicUrl;
        this.commodityUrl = commodityUrl;
        this.commodityMsg = commodityMsg;
        this.price = price;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
}
