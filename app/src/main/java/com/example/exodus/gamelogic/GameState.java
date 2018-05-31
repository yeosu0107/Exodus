package com.example.exodus.gamelogic;
import com.example.exodus.BlockObject;
import com.example.exodus.framework.CollisionBox;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.provider.Contacts;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.example.exodus.R;
import com.example.exodus.framework.AppManager;
import com.example.exodus.framework.BackGround;
import com.example.exodus.framework.CollisionManager;
import com.example.exodus.framework.EffectManagement;
import com.example.exodus.framework.IState;
import com.example.exodus.framework.MapObject;
import com.example.exodus.gamelogic.UIManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 여성우 on 2018-05-17.
 */

public class GameState implements IState{

    final int MAX_PLAYER = 6;

    private int m_nNowPlayers;
    private int m_NumofGem = 1;
    private Player[] m_player;
    private List<BlockObject> m_blocks;
    private BlockObject m_Gem;
    private BlockObject m_Door;

    private MapObject m_map;
    private BackGround m_back;
    private EffectManagement m_Effect;

    private  UIManager m_ui;

    static public final int GAME_RUNNING = 0;
    static public final int GAME_PAUSE = 1;
    static public final int GAME_CLEAR = 2;

    static public final int NON_EVENT = 0;
    static public final int BLANK_EVENT = 1;
    static public final int NEXT_EVENT = 2;
    static public final int HOME_EVENT = 3;
    static public final int EXIT_EVENT = 4;



    private int m_state;

    @Override
    public void Init(int mapIndex) {
        m_blocks = new ArrayList<BlockObject>();
        m_map = new MapObject(AppManager.getInstance().getBitmap(R.drawable.tileset), 25, 23, AppManager.getInstance().getMap(mapIndex));
        List<Integer> startPoint = m_map.getStartPoint();
        List<Integer> boxPoint = m_map.getBoxPoint();
        int[] doorPoint = m_map.get_doorPoint();
        int[] keyPoint = m_map.getKeyPoint();

        m_player = new Player[MAX_PLAYER];
        for(int i=0; i<MAX_PLAYER; ++i) {
            m_player[i] = new Player();
            m_player[i].setting(startPoint.get(i*2), startPoint.get(i*2 + 1));
        }

        for(int i = 0; i < boxPoint.size() / 2; ++i) {
            BlockObject newBlock = new BlockObject(AppManager.getInstance().getBitmap(R.drawable.crate), 1, 1, 2, 0);
            newBlock.setting(boxPoint.get(i), boxPoint.get(i + 1));
            m_blocks.add(newBlock);
        }

        m_Door = new BlockObject(AppManager.getInstance().getBitmap(R.drawable.door), 1, 2, 2,
                BlockObject.FLAG_HOLDING | BlockObject.FLAG_NO_CHANGE_SPRITE);
        //m_Door.settingBoxsize(54,18);
        m_Door.setting(doorPoint[0],doorPoint[1]);
        m_Gem = new BlockObject(AppManager.getInstance().getBitmap(R.drawable.gem),
                10, 5, 1, BlockObject.FLAG_HOLDING);
        m_Gem.setting(keyPoint[0], keyPoint[1]);

        m_back = new BackGround(AppManager.getInstance().getBitmap(R.drawable.back));
        m_Effect = new EffectManagement();
        m_Effect.BuildObjects();

        m_state = GAME_RUNNING;
        m_ui = new UIManager();

        m_nNowPlayers = 6;
    }

    @Override
    public void Destroy() {

    }

