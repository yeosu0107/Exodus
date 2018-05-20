package com.example.exodus.framework;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by 여성우 on 2018-05-17.
 */

public class SpriteObject extends GraphicObject{
    private Rect m_rectangle;

    private int  m_fps;
    private int  m_nFrame;
    private int  m_currFrame;
    private long m_frameTimer;

    private int  m_spriteWidth;
    private int  m_spriteHeight;

    protected int     m_time;
    protected boolean m_bLoop = true;
    protected boolean m_bEnd = false;

    public SpriteObject(Bitmap bitmap) {
        super(bitmap);

        m_rectangle=new Rect(0,0,0,0);
        m_frameTimer = 0;
        m_currFrame = 0;
    }

    public void initSpriteData(int theFPS, int nFrame, int time) { //fps, 프레임갯수, 확대배율
        m_spriteWidth = m_bit.getWidth() / nFrame;
        m_spriteHeight = m_bit.getHeight();
        m_rectangle.bottom = m_spriteHeight;
        m_rectangle.right= m_spriteWidth;
        m_fps = 1000 / theFPS;
        m_nFrame = nFrame;
        m_time = time;
    }

    public void update(long time) {
        if(time>m_frameTimer + m_fps) {
            //이전시간 + m_fps보다 크면 다음 프레임으로
            m_frameTimer = time;
            m_currFrame += 1;
            if(m_currFrame >= m_nFrame)
                m_currFrame = 0;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Rect dest = new Rect(m_x, m_y,
                m_x + m_spriteWidth * m_time, m_y + m_spriteHeight * m_time);

        m_rectangle.left = m_currFrame * m_spriteWidth;
        m_rectangle.right= m_rectangle.left + m_spriteWidth;

        canvas.drawBitmap(m_bit, m_rectangle, dest, null);
    }
}
