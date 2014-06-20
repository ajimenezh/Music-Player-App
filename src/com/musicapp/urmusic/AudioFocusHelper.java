package com.musicapp.urmusic;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

public class AudioFocusHelper implements AudioManager.OnAudioFocusChangeListener {
    AudioManager mAudioManager;
	private Context myContext;

    // other fields here, you'll probably hold a reference to an interface
    // that you can use to communicate the focus changes to your Service

    public AudioFocusHelper(Context ctx) {
    	myContext = ctx;
        mAudioManager = (AudioManager) myContext.getSystemService(Context.AUDIO_SERVICE);
        // ...
    }

    public boolean requestFocus() {
    	if (mAudioManager==null) {
    		Log.w("hello","BAD");
    		return true;
    	}
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
            mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN);
    }

    public boolean abandonFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
            mAudioManager.abandonAudioFocus(this);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        // let your service know about the focus change
    	//MainActivity.am.abandonFocus();
		//MainActivity.mp.pause();
		switch (focusChange) {
        case AudioManager.AUDIOFOCUS_GAIN:
            // resume playback
        	Log.w("hola","gain!!!!");
            if (MainActivity.mp == null) MainActivity.mp = new MediaPlayer();
            else if (!MainActivity.mp.isPlaying()) MainActivity.mp.start();
            MainActivity.mp.setVolume(1.0f, 1.0f);
            MainActivity.isPlaying = true;
            break;

        case AudioManager.AUDIOFOCUS_LOSS:
            // Lost focus for an unbounded amount of time: stop playback and release media player
        	MainActivity.isPlaying = false;
            MainActivity.mp.pause();
            break;

        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            // Lost focus for a short time, but we have to stop
            // playback. We don't release the media player because playback
            // is likely to resume
        	Log.w("hola","loss transient");
            if (MainActivity.mp.isPlaying()) MainActivity.mp.pause();
            break;

        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
            // Lost focus for a short time, but it's ok to keep playing
            // at an attenuated level
        	Log.w("hola","TRNSIENT can duck");
            if (MainActivity.mp.isPlaying()) MainActivity.mp.setVolume(0.1f, 0.1f);
            break;
    }
    }
}