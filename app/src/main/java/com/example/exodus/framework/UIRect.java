package com.example.exodus.framework;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by yeosu on 2018-05-30.
 */

public class UIRect extends GraphicObject {
    private Rect m_source;
    private Rect m_dest;
    private int m_nButton;
    private Rect[] m_ButtonRect;

    public UIRect(Bitmap bitmap, Rect rect, int nButton) {
        super(bitmap);
        m_dest = rect;
        m_source = new Rect(0,0,m_bit.getWidth(), m_bit.getHeight());
        m_nButton = nButton;
        m_ButtonRect = new Rect[m_nButton];
    }

    public void setButtonRects(Rect[] m_tmp) {
        m_ButtonRect = m_tmp;
    }
    public void setButtonRect(int index, Rect tmp) {
        m_ButtonRect[index] = tmp;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(m_bit, m_source, m_dest,null);
    }

    public int isPush(int x, int y) {
        for(int i=0; i<m_nButton; ++i) {
            if(m_ButtonRect[i].contains(x, y))
                return i;
        }

        if(m_dest.contains(x, y))
            return -20;
        return -1;
    }

}
