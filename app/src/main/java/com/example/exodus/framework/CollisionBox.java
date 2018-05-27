package com.example.exodus.framework;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.ViewDebug;

/**
 * Created by MSI on 2018-05-22.
 */

public class CollisionBox {
    public Rect m_ColliisionBox;
    public Point m_Size;
    public Paint m_Paint;

    public int m_Collside;
    public int m_PreCollside;

    public boolean m_NowCollcheck;  // 현재 충돌 체크 중인지 여부
    public int m_Scale = 2;

    public CollisionBox(Rect rect, int scale) {
        m_ColliisionBox = new Rect(rect);
        m_Collside = 0;
        m_Scale = scale;
        m_PreCollside = 0;
        m_NowCollcheck = true;
        m_Size = new Point(rect.width() * m_Scale, rect.height()* m_Scale);
        m_ColliisionBox.right = m_ColliisionBox.left + m_Size.x;
        m_ColliisionBox.bottom = m_ColliisionBox.top + m_Size.y;

        m_Paint= new Paint();
        m_Paint.setColor(Color.WHITE);
    }

    public CollisionBox() {  }

    public void SetPosition(int x, int y) {
        // offsetTo 함수를 통해 충돌박스 위치를 Left, Top위치로 이동
        m_ColliisionBox.offsetTo(x , y );
    }

    public void DrawCollisionBox(Canvas canvas) {
        canvas.drawRect(m_ColliisionBox, m_Paint);
    }

    public void Move(int x, int y) {
        m_ColliisionBox.offset(x, y);
    }

    public int Collside() {
        if(m_NowCollcheck)
            return m_PreCollside;
        return m_Collside;
    }

    public void EndCollision() {
        m_NowCollcheck = false;
    }

    public void Reset() {
        m_NowCollcheck = true;
        m_PreCollside = m_Collside;
        m_Collside = 0;
    }

    public boolean IsEnableMove(int direction)
    {
        return !((Collside() & direction) > 0);
    }
}
