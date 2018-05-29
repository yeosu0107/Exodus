package com.example.exodus.framework;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by 여성우 on 2018-05-16.
 */

public interface IState {
    public void Init(int index);
    public void Destroy();
    public void Update();
    public void Render(Canvas canvas);

    public void MovePlayers(boolean isJump, int moveX);

    public boolean onKeyDown(int keyCode, KeyEvent event);
    public boolean onKeyUp(int keyCode, KeyEvent event);
    public boolean onTouchEvent(MotionEvent event);

}
