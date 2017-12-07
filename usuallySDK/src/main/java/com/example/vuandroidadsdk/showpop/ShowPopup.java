package com.example.vuandroidadsdk.showpop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.vuandroidadsdk.R;

/**
 * Created by CKZ on 2017/7/10.
 */

public class ShowPopup {

    private static ShowPopup showPopup;

    private Context context;

    private LayoutInflater inflater;

    private View popView;

    private PopupWindow popupWindow;

    private LinearLayout simpleView;

    /**
     * 初始化
     * @param context
     */
    public static ShowPopup getInstance(Context context){
        if (showPopup == null){
            synchronized (ShowPopup.class){
                if (showPopup == null){
                    showPopup = new ShowPopup(context);
                }
            }
        }
        return showPopup;
    }

    private ShowPopup(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 输入布局文件，设置popwindow
     * @param layoutId
     * @return
     */
    public ShowPopup createLayoutPopupWindow(int layoutId){
        popView = inflater.inflate(layoutId,null);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0x30000000);
        popupWindow.setBackgroundDrawable(dw);
        return showPopup;
    }
    public ShowPopup enable(boolean enable){
        popupWindow.setFocusable(enable);
        return this;
    }
    public interface OnPositionClickListener {
        void OnPositionClick(PopupWindow popup,View view, int position);
    }
    private OnPositionClickListener positionClickListener;

    public void setPositionClickListener(OnPositionClickListener positionClickListener) {
        this.positionClickListener = positionClickListener;
    }

    /**
     * 简易popupWindow，不用输入布局
     * @param btnName
     * @return
     */
    public ShowPopup createSimplePopupWindow(String... btnName){
        simpleView = new LinearLayout(context);
        simpleView.setGravity(Gravity.CENTER_HORIZONTAL);
        simpleView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        simpleView.setOrientation(LinearLayout.VERTICAL);
        simpleView.setBackgroundColor(Color.WHITE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            simpleView.setDividerDrawable(context.getResources().getDrawable(R.drawable.diver_line,null));
//        }else {
//            simpleView.setDividerDrawable(context.getResources().getDrawable(R.drawable.diver_line));
//        }
//        simpleView.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        simpleView.setPadding(10,8,10,0);
        for (int i = 0;i<btnName.length;i++){
            TextView button = new TextView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            params.setMargins(0,20,0,0);
            button.setLayoutParams(params);
            button.setGravity(Gravity.CENTER);
            button.setText(btnName[i]);
            button.setTextSize(18.0f);
            button.setPadding(0,25,0,25);
            button.setBackgroundColor(Color.parseColor("#ffffff"));
            simpleView.addView(button);
            View view = new View(context);
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1));
            view.setBackgroundColor(Color.parseColor("#D1D1D1"));
            simpleView.addView(view);
            final int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    positionClickListener.OnPositionClick(popupWindow,view, finalI);
                }
            });
        }

        popupWindow = new PopupWindow(simpleView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0x30000000);
        popupWindow.setBackgroundDrawable(dw);
        return showPopup;
    }

    /**
     * 设置弹窗动画
     * @param animId
     * @return showPopu
     */
    public ShowPopup setAnim(int animId){
       if (popupWindow!=null){
           popupWindow.setAnimationStyle(animId);
       }
       return showPopup;
    }
    /**
     * 设置默认动画
     */
    public ShowPopup defaultAnim(){
        if (popupWindow!=null){
            popupWindow.setAnimationStyle(R.style.showPopupAnimation);
        }
        return showPopup;
    }

    /**
     * 将弹窗设置在底部
     * @param parent
     * @return
     */
    public ShowPopup atBottom(View parent){
        if (popupWindow!=null){
            popupWindow.showAtLocation(parent, Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL,0,0);
        }
        return showPopup;
    }

    /**
     * 设置悬浮框位置，偏移量
     * @param paren
     * @param x
     * @param y
     * @return
     */
    public ShowPopup dropDown(View paren, int x, int y){
        if (popupWindow!=null){
            popupWindow.showAsDropDown(paren,x,y);
        }
        return showPopup;
    }

    /**
     * 设置弹窗的位置以及偏移量
     * @param parent
     * @param gravity
     * @param x
     * @param y
     * @return
     */
    public ShowPopup atLocation(View parent, int gravity, int x, int y){
        if (popupWindow!=null){
            popupWindow.showAtLocation(parent,gravity,x,y);
        }
        return showPopup;
    }


    /**
     * 设置布局文件中控件的点击事件
     * @param id
     * @param listener
     * @return
     */
   public ShowPopup setClick(int id,View.OnClickListener listener){
       if (popView!=null){
           popView.findViewById(id).setOnClickListener(listener);
       }
       return showPopup;
   }


    /**
     * 关闭弹窗的点击事件
     * @param id
     * @return
     */
   public ShowPopup setDismissClick(int id){
       if (popupWindow!=null && popView!=null){
           popView.findViewById(id).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   popupWindow.dismiss();
               }
           });
       }
       return showPopup;
   }

   public void closePopupWindow(){
       if (popupWindow!=null){
           popupWindow.dismiss();
       }
   }

    /**
     * 获取view
     * @return
     */
   public View getView(){
       if (popView!=null){
           return popView;
       }
       return null;
   }

}
