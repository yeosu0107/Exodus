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

    static public final int COLL_MAP = 1;
    static public final int COLL_MOVEBOX = 2;
    static public final int COLL_PLAYER = 4;

    public static boolean checkBoxtoBox(CollisionBox box1, CollisionBox box2, int flag, int boxindex) {

        Rect collBox1 = new Rect(box1.m_ColliisionBox);
        Rect collBox2 = new Rect(box2.m_ColliisionBox);
        Rect collChectBox = new Rect();

        int distance = 0;
        int collside1 = 0;
        int collside2 = 0;

        if(collBox1.left <= 0) box1.m_Collside |= SIDE_LEFT;
        if(collBox1.right >= (AppManager.getInstance().getWidth())) box1.m_Collside |= SIDE_RIGHT;

        if(!intersects(collBox1, collBox2)) return false;

        setIntersect(collChectBox, collBox1, collBox2);

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

        AfterCollision(box1, collChectBox,collside1);

        if((flag & COLL_MAP) > 0) {
            //AfterCollision(box2, collChectBox,collside2);
        }

        if((flag & COLL_MOVEBOX) > 0) {
            box1.m_Collmovebox |= (int)Math.pow(2, boxindex);
        }

        if((flag & COLL_PLAYER) > 0) {
            if((box1.m_Collmovebox & (int)Math.pow(2, boxindex)) > 0 )
                box2.m_Collmovebox |= (int)Math.pow(2, boxindex);
            else if((box2.m_Collmovebox & (int)Math.pow(2, boxindex)) > 0)
                box1.m_Collmovebox |= (int)Math.pow(2, boxindex);
        }
        box1.m_Collside |= collside1;
        box2.m_Collside |= collside2;

        return true;
    }

    public static int Length(int x1, int x2) {
        return (x1 - x2) * (x1 - x2);
    }

    public static void AfterCollision(CollisionBox box, Rect collChectBox, int collside){

        if((collside & SIDE_BOTTOM) > 0)
            box.Move(0,-collChectBox.height());
        //if((newCollside & SIDE_TOP) > 0)
        //    box.Move(0,collChectBox.height() / 2);
    }

    public static boolean InfectionMovebox(CollisionBox box1, CollisionBox box2, int boxindex) {

        if(box1.m_DisableCollCheck || box2.m_DisableCollCheck) return false;

        Rect collBox1 = new Rect(box1.m_ColliisionBox);
        Rect collBox2 = new Rect(box2.m_ColliisionBox);

        if(!intersects(collBox1, collBox2)) return false;
        if((box1.m_Collmovebox & (int)Math.pow(2, boxindex)) > 0)
            box2.m_Collmovebox |= (int)Math.pow(2, boxindex);
        else if((box2.m_Collmovebox & (int)Math.pow(2, boxindex)) > 0)
            box1.m_Collmovebox |= (int)Math.pow(2, boxindex);

        return true;
    }

    public static boolean intersects(Rect a, Rect b) {
        return a.left <= b.right && b.left <= a.right && a.top <= b.bottom && b.top <= a.bottom;
    }

    public static boolean setIntersect(Rect origin, Rect a, Rect b) {
        if (a.left <= b.right && b.left <= a.right && a.top <= b.bottom && b.top <= a.bottom) {
            origin.left = Math.max(a.left, b.left);
            origin.top = Math.max(a.top, b.top);
            origin.right = Math.min(a.right, b.right);
            origin.bottom = Math.min(a.bottom, b.bottom);
            return true;
        }
        return false;
    }
}