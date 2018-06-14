package com.example.exodus.framework;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.example.exodus.R;
import com.example.exodus.gamelogic.Player;

/**
 * Created by MSI on 2018-05-30.
 */

public class MainState implements IState {

    final private int YCOUNT = 5;
    final private int XCOUNT = 6;
    final private int MAX_PLAYER = 6;
    final private float SCALE_SIZE = 1.5f;
    static public final int START_SCREEN  = 0;
    static public final int SELECT_SCREEN = 1;

    private int m_state;
    public int m_selectedstage;

    // 모든 상태에서 렌더링
    private Player[] m_player;
    private int[] m_clearstageinfo;
    private SpriteObject m_map;
    private BackGround m_back;

    // 스타트 스크린에서 렌더링
    private SpriteObject m_title;
    private SpriteObject m_gamestart;

    // 선택창에서 렌더링
    private SpriteObject m_clearstage;
    private SpriteObject m_stagebackground;
    private SpriteObject m_unclearstage;
    private SpriteObject m_stage;
    private Point m_offsettile;
    private Point m_stagestartpos;
    private Point m_backgroundtilesize;

    @Override
    public void Init(int index) {

        m_selectedstage = 0;
        m_state = index;
        // 모든 상태에서 렌더링 하는 오브젝트
        m_back  = new BackGround(AppManager.getInstance().getBitmap(R.drawable.back));
        m_map   = new SpriteObject(AppManager.getInstance().getBitmap(R.drawable.ground));
        m_clearstageinfo = AppManager.getInstance().getStageClearinfo();

        m_map.initSpriteData(1,1,1);
        m_map.SetDest(new Point(AppManager.getInstance().getWidth(), AppManager.getInstance().getHeight()));
        m_map.setPosition(0, 0);

        m_player = new Player[MAX_PLAYER];

        Point tilesize = new Point(AppManager.getInstance().getTileWidth(), AppManager.getInstance().getTileHeight());
        for(int i=0; i<MAX_PLAYER; ++i) {
            m_player[i] = new Player();
            m_player[i].setting(tilesize.x * i * 2, (int)(tilesize.y * 23.5));
            m_player[i].setState(Player.run);
        }

        InitRendStartScreen();
        InitRendSelectScreen();

        //SoundManager.getInstance().play(0, -1);
    }

    private void InitRendStartScreen() {
        // 스타트 스크린에서 렌더링 하는 오브젝트
        m_title = new SpriteObject(AppManager.getInstance().getBitmap(R.drawable.title));
        m_title.initSpriteData(1, 1, 1);
        m_title.SetDestTileSize(30, 6);
        m_title.setPosition(AppManager.getInstance().getWidth() / 2 - m_title.GetDest().x / 2,
                AppManager.getInstance().getHeight() / 5 - m_title.GetDest().y / 2);

        m_gamestart = new SpriteObject(AppManager.getInstance().getBitmap(R.drawable.touchtogamestart));
        m_gamestart.initSpriteData( 2, 2, 3);
        m_gamestart.SetDestTileSize(6, 1);
        m_gamestart.setPosition(AppManager.getInstance().getWidth() / 2 - (int)(m_gamestart.GetDest().x * 1.5),
                AppManager.getInstance().getHeight() / 2 + m_gamestart.GetDest().y * 2);

        m_state = START_SCREEN;
    }
    private void InitRendSelectScreen() {
        Point screen = new Point(AppManager.getInstance().getWidth() , AppManager.getInstance().getHeight());
        Point tile = new Point(AppManager.getInstance().getTileWidth(), AppManager.getInstance().getTileHeight());

        m_stage = new SpriteObject(AppManager.getInstance().getBitmap(R.drawable.stagetext));
        m_stage.initSpriteData(1, 1, 1);
        m_stage.SetDestTileSize(16, 4);
        m_stage.setPosition(screen.x / 2 - m_stage.GetDest().x / 2,
                screen.y / 5 - m_stage.GetDest().y / 2);

        m_backgroundtilesize = new Point(54, 20);

        m_stagebackground = new SpriteObject(AppManager.getInstance().getBitmap(R.drawable.selectbackground));
        m_stagebackground.initSpriteData(1, 1, 1);
        m_stagebackground.SetDestTileSize(m_backgroundtilesize.x, m_backgroundtilesize.y);
        m_stagebackground.setPosition(screen.x / 2 - (m_stagebackground.GetDest().x / 2),
                screen.y / 2 - m_stagebackground.GetDest().y / 3);

        m_offsettile = new Point(m_stagebackground.GetDest().x / XCOUNT,
                m_stagebackground.GetDest().y / YCOUNT);
        m_stagestartpos = new Point(m_stagebackground.getX(), m_stagebackground.getY());

        m_unclearstage  = new SpriteObject(AppManager.getInstance().getBitmap(R.drawable.unclearstate));
        m_unclearstage.initSpriteData(1, 11, 1);
        m_unclearstage.SetDestTileSize(
                (int)(m_offsettile.x * SCALE_SIZE) / (int)(tile.x * ratioStageIcon()),
                (int)(m_offsettile.y * SCALE_SIZE) / tile.x
        );

        m_clearstage    = new SpriteObject(AppManager.getInstance().getBitmap(R.drawable.clearstage));
        m_clearstage.initSpriteData(1, 10, 1);
        m_clearstage.SetDest(m_unclearstage.GetDest());
    }

