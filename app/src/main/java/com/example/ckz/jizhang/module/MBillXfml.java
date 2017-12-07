package com.example.ckz.jizhang.module;

import android.content.Context;

import com.example.ckz.jizhang.manager.MonthBillManager;

/**
 * Created by CKZ on 2017/12/6.
 */

public class MBillXfml implements MBillModuel {

    private Context context;
    public MBillXfml(Context context) {
        this.context = context;
    }

    @Override
    public void getMonthBill(String month, MonthBillManager.OnCountListener listener) {
        MonthBillManager.getInstance().getMonthData(month,listener);
    }
}
