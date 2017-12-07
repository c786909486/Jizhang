package com.example.ckz.jizhang.util;

import android.util.Log;

/**
 * Created by CKZ on 2017/11/9.
 */

public class LogUtils {
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WRAN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static final int LEVEL = VERBOSE;

    public static void v(String tag,String msg){
        if (LEVEL<=VERBOSE){
            Log.v(tag,msg);
        }
    }
    public static void d(String tag,String msg){
        if (LEVEL<=DEBUG){
            Log.d(tag,msg);
        }
    }
    public static void i(String tag,String msg){
        if (LEVEL<=INFO){
            Log.i(tag,msg);
        }
    }
    public static void w(String tag,String msg){
        if (LEVEL<=WRAN){
            Log.w(tag,msg);
        }
    }
    public static void e(String tag,String msg){
        if (LEVEL<=ERROR){
            Log.e(tag,msg);
        }
    }
}
