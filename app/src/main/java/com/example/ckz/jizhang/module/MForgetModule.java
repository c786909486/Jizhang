package com.example.ckz.jizhang.module;

import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by CKZ on 2017/11/29.
 */

public interface MForgetModule {

    void requestSmsCode(String phone, QueryListener<Integer> listener);

    void resetPassword(String smsCode, String newPassword, UpdateListener listener);
}
