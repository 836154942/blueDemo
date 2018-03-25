package com.example.myapplication;

import com.baidu.ubs.analytics.BaiDuABAgent;
import com.baidu.ubs.analytics.BaiDuABConfig;

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
        //sdk初始化
        BaiDuABConfig abConfig = new BaiDuABConfig.Builder()
                .content(this)
                .isShowLog(false)
                .setSessionContinueSecond(30) // session后台超时时间30s
                .setSendLogInterval(1) // 日志每间隔一分钟上传一次
                .setOnlyWifiUpload(false)
                .setUploadDelaySecond(15) // 设置日志延时上传的时间
                .setEnableRatio(1000)  // 0-1000 设置sdk生效 sdk 的比例。 1000为全部生效
                .build();
        BaiDuABAgent.init(abConfig);

    }
}
