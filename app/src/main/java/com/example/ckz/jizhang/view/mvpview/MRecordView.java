package com.example.ckz.jizhang.view.mvpview;

import com.example.ckz.jizhang.bean.BillNetBean;
import com.example.ckz.jizhang.db.BillLocalBean;

import java.util.List;

/**
 * Created by CKZ on 2017/12/2.
 */

public interface MRecordView {

    void showLocalData(List<BillLocalBean> localBean);

    void showNetData(List<BillNetBean> netBean);

    void showDialog(int type,int position,int total);

    void hideDialog();

    void addBillData(BillLocalBean data);

}
