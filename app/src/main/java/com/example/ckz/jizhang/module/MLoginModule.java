package com.example.ckz.jizhang.module;

import com.example.ckz.jizhang.manager.UserManager;

import cn.bmob.v3.listener.QueryListener;

/**
 * Created by CKZ on 2017/11/28.
 */

public interface MLoginModule {

    void login(String phoneNum, String password, UserManager.OnLoginStateListener listener);

    void requestSmsCode(String phone, QueryListener<Integer> listener);

    void quickLogin(String phone,String sms,UserManager.OnLoginStateListener loginStateListener);
}
