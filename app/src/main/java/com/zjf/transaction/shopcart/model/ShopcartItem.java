package com.zjf.transaction.shopcart.model;

import com.zjf.transaction.main.model.Commodity;
import com.zjf.transaction.user.model.UserInfo;

/**
 * Created by zhengjiafeng on 2019/3/28
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class ShopcartItem {
    public UserInfo userInfo;
    public Commodity commodity;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Commodity getCommodity() {
        return commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
    }
}
