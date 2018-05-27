package com.example.exodus.gamelogic;

import android.graphics.Bitmap;

import com.example.exodus.R;
import com.example.exodus.framework.AppManager;
import com.example.exodus.framework.GraphicObject;

/**
 * Created by yeosu on 2018-05-27.
 */

public class JumpButton extends GraphicObject {
    private boolean         isEnableJunmp = false;
    int                     size;

    public JumpButton() {
        super(AppManager.getInstance().getBitmap(R.drawable.joystick3));
        size = AppManager.getInstance().getBitmap(R.drawable.joystick3).getWidth() / 2;
    }

    public void setJump(boolean tmp) {
        isEnableJunmp = tmp;
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x - size, y - size);
    }

    public boolean isJump() {return isEnableJunmp; }
}
