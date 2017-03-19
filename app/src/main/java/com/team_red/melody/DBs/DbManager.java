package com.team_red.melody.DBs;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.team_red.melody.Models.Composition;
import com.team_red.melody.Models.User;

import java.util.ArrayList;

public class DbManager {

    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;

    public DbManager(Context context) {
        mDbHelper = new DbHelper(context);
        mDb = mDbHelper.getWritableDatabase();


    }

    public long insertUser (String userName){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableManager.USERNAME, userName);
        return mDb.insert(TableManager.USERS_TABLE, null, contentValues);
    }



    public ArrayList<User> getUsers (){
        ArrayList<User> users = new ArrayList<>();

        Cursor cursor = mDb.query(TableManager.USERS_TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex(TableManager.USERNAME));
            int id = cursor.getInt(cursor.getColumnIndex(TableManager.USER_ID));

            User user = new User(name , id );
            users.add(user);
        }
        return users;
    }

    public  long insertComposition (Composition composition){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableManager.COMPOSITION_NAME, composition.getCompositionName());
        contentValues.put(TableManager.COMPOSITOR_ID, composition.getCompositionID());
        contentValues.put(TableManager.FILENAME, composition.getJsonFileName());
        contentValues.put(TableManager.PATH, composition.getPath());

        return mDb.insert(TableManager.COMPOSITIONS_TABLE, null, contentValues);
    }

    public ArrayList<Composition> getCompositions (int compositorID) {

        ArrayList<Composition> compositions = new ArrayList<>();



        Cursor cursor = mDb.query(TableManager.COMPOSITIONS_TABLE ,null ,TableManager.COMPOSITOR_ID + "=?" , new String[] {Integer.toString(compositorID)},null ,null ,null);
        while (cursor.moveToNext()) {

            int compositionID = cursor.getInt(cursor.getColumnIndex(TableManager.COMPOSITION_ID));
            String compositionName = cursor.getString(cursor.getColumnIndex(TableManager.COMPOSITION_NAME));
//            int compositorID = cursor.getInt(cursor.getColumnIndex(TableManager.COMPOSITOR_ID));
            String jsonFilename = cursor.getString(cursor.getColumnIndex(TableManager.FILENAME));
            String path = cursor.getString(cursor.getColumnIndex(TableManager.PATH));

            Composition composition = new Composition(compositionID, compositionName, compositorID, jsonFilename, path);
            compositions.add(composition);
        }


        return compositions;
    }

}


