package com.example.ckz.jizhang.bean;

import com.example.ckz.jizhang.user.MyUser;

import cn.bmob.v3.BmobObject;

/**
 * Created by CKZ on 2017/12/1.
 */

public class BillNetBean extends BmobObject {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BillNetBean netBean = (BillNetBean) o;

        if (user != null ? !user.equals(netBean.user) : netBean.user != null) return false;
        return billId != null ? billId.equals(netBean.billId) : netBean.billId == null;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (billId != null ? billId.hashCode() : 0);
        return result;
    }

    //用户
    private MyUser user;
    //日期
    private String date;

    private String month;
    //类型
    private String type;
    //金额
    private float money;
    //介绍
    private String text;
    //id
    private String billId;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
