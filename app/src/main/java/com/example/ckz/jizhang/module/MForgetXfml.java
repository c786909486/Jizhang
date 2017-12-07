package com.example.ckz.jizhang.module;

import android.content.Context;

import com.example.ckz.jizhang.manager.UserManager;
import com.example.ckz.jizhang.user.MyUser;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by CKZ on 2017/11/29.
 */

public class MForgetXfml implements MForgetModule {
    private Context context;

    public MForgetXfml(Context context) {
        this.context = context;
    }

    @Override
    public void requestSmsCode(String phone, QueryListener<Integer> listener) {
        BmobSMS.requestSMSCode(phone,"记账",listener);
    }

    @Override
    public void resetPassword(String smsCode, String newPassword, UpdateListener listener) {
        MyUser.resetPasswordBySMSCode(smsCode,newPassword,listener);
    }
}
