package com.example.exodus.framework;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by 여성우 on 2018-05-17.
 */

public class SpriteObject extends GraphicObject{
    public Rect m_rectangle;

    private int  m_fps;
    private int  m_nFrame;
    private int  m_currFrame;
    private long m_frameTimer;

    private int  m_spriteWidth;
    private int  m_spriteHeight;

    protected int     m_time;
    protected boolean m_bLoop = true;
    protected boolean m_bEnd = false;

    private int m_destWidth;
    private int m_destHeight;

    public SpriteObject(Bitmap bitmap) {
        super(bitmap);

        m_rectangle=new Rect(0,0,0,0);
        m_frameTimer = 0;
        m_currFrame = 0;
    }

    public SpriteObject(SpriteObject other) {
        super(other.m_bit);
        m_rectangle= other.m_rectangle;
        m_frameTimer = other.m_frameTimer;
        m_fps = other.m_fps;
        m_nFrame = other.m_nFrame;
        m_currFrame = other.m_currFrame;
        m_spriteWidth = other.m_spriteWidth;
        m_spriteHeight = other.m_spriteHeight;
        m_time = other.m_time;
        m_bLoop = other.m_bLoop;
        m_bEnd = other.m_bEnd;
    }

    public void initSpriteData(int theFPS, int nFrame, int time) { //fps, 프레임갯수, 확대배율
        m_spriteWidth = m_bit.getWidth() / nFrame;
        m_spriteHeight = m_bit.getHeight();
        m_rectangle.bottom = m_spriteHeight;
        m_rectangle.right= m_spriteWidth;
        m_fps = 1000 / theFPS;
        m_nFrame = nFrame;
        m_time = time;

        m_destWidth = AppManager.getInstance().getTileWidth();
        m_destHeight = AppManager.getInstance().getTileHeight();
    }

    public void update(long time) {
        if(m_bEnd) return;
        if(time>m_frameTimer + m_fps) {
            //이전시간 + m_fps보다 크면 다음 프레임으로
            m_frameTimer = time;
            m_currFrame += 1;
            if(m_currFrame >= m_nFrame) {
                m_bEnd = !m_bLoop;
                m_currFrame = 0;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Rect dest = new Rect(m_x, m_y,
                m_x + 32 * m_time, m_y + 32 * m_time);

        m_rectangle.left = m_currFrame * m_spriteWidth;
        m_rectangle.right= m_rectangle.left + m_spriteWidth;

        canvas.drawBitmap(m_bit, m_rectangle, dest, null);
    }

    public int NowFrame() { return m_nFrame; }

    public void SetSpriteFrame(int frame) { m_currFrame = frame; }
    public void Start() { m_bLoop = true; }
    public void SetLoop(boolean loop) { m_bLoop = loop;}
    public boolean IsEnd() { return m_bEnd; }

}
