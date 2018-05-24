package com.example.exodus.framework;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by 여성우 on 2018-05-24.
 */

public class BackGround extends GraphicObject {
    private Rect m_rect;
    private Rect m_dest;
    public BackGround(Bitmap bitmap) {
        super(bitmap);
        m_rect = new Rect(0,0, m_bit.getWidth(), m_bit.getHeight());
        m_dest = new Rect(0,0,1920, 1080);
    }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawBitmap(m_bit, m_rect, m_dest, null);
    }
}
