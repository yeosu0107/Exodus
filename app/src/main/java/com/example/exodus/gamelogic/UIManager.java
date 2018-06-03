package com.example.exodus.gamelogic;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.example.exodus.R;
import com.example.exodus.framework.AppManager;
import com.example.exodus.framework.GraphicObject;
import com.example.exodus.framework.UIRect;

import java.util.prefs.AbstractPreferences;

/**
 * Created by yeosu on 2018-05-30.
 */

public class UIManager {
    private UIRect m_pauseBox;
    private UIRect m_clearBox;
    private UIRect m_pauseButton;
    private UIRect m_failBox;
    private UIRect m_backsheet;

    UIManager() {
        int width = AppManager.getInstance().getTileWidth();
        int height = AppManager.getInstance().getTileHeight();
        int screen_width = AppManager.getInstance().getWidth();
        int screen_height = AppManager.getInstance().getHeight();

        m_pauseButton = new UIRect(AppManager.getInstance().getBitmap(R.drawable.pausebutton), new Rect(width/2,width/2,width*4,width*4), 1 );
        m_pauseBox = new UIRect(AppManager.getInstance().getBitmap(R.drawable.pause), new Rect(width * 15, height*4, screen_width - width *15, screen_height - height * 6), 2);
        m_clearBox = new UIRect(AppManager.getInstance().getBitmap(R.drawable.clear), new Rect(width * 15, height*4, screen_width - width *15, screen_height - height * 6), 2);
        m_failBox = new UIRect(AppManager.getInstance().getBitmap(R.drawable.fail), new Rect(width * 15, height*4, screen_width - width *15, screen_height - height * 6), 2);
        m_backsheet = new UIRect(AppManager.getInstance().getBitmap(R.drawable.backsheet), new Rect(0,0,screen_width, screen_height), 0);

        m_pauseButton.setButtonRect(0, new Rect(width/2,width/2,width*4,width*4));

        m_pauseBox.setButtonRect(0, new Rect(width * 17, height * 11, width * 42, height * 16));
        m_pauseBox.setButtonRect(1, new Rect(width * 17, height * 20, width * 42, height * 25));

        m_clearBox.setButtonRect(0, new Rect(width * 23, height * 19, width * 28, height * 24));
        m_clearBox.setButtonRect(1, new Rect(width * 32, height * 19, width * 37, height * 24));

        m_failBox.setButtonRect(0, new Rect(width * 23, height * 19, width * 28, height * 24));
        m_failBox.setButtonRect(1, new Rect(width * 32, height * 19, width * 37, height * 24));
    }

    public int touchEvent(int state, int x, int y) {
        int buttonIndex = -1;
        switch(state) {
            case GameState.GAME_RUNNING:
                buttonIndex = m_pauseButton.isPush(x, y);
                break;
            case GameState.GAME_PAUSE:
                buttonIndex = m_pauseBox.isPush(x, y);
                if(buttonIndex == -1)
                    buttonIndex = -10;
                break;
            case GameState.GAME_CLEAR:
                buttonIndex = m_clearBox.isPush(x, y);
                if(buttonIndex == -1)
                    buttonIndex = -10;
                break;
            case GameState.GAME_FAIL:
                buttonIndex = m_failBox.isPush(x, y);
                if(buttonIndex == -1)
                    buttonIndex = -10;
                break;
        }
        //Log.e("buttonIndex", ""+buttonIndex);
        //Log.e("pos", x + "   " + y + "  " + x/AppManager.getInstance().getTileWidth() + "  " + y/AppManager.getInstance().getTileHeight());
        return buttonIndex;
    }

    public void render(int state, Canvas canvas) {
        switch(state) {
            case GameState.GAME_RUNNING:
                m_pauseButton.draw(canvas);
                break;
            case GameState.GAME_PAUSE:
                m_backsheet.draw(canvas);
                m_pauseBox.draw(canvas);
                break;
            case GameState.GAME_CLEAR:
                m_backsheet.draw(canvas);
                m_clearBox.draw(canvas);
                break;
            case GameState.GAME_FAIL:
                m_backsheet.draw(canvas);
                m_failBox.draw(canvas);
                break;
        }
    }
}
