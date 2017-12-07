package com.example.ckz.jizhang.manager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ckz.jizhang.R;
import com.example.ckz.jizhang.view.view.ClearEditText;
import com.example.vuandroidadsdk.showpop.ShowPopup;

import java.util.Calendar;

/**
 * Created by CKZ on 2017/12/3.
 */

public class ShowAddPop implements View.OnClickListener,DatePickerDialog.OnDateSetListener,TextWatcher{

    private ShowPopup popup;
    private Context context;

    private TextView mPhone,mOnline,mMarket,mOther;
    private ClearEditText mText;
    private TextView mDate;
    private ImageView mDateBtn;
    private TextView mPay,mGet;
    private ClearEditText mMoney;
    private ImageView mApply;

    private String type;


    public String getType() {
        return type;
    }

    public String getText() {
        return mText.getText().toString();
    }

    public float getMoney() {
        return Float.valueOf(mMoney.getText().toString())*pay;
    }
    public String getDate(){
        return mDate.getText().toString();
    }

    private int pay;

    private int year,month,day;
    private DatePickerDialog dialog;

    public ShowAddPop(Context context) {
        this.context = context;
        popup = ShowPopup.getInstance(context);
    }



    public ShowAddPop createView(View viewParent){
      View containView =  popup.createLayoutPopupWindow(R.layout.pop_add_bill).enable(true).setDismissClick(R.id.close_pop)
              .setClick(R.id.add_check, new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      if (mApply.isSelected()){
                          listener.onClick(view);
                          popup.closePopupWindow();
                      }
                  }
              })
              .getView();
      init(containView);
      popup.atBottom(viewParent).defaultAnim();
        getSystemDate();
      dialog = new DatePickerDialog(context,DatePickerDialog.THEME_HOLO_LIGHT,this,year,month,day);
      mPhone.performClick();
      mPay.performClick();
      return this;
    }

    private void init(View view) {
        mPhone = view.findViewById(R.id.type_phone);
        mOnline = view.findViewById(R.id.type_online);
        mMarket = view.findViewById(R.id.type_market);
        mOther = view.findViewById(R.id.type_other);
        mText = view.findViewById(R.id.input_bill_text);
        mDate = view.findViewById(R.id.bill_date);
        mDateBtn = view.findViewById(R.id.calendar_btn);
        mPay = view.findViewById(R.id.type_pay);
        mGet = view.findViewById(R.id.type_get);
        mMoney = view.findViewById(R.id.input_money);
        mApply = view.findViewById(R.id.add_check);
        mPhone.setOnClickListener(this);
        mOnline.setOnClickListener(this);
        mMarket.setOnClickListener(this);
        mOther.setOnClickListener(this);
        mDateBtn.setOnClickListener(this);
        mPay.setOnClickListener(this);
        mGet.setOnClickListener(this);
//        mApply.setOnClickListener(this);
        mMoney.addTextChangedListener(this);
    }

    private void reset1(){
        mPhone.setSelected(false);
        mOnline.setSelected(false);
        mMarket.setSelected(false);
        mOther.setSelected(false);
    }

    private void reset2(){
        mPay.setSelected(false);
        mGet.setSelected(false);
    }

    private void getSystemDate(){
        Calendar c = Calendar.getInstance();
//        取得系统日期:
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.type_phone:
                reset1();
                mPhone.setSelected(true);
                type = "通讯";
                break;
            case R.id.type_online:
                reset1();
                mOnline.setSelected(true);
                type = "线上";
                break;
            case R.id.type_market:
                reset1();
                mMarket.setSelected(true);
                type="线下";
                break;
            case R.id.type_other:
                reset1();
                mOther.setSelected(true);
                type = "其他";
                break;
            case R.id.type_pay:
                reset2();
                mPay.setSelected(true);
                pay=-1;
                break;
            case R.id.type_get:
                reset2();
                mGet.setSelected(true);
                pay = 1;
                break;
            case R.id.calendar_btn:
                dialog.show();
                break;
            case R.id.apply_btn:
                listener.onClick(view);
                popup.closePopupWindow();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String date = i+"-"+(i1+1)+"-"+i2;
        mDate.setText(date);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length()>0 && !TextUtils.isEmpty(mDate.getText())){
            mApply.setSelected(true);
        }else {
            mApply.setSelected(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private OnApplyClickListener listener;

    public void setOnApplyClickListener(OnApplyClickListener listener){
        this.listener = listener;
    }

    public interface OnApplyClickListener{
        void onClick(View view);
    }
}
