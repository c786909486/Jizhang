package com.example.ckz.jizhang.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ckz.jizhang.R;
import com.example.ckz.jizhang.adapter.AddBillAdapter;
import com.example.ckz.jizhang.bean.BillNetBean;
import com.example.ckz.jizhang.callback.OnTypeDataListener;
import com.example.ckz.jizhang.manager.MonthBillManager;
import com.example.ckz.jizhang.manager.MonthPopManager;
import com.example.ckz.jizhang.presenter.MBillPresenter;
import com.example.ckz.jizhang.util.LogUtils;
import com.example.ckz.jizhang.view.activity.LoginActivity;
import com.example.ckz.jizhang.view.mvpview.MBillView;
import com.example.ckz.jizhang.view.view.MyListView;
import com.example.ckz.jizhang.view.view.ProgressDialog;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.b.V;

/**
 * Created by CKZ on 2017/11/27.
 */

public class BillFragment extends BaseFragment implements MBillView,View.OnClickListener,OnChartValueSelectedListener{
    private PieChart mPieChart;
    private String TAG = getClass().getSimpleName();
    private MBillPresenter presenter;
    private TextView monthText;
    private ScrollView mScroll;
    private TextView mNoData;
    private ProgressDialog dialog;
    private ImageView expandBtn;
    private MonthPopManager pop;
    private  PieDataSet dataSet;

    private  List<PieEntry> entry = new ArrayList<>();
    private TextView loginBtn;

