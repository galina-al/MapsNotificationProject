package com.example.user.mapsproject;


import android.app.Application;

import com.example.user.mapsproject.DB.DB;

public class MyApplication extends Application{

    @Override
    public void onCreate() {

        super.onCreate();
        DB.init(this);
    }
}
