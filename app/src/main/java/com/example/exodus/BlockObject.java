package com.example.exodus;

import android.graphics.Canvas;
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

    public CollisionBox m_collBox;

    private int m_x, m_y;
    private SpriteObject m_Texture;

    public BlockObject() {
        this.setPosition(0,0);

        m_Texture = new SpriteObject(AppManager.getInstance().getBitmap(R.drawable.crate));
        m_Texture.initSpriteData(1, 1,2);

        m_collBox = new CollisionBox(m_Texture.m_rectangle);
    }

    public void setting(int x, int y) {
        this.setPosition(x, y);
        m_collBox.SetPosition(x,y);
        m_Texture.setPosition(x, y);
    }

    public void draw(Canvas canvas) {
        m_collBox.DrawCollisionBox(canvas);
        m_Texture.draw(canvas);
    }

    public void update(long time) {
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

    public void ResetCollside() {
        m_collBox.Reset();
    }
}
