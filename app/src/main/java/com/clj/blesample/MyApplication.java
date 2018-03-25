package com.clj.blesample;

import android.app.Application;

/**
 * Created by spc on 2017/3/16.
 */

public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }


}
