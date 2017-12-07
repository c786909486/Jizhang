package com.example.ckz.jizhang.module;

import android.content.Context;

import com.example.ckz.jizhang.bean.BillNetBean;
import com.example.ckz.jizhang.db.BillLocalBean;
import com.example.ckz.jizhang.manager.DataManager;
import com.example.ckz.jizhang.user.MyUser;
import com.example.ckz.jizhang.util.LogUtils;
import com.example.ckz.jizhang.util.SPUtils;

import java.util.concurrent.TimeUnit;

import cn.bmob.v3.listener.FindListener;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;


/**
 * Created by CKZ on 2017/12/3.
 */

public class MRecordXfml implements MRecordModule {

    private Context context;
    private DataManager manager;
    public MRecordXfml(Context context) {
        this.context = context;
        manager = DataManager.getInstance(context);
    }

    @Override
    public void getLocalData(DataManager.OnLocalGetListener listener) {
        manager.getLocalData(listener);
    }


    @Override
    public void getNetData(FindListener<BillNetBean> listener) {
        manager.getAllNetData(listener);
    }

    @Override
    public void synchronizeData(final DataManager.OnGetCurrenListener listener, final DataManager.OnUpdateListener listener1) {


                manager.updateAfterLogin(listener1,listener);

                manager.synchronizeData(listener);


    }


    private BillLocalBean localBean;

    @Override
    public void addBill(final String billId, final String type, final String text, final String date, final float money,
                        final DataManager.OnUpdateListener listener, final DataManager.OnDataCallback callback) {

        Observable.interval(0,1, TimeUnit.SECONDS).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(Long aLong) {
                if (aLong == 0){
                    localBean = manager.addLocalData(billId,type,text,date,money);
                    callback.onCallback(localBean);
                }else if (aLong == 1){
                    if (MyUser.getCurrentUser()!=null){
                        manager.localToNet(localBean,listener);
                    }
                }

            }
        });
    }
}
