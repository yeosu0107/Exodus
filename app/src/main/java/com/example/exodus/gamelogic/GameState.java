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

    private int m_NumofGem = 1;
    private Player[] m_player;
    private BlockObject m_testblock;
    private BlockObject m_Gem;
    private BlockObject m_Door;

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

        m_Door = new BlockObject(AppManager.getInstance().getBitmap(R.drawable.door), 1, 2, 2,
                BlockObject.FLAG_HOLDING | BlockObject.FLAG_NO_CHANGE_SPRITE);
        m_Door.settingBoxsize(54,18);

        m_Gem = new BlockObject(AppManager.getInstance().getBitmap(R.drawable.gem),
                10, 5, 1, BlockObject.FLAG_HOLDING);
        m_Gem.setting(100, 600);
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

        CollisionCheck();

        for(int i = 0; i < MAX_PLAYER; ++i) {
            if(m_player[i].State() != Player.jumpup) {
                if (m_player[i].State() == Player.unclick) continue;
                m_player[i].move(0, 8);
            }
        }

        EndCollside();


        if(m_NumofGem == 0)
            m_Door.SetSpriteFrame(1);
        m_testblock.update(time);
        m_Gem.update(time);
        m_Effect.Update(time);
    }

    public void CollisionCheck() {
        for(int i = 0; i < MAX_PLAYER; ++i) {

            //  플레이어 - 문 충돌
            if (m_Door.NowFrame() == 1) {
                if (!m_player[i].GetClear() && Rect.intersects(m_player[i].CollisionBox(), m_Door.CollisionBox())) {
                    m_Effect.StartStarEffect(m_player[i].GetPosition());
                    m_player[i].SetClear(true);
                }
            }

            // 플레이어 - 맵 충돌
            m_map.CollisionCheck(m_player[i].m_collBox);
            for (int j = i + 1; j < MAX_PLAYER; ++j)
                CollisionManager.checkBoxtoBox(m_player[i].m_collBox, m_player[j].m_collBox, false);

            CollisionManager.checkBoxtoBox(m_player[i].m_collBox, m_testblock.m_collBox, true);
            if (m_Gem.m_player == null && m_Gem.getDrawalbe()) {
                if (Rect.intersects(m_player[i].CollisionBox(), m_Gem.CollisionBox())) {
                    m_Gem.SetPlayer(m_player[i]);
                }
            }
        }

        if(m_Gem.getDrawalbe()) {
            if(Rect.intersects(m_Door.CollisionBox(), m_Gem.CollisionBox())) {
                m_Gem.setDrawable(false);
                m_NumofGem--;
            }

        }

        m_map.CollisionCheck(m_testblock.m_collBox);
    }
    public void EndCollside() {
        for(Player cur : m_player) {
            cur.EndCollision();
        }
        m_testblock.EndCollision();
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
        m_Door.draw(canvas);
        m_Effect.draw(canvas);
    }

    @Override
    public void MovePlayers(boolean isJump, int moveX) {
        for(Player cur : m_player) {
            if (cur.State() == Player.unclick)
                continue;
            if (isJump) {
                if (cur.State() != Player.jumpdown) {
                    //Log.d("IsJump", String.valueOf(m_player[0].State()));
                    cur.setState(Player.jumpup);
                }
            }
            if (moveX >-40 && moveX<40) {
                if (cur.IsJump()) {
                    cur.setState(Player.idle);
                }
            } else if (moveX > 40) {
                cur.setDir(0);
                if (cur.IsJump()) {
                    cur.setState(Player.run);
                }
                cur.move(10, 0);
            } else if (moveX < -40) {
                cur.setDir(1);
                if (cur.IsJump()) {
                    cur.setState(Player.run);
                }
                cur.move(-10, 0);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_W) {
            if(m_player[0].IsJump())
                m_player[0].setState(Player.run);
        }
        if(keyCode == event.KEYCODE_S) {
            if(m_player[0].IsJump())
                m_player[0].setState(Player.run);
            m_player[0].move(0, 10);
        }
        if(keyCode == event.KEYCODE_A) {
            m_player[0].setDir(1);
            if(m_player[0].IsJump())
                m_player[0].setState(Player.run);
            m_player[0].move(-10, 0);
        }
        if(keyCode == event.KEYCODE_D) {
            m_player[0].setDir(0);
            if(m_player[0].IsJump())
                m_player[0].setState(Player.run);
            m_player[0].move(10, 0);
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        //m_player[0].setState(Player.idle);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean touched = false;
        if(event.getAction() != MotionEvent.ACTION_DOWN) return false;
        for(Player cur : m_player) {
            Rect playerRect = cur.CollisionBox();
            if(playerRect.contains((int)event.getX(), (int)event.getY())) {
                if (cur.State() != Player.unclick)
                    cur.setState(Player.unclick);
                else
                    cur.setState(Player.idle);
                touched = true;
            }
        }
        return touched;
    }
}
