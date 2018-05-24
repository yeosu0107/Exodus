package com.example.exodus.gamelogic;
import com.example.exodus.BlockObject;
import com.example.exodus.framework.CollisionBox;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.example.exodus.R;
import com.example.exodus.framework.AppManager;
import com.example.exodus.framework.BackGround;
import com.example.exodus.framework.CollisionManager;
import com.example.exodus.framework.IState;
import com.example.exodus.framework.MapObject;

/**
 * Created by 여성우 on 2018-05-17.
 */

public class GameState implements IState{

    final int MAX_PLAYER = 6;
    private Player[] m_player;
    private CollisionBox m_groundCollBox;
    private BlockObject m_testblock;

    private MapObject m_map;
    private BackGround m_back;

    @Override
    public void Init() {
        m_player = new Player[MAX_PLAYER];
        for(int i=0; i<MAX_PLAYER; ++i) {
            m_player[i] = new Player();
            m_player[i].setting(i*150, i*168);
        }
        m_player[0].setState(Player.idle);
        m_groundCollBox = new CollisionBox(new Rect(0, 1030, 1920, 1080 ));
        m_testblock = new BlockObject();
        m_testblock.setting(150,300);
        m_map = new MapObject(AppManager.getInstance().getBitmap(R.drawable.tileset), 25, 23, AppManager.getInstance().getMap(0));
        m_back = new BackGround(AppManager.getInstance().getBitmap(R.drawable.back));
    }

    @Override
    public void Destroy() {

    }

    @Override
    public void Update() {
        long time = System.currentTimeMillis();
        for(Player cur : m_player) {
            cur.update(time);
            cur.ResetCollside();
        }
        m_testblock.ResetCollside();
        //CollisionManager.checkBoxtoBox(m_player[0].m_collBox, m_player[1].m_collBox);
        for(int i = 0; i < MAX_PLAYER; ++i) {
            CollisionManager.checkBoxtoBox(m_player[i].m_collBox, m_groundCollBox);
            for (int j = i + 1; j < MAX_PLAYER; ++j)
                CollisionManager.checkBoxtoBox(m_player[i].m_collBox, m_player[j].m_collBox);
            CollisionManager.checkBoxtoBox(m_player[i].m_collBox, m_testblock.m_collBox);
        }
        CollisionManager.checkBoxtoBox(m_testblock.m_collBox, m_groundCollBox);

        for(int i = 0; i < MAX_PLAYER; ++i)
            m_player[i].move(0, 10);

        m_testblock.update(time);
    }

    @Override
    public void Render(Canvas canvas) {

        m_back.draw(canvas);
        m_map.draw(canvas);

        for(Player cur : m_player) {
            m_groundCollBox.DrawCollisionBox(canvas);
            m_testblock.draw(canvas);
            cur.draw(canvas);
            if(cur == m_player[0]) {
                //cur.setState(Player.idle);

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_W) {
            m_player[0].setState(Player.run);
            m_player[0].move(0, -10);
        }
        if(keyCode == event.KEYCODE_S) {
            m_player[0].setState(Player.run);
            m_player[0].move(0, 10);
        }
        if(keyCode == event.KEYCODE_A) {
            m_player[0].setDir(1);
            m_player[0].setState(Player.run);
            m_player[0].move(-10, 0);
        }
        if(keyCode == event.KEYCODE_D) {
            m_player[0].setDir(0);
            m_player[0].setState(Player.run);
            m_player[0].move(10, 0);
        }
        Log.d("down", "down"+event.getDownTime());
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        m_player[0].setState(Player.idle);
        Log.d("up", "up");
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("touch", "touch" + event.getAction());
        return false;
    }
}
