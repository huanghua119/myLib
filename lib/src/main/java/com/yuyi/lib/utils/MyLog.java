package com.yuyi.lib.utils;


/**
 * @author huanghua
 */

public class MyLog {

    private static final String TAG = "my_lib";

    public static void i(String msg) {
        android.util.Log.i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        android.util.Log.i(tag, msg);
    }

}
