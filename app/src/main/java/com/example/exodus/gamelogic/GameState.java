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
import com.example.exodus.framework.EffectManagement;
import com.example.exodus.framework.IState;
import com.example.exodus.framework.MapObject;

/**
 * Created by 여성우 on 2018-05-17.
 */

public class GameState implements IState{

    final int MAX_PLAYER = 6;
    private Player[] m_player;
    private BlockObject m_testblock;
    private BlockObject m_Gem;

    private MapObject m_map;
    private BackGround m_back;
    private EffectManagement m_Effect;

    @Override
    public void Init() {
        m_player = new Player[MAX_PLAYER];
        for(int i=0; i<MAX_PLAYER; ++i) {
            m_player[i] = new Player();
            m_player[i].setting(i*150, 0);
        }
        m_player[0].setState(Player.idle);
        m_testblock = new BlockObject(AppManager.getInstance().getBitmap(R.drawable.crate), 1, 1, 2, 0);
        m_testblock.setting(150,300);
        m_Gem = new BlockObject(AppManager.getInstance().getBitmap(R.drawable.gem),
                10, 5, 2, BlockObject.FLAG_HOLDING);
        m_Gem.setting(100, 700);
        m_map = new MapObject(AppManager.getInstance().getBitmap(R.drawable.tileset), 25, 23, AppManager.getInstance().getMap(0));
        m_back = new BackGround(AppManager.getInstance().getBitmap(R.drawable.back));
        m_Effect = new EffectManagement();
        m_Effect.BuildObjects();
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

        for(int i = 0; i < MAX_PLAYER; ++i) {
            CollisionManager.checkBoxtoBox(m_player[i].m_collBox, m_map.m_Collbox, false);
            for (int j = i + 1; j < MAX_PLAYER; ++j)
                CollisionManager.checkBoxtoBox(m_player[i].m_collBox, m_player[j].m_collBox, false);
            CollisionManager.checkBoxtoBox(m_player[i].m_collBox, m_testblock.m_collBox, false);
            if(m_Gem.getDrawalbe()) {
                if(CollisionManager.checkBoxtoBox(m_player[i].m_collBox, m_Gem.m_collBox, true)) {
                    m_Gem.setDrawable(false);
                    m_Effect.StartStarEffect(m_Gem.GetPosition());
                }
            }
        }
        CollisionManager.checkBoxtoBox(m_testblock.m_collBox, m_map.m_Collbox, false);
        for(int i = 0; i < MAX_PLAYER; ++i)
            m_player[i].move(0, 10);

        m_testblock.update(time);
        m_Gem.update(time);
        m_Effect.Update(time);
    }

    @Override
    public void Render(Canvas canvas) {

        m_back.draw(canvas);
        m_map.draw(canvas);

        for(Player cur : m_player) {
            m_testblock.draw(canvas);
            cur.draw(canvas);
            if(cur == m_player[0]) {
                //cur.setState(Player.idle);

            }
        }
        m_Gem.draw(canvas);
        m_Effect.draw(canvas);
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
