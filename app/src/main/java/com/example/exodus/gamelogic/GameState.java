package com.example.exodus.gamelogic;
import com.example.exodus.BlockObject;
import com.example.exodus.framework.CollisionBox;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.provider.Contacts;
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
    private List<MoveBlock> m_moveblock;

    private MapObject m_map;
    private BackGround m_back;
    private EffectManagement m_Effect;

    private  UIManager m_ui;

    static public final int GAME_RUNNING = 0;
    static public final int GAME_PAUSE = 1;
    static public final int GAME_CLEAR = 2;
    static public final int GAME_FAIL = 3;

    static public final int NON_EVENT = 0;
    static public final int BLANK_EVENT = 1;
    static public final int NEXT_EVENT = 2;
    static public final int CLEAR_EVENT = 3;
    static public final int EXIT_EVENT = 4;


    private int m_thisStage;
    private int m_state;

    @Override
    public void Init(int mapIndex) {
        m_thisStage = mapIndex;

        m_blocks = new ArrayList<BlockObject>();
        m_map = new MapObject(AppManager.getInstance().getBitmap(R.drawable.tileset), 25, 23, AppManager.getInstance().getMap(mapIndex));
        List<Integer> startPoint = m_map.getStartPoint();
        List<Integer> boxPoint = m_map.getBoxPoint();
        List<Point> moveBlockPoint = m_map.getMoveBoxPoint();

        int[] doorPoint = m_map.get_doorPoint();
        int[] keyPoint = m_map.getKeyPoint();

        m_player = new Player[MAX_PLAYER];
        for(int i=0; i<MAX_PLAYER; ++i) {
            m_player[i] = new Player();
            m_player[i].setting(startPoint.get(i*2), startPoint.get(i*2 + 1));
        }

        for(int i = 0; i < boxPoint.size(); i+=2) {
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

        m_moveblock = new ArrayList<MoveBlock>();

        for(Point p : moveBlockPoint) {
            MoveBlock moveblock = new MoveBlock(AppManager.getInstance().getBitmap(R.drawable.longblock),
                    MoveBlock.FLAG_MOVE_UP, 50, 10);
            moveblock.SetNeedPlayer(3);
            moveblock.SetDestTileSize(4 , 1);
            moveblock.setting(p.x, p.y);
            m_moveblock.add(moveblock);
        }
    }

    @Override
    public void Destroy() {

    }

    @Override
    public void Update() {
        if(m_state == GAME_PAUSE)
            return;

        long time = System.currentTimeMillis();
        int[] numofOnMoveBox = new int[m_moveblock.size()];

        for(Player cur : m_player) {
            cur.update(time);
            for(int i = 0; i < m_moveblock.size(); ++i)
                if((cur.m_collBox.m_Collmovebox & (int)Math.pow(2, i)) > 0)
                    cur.move(m_moveblock.get(i).m_MDistanceNowFrame.x, m_moveblock.get(i).m_MDistanceNowFrame.y);
            cur.ResetCollside();
        }


        for(BlockObject b : m_blocks)
            b.ResetCollside();

        for(int i = 0; i < m_moveblock.size(); ++i)
            m_moveblock.get(i).ResetCollside();
        CollisionCheck();

        for(int i = 0; i < MAX_PLAYER; ++i) {
            if(m_player[i].State() != Player.jumpup) {
                //if (m_player[i].State() == Player.unclick) continue;
                m_player[i].move(0, 8);
            }
            if(m_player[i].GetPosition().y > AppManager.getInstance().getHeight())
                m_state = GAME_FAIL;
        }

        EndCollside();


        if(m_NumofGem == 0)
            m_Door.SetSpriteFrame(1);

        if(m_nNowPlayers <= 0) {
            m_state = GAME_CLEAR;
            AppManager.getInstance().setStageClearInfo(m_thisStage,  AppManager.getInstance().STAGE_CLEAR);
            if(m_thisStage + 1 < 10) {
                if (AppManager.getInstance().getStageClearinfo()[m_thisStage + 1] != AppManager.getInstance().STAGE_CLEAR)
                    AppManager.getInstance().setStageClearInfo(m_thisStage + 1, AppManager.getInstance().STAGE_OPEN);
            }
        }

        for(BlockObject b : m_blocks)
            b.update(time);

        for(Player cur : m_player)
            for(int i = 0; i < m_moveblock.size(); ++i)
                numofOnMoveBox[i] = (cur.m_collBox.m_Collmovebox & (int)Math.pow(2, i)) > 0 ? ++numofOnMoveBox[i] : numofOnMoveBox[i];

        m_Gem.update(time);
        m_Effect.Update(time);
        for(int i = 0; i < m_moveblock.size(); ++i) {
            m_moveblock.get(i).Work(numofOnMoveBox[i]);
            Log.d("MoveBox", String.valueOf(numofOnMoveBox[i]));
            m_moveblock.get(i).update(time);
        }
    }

    public void CollisionCheck() {
        for(int i = 0; i < MAX_PLAYER; ++i)
            for(int j = 0; j < m_moveblock.size(); ++j)
                CollisionManager.checkBoxtoBox(m_player[i].m_collBox, m_moveblock.get(j).m_collBox,  CollisionManager.COLL_MOVEBOX, j);

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
            for (int j = 0; j < MAX_PLAYER; ++j)
                if(i != j && !m_player[j].GetClear() && !m_player[i].GetClear())
                    CollisionManager.checkBoxtoBox(m_player[i].m_collBox, m_player[j].m_collBox, CollisionManager.COLL_PLAYER, 0);

            // 플레이어 - 블럭 충돌
            for(BlockObject b : m_blocks)
                CollisionManager.checkBoxtoBox(m_player[i].m_collBox, b.m_collBox, CollisionManager.COLL_MAP, 0);

            // 플레이어 - 보석
            if (m_Gem.m_player == null && m_Gem.getDrawalbe()) {
                if (Rect.intersects(m_player[i].CollisionBox(), m_Gem.CollisionBox())) {
                    m_Gem.SetPlayer(m_player[i]);
                }
            }
        }

        for(int j = 0; j < MAX_PLAYER; ++j)
            for(int i = 0; i < m_moveblock.size(); ++i)
                InfectionMovebox(j, i);

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
                CollisionManager.checkBoxtoBox(m_blocks.get(i).m_collBox, m_blocks.get(j).m_collBox, CollisionManager.COLL_PLAYER, 0);
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
        for(int i = 0; i < m_moveblock.size(); ++i)
            m_moveblock.get(i).draw(canvas);
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
                        touched = EXIT_EVENT;
                    }
                    else if(uiEvent == 1) {
                        touched = NEXT_EVENT;
                    }
                    break;
                case GAME_FAIL:
                    if(uiEvent == 0)
                        touched = EXIT_EVENT;
                    else
                        resetMap();
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
        m_Gem.SetPlayer(null);

        m_NumofGem = 1;
        m_nNowPlayers = 6;

        m_state = GAME_RUNNING;
    }

    // 움직이는 벽과 충돌한 캐릭터주위에 있는 캐릭터들을 탐색하기 위한 함수
    public void InfectionMovebox(int index1, int boxindex) {
        if(index1 >= MAX_PLAYER) return ;
        if(!((m_player[index1].m_collBox.m_Collmovebox & (int)Math.pow(2, boxindex)) > 0)) {
            InfectionMovebox(index1 + 1, boxindex);
            return ;
        }
        for(int i = 0 ; i < MAX_PLAYER; ++i) {
            if (i != index1) {
                if (!((m_player[i].m_collBox.m_Collmovebox & (int)Math.pow(2, boxindex)) > 0))
                    if (CollisionManager.InfectionMovebox(m_player[index1].m_collBox, m_player[i].m_collBox, boxindex))
                        InfectionMovebox(i, boxindex);
            } else continue;
        }
    }
}
