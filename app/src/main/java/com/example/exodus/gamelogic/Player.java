package com.example.exodus.gamelogic;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.exodus.R;
import com.example.exodus.framework.AppManager;
import com.example.exodus.framework.SpriteObject;

/**
 * Created by yeosu on 2018-05-20.
 */

public class Player{
    private SpriteObject[] m_ani;

    private int m_state;
    private int m_dir;
    private boolean m_clear;

    private int m_x, m_y;

    public static final int unclick = 8;
    public static final int idle = 0;
    public static final int run = 2;
    public static final int jumpup = 4;
    public static final int jumpdown = 6;

    public Player() {
        m_ani = new SpriteObject[9];
        m_ani[0] = new SpriteObject(AppManager.getInstance().getBitmap(R.drawable.idle1));
        m_ani[1] = new SpriteObject(AppManager.getInstance().getBitmap(R.drawable.idle2));
        m_ani[2] = new SpriteObject(AppManager.getInstance().getBitmap(R.drawable.run1));
        m_ani[3] = new SpriteObject(AppManager.getInstance().getBitmap(R.drawable.run2));
        m_ani[4] = new SpriteObject(AppManager.getInstance().getBitmap(R.drawable.jumpup1));
        m_ani[5] = new SpriteObject(AppManager.getInstance().getBitmap(R.drawable.jumpup2));
        m_ani[6] = new SpriteObject(AppManager.getInstance().getBitmap(R.drawable.jumpdown1));
        m_ani[7] = new SpriteObject(AppManager.getInstance().getBitmap(R.drawable.jumpdown2));
        m_ani[8] = new SpriteObject(AppManager.getInstance().getBitmap(R.drawable.unclick));

        m_ani[0].initSpriteData(12, 4, 2);
        m_ani[1].initSpriteData(12, 4, 2);
        m_ani[2].initSpriteData(12, 6, 2);
        m_ani[3].initSpriteData(12, 6, 2);
        m_ani[4].initSpriteData(12, 1, 2);
        m_ani[5].initSpriteData(12, 1, 2);
        m_ani[6].initSpriteData(12, 1, 2);
        m_ani[7].initSpriteData(12, 1, 2);
        m_ani[8].initSpriteData(12, 1, 2);

        this.setPosition(0,0);
        m_state = unclick;
        m_dir = 0;
    }

    public void setting(int x, int y) {
        m_clear = false;
        m_state = unclick;
        this.setPosition(x, y);
        m_ani[m_state].setPosition(x, y);
    }

    public void draw(Canvas canvas) {
        if(m_clear)
            return;
        if(m_state == unclick)
            m_ani[m_state].draw(canvas);
        else
            m_ani[m_state + m_dir].draw(canvas);
    }

    public void update(long time) {
        if(m_clear)
            return;
        if(m_state < unclick) {
            m_ani[m_state + m_dir].update(time);
            //m_ani[m_state + m_dir].setPosition(m_x, m_y);
        }
    }

    public void setPosition(int x, int y) {
        m_x = x;
        m_y = y;
    }

    public void move(int x, int y) {
        m_x += x;
        m_y += y;
        m_ani[m_state + m_dir].setPosition(m_x, m_y);
    }

    public void setState(int state) {
        m_state = state;
        m_ani[m_state + m_dir].setPosition(m_x, m_y);
    }
    public void setDir(int dir) {
        m_dir = dir;
        m_ani[m_state + m_dir].setPosition(m_x, m_y);
    }
}
