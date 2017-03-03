package com.team_red.melody;

import android.app.Application;

import com.team_red.melody.melodyboard.MelodyBoard;


public class Melody extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MelodyBoard.setOnEditMode(false);
    }
}
