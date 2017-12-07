package com.example.vuandroidadsdk.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by CKZ on 2017/7/23.
 */

public class ShowToastUtil {

    public static void showToast(Context context,CharSequence s){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, @StringRes int stringId){
        Toast.makeText(context,context.getResources().getText(stringId),Toast.LENGTH_SHORT).show();
    }
}
