package com.team_red.melody;

import android.app.Application;
import android.content.Context;

import com.team_red.melody.melodyboard.MelodyBoard;


public class Melody extends Application {
    private static Melody instance;

    public static Melody getInstance(){
        return instance;
    }

    public static Context getContext(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MelodyBoard.setOnEditMode(false);
    }
}
