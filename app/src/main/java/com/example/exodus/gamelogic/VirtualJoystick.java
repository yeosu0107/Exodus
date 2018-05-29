package com.example.exodus.gamelogic;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.exodus.R;
import com.example.exodus.framework.AppManager;
import com.example.exodus.framework.GraphicObject;

/**
 * Created by yeosu on 2018-05-27.
 */

public class VirtualJoystick{
    private GraphicObject backStick;
    private GraphicObject moveStick;

    private int back_size;
    private int move_size;

    private int now_x = -1;
    private int now_y = -1;

    private boolean onDraw = false;

    public VirtualJoystick() {
        backStick = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.joystick1));
        moveStick = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.joystick2));

        back_size = AppManager.getInstance().getBitmap(R.drawable.joystick1).getWidth() / 2;
        move_size = AppManager.getInstance().getBitmap(R.drawable.joystick2).getWidth() / 2;
    }

    public void enableJoystick(int x, int y) {
        now_x = x;
        now_y = y;

        backStick.setPosition(x - back_size, y - back_size);
        moveStick.setPosition(x - move_size, y - move_size);
        onDraw = true;
    }

    public void disableJoystick() {
        onDraw = false;
    }

    public void moveJoystick(int x, int y) {
        int tx = x-move_size;
        int ty = y-move_size;

        if(x < now_x - back_size) {
            tx = now_x - back_size - move_size;
        }
        else if(x > now_x + back_size) {
            tx = now_x + back_size - move_size;
        }

        if(y < now_y - back_size) {
            ty = now_y - back_size - move_size;
        }
        else if(y > now_y + back_size) {
            ty = now_y + back_size - move_size;
        }

        moveStick.setPosition(tx, ty);

        //return x - now_x;
    }

    public boolean isDraw() {return onDraw;}

    public void render(Canvas canvas) {
        backStick.draw(canvas);
        moveStick.draw(canvas);
    }

    public int distX() {
        return moveStick.getX() - now_x;
    }
}
