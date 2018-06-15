package com.example.user.mapsproject.db;

import android.content.Context;

public class DB {

    private static DB db;
    private  MapsDbHelper mapsDbHelper;
    private static Context context;

    private DB(){
        mapsDbHelper = new MapsDbHelper(context);
    }

    public static DB getDb(){
        if (db == null){
            db = new DB();
        }
        return db;
    }

    public static void init(Context context){
        DB.context = context;
    }

    public MarkersRepository getMarkersRepository(){
        return new MarkersRepository(mapsDbHelper);
    }

}
