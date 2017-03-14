package com.team_red.melody;

import android.app.Application;
import android.content.Context;

import com.team_red.melody.melodyboard.MelodyBoard;
import com.team_red.melody.sound.SoundPoolManager;


public class MelodyApplication extends Application {
    private static MelodyApplication instance;

    public static MelodyApplication getInstance(){
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
        SoundPoolManager.CreateInstance();
    }
}
