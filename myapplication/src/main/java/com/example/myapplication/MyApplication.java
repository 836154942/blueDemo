package com.example.myapplication;



import android.app.Application;

/**
 * Created by shipinchao on 2018/3/22.
 */

public class MyApplication extends Application {
    public static MyApplication intance;

    @Override
    public void onCreate() {
        super.onCreate();
        intance = this;
    }
}
