package com.team_red.melody.DBs;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{
    public static final String DB_NAME = "MelodyDB";
    public static final int DB_VERSION = 1;


    public DbHelper(Context context)  {
        super(context, DB_NAME ,null , DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        TableManager.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
