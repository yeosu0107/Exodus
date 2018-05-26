package com.example.exodus.framework;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.renderscript.Sampler;

import com.example.exodus.BlockObject;
import com.example.exodus.R;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MSI on 2018-05-26.
 */
public class EffectManagement {

    static final protected  int NUM_EFFECT = 1;
    protected SpriteObject[] m_pEffects;
    protected List<SpriteObject> m_pExistEffect = new ArrayList<SpriteObject>();

    public void BuildObjects() {
        m_pEffects = new SpriteObject[NUM_EFFECT];
        m_pEffects[0] = new SpriteObject(AppManager.getInstance().getBitmap(R.drawable.star));
        m_pEffects[0].initSpriteData( 25, 4, 2);
        for(int i = 0 ; i < NUM_EFFECT; ++i) {
            m_pEffects[i].m_bLoop = false;
            m_pEffects[i].m_bEnd = false;
        }
    }

    public void StartStarEffect(int x, int y) {
        SpriteObject newEff = new SpriteObject(m_pEffects[0]);
        newEff.setPosition(x, y);
        m_pExistEffect.add(newEff);
    }
    public void StartStarEffect(Point pos) {
        StartStarEffect(pos.x, pos.y);
    }

    public void Update(long time) {
        for(int i = m_pExistEffect.size(); i > 0; i--) {
            m_pExistEffect.get(i - 1).update(time);
            if(m_pExistEffect.get(i - 1).IsEnd())
                m_pExistEffect.remove(i - 1);
        }
    }

    public void draw(Canvas canvas) {
        for(int i = m_pExistEffect.size(); i > 0; i--) {
            m_pExistEffect.get(i - 1).draw(canvas);
        }
    }
}
