package com.example.ckz.jizhang.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ckz.jizhang.R;
import com.example.ckz.jizhang.manager.DataManager;
import com.example.ckz.jizhang.manager.UserManager;
import com.example.ckz.jizhang.presenter.MLoginPresenter;
import com.example.ckz.jizhang.user.MyUser;
import com.example.ckz.jizhang.util.StatusBarUtil;
import com.example.ckz.jizhang.view.mvpview.MLoginView;
import com.example.ckz.jizhang.view.view.ClearEditText;
import com.example.ckz.jizhang.view.view.ProgressDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,MLoginView{

    private ImageView mBack;
    private ClearEditText mPhoneNum;
    private ClearEditText mPassword;
    private TextView mForget;
    private TextView mSign;
    private Button mLogin;
    private Button mSimple;

    private MLoginPresenter mPresenter;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        StatusBarUtil.setTransparent(this);
        mPresenter = new MLoginPresenter(this,this);
        dialog = new ProgressDialog(this,R.style.progressDialog);
    }

    private void initView() {
        mBack = ((ImageView) findViewById(R.id.back_btn));
        mPhoneNum = ((ClearEditText) findViewById(R.id.input_phone));
        mPassword = ((ClearEditText) findViewById(R.id.input_password));
        mForget = ((TextView) findViewById(R.id.forget_password));
        mSign = ((TextView) findViewById(R.id.sign_by_phone));
        mLogin = ((Button) findViewById(R.id.login_btn));
        mSimple = ((Button) findViewById(R.id.simple_login));
        mBack.setOnClickListener(this);
        mForget.setOnClickListener(this);
        mSign.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mSimple.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.forget_password:
                //忘记密码
                startActivity(new Intent(this,ForgetPasswordActivity.class));
                break;
            case R.id.sign_by_phone:
                //手机号注册

                break;
            case R.id.login_btn:
                //登陆
               mPresenter.login(mPhoneNum.getText().toString(),mPassword.getText().toString());
                break;
            case R.id.simple_login:
                //一键登陆或注册
                startActivity(new Intent(this,QuickLoginActivity.class));
                finish();
                break;

        }
    }


    @Override
    public void showProgressDialog() {
        dialog.show();
    }

    @Override
    public void hideProgressDialog() {
        dialog.dismiss();
    }

    @Override
    public void finishActivity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    @Override
    public void upLoadLocal() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataManager.getInstance(LoginActivity.this).upDateNoLoginData();
            }
        }).start();
    }
}