    private List<Object> mData;
    private AddBillAdapter mAdapter;
    private TextView typeText;
    private MyListView mList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill,container,false);
        presenter = new MBillPresenter(this,getContext());
        pop = new MonthPopManager(getContext());
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initView(View view) {
        mPieChart = ((PieChart) view.findViewById(R.id.pie_chart));
        initPieChart();
        monthText = ((TextView) view.findViewById(R.id.month_text));
        loginBtn = ((TextView) view.findViewById(R.id.login_btn));
        mScroll = ((ScrollView) view.findViewById(R.id.scroll_view));
        mNoData = ((TextView) view.findViewById(R.id.no_data));
        dialog = new ProgressDialog(getContext());
        expandBtn = ((ImageView) view.findViewById(R.id.expand_btn));
        typeText = ((TextView) view.findViewById(R.id.type_text));
        mList =  view.findViewById(R.id.detail_list);
        mData = new ArrayList<>();
        mAdapter = new AddBillAdapter(getContext(),mData);
        mList.setAdapter(mAdapter);
        expandBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        dataSet = new PieDataSet(entry,"");
        mPieChart.setOnChartValueSelectedListener(this);
    }

    private void initPieChart() {
        // 设置饼图是否接收点击事件，默认为true
        mPieChart.setTouchEnabled(true);
        //设置饼图是否使用百分比
        mPieChart.setUsePercentValues(false);
        //设置饼图右下角的文字描述
        Description des = new Description();
        des.setText("");
        mPieChart.setDescription(des);
        mPieChart.setNoDataText("暂无数据");
        //设置饼图右下角的文字大小
//        mPieChart.setDescriptionTextSize(16);
        //是否显示圆盘中间文字，默认显示
        mPieChart.setDrawCenterText(true);
        //设置圆盘中间文字
        mPieChart.setCenterText("");
        //设置圆盘中间文字的大小
        mPieChart.setCenterTextSize(17);
        //设置圆盘中间文字的颜色
        mPieChart.setCenterTextColor(Color.BLACK);
        //设置圆盘中间文字的字体
        mPieChart.setCenterTextTypeface(Typeface.DEFAULT);

        //设置中间圆盘的颜色
        mPieChart.setHoleColor(getContext().getResources().getColor(R.color.skin_bg_color));
        //是否显示饼图中间空白区域，默认显示
        mPieChart.setDrawHoleEnabled(true);
        //设置圆盘是否转动，默认转动
        mPieChart.setRotationEnabled(false);
        //设置初始旋转角度
        mPieChart.setRotationAngle(0);
        Legend mLegend = mPieChart.getLegend();  //设置比例图
        mLegend.setEnabled(false);
        mLegend.setForm(Legend.LegendForm.SQUARE);//设置比例块形状，默认为方块

        //设置X轴动画
        mPieChart.animateX(1800);
    }

    private void setData(float txPay, float txGet, float onlinePay, float onlineGet,
                         float marketPay, float marketGet, float otherPay, float otherGet, float sum, List<BillNetBean> list) {

        if (txPay!=0){
            entry.add(new PieEntry(Math.abs(txPay/sum*100),"通讯支出：\n"+Math.abs(txPay)+"元"));
        }
        if (txGet!=0) {
            entry.add(new PieEntry(Math.abs(txGet/sum * 100), "通讯收入：\n"+Math.abs(txGet)+"元"));
        }
        if (onlinePay!=0) {
            entry.add(new PieEntry(Math.abs(onlinePay/sum*100),"线上支出：\n"+Math.abs(onlinePay)+"元"));
        }
        if (onlineGet!=0){
            entry.add(new PieEntry(Math.abs(onlineGet/sum*100),"线上收入：\n"+Math.abs(onlineGet)+"元"));
        }
        if (marketPay!=0) {
            entry.add(new PieEntry(Math.abs(marketPay/sum*100),"线下支出：\n"+Math.abs(marketPay)+"元"));
        }
        if (marketGet!=0) {
            entry.add(new PieEntry(Math.abs(marketGet/sum*100),"线下收入：\n"+Math.abs(marketGet)+"元"));
        }
        if (otherPay!=0) {
            entry.add(new PieEntry(Math.abs(otherPay/sum*100),"其他支出：\n"+Math.abs(otherPay)+"元"));
        }
        if (otherGet!=0) {
            entry.add(new PieEntry(Math.abs(otherGet/sum*100),"其他收入：\n"+Math.abs(otherGet)+"元"));
        }


        PieData pieData = new PieData(dataSet);
        mPieChart.setData(pieData);

        // 设置饼图各个区域颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);
        colors.add(Color.CYAN);
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(15);
        dataSet.setSliceSpace(3f);
        dataSet.setDrawValues(false);
        mPieChart.highlightValues(null);
        mPieChart.invalidate();
    }



    private int year;
    private int month;

    private String getSystemMonth(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        LogUtils.d(TAG,year+"-"+month);
        monthText.setText((month)+"月账单");
        return year+"-"+(month);
    }

    private void initData() {
        presenter.getMonthData(getSystemMonth());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.expand_btn:
                presenter.showMonthPopup();
                break;
            case R.id.login_btn:
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;

        }
    }

    @Override
    public void showDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();

            }
        });
    }

    @Override
    public void hideDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void showUI(final float txPay, final float txGet, final float onlinePay, final float onlineGet,
                       final float marketPay, final float marketGet, final float otherPay, final float otherGet, final float sum, final List<BillNetBean> list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mScroll.setVisibility(View.VISIBLE);
                entry.clear();
//                dataSet.clear();
                LogUtils.d(TAG,sum+"");
                setData(txPay,txGet,onlinePay,onlineGet,marketPay,marketGet,otherPay,otherGet,sum,list);
            }
        });
    }

    @Override
    public void showNoData() {
       getActivity().runOnUiThread(new Runnable() {
           @Override
           public void run() {
               mNoData.setVisibility(View.VISIBLE);
           }
       });
    }

    @Override
    public void hideNoData() {
       getActivity().runOnUiThread(new Runnable() {
           @Override
           public void run() {
               mNoData.setVisibility(View.GONE);
           }
       });
    }

    @Override
    public void hideUI() {
       getActivity().runOnUiThread(new Runnable() {
           @Override
           public void run() {
               mScroll.setVisibility(View.GONE);
           }
       });
    }

    @Override
    public void showMonthPopup() {
        expandBtn.setSelected(true);
        pop.create().showPop(expandBtn).setListener(new MonthPopManager.OnDateGetListener() {
            @Override
            public void getDate(String date) {
                mData.clear();
                mAdapter.notifyDataSetChanged();
                String month = date.split("-")[1];
                monthText.setText(month+"月账单");
                expandBtn.performClick();
                presenter.getMonthData(date);
            }
        });

    }

    @Override
    public void hideMonthPopup() {
        expandBtn.setSelected(false);
        pop.closePop();
    }

    @Override
    public void hideLogin() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loginBtn.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void showLogin() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loginBtn.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

        PieEntry entry = (PieEntry) e;
        String type = entry.getLabel().substring(0,4);
        typeText.setText(type);
//        Toast.makeText(getContext(),type,Toast.LENGTH_SHORT).show();
        mData.clear();
        mScroll.smoothScrollTo(0,0);
        presenter.getTypeData(type, new OnTypeDataListener() {
            @Override
            public void onDataGet(final List<BillNetBean> beans) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mData.clear();
                        mData.addAll(beans);
                        mAdapter.notifyDataSetChanged();
                        LogUtils.d(TAG,beans.size()+"and"+mData.size());
                    }
                });
            }
        });
    }

    @Override
    public void onNothingSelected() {

    }
}
