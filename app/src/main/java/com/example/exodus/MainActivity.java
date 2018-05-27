package com.example.exodus;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.example.exodus.framework.AppManager;
import com.example.exodus.framework.GameView;

import java.io.IOException;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getHeight();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        AppManager.getInstance().setScreenSize(size.x, size.y);

        try {
            setContentView(new GameView(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //setContentView(R.layout.activity_main);


    }

    private void getHeight() {
        Rect rect = new Rect();
        Window win = this.getWindow();
        win.getDecorView().getWindowVisibleDisplayFrame(rect);

        Log.d("linsoo","알림바:"+rect.top);
        Log.d("linsoo","화면전체너비:"+rect.right);
    }
}
