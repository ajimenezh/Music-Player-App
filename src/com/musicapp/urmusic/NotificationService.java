package com.musicapp.urmusic;

import com.musicapp.urmusic.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class NotificationService extends Service {

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
	  
	Notification note = new Notification(R.drawable.ic_launcher,
            MainActivity.songPlayingTitle + " - " + MainActivity.songPlayingArtist,
    	System.currentTimeMillis());

	 Intent i=new Intent(this, MainActivity.class);
	    
     i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
   
     PendingIntent pi=PendingIntent.getActivity(this, 0,
                                                 i, 0);
     
     note.setLatestEventInfo(this, "URMusic",
                             MainActivity.songPlayingTitle + " - " + MainActivity.songPlayingArtist,
                             pi);
     //note.flags|=Notification.FLAG_NO_CLEAR;

	startForeground(1, note);     

    return Service.START_NOT_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
  //TODO for communication return IBinder implementation
    return null;
  }
  
  @Override
  public void onDestroy() {
	  Log.w("hello", "hola");
	  stopForeground(true);
	  super.onDestroy(); 
  }

} 