package com.example.ckz.jizhang.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by CKZ on 2017/2/12.
 */

public class SPUtils {
    public static SharedPreferences sp;
    public static SharedPreferences.Editor ed;

    public static String getStringSp(Context context,String key){
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        String result = sp.getString(key,"");
        return result;
    }
    public static long getlongSp(Context context,String key){
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        long result = sp.getLong(key,0);
        return result;
    }
    public static int getIntSp(Context context,String key){
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        int result = sp.getInt(key,0);
        return result;
    }
    public static boolean getBooleanSp(Context context,String key){
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean result = sp.getBoolean(key,true);
        return result;
    }
    @SuppressLint("CommitPrefEdits")
    public static void putStringSp(Context context, String key, String value){
        ed = PreferenceManager.getDefaultSharedPreferences(context).edit();
        ed.putString(key,value);
        ed.commit();
    }
    public static void putlongSp(Context context,String key,long value){
        ed = PreferenceManager.getDefaultSharedPreferences(context).edit();
        ed.putLong(key,value);
        ed.commit();
    }

    public static void putIntSp(Context context, String key, int value){
        ed = PreferenceManager.getDefaultSharedPreferences(context).edit();
        ed.putInt(key,value);
        ed.commit();
    }
    public static void putBooleanSp(Context context, String key, boolean value){
        ed = PreferenceManager.getDefaultSharedPreferences(context).edit();
        ed.putBoolean(key,value);
        ed.commit();
    }
}
