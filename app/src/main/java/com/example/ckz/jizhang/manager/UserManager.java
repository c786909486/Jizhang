package com.example.ckz.jizhang.manager;

import com.example.ckz.jizhang.user.MyUser;

import java.io.File;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by CKZ on 2017/11/28.
 */

public class UserManager {

    private static UserManager manager;

    private UserManager() {
    }

    public static UserManager getInstance(){
        if (manager == null){
            synchronized (UserManager.class){
                if (manager == null){
                    manager = new UserManager();
                }
            }
        }
        return manager;
    }

    /**
     * @function 手机号密码登陆
     * @param phoneNum
     * @param password
     * @param listener
     */
    public void loginByPassword(String phoneNum, String password, LogInListener<MyUser> listener){
        MyUser.loginByAccount(phoneNum,password,listener);
    }

    /**
     * @function 一键注册
     * @param phoneNum
     * @param code
     * @param listener
     */
    public void simpleLoginOrSign(String phoneNum,String code,LogInListener<MyUser> listener){
        MyUser.signOrLoginByMobilePhone(phoneNum,code,listener);
    }

    /**
     * @function 请求验证码
     * @param phoneNum
     * @param listener
     */
    public void requestSms(String phoneNum, final OnSmsGetListener listener){
        BmobSMS.requestSMSCode(phoneNum, "记账", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null){
                    listener.onGetSuccess(integer);
                }else {
                    listener.onFailed(e);
                }
            }
        });
    }


    /**
     * @function 获取登陆数据
     * @param listener
     */
    public void getLoginState(OnUserLoginListener listener){
        if (MyUser.getCurrentUser()!=null){
            listener.onLogin(MyUser.getCurrentUser(MyUser.class));
        }else {
            listener.onLocal();
        }
    }

    /**
     * @function 判断是否为登陆
     * @return
     */
    public boolean isLogin(){
        return MyUser.getCurrentUser() != null;
    }

    /**
     * @function 保存生日
     * @param birthday
     * @param listener
     */
    public void saveBirthday(String birthday, final OnUpdateListener listener){
        MyUser user = new MyUser();
        user.setUserBirthday(birthday);
        user.update(MyUser.getCurrentUser().getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    listener.onSuccess();
                }else {
                    listener.onFaild(e);
                }
            }
        });
    }

    /**
     * @function 保存性别
     * @param sex
     * @param listener
     */
    public void saveSex(String sex, final OnUpdateListener listener){
        MyUser user = new MyUser();
        user.setUserSex(sex);
        user.update(MyUser.getCurrentUser().getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    listener.onSuccess();
                }else {
                    listener.onFaild(e);
                }
            }
        });
    }

    public void saveName(String name, final OnUpdateListener listener){
        MyUser user = new MyUser();
        user.setUserName(name);
        user.update(MyUser.getCurrentUser().getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    listener.onSuccess();
                }else {
                    listener.onFaild(e);
                }
            }
        });
    }

    public void fetchUserInfo() {
        BmobUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {

                } else {

                }
            }
        });
    }
    public interface OnUpdateListener{
        void onSuccess();
        void onFaild(BmobException e);
    }

    public interface OnUserLoginListener{
        void onLogin(MyUser user);
        void onLocal();
    }

    public interface OnLoginStateListener{
        void onSuccess();
        void onFailed(BmobException e);
    }

    public interface OnSmsGetListener{

        void onGetSuccess(int smsCode);

        void onFailed(BmobException e);
    }


}
