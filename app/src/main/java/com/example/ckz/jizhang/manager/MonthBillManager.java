package com.example.ckz.jizhang.manager;

import android.util.Log;

import com.example.ckz.jizhang.bean.BillNetBean;
import com.example.ckz.jizhang.user.MyUser;
import com.example.ckz.jizhang.util.LogUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by CKZ on 2017/12/5.
 */

public class MonthBillManager {
    private static final String TAG = "MonthBillManager";

    private static MonthBillManager manager;
    private float txPay = 0;
    private float txGet = 0;
    private float onlinePay = 0;
    private float onlineGet = 0;
    private float marketPay = 0;
    private float marketGet = 0;
    private float otherPay = 0;
    private float otherGet = 0;
    private float sum = 0;

    public static MonthBillManager getInstance(){
        if (manager == null){
            synchronized (MonthBillManager.class){
                if (manager == null){
                    manager = new MonthBillManager();
                }
            }
        }
        return manager;
    }
    private void clearAll(){
       txPay = 0;
       txGet = 0;
       onlinePay = 0;
       onlineGet = 0;
       marketPay = 0;
       marketGet = 0;
       otherPay = 0;
       otherGet = 0;
       sum = 0;
    }

    public void getMonthData(String date, final OnCountListener listener){
        clearAll();
        BmobQuery<BillNetBean> query = new BmobQuery<BillNetBean>();
        query.addWhereEqualTo("user",MyUser.getCurrentUser(MyUser.class))
                .addWhereEqualTo("month",date)
                .findObjects(new FindListener<BillNetBean>() {
                    @Override
                    public void done(final List<BillNetBean> list, BmobException e) {
                        if (e == null){
                            Log.d(TAG,list.size()+"");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (BillNetBean bean : list){
                                        sum = Math.abs(bean.getMoney())+sum;
                                        if (bean.getType().equals("通讯") && bean.getMoney()<0){
                                            txPay = bean.getMoney()+txPay;
                                        }else if (bean.getType().equals("通讯") && bean.getMoney()>0){
                                            txGet = bean.getMoney()+txGet;
                                        }else if (bean.getType().equals("线上") && bean.getMoney()<0){
                                            onlinePay = bean.getMoney()+onlinePay;
                                        }else if (bean.getType().equals("线上") && bean.getMoney()>0){
                                            onlineGet = bean.getMoney();
                                        }else if (bean.getType().equals("线下") && bean.getMoney()<0){
                                            marketPay = bean.getMoney();
                                        }else if (bean.getType().equals("线下") && bean.getMoney()>0){
                                            marketGet = bean.getMoney();
                                        }else if (bean.getType().equals("其他") && bean.getMoney()<0){
                                            otherPay = bean.getMoney();
                                        }else if (bean.getType().equals("其他") && bean.getMoney()>0){
                                            otherGet = bean.getMoney();
                                        }

                                    }
                                    listener.getCount(txPay,txGet,onlinePay,onlineGet,marketPay,marketGet,otherPay,otherGet,sum,list);
                                }
                            }).start();
                        }else {
                            listener.onFaild(e);
                            Log.d(TAG,e.toString());
                        }
                    }
                });
    }

    public interface OnCountListener{

        void getCount(float txPay,float txGet,float onlinePay,float onlineGet,
                      float marketPay,float marketGet,float otherPay,float otherGet,float sum,List<BillNetBean> list);
        void onFaild(BmobException e);
    }
}
