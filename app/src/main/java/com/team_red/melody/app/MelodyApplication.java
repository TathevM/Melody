package com.team_red.melody.app;

import android.app.Application;
import android.content.Context;

import com.team_red.melody.melodyboard.MelodyBoard;
import com.team_red.melody.models.User;
import com.team_red.melody.sound.MelodyPoolManager;


public class MelodyApplication extends Application {
    private static MelodyApplication instance;
    private static User loggedInUser;

    public static Context getContext(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MelodyBoard.setOnEditMode(false);
        MelodyPoolManager.CreateInstance();
    }


    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        MelodyApplication.loggedInUser = loggedInUser;
    }
}
