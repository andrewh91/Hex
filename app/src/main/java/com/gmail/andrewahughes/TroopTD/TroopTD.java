package com.gmail.andrewahughes.TroopTD;

import android.content.Context;

import com.gmail.andrewahughes.framework.Screen;
import com.gmail.andrewahughes.framework.implementation.AndroidGame;

public class TroopTD extends AndroidGame {
    private static Context context;
    public void onCreate(){

        TroopTD.context = getApplicationContext();
    }
    public static Context getAppContext() {
        return TroopTD.context;
    }
    @Override
    public Screen getInitScreen() {
        return new LoadingScreen(this); //
    }
    @Override
    public void onBackPressed() {
    getCurrentScreen().backButton();
    }
}