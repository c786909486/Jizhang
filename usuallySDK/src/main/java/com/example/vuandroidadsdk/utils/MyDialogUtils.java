package com.example.vuandroidadsdk.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;


/**
 * Created by CKZ on 2017/7/3.
 */

public class MyDialogUtils {

    private static MyDialogUtils instance;

    public static MyDialogUtils getInstance(){
        if (instance == null){
            instance = new MyDialogUtils();
        }
        return instance;
    }

    /**
     * 普通dialog
     * @param context
     * @param title
     * @param message
     * @param listener
     */
    public void createNormalDialog(Context context,String title,String message,DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.setTitle(title)
                .setMessage(message)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定",listener).create();
        dialog.show();
    }

    /**
     * 普通列表dialog
     * @param context
     * @param title
     * @param list
     * @param listener
     */
    public void createNormalListDialog(Context context,String title,String[] list,DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.setTitle(title)
                .setItems(list,listener)
                .create();
        dialog.show();
    }

    /**
     * 单选dialog
     * @param context
     * @param title
     * @param list
     * @param listener
     */
    public void createSingleListDialog(Context context,String title,String[] list,DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.setTitle(title)
                .setSingleChoiceItems(list,0,listener)
                .create();
        dialog.show();
    }

    /**
     * 复选dialog
     * @param context
     * @param title
     * @param list
     * @param checkItem
     * @param multiListener
     * @param listener
     */
    public void createMuiltyListDialog(Context context,String title,String[] list,boolean[] checkItem,DialogInterface.OnMultiChoiceClickListener multiListener,DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.setTitle(title)
                .setMultiChoiceItems(list,checkItem,multiListener)
                .setPositiveButton("确定",listener).create();
        dialog.show();
    }

}
