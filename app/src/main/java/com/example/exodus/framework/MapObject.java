package com.example.exodus.framework;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 여성우 on 2018-05-24.
 */

public class MapObject extends GraphicObject{

    private int  m_Width;
    private int  m_Height;
    private List<int[]> m_tiles;

    public List<CollisionBox> m_Collboxs;

    public MapObject(Bitmap bitmap, int nWidth, int nHeight, List<int[]> tiles) {
        super(bitmap);

        m_Width = m_bit.getWidth() / nWidth;
        m_Height = m_bit.getHeight() / nHeight;
        m_tiles = tiles;
        m_Collboxs = new ArrayList<CollisionBox>();
        m_bit = CreateMapResource();

    }

    // 하나의 완성된 텍스쳐 생성 : 프레임 저하 방지
    public Bitmap CreateMapResource() {
        Bitmap bit = Bitmap.createBitmap(AppManager.getInstance().getWidth(), AppManager.getInstance().getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bit);
        MakeResource(canvas);

        return bit;
    }

    public void MakeResource(Canvas canvas) {
        Rect m_rectangle = new Rect(0,0,0,0);

        int sliceX = 0;
        int sliceY = AppManager.getInstance().getHeight() / m_tiles.size();

        Rect collrect = new Rect(-1, -1, -1, -1);
        Rect dest = new Rect();

        for(int i=0; i<m_tiles.size(); ++i) {
            int[] line = m_tiles.get(i);
            sliceX = AppManager.getInstance().getWidth() / (line.length - 1);
            for(int j=0; j<line.length; ++j) {
                if(line[j] == -1) {
                    if(collrect.left != -1) {
                        AddCollisionBox(collrect);;
                        collrect.set(-1, -1, -1, -1);
                    }
                    continue;
                }
                int xValue = line[j]%25;
                int yValue = line[j]/25;

                m_rectangle.left = xValue*m_Width;
                m_rectangle.top = yValue * m_Height;
                m_rectangle.right = (xValue + 1) * m_Width;
                m_rectangle.bottom = (yValue + 1) * m_Height;

                dest.set((j)*sliceX, (i+1)*sliceY, (j+1)*sliceX +1, (i+2)*sliceY + 1);
                //= new Rect((j)*sliceX, (i+1)*sliceY,
                  //      (j+1)*sliceX +1, (i+2)*sliceY + 1);

                if (collrect.left == -1 && collrect.top == -1) {
                    collrect.set(dest);
                }
                if (collrect.left <= dest.left) {
                    collrect.right = dest.right;
                    collrect.bottom = dest.bottom;
                }
                else {
                    AddCollisionBox(collrect);
                    collrect.set(dest);
                }
                canvas.drawBitmap(m_bit, m_rectangle, dest, null);
            }

        }
        m_Collboxs.add(new CollisionBox(collrect, 1));
    }

    public void CollisionCheck(CollisionBox other) {
        for(int i = m_Collboxs.size(); i > 0; --i) {
            CollisionManager.checkBoxtoBox(other, m_Collboxs.get(i - 1), false);
        }
    }
    @Override
    public void draw(Canvas canvas) {

        for(int i = m_Collboxs.size(); i > 0; --i) {
            m_Collboxs.get(i - 1).DrawCollisionBox(canvas);
        }
        super.draw(canvas);

    }

    public void AddCollisionBox(Rect rect) {
        boolean IsUnion = false;
        for(int i = 0; i < m_Collboxs.size(); ++i) {
            Rect originRect = m_Collboxs.get(i).m_ColliisionBox;
            if(originRect.left == rect.left &&
                    originRect.right == rect.right&&
                    originRect.bottom == rect.top + 1) {
                originRect.union(rect);
                IsUnion = true;
            }
        }
        if(!IsUnion)
            m_Collboxs.add(new CollisionBox(rect, 1));
    }
}
