package com.example.exodus.framework;

import android.graphics.Rect;

/**
 * Created by 여성우 on 2018-05-17.
 */

public class CollisionManager {
    public static boolean checkBoxtoBox(Rect rt1, Rect rt2) {
        if(rt1.intersect(rt2))
            return true;

        return false;
    }
}