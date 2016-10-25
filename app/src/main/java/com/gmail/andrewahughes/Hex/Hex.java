package com.gmail.andrewahughes.Hex;

import android.content.Context;

import com.gmail.andrewahughes.framework.Screen;
import com.gmail.andrewahughes.framework.implementation.AndroidGame;

public class Hex extends AndroidGame {
    private static Context context;
    public void onCreate(){

        Hex.context = getApplicationContext();
    }
    public static Context getAppContext() {
        return Hex.context;
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