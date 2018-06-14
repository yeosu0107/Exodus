package com.example.exodus.framework;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.exodus.R;
import com.example.exodus.gamelogic.GameState;
import com.example.exodus.gamelogic.JumpButton;
import com.example.exodus.gamelogic.VirtualJoystick;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by 여성우 on 2018-05-16.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameViewThread  m_thread;
    private IState          m_state;
    private VirtualJoystick m_stick;
    private JumpButton      m_jump;
    private int             move_x;

    private int m_nowScene;
    private int m_stage = 0;

    final int MAX_MAP = 10;
    final int MAIN_SCENE = 0;
    final int GAME_SCENE = 1;

    public GameView(Context context) throws IOException {
        super(context);

        SoundManager.getInstance().Init(context);
        //SoundManager.getInstance().addSound(0, R.raw.bgm);
        SoundManager.getInstance().addSound(1, R.raw.clear);
        SoundManager.getInstance().addSound(2, R.raw.jump);

        AppManager.getInstance().setGameview(this);
        AppManager.getInstance().setResources(getResources());

        //키보드 이용
        setFocusable(true);

        getHolder().addCallback(this);
        m_thread = new GameViewThread(getHolder(), this);
        InputStream is = null;
        List<int[]> tmp = null;
        for(int i=0; i<MAX_MAP; ++i) {
            is = getResources().getAssets().open("map_no"+i+".csv");
            tmp = CSVReader.read(is);
            AppManager.getInstance().addMap(tmp);
        }


        m_stage = 0;
        ChangeGameState(new MainState(), m_stage);

        m_stick = new VirtualJoystick();
        m_jump = new JumpButton();


    }

    //@Override
    protected void MyonDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        m_state.Render(canvas);
        if(m_stick.isDraw())
            m_stick.render(canvas);
        if(m_jump.isJump())
            m_jump.draw(canvas);
    }

    void Update() {
        m_state.MovePlayers(m_jump.isJump(), move_x);
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
        return m_state.onKeyDown(keyCode, event);
        //return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        //return super.onKeyUp(keyCode, event);
        return m_state.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int gameEvent = m_state.onTouchEvent(event);

        if(m_nowScene == MAIN_SCENE) {
            if(gameEvent > 0) {
                m_nowScene = GAME_SCENE;
                m_stage = gameEvent - 1;
                ChangeGameState(new GameState(), gameEvent - 1);
                return true;
            }
            else
                return true;
        }

        if(gameEvent == GameState.NON_EVENT) { //state에서 터치이벤트가 없을 때만 발동
            move_x = 0;
            final int action = event.getAction();
            final int pointer_count = event.getPointerCount();
            float multi_x, multi_y;
            int pointerIndex = -1;

            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: // 처음 터치가 눌러졌을 때
                    if (event.getX() < AppManager.getInstance().getWidth() / 2-100) {
                        m_stick.enableJoystick((int) event.getX(), (int) event.getY());
                    } else if(event.getX() > AppManager.getInstance().getWidth() / 2+100){
                        m_jump.setPosition((int) event.getX(), (int) event.getY());
                        m_jump.setJump(true);
                        SoundManager.getInstance().play(2, 0);
                    }
                    break;
                case MotionEvent.ACTION_MOVE: // 터치가 눌린 상태에서 움직일 때
                    m_jump.setJump(false);
                    for(int i=0; i<pointer_count; ++i) {
                        if (event.getX(i) < AppManager.getInstance().getWidth() / 2-100) {
                            //move_x = m_stick.moveJoystick((int) event.getX(i), (int) event.getY(i));
                            m_stick.moveJoystick((int) event.getX(i), (int) event.getY(i));
                        }
                        else if (event.getX(i) > AppManager.getInstance().getWidth() / 2-100 &&
                                event.getX(i) < AppManager.getInstance().getWidth() / 2+100){
                            m_stick.disableJoystick();
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP: // 터치가 떼어졌을 때
                    if (event.getX() < AppManager.getInstance().getWidth() / 2-100) {
                        m_stick.disableJoystick();
                    } else if(event.getX() > AppManager.getInstance().getWidth() / 2+100){
                        m_jump.setJump(false);
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN: // 터치가 두 개 이상일 때 눌러졌을 때
                    pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    multi_x = event.getX(pointerIndex);
                    multi_y = event.getY(pointerIndex);

                    if (multi_x < AppManager.getInstance().getWidth() / 2-100) {
                        m_stick.enableJoystick((int) multi_x, (int) multi_y);
                    } else if(multi_x > AppManager.getInstance().getWidth() / 2+100){
                        m_jump.setPosition((int) multi_x, (int) multi_y);
                        m_jump.setJump(true);
                        SoundManager.getInstance().play(2, 0);
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP: // 터치가 두 개 이상일 때 떼어졌을 때
                    pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    multi_x = event.getX(pointerIndex);

                    if (multi_x < AppManager.getInstance().getWidth() / 2-100) {
                        m_stick.disableJoystick();
                    } else if(multi_x > AppManager.getInstance().getWidth() / 2+100){
                        m_jump.setJump(false);
                    }
                    break;

                default:
                    break;
            }
            if(m_stick.isDraw()) {
                move_x = m_stick.distX();
            }
            //m_state.MovePlayers(m_jump.isJump(), move_x);
        }
        else {
            m_stick.disableJoystick();
            m_jump.setJump(false);
            if(gameEvent == GameState.EXIT_EVENT) {
                m_nowScene = MAIN_SCENE;
                ChangeGameState(new MainState(), MainState.SELECT_SCREEN);
            }
            if(gameEvent == GameState.NEXT_EVENT) {
                //AppManager.getInstance().setStageClearInfo(m_stage,  AppManager.getInstance().STAGE_CLEAR);
                m_stage += 1;

                if (m_stage > MAX_MAP - 1)
                    m_stage = 0;
                //if (AppManager.getInstance().getStageClearinfo()[m_stage] != AppManager.getInstance().STAGE_CLEAR)
                    //AppManager.getInstance().setStageClearInfo(m_stage,  AppManager.getInstance().STAGE_OPEN);
                ChangeGameState(new GameState(), m_stage);
            }

        }
        return true;
    }

    public void ChangeGameState(IState state, int index) {
        if(m_state != null)
            m_state.Destroy();

        state.Init(index);
        m_state = state;
    }
}
