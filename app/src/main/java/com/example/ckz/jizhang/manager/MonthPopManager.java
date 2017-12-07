package com.example.ckz.jizhang.manager;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ckz.jizhang.R;
import com.example.ckz.jizhang.adapter.MonthAdapter;
import com.example.vuandroidadsdk.showpop.ShowPopup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by CKZ on 2017/12/7.
 */

public class MonthPopManager implements View.OnClickListener,MonthAdapter.OnItemClickListener{
    private Context context;
    private ShowPopup popup;
    private ImageView mLeft;
    private ImageView mRight;
    private TextView mYears;
    private RecyclerView mList;
    private List<String> mData;
    private MonthAdapter mAdapter;

    public MonthPopManager(Context context) {
        this.context = context;
        popup = ShowPopup.getInstance(context);
    }

    public MonthPopManager create(){
        View view = popup.createLayoutPopupWindow(R.layout.pop_month).enable(false).getView();
        initView(view);

        return this;
    }

    public MonthPopManager showPop(View viewParent){
        if (popup!=null){
            popup.dropDown(viewParent,0,0);
        }
        return this;
    }

    public void closePop(){
        if (popup!=null){
            popup.closePopupWindow();
        }
    }

    private String getYear(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return year+"";
    }

    private void initView(View view) {
        mLeft = ((ImageView) view.findViewById(R.id.arrow_left));
        mRight = ((ImageView) view.findViewById(R.id.arrow_right));
        mYears = ((TextView) view.findViewById(R.id.years_text));
        mList = ((RecyclerView) view.findViewById(R.id.month_list));
        mList.setLayoutManager(new GridLayoutManager(context,4));

        mYears.setText(getYear());
        mData = new ArrayList<>();
        initData();
        mAdapter = new MonthAdapter(context,mData);
        mList.setAdapter(mAdapter);
        mLeft.setOnClickListener(this);
        mRight.setOnClickListener(this);
        mAdapter.setListener(this);
    }

    private void initData() {
        for (int i = 1; i <= 12; i++) {
            mData.add(i+"月");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.arrow_left:
                int y = Integer.valueOf(mYears.getText().toString())-1;
                mYears.setText(y+"");
                break;
            case R.id.arrow_right:
                int y1 =Integer.valueOf(mYears.getText().toString())+1;
                mYears.setText(y1+"");
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        String s = mData.get(position);
        String month = s.substring(0,s.length()-1);
        String year = mYears.getText().toString();
        listener.getDate(year+"-"+month);
    }

    private OnDateGetListener listener;

    public void setListener(OnDateGetListener listener) {
        this.listener = listener;
    }

    public interface OnDateGetListener{
        void getDate(String date);
    }
}
