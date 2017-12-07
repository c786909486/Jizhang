package com.example.ckz.jizhang.view.mvpview;

import com.example.ckz.jizhang.bean.BillNetBean;

import java.util.List;

/**
 * Created by CKZ on 2017/12/6.
 */

public interface MBillView {

    void showDialog();
    void hideDialog();
    void showUI(float txPay, float txGet, float onlinePay, float onlineGet,
                float marketPay, float marketGet, float otherPay, float otherGet, float sum, List<BillNetBean> list);

    void showNoData();

    void hideNoData();

    void hideUI();

    void showMonthPopup();

    void hideMonthPopup();

    void hideLogin();

    void showLogin();
}
