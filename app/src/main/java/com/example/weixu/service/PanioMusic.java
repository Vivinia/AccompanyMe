package com.example.weixu.service;

/**
 * 音乐播放帮助类
 */

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;


import com.example.weixu.accompanyme.R;

import java.util.HashMap;

public class PanioMusic {
    // 资源文件
    int Music[] = {R.raw.do1, R.raw.re2, R.raw.mi3, R.raw.fa4, R.raw.sol5,
            R.raw.la6, R.raw.si7,};
    SoundPool soundPool;
    HashMap<Integer, Integer> soundPoolMap;

    public PanioMusic(Context context) {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < Music.length; i++) {
            soundPoolMap.put(i, soundPool.load(context, Music[i], 1));
        }
    }

    public int soundPlay(int no) {
        return soundPool.play(soundPoolMap.get(no), 100, 100, 1, 0, 1.0f);
    }

    public int soundOver() {
        return soundPool.play(soundPoolMap.get(1), 100, 100, 1, 0, 1.0f);
    }

    @Override
    protected void finalize() throws Throwable {
        soundPool.release();
        super.finalize();
    }
}

