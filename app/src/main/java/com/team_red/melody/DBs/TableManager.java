package com.team_red.melody.DBs;


import android.database.sqlite.SQLiteDatabase;

public class TableManager {


    //         COMPOSITIONS TABLE


    public static final String COMPOSITIONS_TABLE = "COMPOSITIONS";

    public static final String COMPOSITION_ID = "_id";
    public static final String COMPOSITOR_ID = "compositor";
    public static final String COMPOSITION_NAME = "composition";
    public static final String FILENAME = "json_name";
    public static final String TYPE = "type";

    private static final String CREATE_COMPOSITIONS_TABLE = "CREATE TABLE " + COMPOSITIONS_TABLE + " ("
            + COMPOSITION_ID + " integer primary key, "
            + COMPOSITOR_ID + " integer, "
            + COMPOSITION_NAME + " text, "
            + FILENAME + " text, "
            + TYPE + " text)";



    //            USERS TABLE

    public static final String USERS_TABLE = "USERS";
    public static final String USER_ID = "_id";
    public static final String USERNAME = "username";

    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + USERS_TABLE + " ("
            + USER_ID + " integer primary key, "
            + USERNAME + " text)";



    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_COMPOSITIONS_TABLE);

    }




}
