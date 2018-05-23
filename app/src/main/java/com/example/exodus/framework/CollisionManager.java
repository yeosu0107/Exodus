package com.example.exodus.framework;

import android.graphics.Rect;
import android.util.Log;

/**
 * Created by 여성우 on 2018-05-17.
 */

public class CollisionManager {
    static public final int SIDE_LEFT = 1;
    static public final int SIDE_RIGHT = 2;
    static public final int SIDE_TOP = 4;
    static public final int SIDE_BOTTOM = 8;

    public static boolean checkBoxtoBox(CollisionBox box1, CollisionBox box2) {
        Rect collBox1 = new Rect(box1.m_ColliisionBox);
        Rect collBox2 = new Rect(box2.m_ColliisionBox);
        Rect collChectBox = new Rect();

        if(!collBox1.intersect(collBox2))
            return false;

        collChectBox.setIntersect(collBox1, collBox2);
        if(collChectBox.centerX() <= box1.m_ColliisionBox.centerX()) box1.m_Collside += SIDE_LEFT;
        else box1.m_Collside += SIDE_RIGHT;

        Log.d("originX", String.valueOf(box1.m_ColliisionBox.centerX()));
        Log.d("CollX",  String.valueOf(collChectBox.centerX()));
        Log.d("CollSide",  String.valueOf(box1.m_Collside));

        if(collChectBox.centerY() < box1.m_ColliisionBox.centerY()) box1.m_Collside += SIDE_TOP;
        if(collChectBox.centerY() > box1.m_ColliisionBox.centerY()) box1.m_Collside += SIDE_BOTTOM;

        return false;
    }
}