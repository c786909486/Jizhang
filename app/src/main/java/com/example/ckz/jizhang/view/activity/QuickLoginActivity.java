package com.example.ckz.jizhang.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ckz.jizhang.R;
import com.example.ckz.jizhang.manager.UserManager;
import com.example.ckz.jizhang.presenter.MLoginPresenter;
import com.example.ckz.jizhang.view.mvpview.MLoginView;
import com.example.ckz.jizhang.view.view.ClearEditText;
import com.example.ckz.jizhang.view.view.ProgressDialog;
import com.example.ckz.jizhang.view.view.SmsButton;

import cn.bmob.v3.exception.BmobException;

public class QuickLoginActivity extends AppCompatActivity implements View.OnClickListener,MLoginView{

    private ImageView mBack;
    private ClearEditText mPhone;
    private ClearEditText mSms;
    private SmsButton mSmsBtn;
    private Button mLogin;
    private ProgressDialog dialog;
    private MLoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_login);
        initView();
        mPresenter = new MLoginPresenter(this,this);
    }

    private void initView() {
        mLogin = (Button) findViewById(R.id.login_btn);
        mSmsBtn = (SmsButton) findViewById(R.id.sms_btm);
        mSms = (ClearEditText) findViewById(R.id.input_sms);
        mPhone = (ClearEditText) findViewById(R.id.input_phone);
        mBack = (ImageView) findViewById(R.id.back_btn);
        dialog = new ProgressDialog(this);
        mBack.setOnClickListener(this);
        mSmsBtn.setOnClickListener(this);
        mLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.sms_btm:
                //获取验证码
                mPresenter.requestSms(mPhone.getText().toString(), new UserManager.OnSmsGetListener() {
                    @Override
                    public void onGetSuccess(int smsCode) {
                       mSmsBtn.start();
                    }

                    @Override
                    public void onFailed(BmobException e) {
                        Toast.makeText(QuickLoginActivity.this, R.string.get_faild,Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.login_btn:
                //快速登陆
                mPresenter.quickLogin(mPhone.getText().toString(),mSms.getText().toString());
                break;
        }
    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void finishActivity() {

    }

    @Override
    public void upLoadLocal() {

    }
}
