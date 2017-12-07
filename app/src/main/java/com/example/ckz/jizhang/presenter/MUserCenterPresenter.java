package com.example.ckz.jizhang.presenter;

import com.example.ckz.jizhang.manager.UserManager;
import com.example.ckz.jizhang.user.MyUser;
import com.example.ckz.jizhang.util.LogUtils;
import com.example.ckz.jizhang.view.mvpview.MUserCenterView;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by CKZ on 2017/11/30.
 */

public class MUserCenterPresenter {

    MUserCenterView mView;

    public MUserCenterPresenter(MUserCenterView mView) {
        this.mView = mView;
    }

    public void saveUserSex(String sex){
        UserManager.getInstance().saveSex(sex, new UserManager.OnUpdateListener() {
            @Override
            public void onSuccess() {
                showSuccess();
            }

            @Override
            public void onFaild(BmobException e) {
                showFaild();
            }
        });
    }

    public void saveBirth(String birthday){
        UserManager.getInstance().saveBirthday(birthday, new UserManager.OnUpdateListener() {
            @Override
            public void onSuccess() {
                showSuccess();
            }

            @Override
            public void onFaild(BmobException e) {
                showFaild();
            }
        });
    }

    public void saveName(String name){
        UserManager.getInstance().saveName(name, new UserManager.OnUpdateListener() {
            @Override
            public void onSuccess() {
                showSuccess();
            }

            @Override
            public void onFaild(BmobException e) {
               showFaild();
            }
        });
    }

    public void saveIcon(File file){
        final MyUser userModule = new MyUser();
        final BmobFile iconFile = new BmobFile(file);
        iconFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    userModule.setUserIcon(iconFile);
                    userModule.update(MyUser.getCurrentUser().getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                showSuccess();
                            }else {
                                LogUtils.d(getClass().getSimpleName(),e.toString());
                                showFaild();
                            }
                        }
                    });
                }
            }
        });
    }


    private void showSuccess(){
        if (mView!=null){
            mView.showSuccess();
        }
    }

    private void showFaild(){
        if (mView!=null){
            mView.showFaild();
        }
    }
}
