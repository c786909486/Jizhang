package com.example.ckz.jizhang.presenter;

import android.content.Context;
import android.widget.Toast;

import com.example.ckz.jizhang.bean.BillNetBean;
import com.example.ckz.jizhang.callback.OnTypeDataListener;
import com.example.ckz.jizhang.manager.MonthBillManager;
import com.example.ckz.jizhang.module.MBillModuel;
import com.example.ckz.jizhang.module.MBillXfml;
import com.example.ckz.jizhang.user.MyUser;
import com.example.ckz.jizhang.view.mvpview.MBillView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by CKZ on 2017/12/6.
 */

public class MBillPresenter {
    private static final String TAG = "MBillPresenter";
    private static final int TYPE_HIED = 0;
    private static final int TYPE_SHOW = 1;
    private int current = TYPE_HIED;
    MBillView mView;
    MBillModuel mBillModuel;
    Context context;
    private List<BillNetBean> returnData ;

    public MBillPresenter(MBillView mView, Context context) {
        this.mView = mView;
        this.context = context;
        mBillModuel = new MBillXfml(context);
        returnData = new ArrayList<>();
    }

    /**
     * 获取月账单
     * @param month
     */
    public void getMonthData(String month){
       if (MyUser.getCurrentUser()!=null){
           hideLogin();
           showDialog();
           mBillModuel.getMonthBill(month, new MonthBillManager.OnCountListener() {
               @Override
               public void getCount(float txPay, float txGet, float onlinePay, float onlineGet, float marketPay,
                                    float marketGet, float otherPay, float otherGet, float sum, List<BillNetBean> list) {
                   hideDialog();
                   if (list.size() == 0){
                       showNoData();
                       hideUI();
                   }else {
                       showUI( txPay,txGet,onlinePay,onlineGet,marketPay,marketGet,otherPay,otherGet,sum,list);
                       returnData.clear();
                       returnData.addAll(list);
                       hideNoData();
                   }
               }

               @Override
               public void onFaild(BmobException e) {
                   hideDialog();
                   Toast.makeText(context,"获取数据失败",Toast.LENGTH_SHORT).show();
               }
           });
       }else {
           showLogin();
           hideUI();
       }
    }

    public void getTypeData(String type, OnTypeDataListener listener){
        if (type.equals("通讯支出")) listener.onDataGet(getTxPay());
        if (type.equals("通讯收入")) listener.onDataGet(getTxGet());
        if (type.equals("线上支出")) listener.onDataGet(getOnlinePay());
        if (type.equals("线上收入")) listener.onDataGet(getOnlineGet());
        if (type.equals("线下支出")) listener.onDataGet(getMarketPay());
        if (type.equals("线下收入")) listener.onDataGet(getMarketGet());
        if (type.equals("其他支出")) listener.onDataGet(getOtherPay());
        if (type.equals("其他收入")) listener.onDataGet(getOtherGet());

    }
    private List<BillNetBean> getTxPay(){
        List<BillNetBean> beans = new ArrayList<>();
        beans.clear();
        for (BillNetBean netBean:returnData){
            if (netBean.getType().equals("通讯") && netBean.getMoney()<0){
                beans.add(netBean);
            }
        }
        return beans;
    }

    private List<BillNetBean> getTxGet(){
        List<BillNetBean> beans = new ArrayList<>();
        beans.clear();
        for (BillNetBean netBean:returnData){
            if (netBean.getType().equals("通讯") && netBean.getMoney()>0){
                beans.add(netBean);
            }
        }
        return beans;
    }

    private List<BillNetBean> getOnlinePay(){
        List<BillNetBean> beans = new ArrayList<>();
        beans.clear();
        for (BillNetBean netBean:returnData){
            if (netBean.getType().equals("线上") && netBean.getMoney()<0){
                beans.add(netBean);
            }
        }
        return beans;
    }

    private List<BillNetBean> getOnlineGet(){
        List<BillNetBean> beans = new ArrayList<>();
        beans.clear();
        for (BillNetBean netBean:returnData){
            if (netBean.getType().equals("线上") && netBean.getMoney()>0){
                beans.add(netBean);
            }
        }
        return beans;
    }

    private List<BillNetBean> getMarketPay(){
        List<BillNetBean> beans = new ArrayList<>();
        beans.clear();
        for (BillNetBean netBean:returnData){
            if (netBean.getType().equals("线下") && netBean.getMoney()<0){
                beans.add(netBean);
            }
        }
        return beans;
    }

    private List<BillNetBean> getMarketGet(){
        List<BillNetBean> beans = new ArrayList<>();
        beans.clear();
        for (BillNetBean netBean:returnData){
            if (netBean.getType().equals("线下") && netBean.getMoney()>0){
                beans.add(netBean);
            }
        }
        return beans;
    }

    private List<BillNetBean> getOtherPay(){
        List<BillNetBean> beans = new ArrayList<>();
        beans.clear();
        for (BillNetBean netBean:returnData){
            if (netBean.getType().equals("其他") && netBean.getMoney()<0){
                beans.add(netBean);
            }
        }
        return beans;
    }

    private List<BillNetBean> getOtherGet(){
        List<BillNetBean> beans = new ArrayList<>();
        beans.clear();
        for (BillNetBean netBean:returnData){
            if (netBean.getType().equals("其他") && netBean.getMoney()>0){
                beans.add(netBean);
            }
        }
        return beans;
    }

    private void showLogin() {
        if (mView!=null){
            mView.showLogin();
        }
    }

    private void hideLogin() {
        if (mView!=null){
            mView.hideLogin();
        }
    }

    public void showMonthPopup(){
        if (current == TYPE_HIED){
            showPopup();
            current = TYPE_SHOW;
        }else {
            hidePopup();
            current = TYPE_HIED;
        }
    }

    private void hidePopup() {
        if (mView!=null){
            mView.hideMonthPopup();
        }
    }

    private void showPopup() {
        if (mView!=null){
            mView.showMonthPopup();
        }
    }


    private void hideNoData() {
        if (mView!=null){
            mView.hideNoData();
        }
    }

    private void hideUI() {
        if (mView!=null){
            mView.hideUI();
        }
    }

    private void showNoData() {
        if (mView!=null){
            mView.showNoData();
        }
    }

    private void showDialog(){
        if (mView!=null){
            mView.showDialog();
        }
    }
    private void hideDialog(){
        if (mView!=null){
            mView.hideDialog();
        }
    }
    private void showUI(float txPay, float txGet, float onlinePay, float onlineGet,
                        float marketPay, float marketGet, float otherPay, float otherGet, float sum, List<BillNetBean> list){
        if (mView!=null){
            mView.showUI( txPay,   txGet,   onlinePay,   onlineGet,
             marketPay,   marketGet,   otherPay,   otherGet,  sum,list);
        }
    }
}
