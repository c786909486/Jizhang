package com.example.ckz.jizhang.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ckz.jizhang.R;
import com.example.ckz.jizhang.manager.UserManager;
import com.example.ckz.jizhang.presenter.MForgetPresenter;
import com.example.ckz.jizhang.util.StatusBarUtil;
import com.example.ckz.jizhang.view.mvpview.MForgetView;
import com.example.ckz.jizhang.view.view.ClearEditText;
import com.example.ckz.jizhang.view.view.ProgressDialog;
import com.example.ckz.jizhang.view.view.SmsButton;

import cn.bmob.v3.exception.BmobException;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener,MForgetView{

    private ImageView mBack;
    private ClearEditText mPhone;
    private ClearEditText mSms;
    private SmsButton mSmsBtn;
    private ClearEditText mPassword;
    private ClearEditText mAgain;
    private Button mApply;
    private ProgressDialog dialog;

    private MForgetPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initView();
        StatusBarUtil.setTransparent(this);
    }

    private void initView() {
        mBack = ((ImageView) findViewById(R.id.back_btn));
        mPhone = ((ClearEditText) findViewById(R.id.input_phone));
        mSms = ((ClearEditText) findViewById(R.id.input_sms));
        mSmsBtn = ((SmsButton) findViewById(R.id.sms_btn));
        mPassword = ((ClearEditText) findViewById(R.id.input_password));
        mAgain = ((ClearEditText) findViewById(R.id.input_again));
        mApply = ((Button) findViewById(R.id.apply_btn));
        dialog = new ProgressDialog(this,R.style.progressDialog);
        presenter = new MForgetPresenter(this,this);

        mBack.setOnClickListener(this);
        mSmsBtn.setOnClickListener(this);
        mApply.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.sms_btn:
                if (mSmsBtn.getCurrent() == SmsButton.CLICKABLE){
                    presenter.requestSms(mPhone.getText().toString(), new UserManager.OnSmsGetListener() {
                        @Override
                        public void onGetSuccess(int smsCode) {
                            mSmsBtn.start();
                        }

                        @Override
                        public void onFailed(BmobException e) {

                        }
                    });
                }
                break;
            case R.id.apply_btn:
                presenter.resetPassword(mSms.getText().toString(),mPassword.getText().toString(),mAgain.getText().toString());
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
        finish();
    }
}
