package com.example.ckz.jizhang.module;

import com.example.ckz.jizhang.bean.BillNetBean;
import com.example.ckz.jizhang.manager.DataManager;

import cn.bmob.v3.listener.FindListener;

/**
 * Created by CKZ on 2017/12/2.
 */

public interface MRecordModule {
    //获取本地数据
    void getLocalData(DataManager.OnLocalGetListener listener);
    //收取网络数据
    void getNetData(FindListener<BillNetBean> listener);

    //同步数据
    void synchronizeData(DataManager.OnGetCurrenListener listener, DataManager.OnUpdateListener listener1);

    //添加bill
    void addBill(String billId, String type, String text, String date, float money, DataManager.OnUpdateListener listener,DataManager.OnDataCallback callback);
}
