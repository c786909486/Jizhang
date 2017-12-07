package com.example.ckz.jizhang.db;

import org.litepal.crud.DataSupport;

/**
 * Created by CKZ on 2017/11/30.
 */

public class BillLocalBean extends DataSupport {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BillLocalBean localBean = (BillLocalBean) o;

        if (userId != null ? !userId.equals(localBean.userId) : localBean.userId != null)
            return false;
        return billId != null ? billId.equals(localBean.billId) : localBean.billId == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (billId != null ? billId.hashCode() : 0);
        return result;
    }

    //用户
    private String userId;
    //日期
    private String date;
    //类型
    private String type;
    //金额
    private float money;
    //介绍
    private String text;
    //是否已同步
    private boolean isUp;
    //id
    private String billId;

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public boolean isUpload() {
        return isUp;
    }

    public void setIsUpload(boolean up) {
        isUp = up;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
