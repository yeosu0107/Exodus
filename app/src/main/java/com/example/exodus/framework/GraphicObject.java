package com.example.exodus.framework;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by 여성우 on 2018-05-17.
 */

public class GraphicObject {
    protected Bitmap m_bit;
    protected int     m_x;
    protected int     m_y;

    public Rect m_boundBox;

    public GraphicObject(Bitmap bitmap) {
        m_bit = bitmap;
        m_x = 0;
        m_y = 0;
        m_boundBox=new Rect();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(m_bit, m_x, m_y, null);
    }

    public void setPosition(int x, int y) {
        m_x=x;
        m_y=y;
    }

    public int getX() {return m_x; }
    public int getY() {return m_y; }
}
