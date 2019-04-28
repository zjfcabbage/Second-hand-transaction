package com.zjf.transaction.shopcart.model;

import com.zjf.transaction.main.model.Commodity;
import com.zjf.transaction.user.model.User;

/**
 * Created by zhengjiafeng on 2019/3/28
 *
 * @author 郑佳锋 zhengjiafeng@bytedance.com
 */
public class ShopcartItem {
    private boolean isChecked;
    public User user;
    public Commodity commodity;

    public ShopcartItem() {
    }

    public ShopcartItem(User user, Commodity commodity) {
        this.user = user;
        this.commodity = commodity;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Commodity getCommodity() {
        return commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
    }
}
