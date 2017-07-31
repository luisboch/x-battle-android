package br.com.luis.apps.simple2dgame.util;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Manage all game sounds
 * Created by luis on 06/07/17.
 */

public class SoundManager {
    // Play background musics.
    private MediaPlayer player;
    // Play all effects
    private SoundPool pool;
    // Manage all effects;
    Map<String, Integer> effecRef = new HashMap<>();

    public static final String FIRE = "laser";
    public static final String LOW_EXPLOSION = "low-explosion";
    public static final String HIGTH_EXPLOSION = "higth-explosion";

    public SoundManager() {
        player = new MediaPlayer();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pool = new SoundPool.Builder().setMaxStreams(5).build();
        } else {
            pool = new SoundPool(5, AudioManager.STREAM_MUSIC, 1);
        }
    }

    public SoundManager addEffect(String name, String asset) {// Creates cache of this sound
        final AssetFileDescriptor fd = AssetUtil.getInstance().getFD(asset);
        if (fd != null) {
            int ref = pool.load(fd, 1);
            effecRef.put(name, ref);
        }
        return this;
    }

    public SoundManager play(String audioName, boolean loop) {
        final AssetFileDescriptor ad = AssetUtil.getInstance().getFD(audioName);

        if (ad != null) {
            try {
                player.setDataSource(
                        ad.getFileDescriptor(),
                        ad.getStartOffset(), ad.getLength());
                player.prepare();
                player.setLooping(loop);
                player.start();
            } catch (Exception ex) {
                Log.e("ERROR", "Can't start audio: " + ex.getMessage(), ex);
            }
        }
        return this;
    }

    public SoundManager playEffect(String name) {
        return playEffect(name, 1, 1);
    }

    public SoundManager playEffect(String name, int leftVol, int rightVol) {
        if (effecRef.containsKey(name)) {
            pool.play(effecRef.get(name), leftVol, rightVol, 0, 0, 1);
        } else {
            throw new IllegalStateException("No effect with name \"" + name + "\" found, register it first using method #addEffect");
        }
        return this;
    }

    public SoundManager pause() {
        player.pause();
        return this;
    }

    public SoundManager resume() {
        player.start();
        return this;
    }

    public SoundManager stop() {
        player.stop();
        player.release();
        return this;
    }
}
