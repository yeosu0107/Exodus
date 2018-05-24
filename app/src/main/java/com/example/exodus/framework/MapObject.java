package com.example.exodus.framework;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.List;

/**
 * Created by 여성우 on 2018-05-24.
 */

public class MapObject extends GraphicObject{

    private int  m_Width;
    private int  m_Height;
    private List<int[]> m_tiles;

    public MapObject(Bitmap bitmap, int nWidth, int nHeight, List<int[]> tiles) {
        super(bitmap);

        m_Width = m_bit.getWidth() / nWidth;
        m_Height = m_bit.getHeight() / nHeight;
        m_tiles = tiles;

    }

    @Override
    public void draw(Canvas canvas) {
        Rect m_rectangle = new Rect(0,0,0,0);

        int sliceX = 0;
        int sliceY = 1080 / m_tiles.size();

        for(int i=0; i<m_tiles.size(); ++i) {
            int[] line = m_tiles.get(i);
            sliceX = 1920 / line.length;
            for(int j=0; j<line.length; ++j) {
                if(line[j] == -1)
                    continue;
                int xValue = line[j]%25;
                int yValue = line[j]/24;

                m_rectangle.left = xValue*m_Width;
                m_rectangle.top = yValue * m_Height;
                m_rectangle.right = (xValue + 1) * m_Width;
                m_rectangle.bottom = (yValue + 1) * m_Height;

                Rect dest = new Rect((j)*sliceX, (i+1)*sliceY,
                        (j+1)*sliceX +1, (i+2)*sliceY + 1);

                //Rect dest = new Rect(0,1000,50,1050);

                canvas.drawBitmap(m_bit, m_rectangle, dest, null);
            }
        }
    }

}
