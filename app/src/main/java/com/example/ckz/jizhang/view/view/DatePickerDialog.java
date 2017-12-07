package com.example.ckz.jizhang.view.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.ckz.jizhang.R;

import java.util.Calendar;

/**
 * Created by CKZ on 2017/11/29.
 */

public class DatePickerDialog extends Dialog implements View.OnClickListener,DatePicker.OnDateChangedListener{
    private DatePicker datepicker;
    private TextView canclebtn;
    private TextView applybtn;
    private Context context;
    private String date = "";

    public interface OnDateClickListener{
        void onApply(String date);
        void onCancle();
    }
    private OnDateClickListener listener;

    public void setOnDateClickListener(OnDateClickListener listener){
        this.listener = listener;
    }

    public DatePickerDialog(@NonNull Context context) {
        this(context, R.style.progressDialog);
    }

    public DatePickerDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        setContentView(R.layout.layout_date_dialog);
        setCanceledOnTouchOutside(false);// 点击Dialog外部消失
        initView();
    }

    private void initView() {
        applybtn = (TextView) findViewById(R.id.apply_btn);
        canclebtn = (TextView) findViewById(R.id.cancle_btn);
        datepicker = (DatePicker) findViewById(R.id.date_picker);
        applybtn.setOnClickListener(this);
        canclebtn.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int monthOfYear=calendar.get(Calendar.MONTH);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
        datepicker.init(year,monthOfYear,dayOfMonth,this);
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int moth, int day) {
        date = year+"-"+moth+"-"+day;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.apply_btn:
                //确定
                if (listener!=null){
                    listener.onApply(date);
                }
                break;
            case R.id.cancle_btn:
                //取消
                if (listener!=null){
                    listener.onCancle();

                }
                break;
        }
    }
}
