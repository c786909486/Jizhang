package com.example.ckz.jizhang.module;

import com.example.ckz.jizhang.manager.MonthBillManager;

/**
 * Created by CKZ on 2017/12/6.
 */

public interface MBillModuel {

    void getMonthBill(String month, MonthBillManager.OnCountListener listener);
}
