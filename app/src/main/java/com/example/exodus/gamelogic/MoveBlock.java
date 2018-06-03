package com.example.exodus.gamelogic;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.example.exodus.BlockObject;
import com.example.exodus.framework.AppManager;
import com.example.exodus.framework.CollisionManager;

/**
 * Created by MSI on 2018-06-02.
 */

public class MoveBlock extends BlockObject {

    static public final int FLAG_MOVE_LEFT = 8;
    static public final int FLAG_MOVE_RIGHT = 16;
    static public final int FLAG_MOVE_UP = 32;
    static public final int FLAG_MOVE_DOWN = 64;

    int m_moveDistance;
    int m_maxDistance;
    int m_moveSpeed;
    int m_randTopPos;

    int m_needPlayer;
    boolean m_workcondition;

    Point m_moveDirection;
    Point m_MDistanceNowFrame;

    public MoveBlock(Bitmap bitmap, int flag, int maxdist, int moveSpeed) {
        super(bitmap, 1, 1, 1, flag);

        m_maxDistance = maxdist * AppManager.getInstance().getTileWidth();
        m_moveDistance = 0;
        m_moveSpeed = moveSpeed;
        m_moveDirection = new Point();
        m_MDistanceNowFrame = new Point();
        // 임시값
        m_randTopPos = 1080;
        m_workcondition = false;

        if((flag & FLAG_MOVE_LEFT) > 0)     m_moveDirection.x = -1;
        if((flag & FLAG_MOVE_RIGHT) > 0)    m_moveDirection.x = +1;
        if((flag & FLAG_MOVE_UP) > 0)       m_moveDirection.y = -1;
        if((flag & FLAG_MOVE_DOWN) > 0)     m_moveDirection.y = +1;
    }

    public void SetNeedPlayer(int n) { m_needPlayer = n; }
    @Override
    public void update(long time){
        //if(OverMaxDistace() || CollScreenSide())
        //    moveOpposite();

        if(!OverMaxDistace()) {
            if (m_workcondition)
                m_MDistanceNowFrame = move(m_moveDirection.x * m_moveSpeed, m_moveDirection.y * m_moveSpeed);
            else
                m_MDistanceNowFrame = move(-m_moveDirection.x * m_moveSpeed, -m_moveDirection.y * m_moveSpeed);
        }
        else { m_MDistanceNowFrame.x = 0; m_MDistanceNowFrame.y = 0; }


        m_moveDistance +=m_MDistanceNowFrame.x + m_MDistanceNowFrame.y;
    }
    @Override
    public Point move(int x, int y) {
        if(!m_drawable) return new Point();
        if(x > 0 && !m_collBox.IsEnableMove(CollisionManager.SIDE_RIGHT)) x = 0;
        else if(x < 0 && !m_collBox.IsEnableMove(CollisionManager.SIDE_LEFT)) x = 0;
        if( y > 0 && !m_collBox.IsEnableMove(CollisionManager.SIDE_BOTTOM)) y = 0 ;
        //else if( y < 0 && !m_collBox.IsEnableMove(CollisionManager.SIDE_TOP)) y = 0;

        m_collBox.Move(x, y);
        m_x = m_collBox.GetPosition().x;
        m_y = m_collBox.GetPosition().y;

        m_Texture.setPosition(m_x, m_y);
        return new Point(x,y);
    }

    public void moveOpposite() {
        m_moveDirection.x *= -1;
        m_moveDirection.y *= -1;
        m_moveDistance = Math.abs(m_moveDistance - m_maxDistance);
    }

    public boolean CollScreenSide() {
        //if((m_Flags & FLAG_MOVE_SIDE) > 0)
        //    return ((m_collBox.m_ColliisionBox.left < 0) && m_moveDirection.x < 0) ||
        //            ((m_collBox.m_ColliisionBox.right > AppManager.getInstance().getWidth()) && m_moveDirection.x > 0);
        //else if((m_Flags & FLAG_MOVE_UPDOWN) > 0)
        //    return ((m_collBox.m_ColliisionBox.top < 0) && m_moveDirection.y == -1) ||
        //            ((m_collBox.m_ColliisionBox.bottom > m_randTopPos) && m_moveDirection.y == 1);

        return false;
    }

    public void Work(int numofPlayer) { m_workcondition = (m_needPlayer == numofPlayer);  }

    public boolean OverMaxDistace() {
        return (Math.abs(m_moveDistance) <= 0 && !m_workcondition)
                || (Math.abs(m_moveDistance) >= m_maxDistance && m_workcondition);
    }
}
