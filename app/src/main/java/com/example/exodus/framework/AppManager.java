package com.example.exodus.framework;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.ScriptGroup;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 여성우 on 2018-05-16.
 */

public class AppManager {
    static public final int STAGE_LOCK = 0;
    static public final int STAGE_OPEN = 1;
    static public final int STAGE_CLEAR = 2;

    private static AppManager g_interface;

    private GameView m_gameview;
    private Resources m_resource;

    private List<List<int[]>> m_mapList;
    private int[] m_ClearStage;

    private int screen_width;
    private int screen_height;
    //private GameState m_gamestate;

    public AppManager() {
        m_ClearStage = new int[10];
        for(int b : m_ClearStage) b = STAGE_LOCK;
        m_ClearStage[0] = STAGE_OPEN;

        m_mapList=new ArrayList<List<int[]>>();
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

    public void addMap(List<int[]> is) {
        m_mapList.add(is);
    }

    public void setScreenSize(int x, int y) {
        screen_width = x;
        screen_height = y;
    }

    public int[] getStageClearinfo() { return m_ClearStage; }
    public void setStageClearInfo(int index, int info) { m_ClearStage[index] = info; }
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

    public List<int[]> getMap(int index) {
       return m_mapList.get(index);
    }

    public int getWidth() {return screen_width;}
    public int getHeight() {return screen_height;}

    public int getTileWidth() {return screen_width / (m_mapList.get(0).get(0).length - 1);}
    public int getTileHeight() {return screen_height / m_mapList.get(0).size();}
}
