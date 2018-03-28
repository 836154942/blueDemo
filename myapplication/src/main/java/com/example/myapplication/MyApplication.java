package com.example.myapplication;



import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by shipinchao on 2018/3/22.
 */

public class MyApplication extends Application {
    public static MyApplication intance;
    public static int screenWidth;
    public static int screenHeight;
    public static float screenDensity;
    @Override
    public void onCreate() {
        super.onCreate();
        intance = this;
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;
        screenDensity = outMetrics.density;
    }
}
