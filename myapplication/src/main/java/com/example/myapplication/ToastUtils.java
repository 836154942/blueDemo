package com.example.myapplication;

import android.widget.Toast;

/**
 * Created by shipinchao on 2018/3/24.
 */

public class ToastUtils {
    public static  Toast toast;


    public static void show(String text){
        if (toast==null){
            toast = Toast.makeText(MyApplication.intance,text,Toast.LENGTH_SHORT);
        }else {
            toast.setText(text);
        }

        toast.show();
    }
}
