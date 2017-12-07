package com.example.ckz.jizhang.presenter;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.example.ckz.jizhang.bean.BillNetBean;
import com.example.ckz.jizhang.callback.OnDataCallback;
import com.example.ckz.jizhang.db.BillLocalBean;
import com.example.ckz.jizhang.manager.DataManager;
import com.example.ckz.jizhang.module.MRecordModule;
import com.example.ckz.jizhang.module.MRecordXfml;
import com.example.ckz.jizhang.user.MyUser;
import com.example.ckz.jizhang.util.LogUtils;
import com.example.ckz.jizhang.util.SPUtils;
import com.example.ckz.jizhang.view.mvpview.MRecordView;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by CKZ on 2017/12/3.
 */

public class MRecordPresenter {

    private String TAG = getClass().getSimpleName();

    private Context context;

    private MRecordView mView;

    private MRecordModule mModle;

    public MRecordPresenter(Context context, MRecordView mView) {
        this.context = context;
        this.mView = mView;
        mModle = new MRecordXfml(context);
    }

    /**
     * 获取本地数据
     */
    public void getLocalData(){
        if (mModle!=null){
            mModle.getLocalData(new DataManager.OnLocalGetListener() {
                @Override
                public void onSuccess(List<BillLocalBean> data) {
                    showLocalData(data);
                }
            });
        }
    }

    /**
     * 获取网络数据
     */
    public void getNetData(){
        if (mModle!=null){
            mModle.getNetData(new FindListener<BillNetBean>() {
                @Override
                public void done(List<BillNetBean> list, BmobException e) {
                    if (e == null){
                        showNetData(list);
                    }
                }
            });
        }
    }

    /**
     * 同步数据
     */
    public void synchronizeData(){
        if (mModle!=null){
           if (SPUtils.getBooleanSp(context,"need_sync" ) && MyUser.getCurrentUser()!=null){
               LogUtils.d(TAG,"需要同步");
               mModle.synchronizeData(new DataManager.OnGetCurrenListener() {
                   @Override
                   public void onDownloadSuccess(int type, int position, int total) {
                       LogUtils.d(TAG,type+"一个"+total+"个");
                       if (position<total-1){
                           showDialog(type,position,total);
//                           callback.onSuccess();
                       }else {
                           SPUtils.putBooleanSp(context,"need_sync",false);

                           hideDialog();
                       }
                   }

                   @Override
                   public void onUploadSuccess(int type, int position, int total) {
                       LogUtils.d(TAG,type+"total");
                       if (position<total-1){
                           showDialog(type,position,total);
                       }else {
                           hideDialog();
                       }
                   }

                   @Override
                   public void onFaild(int type,BmobException e) {
                       if (e.getErrorCode() != 101){
                           if (type == DataManager.DOWNLOAD_TYPE){
                               Toast.makeText(context,"下载数据失败",Toast.LENGTH_SHORT).show();
                               LogUtils.d(TAG,e.toString());
                           }else {
                               Toast.makeText(context,"上传数据失败",Toast.LENGTH_SHORT).show();
                               LogUtils.d(TAG,e.toString());
                           }
                           SPUtils.putBooleanSp(context,"need_sync",true);
                       }
                   }
               }, new DataManager.OnUpdateListener() {
                   @Override
                   public void onSuccess() {

                   }

                   @Override
                   public void onFaild(BmobException e) {
                       Toast.makeText(context,"上传数据失败",Toast.LENGTH_SHORT).show();
                       LogUtils.d(TAG,e.toString());
                   }
               });
           }else {
               LogUtils.d(TAG,"不需要同步");
           }
        }
    }

    /**
     * 添加billData
     * @param billId
     * @param type
     * @param text
     * @param date
     * @param money
     */
    public void addBill( String billId,  String type,  String text,  String date,  float money){
        if (mModle!=null){
            mModle.addBill(billId, type, text, date, money, new DataManager.OnUpdateListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFaild(BmobException e) {
                    Toast.makeText(context,"上传数据失败，请检查网络",Toast.LENGTH_SHORT).show();
                    LogUtils.d(TAG,e.toString());
                }
            }, new DataManager.OnDataCallback() {
                @Override
                public void onCallback(BillLocalBean localBean) {
                    addBillData(localBean);
                }
            });
        }
    }


    private void showLocalData(List<BillLocalBean> data){
        if (mView!=null){
            mView.showLocalData(data);
        }
    }

    private void showNetData(List<BillNetBean> data){
        if (mView!=null){
            mView.showNetData(data);
        }
    }

    private void showDialog(int type,int position,int total){
        if (mView!=null){
            mView.showDialog(type,position,total);
        }
    }

    private void hideDialog(){
        if (mView!=null){
            mView.hideDialog();
        }
    }

    private void addBillData(BillLocalBean data){
        if (mView!=null){
            mView.addBillData(data);
        }
    }


}
