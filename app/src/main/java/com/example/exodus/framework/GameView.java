package com.example.exodus.framework;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by 여성우 on 2018-05-16.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameViewThread  m_thread;
    private IState          m_state;


    public GameView(Context context) {
        super(context);

        AppManager.getInstance().setGameview(this);
        AppManager.getInstance().setResources(getResources());

        //키보드 이용
        setFocusable(true);

        getHolder().addCallback(this);
        m_thread = new GameViewThread(getHolder(), this);

        //ChangeGameState(new GameState());
    }

    //@Override
    protected void MyonDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        m_state.Render(canvas);
    }

    void Update() {
        m_state.Update();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        m_thread.SetRunning(true);
        m_thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        m_thread.SetRunning(false);
        while(retry) {
            try {
                m_thread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        m_state.onKeyDown(keyCode, event);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        m_state.onTouchEvent(event);
        return true;
    }

    public void ChangeGameState(IState state) {
        if(m_state != null)
            m_state.Destroy();

        state.Init();
        m_state = state;
    }
}
