package com.example.exodus.gamelogic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.example.exodus.R;
import com.example.exodus.framework.AppManager;
import com.example.exodus.framework.CollisionBox;
import com.example.exodus.framework.CollisionManager;
import com.example.exodus.framework.SpriteObject;

/**
 * Created by yeosu on 2018-05-20.
 */

public class Player{
    private SpriteObject[] m_ani;
    private int m_state;
    private int m_dir;
    private boolean m_clear;
    private int m_jumpHeight;

    private int m_x, m_y;
    public CollisionBox m_collBox;
    public int MAX_JUMP_HEIGHT;

    public static final int JUMP_SPEED = -8;
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
        m_jumpHeight = 0;
        MAX_JUMP_HEIGHT = AppManager.getInstance().getTileHeight() * -3;
        m_collBox = new CollisionBox(new Rect(0, 0, AppManager.getInstance().getTileWidth(), AppManager.getInstance().getTileHeight()), 2);
    }

    public void setting(int x, int y) {
        m_clear = false;
        m_state = unclick;
        this.setPosition(x, y);
        m_ani[m_state].setPosition(x, y);
        m_collBox.SetPosition(x,y);
    }

    public void draw(Canvas canvas) {
        if(m_clear)
            return;
        m_collBox.DrawCollisionBox(canvas);
        if(m_state == unclick)
            m_ani[m_state].draw(canvas);
        else
            m_ani[m_state + m_dir].draw(canvas);

    }
    public boolean IsJump() {
        return m_state != Player.jumpdown && m_state != Player.jumpup;
    }

    public void update(long time) {
        if(m_clear)
            return;
        if(m_state == jumpup) {
            move(0, JUMP_SPEED);
            m_jumpHeight += JUMP_SPEED;
            if(m_jumpHeight < MAX_JUMP_HEIGHT || !m_collBox.IsEnableMove(CollisionManager.SIDE_TOP)) {
                m_jumpHeight = 0;
                m_state = jumpdown;
            }

        }
        if(m_state < unclick) {
            m_ani[m_state + m_dir].update(time);
            //m_ani[m_state + m_dir].setPosition(m_x, m_y);
        }
    }

    public void setPosition(int x, int y) {
        m_x = x;
        m_y = y;
        if(m_collBox != null)
            m_collBox.SetPosition(x, y);
    }

    public void move(int x, int y) {
        if(x > 0 && !m_collBox.IsEnableMove(CollisionManager.SIDE_RIGHT)) x = 0;
        else if(x < 0 && !m_collBox.IsEnableMove(CollisionManager.SIDE_LEFT)) x = 0;
        if( y > 0 && !m_collBox.IsEnableMove(CollisionManager.SIDE_BOTTOM)) {
            if(m_state == jumpdown)
                m_state = idle;
            y = 0 ;
        }
        else if( y < 0 && !m_collBox.IsEnableMove(CollisionManager.SIDE_TOP)) y = 0;

        if( y > 0) m_state = jumpdown;

        m_collBox.Move(x, y);
        m_x = m_collBox.GetPosition().x;
        m_y = m_collBox.GetPosition().y;
        //m_x += x;
        //m_y += y;

        if(m_state != unclick)
            m_ani[m_state + m_dir].setPosition(m_x, m_y);
        else
            m_ani[m_state].setPosition(m_x, m_y);
    }

    public Point GetPosition() {
        return new Point(m_collBox.GetPosition().x,  m_collBox.GetPosition().y);
    }

    public void SetClear(boolean isclear) {
        m_state = unclick;
        m_clear = isclear;
        m_collBox.m_DisableCollCheck = isclear;
    }
    public boolean GetClear() {
        return m_clear;
    }

    public int State() { return m_state;}

    public void ResetCollside() {
        m_collBox.Reset();
    }

    public void EndCollision() {
        m_collBox.EndCollision();
    }

    public void setState(int state) {
        m_state = state;
        if(state != unclick)
            m_ani[m_state + m_dir].setPosition(m_x, m_y);
        else
            m_ani[m_state].setPosition(m_x, m_y);

    }
    public void setDir(int dir) {
        m_dir = dir;
        if(m_state != unclick)
            m_ani[m_state + m_dir].setPosition(m_x, m_y);
        else
            m_ani[m_state].setPosition(m_x, m_y);
    }

    public Rect CollisionBox() { return m_collBox.m_ColliisionBox; }
    public int GetScale() { return m_ani[0].GetScale(); }
}