    public float ratioStageIcon() {
        return ((float)m_backgroundtilesize.x / (float)XCOUNT) / ((float)m_backgroundtilesize.y / (float)YCOUNT);
    }

    @Override
    public void Destroy() {

    }

    @Override
    public void Update() {
        long time = System.currentTimeMillis();
        m_gamestart.update(time);
        for(int i=0; i<MAX_PLAYER; ++i) {
            m_player[i].update(time);
            m_player[i].move(10, 0);
            if(m_player[i].GetPosition().x > AppManager.getInstance().getWidth())
                m_player[i].move(-AppManager.getInstance().getWidth() -
                        m_player[i].CollisionBox().width() , 0);
        }
    }

    @Override
    public void Render(Canvas canvas) {
        m_back.draw(canvas);
        m_map.draw(canvas);
        for(int i=0; i<MAX_PLAYER; ++i) {
            m_player[i].draw(canvas);
        }

        if(m_state == START_SCREEN) RendStartScreen(canvas);
        else if (m_state == SELECT_SCREEN) RenderSelectScreen(canvas);
    }

    private void RendStartScreen(Canvas canvas) {
        m_gamestart.draw(canvas);
        m_title.draw(canvas);
    }

    private void RenderSelectScreen(Canvas canvas) {
        m_stage.draw(canvas);
        m_stagebackground.draw(canvas);
        Point size = m_unclearstage.GetDest();

        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 5; j++) {
                Point newpos = new Point(m_stagestartpos.x + m_offsettile.x * (j + 1) - size.x / 2 ,
                        m_stagestartpos.y + m_offsettile.y * (i * 2 + 1) );
                if(m_clearstageinfo[5 * i + j] == AppManager.getInstance().STAGE_CLEAR) {
                    m_clearstage.SetFrame( 5 * i + j  );
                    m_clearstage.setPosition(newpos);
                    m_clearstage.draw(canvas);
                }
                else {
                    if (m_clearstageinfo[5 * i + j] == AppManager.getInstance().STAGE_OPEN)
                        m_unclearstage.SetFrame(5 * i + j + 1);

                    else
                        m_unclearstage.SetFrame(0);

                    m_unclearstage.setPosition(newpos);
                    m_unclearstage.draw(canvas);
                }
            }
        }
    }

    @Override
    public void MovePlayers(boolean isJump, int moveX) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public int onTouchEvent(MotionEvent event) {
        if(m_state == START_SCREEN)  m_state = SELECT_SCREEN;
        else if(m_state == SELECT_SCREEN && event.getAction() == MotionEvent.ACTION_DOWN)
            return FindObject((int)event.getX(), (int)event.getY());
        return -10;
    }

    public int FindObject(int x, int y) {
        Point size = m_unclearstage.GetDest();

        x -= m_stagestartpos.x;
        y -= m_stagestartpos.y;

        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 5; j++) {
                int xpos = m_offsettile.x * (j + 1) - size.x / 2;
                int ypos = m_offsettile.y * (i * 2 + 1);

                Rect newrect = new Rect(xpos, ypos,
                        xpos + size.x ,ypos + size.y);
                if(newrect.contains(x, y)) {
                    int stagenum = 5 * i + j + 1;
                    if( m_clearstageinfo[stagenum - 1] != AppManager.getInstance().STAGE_LOCK)
                        return stagenum;
                    else
                        return -10;
                }
            }
        }
        return -10;
    }
}
