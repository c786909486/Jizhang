package com.example.ckz.jizhang.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.ckz.jizhang.R;
import com.example.ckz.jizhang.manager.UserManager;
import com.example.ckz.jizhang.module.MForgetModule;
import com.example.ckz.jizhang.module.MForgetXfml;
import com.example.ckz.jizhang.util.LogUtils;
import com.example.ckz.jizhang.view.mvpview.MForgetView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Observable;
import rx.Observer;


/**
 * Created by CKZ on 2017/11/29.
 */

public class MForgetPresenter {
    MForgetView mView;
    MForgetModule mModule;
    Context context;

    public MForgetPresenter(MForgetView mView, Context context) {
        this.mView = mView;
        this.context = context;
        mModule = new MForgetXfml(context);
    }

    /**
     * 申请验证码
     * @param phone
     */
    public void requestSms(String phone, final UserManager.OnSmsGetListener listener){
        if (TextUtils.isEmpty(phone)){
            Toast.makeText(context, R.string.phone_empty,Toast.LENGTH_SHORT).show();
        }else if(phone.length()!=11){
            Toast.makeText(context,context.getResources().getString(R.string.below_11),Toast.LENGTH_SHORT).show();
        }else {
            if (mModule!=null){
                mModule.requestSmsCode(phone, new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e == null){
                            listener.onGetSuccess(integer);
                            LogUtils.d(getClass().getSimpleName(),integer+"");
                        }else {
                            listener.onFailed(e);
                            LogUtils.d(getClass().getSimpleName(),e.toString());
                        }
                    }
                });
            }
        }
    }

    public void resetPassword(String sms,String newPassword,String againPassword){
        if (TextUtils.isEmpty(sms) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(againPassword)){
            Toast.makeText(context, R.string.sms_not_empty,Toast.LENGTH_SHORT).show();
        }else if (newPassword.length()<9){
            Toast.makeText(context, R.string.password_length,Toast.LENGTH_SHORT).show();
        }else if (!newPassword.equals(againPassword)){
            Toast.makeText(context, R.string.not_right,Toast.LENGTH_SHORT).show();
        }else {
            showDialog();
            if (mModule!=null){
                mModule.resetPassword(sms, newPassword, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            Observable.just(1,2).subscribe(new Observer<Integer>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable throwable) {

                                }

                                @Override
                                public void onNext(Integer integer) {
                                    switch (integer){
                                        case 1:
                                            hideDialog();
                                            break;
                                        case 2:
                                            finishActivity();
                                            break;
                                    }
                                }
                            });
                        }else {
                            hideDialog();
                        }
                    }
                });
            }
        }
    }


    private void showDialog(){
        if (mView!=null){
            mView.showProgressDialog();
        }
    }

    private void hideDialog(){
        if (mView!=null){
            mView.hideProgressDialog();
        }
    }

    private void finishActivity(){
        if (mView!=null){
            mView.finishActivity();
        }
    }
}
