package com.example.exodus.framework;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by 여성우 on 2018-05-16.
 */

public class GameViewThread extends Thread {
    private SurfaceHolder m_surface;
    private GameView        m_view;
    private boolean         m_run = false;

    GameViewThread(SurfaceHolder surface, GameView myView) {
        m_surface = surface;
        m_view = myView;
    }

    public void SetRunning(boolean run) {
        m_run = run;
    }

    @Override
    public void run() {
        super.run();

        Canvas canvas = null;
        while(m_run) {
            try {
                m_view.Update();
                canvas=m_surface.lockCanvas(null);
                synchronized (m_surface) {
                    m_view.MyonDraw(canvas);
                }
            } finally {
                if(canvas != null)
                    m_surface.unlockCanvasAndPost(canvas);
            }
        }
    }
}
