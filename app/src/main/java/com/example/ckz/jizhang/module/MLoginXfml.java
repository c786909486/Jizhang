package com.example.ckz.jizhang.module;

import android.content.Context;

import com.example.ckz.jizhang.manager.UserManager;
import com.example.ckz.jizhang.user.MyUser;
import com.example.ckz.jizhang.util.LogUtils;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by CKZ on 2017/11/28.
 */

public class MLoginXfml implements MLoginModule {

    private Context context;

    public MLoginXfml(Context context) {
        this.context = context;
    }

    @Override
    public void login(String phoneNum, String password, final UserManager.OnLoginStateListener listener) {
        UserManager.getInstance().loginByPassword(phoneNum, password, new LogInListener<MyUser>() {
            @Override
            public void done(MyUser user, BmobException e) {
                if (e == null){
                    listener.onSuccess();
                }else {
                    LogUtils.d(getClass().getSimpleName(),e.toString());
                    listener.onFailed(e);
                }
            }
        });
    }

    @Override
    public void requestSmsCode(String phone, QueryListener<Integer> listener) {
        BmobSMS.requestSMSCode(phone,"记账",listener);
    }

    @Override
    public void quickLogin(String phone, String sms, final UserManager.OnLoginStateListener listener) {
        UserManager.getInstance().simpleLoginOrSign(phone, sms, new LogInListener<MyUser>() {
            @Override
            public void done(MyUser user, BmobException e) {
                if (user!=null){
                    listener.onSuccess();
                }else {
                    listener.onFailed(e);
                    LogUtils.d(getClass().getSimpleName(),e.toString());
                }
            }
        });
    }
}