    @Override
    public void Update() {
        if(m_state == GAME_PAUSE)
            return;

        long time = System.currentTimeMillis();
        for(Player cur : m_player) {
            cur.update(time);
            cur.ResetCollside();
        }

        for(BlockObject b : m_blocks)
            b.ResetCollside();

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

        if(m_nNowPlayers <= 0)
            m_state = GAME_CLEAR;

        for(BlockObject b : m_blocks)
            b.update(time);
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
                    m_nNowPlayers -= 1;
                }
            }

            // 플레이어 - 맵 충돌
            m_map.CollisionCheck(m_player[i].m_collBox);
            for (int j = i + 1; j < MAX_PLAYER; ++j)
                CollisionManager.checkBoxtoBox(m_player[i].m_collBox, m_player[j].m_collBox, false);

            // 플레이어 - 블럭 충돌
            for(BlockObject b : m_blocks)
                CollisionManager.checkBoxtoBox(m_player[i].m_collBox, b.m_collBox, true);

            // 플레이어 - 보석
            if (m_Gem.m_player == null && m_Gem.getDrawalbe()) {
                if (Rect.intersects(m_player[i].CollisionBox(), m_Gem.CollisionBox())) {
                    m_Gem.SetPlayer(m_player[i]);
                }
            }
        }

        // 문 - 보석
        if(m_Gem.getDrawalbe()) {
            if(Rect.intersects(m_Door.CollisionBox(), m_Gem.CollisionBox())) {
                m_Gem.setDrawable(false);
                m_NumofGem--;
            }

        }

        // 블럭 - 블럭
        for(int i = 0; i < m_blocks.size(); i++) {
            for(int j = 1; j < m_blocks.size(); j++)
                CollisionManager.checkBoxtoBox(m_blocks.get(i).m_collBox, m_blocks.get(j).m_collBox, false);
        }

        // 블럭 - 맵
        for(BlockObject b : m_blocks)
            m_map.CollisionCheck(b.m_collBox);
    }
    public void EndCollside() {
        for(Player cur : m_player) {
            cur.EndCollision();
        }
        for(BlockObject b : m_blocks)
            b.EndCollision();
    }


    @Override
    public void Render(Canvas canvas) {

        m_back.draw(canvas);
        m_map.draw(canvas);

        for(BlockObject b : m_blocks)
            b.draw(canvas);

        for(Player cur : m_player) {
            cur.draw(canvas);
            if(cur == m_player[0]) {
                //cur.setState(Player.idle);

            }
        }
        m_Gem.draw(canvas);
        m_Door.draw(canvas);
        m_Effect.draw(canvas);

        m_ui.render(m_state, canvas);
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

        if(keyCode == event.KEYCODE_0) {
            resetMap();
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        //m_player[0].setState(Player.idle);
        return true;
    }

    @Override
    public int onTouchEvent(MotionEvent event) {
        int touched = NON_EVENT;
        //if(event.getAction() != MotionEvent.ACTION_DOWN) return false;
        int uiEvent = m_ui.touchEvent(m_state, (int)event.getX(), (int)event.getY());

        if(uiEvent == -1) {
            for (Player cur : m_player) {
                Rect playerRect = cur.CollisionBox();
                if (playerRect.contains((int) event.getX(), (int) event.getY())) {
                    touched = BLANK_EVENT;
                    if (event.getAction() != MotionEvent.ACTION_DOWN) continue;
                    if (cur.State() != Player.unclick)
                        cur.setState(Player.unclick);
                    else
                        cur.setState(Player.idle);
                    //touched = true;
                }
            }

            if(event.getX() > AppManager.getInstance().getWidth() - 100 && event.getY() > AppManager.getInstance().getHeight() - 100 &&
                    event.getAction() == MotionEvent.ACTION_DOWN)
                touched = NEXT_EVENT;
        }
        else {
            touched = BLANK_EVENT;
            if(event.getAction() != MotionEvent.ACTION_DOWN)
                return touched;
            switch (m_state) {
                case GAME_RUNNING:
                    m_state = GAME_PAUSE;
                    break;
                case GAME_PAUSE:
                    if(uiEvent == 0)
                        resetMap();
                    else if(uiEvent == 1) {
                        touched = EXIT_EVENT;
                    }
                    else if(uiEvent == -10)
                        m_state = GAME_RUNNING;
                    break;
                case GAME_CLEAR:
                    if(uiEvent == 0) {

                    }
                    else if(uiEvent == 1) {
                        touched = NEXT_EVENT;
                    }
                    else if(uiEvent == 2) {

                    }
                    break;
            }
        }
        return touched;
    }

    public void resetMap() {
        List<Integer> startPoint = m_map.getStartPoint();
        List<Integer> boxPoint = m_map.getBoxPoint();
        int[] doorPoint = m_map.get_doorPoint();
        int[] keyPoint = m_map.getKeyPoint();

        for(int i=0; i<MAX_PLAYER; ++i) {
            m_player[i].setting(startPoint.get(i*2), startPoint.get(i*2 + 1));
            m_player[i].SetClear(false);
        }

        for(int i = 0; i < boxPoint.size() / 2; ++i) {
            m_blocks.get(i).setting(boxPoint.get(i), boxPoint.get(i + 1));
        }

        m_Door.setting(doorPoint[0],doorPoint[1]);
        m_Door.setDrawable(true);
        m_Door.SetSpriteFrame(0);

        m_Gem.setting(keyPoint[0], keyPoint[1]);
        m_Gem.setDrawable(true);

        m_NumofGem = 1;
        m_nNowPlayers = 6;

        m_state = GAME_RUNNING;
    }
}
