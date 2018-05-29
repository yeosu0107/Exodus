package com.example.exodus;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import com.example.exodus.framework.AppManager;
import com.example.exodus.framework.CollisionBox;
import com.example.exodus.framework.CollisionManager;
import com.example.exodus.framework.GraphicObject;
import com.example.exodus.framework.SpriteObject;

/**
 * Created by MSI on 2018-05-23.
 */

public class BlockObject {

    static public final int FLAG_HOLDING = 1;
    static public final int FLAG_NO_CHANGE_SPRITE = 2;
    static public final int FLAG_NO_COLLISION_BOX = 4;

    public CollisionBox m_collBox;
    private boolean m_drawable = true;
    private int m_Flags = 0;
    private int m_x, m_y;
    private SpriteObject m_Texture;

    public BlockObject(Bitmap bitmap, int FPS, int nFrame, int time, int flag) {
        this.setPosition(0,0);

        m_Flags = flag;
        m_Texture = new SpriteObject(bitmap);
        m_Texture.initSpriteData(FPS, nFrame,time);
        if((m_Flags & FLAG_NO_COLLISION_BOX) > 0)
            m_collBox = new CollisionBox(new Rect(0,0,0,0), 1);
        else
            m_collBox = new CollisionBox(new Rect(0,0,AppManager.getInstance().getTileWidth(),AppManager.getInstance().getTileHeight()), time);
    }

    public void setting(int x, int y) {
        this.setPosition(x, y);
        m_collBox.SetPosition(x,y);
        m_Texture.setPosition(x, y);
    }

    public void draw(Canvas canvas) {
        if(!m_drawable) return;
        m_collBox.DrawCollisionBox(canvas);
        m_Texture.draw(canvas);

    }

    public void update(long time) {
        if((m_Flags & FLAG_NO_CHANGE_SPRITE) == 0)
            m_Texture.update(time);

        if((m_Flags & FLAG_HOLDING) > 0)
            return;

        // 오른쪽 충돌이 일어난 경우 왼쪽으로 밈
        if(!m_collBox.IsEnableMove(CollisionManager.SIDE_RIGHT)) move(-5,0);
        if(!m_collBox.IsEnableMove(CollisionManager.SIDE_LEFT)) move(10, 0);
        if(m_collBox.IsEnableMove(CollisionManager.SIDE_BOTTOM)) move(0, 5) ;
    }

    public void setPosition(int x, int y) {
        m_x = x;
        m_y = y;
        if(m_collBox != null)
            m_collBox.SetPosition(x, y);
    }

    public void setDrawable(boolean able) {
        m_drawable = able;
    }

    public void SetSpriteFrame(int frame) { m_Texture.SetSpriteFrame(frame); }

    public boolean getDrawalbe() { return m_drawable; }

    public void move(int x, int y) {
        if(x > 0 && !m_collBox.IsEnableMove(CollisionManager.SIDE_RIGHT)) x = 0;
        else if(x < 0 && !m_collBox.IsEnableMove(CollisionManager.SIDE_LEFT)) x = 0;
        if( y > 0 && !m_collBox.IsEnableMove(CollisionManager.SIDE_BOTTOM)) y = 0 ;
        else if( y < 0 && !m_collBox.IsEnableMove(CollisionManager.SIDE_TOP)) y = 0;

        m_x += x;
        m_y += y;

        m_collBox.Move(x,y);
        m_Texture.setPosition(m_x, m_y);
    }

    public Rect CollisionBox() {return m_collBox.m_ColliisionBox;}
    public int NowFrame() { return m_Texture.NowFrame(); }
    public void ResetCollside() {
        m_collBox.Reset();
    }
    public void EndCollision() { m_collBox.EndCollision();}
    public Point GetPosition() {return new Point(m_x, m_y); }
}
