package com.example.ckz.jizhang.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.ckz.jizhang.R;
import com.example.ckz.jizhang.manager.UserManager;
import com.example.ckz.jizhang.util.StatusBarUtil;
import com.example.ckz.jizhang.view.fragment.BillFragment;
import com.example.ckz.jizhang.view.fragment.MineFragment;
import com.example.ckz.jizhang.view.fragment.RecordFragment;
import com.zhy.changeskin.base.BaseSkinActivity;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class RecondActivity extends AppCompatActivity implements View.OnClickListener{

    private FrameLayout mFragment;
    private TextView mRecord;
    private TextView mMine;
    private TextView mBill;

    private FragmentManager mManager;

//    fragment
    private RecordFragment mRecordFragment;
    private BillFragment mBillFragment;
    private MineFragment mMineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recond);
        initView();
        StatusBarUtil.setTransparentForImageView(this,mFragment);
        mManager = getSupportFragmentManager();
        setDefaultFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (BmobUser.getCurrentUser()!=null){
            UserManager.getInstance().fetchUserInfo();
        }
    }

    private void initView() {
        mMine = (TextView) findViewById(R.id.mine_btn);
        mRecord = (TextView) findViewById(R.id.record_btn);
        mBill = ((TextView) findViewById(R.id.bill_btn));
        mFragment = (FrameLayout) findViewById(R.id.center_contain);
        mMine.setOnClickListener(this);
        mRecord.setOnClickListener(this);
        mBill.setOnClickListener(this);
    }

    private void hideAll(FragmentTransaction transaction){
        if (mRecordFragment!=null) transaction.hide(mRecordFragment);
        if (mBillFragment!=null) transaction.hide(mBillFragment);
        if (mMineFragment!=null) transaction.hide(mMineFragment);
    }

    private void resetSelect(){
        mRecord.setSelected(false);
        mMine.setSelected(false);
        mBill.setSelected(false);
    }

    private void setDefaultFragment(){
        mRecord.performClick();
    }


    @Override
    public void onClick(View view) {

        FragmentTransaction transaction = mManager.beginTransaction();
        resetSelect();
        hideAll(transaction);
        switch (view.getId()){
            case R.id.record_btn:
                mRecord.setSelected(true);
                if (mRecordFragment == null){
                    mRecordFragment = new RecordFragment();
                    transaction.add(R.id.center_contain,mRecordFragment);
                }else {
                    transaction.show(mRecordFragment);
                }
                break;
            case R.id.bill_btn:
                mBill.setSelected(true);
                if (mBillFragment == null){
                    mBillFragment = new BillFragment();
                    transaction.add(R.id.center_contain,mBillFragment);
                }else {
                    transaction.show(mBillFragment);
                }
                break;
            case R.id.mine_btn:
                mMine.setSelected(true);
                if (mMineFragment == null){
                    mMineFragment = new MineFragment();
                    transaction.add(R.id.center_contain,mMineFragment);
                }else {
                    transaction.show(mMineFragment);
                }

                break;
        }
        transaction.commit();
    }
}
