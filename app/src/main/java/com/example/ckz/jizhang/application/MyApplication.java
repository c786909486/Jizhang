package com.example.ckz.jizhang.application;

import com.example.ckz.jizhang.api.KeyCenter;
import com.zhy.changeskin.SkinManager;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import cn.bmob.v3.Bmob;
import skin.support.SkinCompatManager;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;


/**
 * Created by CKZ on 2017/11/24.
 */

public class MyApplication extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        initSkinManager();
        initBmob();
    }

    private void initBmob() {
        Bmob.initialize(getApplicationContext(),KeyCenter.BMOB_KEY);
    }


    private void initSkinManager(){
        SkinCompatManager.withoutActivity(this)                         // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();
    }


}
