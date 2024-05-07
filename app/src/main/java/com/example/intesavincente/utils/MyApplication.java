package com.example.intesavincente.utils;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static  Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext(); // Grab the Context you want.
    }
    public static Context getAppContext() { return MyApplication.context; }

}