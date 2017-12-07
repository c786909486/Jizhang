package com.example.ckz.jizhang.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.ckz.jizhang.R;
import com.example.ckz.jizhang.manager.UserManager;
import com.example.ckz.jizhang.module.MLoginModule;
import com.example.ckz.jizhang.module.MLoginXfml;
import com.example.ckz.jizhang.user.MyUser;
import com.example.ckz.jizhang.util.LogUtils;
import com.example.ckz.jizhang.view.mvpview.MLoginView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import rx.Observable;
import rx.Observer;


/**
 * Created by CKZ on 2017/11/28.
 */

public class MLoginPresenter {
    MLoginView mView;
    MLoginModule mModule;
    Context context;

    public MLoginPresenter(Context context,MLoginView mView) {
        this.mView = mView;
        this.context = context;
        mModule = new MLoginXfml(context);
    }

    public void login(String phoneNum,String password){
        if (TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(password)){
            Toast.makeText(context, R.string.not_empty,Toast.LENGTH_SHORT).show();
        }else if (phoneNum.length()!=11){
            Toast.makeText(context, R.string.below_11,Toast.LENGTH_SHORT).show();
        }else if (password.length()<9){
            Toast.makeText(context, R.string.password_length,Toast.LENGTH_SHORT).show();
        }else {
            showDialog();
            if (mModule!=null){
                mModule.login(phoneNum, password, new UserManager.OnLoginStateListener() {
                    @Override
                    public void onSuccess() {
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
                                        upLoadLocal();
                                        break;
                                    case 2:
                                        finish();
                                        break;
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailed(BmobException e) {
                        hideDialog();
                        Toast.makeText(context,"账号或密码错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
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

    public void quickLogin(String phone,String sms){
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(sms)){
            Toast.makeText(context, R.string.sms_phone_not_empty,Toast.LENGTH_SHORT).show();
        }else if (phone.length()!=11){
            Toast.makeText(context, R.string.below_11,Toast.LENGTH_SHORT).show();
        }else {
            showDialog();
            if (mModule!=null){
                mModule.quickLogin(phone, sms, new UserManager.OnLoginStateListener() {
                    @Override
                    public void onSuccess() {
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
                                       finish();
                                       break;
                               }
                           }
                       });
                    }

                    @Override
                    public void onFailed(BmobException e) {
//                        Toast.makeText(context, R.string.input_right_sms,Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

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
    private void finish(){
        if (mView!=null){
            mView.finishActivity();
        }
    }
    private void upLoadLocal(){
        if (mView!=null){
            mView.upLoadLocal();
        }
    }

}
