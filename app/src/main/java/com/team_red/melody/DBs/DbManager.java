package com.team_red.melody.DBs;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.team_red.melody.models.Composition;
import com.team_red.melody.models.Quote;
import com.team_red.melody.models.User;

import java.util.ArrayList;

public class DbManager {


    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;

    ArrayList<Quote> quotes= new ArrayList<>();

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

    public User getUserByID(int id){
        Cursor cursor = mDb.query(TableManager.USERS_TABLE , null , TableManager.USER_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            String userName = cursor.getString(cursor.getColumnIndex(TableManager.USERNAME));
            cursor.close();
            return new User(userName , id);
        }
        else{
            cursor.close();
            return null;
        }
    }

    public Composition getCompByID(int id){
        Cursor cursor = mDb.query(TableManager.COMPOSITIONS_TABLE , null , TableManager.COMPOSITION_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            String compName = cursor.getString(cursor.getColumnIndex(TableManager.COMPOSITION_NAME));
            String compFileName = cursor.getString(cursor.getColumnIndex(TableManager.FILENAME));
            int compositorID = cursor.getInt(cursor.getColumnIndex(TableManager.COMPOSITOR_ID));
            int type = cursor.getInt(cursor.getColumnIndex(TableManager.TYPE));
            cursor.close();
            return new Composition(id , compName , compositorID , compFileName, type);
        }
        else{
            cursor.close();
            return null;
        }
    }

    public long insertComposition (String compName, int compositorID , String fileName, int type){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableManager.COMPOSITION_NAME, compName);
        contentValues.put(TableManager.COMPOSITOR_ID, compositorID);
        contentValues.put(TableManager.FILENAME, fileName);
        contentValues.put(TableManager.TYPE , type);
        return mDb.insert(TableManager.COMPOSITIONS_TABLE, null, contentValues);
    }

    public ArrayList<Composition> getCompositions (int compositorID) {

        ArrayList<Composition> compositions = new ArrayList<>();

        Cursor cursor = mDb.query(TableManager.COMPOSITIONS_TABLE ,null ,TableManager.COMPOSITOR_ID + "=?" , new String[] {Integer.toString(compositorID)},null ,null ,null);
        while (cursor.moveToNext()) {

            int compositionID = cursor.getInt(cursor.getColumnIndex(TableManager.COMPOSITION_ID));
            String compositionName = cursor.getString(cursor.getColumnIndex(TableManager.COMPOSITION_NAME));
            String jsonFilename = cursor.getString(cursor.getColumnIndex(TableManager.FILENAME));
            int type = cursor.getInt(cursor.getColumnIndex(TableManager.TYPE));
            Composition composition = new Composition(compositionID, compositionName, compositorID, jsonFilename, type);
            compositions.add(composition);
        }
        cursor.close();
        return compositions;
    }

    public void deleteCompByID( int compositionID ){
            mDb.delete(TableManager.COMPOSITIONS_TABLE, TableManager.COMPOSITION_ID + "=" + compositionID, null);
    }

}


