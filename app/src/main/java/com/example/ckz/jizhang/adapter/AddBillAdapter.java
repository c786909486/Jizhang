package com.example.ckz.jizhang.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ckz.jizhang.R;
import com.example.ckz.jizhang.bean.BillNetBean;
import com.example.ckz.jizhang.db.BillLocalBean;
import com.example.ckz.jizhang.view.view.TypeView;

import java.util.List;

/**
 * Created by CKZ on 2017/12/2.
 */

public class AddBillAdapter extends BaseAdapter {

    private static final int LOCAL_DATA = 0;

    private static final int NET_DATA = 1;

    private Context mContext;

    private List<Object> mData;
    private int color;

    public AddBillAdapter(Context mContext, List<Object> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) instanceof BillLocalBean){
            return LOCAL_DATA;
        }else {
            return NET_DATA;
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_bill,viewGroup,false);
            holder = new ViewHolder();
            holder.mType = view.findViewById(R.id.bill_type);
            holder.mDate = view.findViewById(R.id.bill_date);
            holder.mText = view.findViewById(R.id.bill_text);
            holder.mMoney = view.findViewById(R.id.bill_money);
            view.setTag(holder);
        }else {

            holder = (ViewHolder) view.getTag();
        }


        int[] colors = new int[]{R.color.bill_red,
                R.color.bill_black,
                R.color.bill_blue,
                R.color.bill_green,
                R.color.bill_yellow};
        int color = colors[(int) (Math.random()*colors.length)];
        switch (getItemViewType(position)){
            case LOCAL_DATA:
                BillLocalBean localBean = (BillLocalBean) mData.get(position);
                holder.mText.setText(localBean.getText());
                holder.mMoney.setText(localBean.getMoney()+"元");
                holder.mDate.setText(localBean.getDate());
                holder.mType.setText(localBean.getType());

                holder.mType.setBgColor(color);
//                if (localBean.getType().equals("通讯")) holder.mType.setBgColor(colors[0]);
//                if (localBean.getType().equals("线上")) holder.mType.setBgColor(colors[1]);
//                if (localBean.getType().equals("线下")) holder.mType.setBgColor(colors[2]);
//                if (localBean.getType().equals("其他")) holder.mType.setBgColor(colors[3]);

                break;
            case NET_DATA:
                BillNetBean netBean = (BillNetBean) mData.get(position);
                holder.mText.setText(netBean.getText());
                holder.mMoney.setText(netBean.getMoney()+"元");
                holder.mDate.setText(netBean.getDate());
                holder.mType.setText(netBean.getType());

                holder.mType.setBgColor(color);
//                if (netBean.getType().equals("通讯")) holder.mType.setBgColor(colors[0]);
//                if (netBean.getType().equals("线上")) holder.mType.setBgColor(colors[1]);
//                if (netBean.getType().equals("线下")) holder.mType.setBgColor(colors[2]);
//                if (netBean.getType().equals("其他")) holder.mType.setBgColor(colors[3]);
                break;
        }
        return view;
    }

    class ViewHolder{
        TypeView mType;
        TextView mDate,mText,mMoney;
    }
}
