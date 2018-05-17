package com.example.exodus.framework;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

/**
 * Created by 여성우 on 2018-05-17.
 */

public class SoundManager {
    private static SoundManager g_instance;

    private SoundPool m_soundPool;
    private HashMap m_SoundPoolMap;
    private AudioManager m_AudioManager;
    private Context m_Activity;

    public static  SoundManager getInstance() {
        if(g_instance == null)
            g_instance = new SoundManager();

        return g_instance;
    }

    public void Init(Context context) {
        SoundPool.Builder   m_SoundPoolBuilder;
        m_SoundPoolBuilder = new SoundPool.Builder();
        //m_soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        m_soundPool = m_SoundPoolBuilder.build();
        m_SoundPoolMap = new HashMap();
        m_AudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        m_Activity = context;
    }

    public void addSound(int index, int soundID) {
        int id = m_soundPool.load(m_Activity, soundID, 1);
        m_SoundPoolMap.put(index, id);
    }

    public void play(int index, int loop) {
        float volume=m_AudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volume=volume/m_AudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        m_soundPool.play((Integer)m_SoundPoolMap.get(index),
                volume, volume, 1, loop, 1f);
    }
}
