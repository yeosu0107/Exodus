package com.example.exodus.framework;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by 여성우 on 2018-05-16.
 */

public class AppManager {
    private static AppManager g_interface;

    private GameView m_gameview;
    private Resources m_resource;
    //private GameState m_gamestate;

    public AppManager() {
    }

    public static AppManager getInstance() {
        if(g_interface == null)
            g_interface = new AppManager();

        return g_interface;
    }

    public void setGameview(GameView view) {
        m_gameview=view;
    }

    public void setResources(Resources resource) {
        m_resource=resource;
    }

    //public void setGameState(GameState state) {m_gamestate=state;}

    public  GameView getGameview() {
        return m_gameview;
    }

    //public GameState getGaetState() {return m_gamestate;}

    public Resources getResources() {
        return m_resource;
    }

    public Bitmap getBitmap(int id) {
        return BitmapFactory.decodeResource(m_resource, id);
    }
}
