package com.musicapp.urmusic;

import com.musicapp.urmusic.R;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MusicIntentReceiver extends android.content.BroadcastReceiver {
   @Override
   public void onReceive(Context ctx, Intent intent) {
      if (intent.getAction().equals(
                    android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
          // signal your service to stop playback
          // (via an Intent, for instance)
  			    	  	
    	  	MainActivity.am.abandonFocus();
			MainActivity.mp.pause();
			MainActivity.isPlaying = false;
			
			if (PlayerFragment.btnPlay!=null) PlayerFragment.btnPlay.setImageResource(R.drawable.btn_play);
      }
   }

}