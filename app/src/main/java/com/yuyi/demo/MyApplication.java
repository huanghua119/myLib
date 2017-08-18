package com.yuyi.demo;

import android.app.Application;

import com.yuyi.lib.MyLib;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MyLib.init(this);
    }

}
