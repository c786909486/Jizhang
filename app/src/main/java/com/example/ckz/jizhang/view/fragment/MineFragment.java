package com.example.ckz.jizhang.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ckz.jizhang.R;
import com.example.ckz.jizhang.manager.UserManager;
import com.example.ckz.jizhang.user.MyUser;
import com.example.ckz.jizhang.util.LogUtils;
import com.example.ckz.jizhang.util.SPUtils;
import com.example.ckz.jizhang.view.activity.LoginActivity;
import com.example.ckz.jizhang.view.activity.UserCenterActivity;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;
import skin.support.SkinCompatManager;

/**
 * Created by CKZ on 2017/11/27.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener{
    private String TAG = getClass().getSimpleName();
    private CircleImageView mUserIcon;
    private TextView mUserName;
    private ImageView mSwitchBtn;
    private UserManager manager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine,container,false);
        manager = UserManager.getInstance();
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置用户信息
        if (BmobUser.getCurrentUser()!=null){
            UserManager.getInstance().fetchUserInfo();
        }
        manager.getLoginState(new UserManager.OnUserLoginListener() {
            @Override
            public void onLogin(MyUser user) {
                if (TextUtils.isEmpty(user.getUserName())){
                    mUserName.setText(user.getMobilePhoneNumber());
                }else {
                    mUserName.setText(user.getUserName());
                }

                if (user.getUserIcon()!=null)
                Glide.with(getActivity()).load(user.getUserIcon().getUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
                        .into(mUserIcon);
            }

            @Override
            public void onLocal() {

            }
        });
    }

    private void initView(View view) {
        mSwitchBtn = (ImageView) view.findViewById(R.id.switch_btn);
        mUserName = (TextView) view.findViewById(R.id.user_name);
        mUserIcon = (CircleImageView) view.findViewById(R.id.user_icon);
        mUserIcon.setOnClickListener(this);
        mSwitchBtn.setOnClickListener(this);
        mUserName.setOnClickListener(this);
        setSelected();
    }

    private void setSelected() {
        //夜间切换
        if (SPUtils.getBooleanSp(getContext(),"IS_DAY")){
            mSwitchBtn.setSelected(false);
        }else {
            mSwitchBtn.setSelected(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.user_icon:
                //未登录跳转登陆，登陆跳转用户中心
                if (manager.isLogin()){
                    startActivity(new Intent(getContext(), UserCenterActivity.class));
                    LogUtils.d(TAG,"已登录");
                }else {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    LogUtils.d(TAG,"未登录");
                }
                break;
            case R.id.user_name:
                mUserIcon.performClick();
                break;
            case R.id.switch_btn:
                //夜间切换
                if (SPUtils.getBooleanSp(getContext(),"IS_DAY")){
//                    SkinManager.getInstance().changeSkin("night");
                    SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // 后缀加载
                    mSwitchBtn.setSelected(true);
                    SPUtils.putBooleanSp(getContext(),"IS_DAY",false);
                }else {
//                    SkinManager.getInstance().removeAnySkin();
                    SkinCompatManager.getInstance().restoreDefaultTheme();
                    mSwitchBtn.setSelected(false);
                    SPUtils.putBooleanSp(getContext(),"IS_DAY",true);
                }
                break;

        }
    }
}
