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

        int distance = 0;
        int collside1 = 0;
        int collside2 = 0;

        if(!collBox1.intersect(collBox2)) return false;

        collChectBox.setIntersect(collBox1, collBox2);

        if(collChectBox.centerY() < box1.m_ColliisionBox.centerY()) {
            collside1 |= SIDE_TOP;
            collside2 |= SIDE_BOTTOM;
            distance = Length(collChectBox.centerY(), box1.m_ColliisionBox.centerY());
        }
        if(collChectBox.centerY() > box1.m_ColliisionBox.centerY()) {
            collside1 |= SIDE_BOTTOM;
            collside2 |= SIDE_TOP;
            distance = Length(collChectBox.centerY(), box1.m_ColliisionBox.centerY());
        }

        if(collChectBox.centerX() <= box1.m_ColliisionBox.centerX()) {
            if(distance < Length(collChectBox.centerX(), box1.m_ColliisionBox.centerX())) {
                collside1 = SIDE_LEFT;
                collside2 = SIDE_RIGHT;
            }
        }
        else {
            if(distance < Length(collChectBox.centerX(), box1.m_ColliisionBox.centerX())) {
                collside1 = SIDE_RIGHT;
                collside2 = SIDE_LEFT;
            }
        }

        if(collBox1.left <= 0) collside1 |= SIDE_LEFT;
        if(collBox1.right >= (1794)) collside1 |= SIDE_RIGHT;

        box1.m_Collside |= collside1;
        box2.m_Collside |= collside2;
        return true;
    }
    public static int Length(int x1, int x2) {
        return (x1 - x2) * (x1 - x2);
    }
}