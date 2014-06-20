package com.musicapp.urmusic;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.musicapp.urmusic.MainActivity;
import com.musicapp.urmusic.R;
import com.musicapp.urmusic.Utilities;
import com.musicapp.urmusic.SongsListFragment.OnHeadlineSelectedListener;


import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {
	
	public static ImageButton btnPlay;
	private ImageButton btnForward;
	private ImageButton btnBackward;
	private ImageButton btnNext;
	private ImageButton btnPrevious;
	private ImageButton btnPlaylist;
	private ImageButton btnRepeat;
	private ImageButton btnShuffle;
	private TextView btnComments;
	private TextView btnAddPlaylist;
	private TextView songTitleLabel;
	private TextView songCurrentDurationLabel;
	private TextView songTotalDurationLabel;
	// Handler to update UI timer, progress bar etc,.
	private Handler mHandler = new Handler();;
	private Utilities utils;
	private int seekForwardTime = 5000; // 5000 milliseconds
	private int seekBackwardTime = 5000; // 5000 milliseconds
	public static int currentSongIndex = 0; 
	private boolean isShuffle = false;
	private boolean isRepeat = false;
	public static Integer curSong;
	OnHeadlineSelectedListener mCallback;
	public static String songHashString;
	public static String songPath;
	Random rand = new Random();
	private TextView titleText;
	private TextView artistText;
		
	public PlayerFragment(){
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.player, container, false);
      
		
		// Mediaplayer
		
		//songManager = new SongsManager();
		utils = new Utilities();
		
		// Getting all songs list
		//songsList = songManager.getPlayList();
		//songsList = SongsManager.getInstance().songsList;
		
		songHashString = getArguments().getString("SongHash");

		// selecting single ListView item
         
        return rootView;
    }
	
	public void onActivityCreated (Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		btnPlay = (ImageButton) getView().findViewById(R.id.btnPlay);
		btnNext = (ImageButton) getView().findViewById(R.id.btnNext);
		btnPrevious = (ImageButton) getView().findViewById(R.id.btnPrevious);
		btnPlaylist = (ImageButton) getView().findViewById(R.id.btnPlaylist);
		btnRepeat = (ImageButton) getView().findViewById(R.id.btnRepeat);
		btnShuffle = (ImageButton) getView().findViewById(R.id.btnShuffle);
		songTitleLabel = (TextView) getView().findViewById(R.id.songTitle);
		MainActivity.songProgressBar = (SeekBar) getView().findViewById(R.id.songProgressBar);
		songCurrentDurationLabel = (TextView) getView().findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) getView().findViewById(R.id.songTotalDurationLabel);
		//btnComments = (TextView) getView().findViewById(R.id.comments);		
		//btnAddPlaylist = (TextView) getView().findViewById(R.id.add_playlist);	
		//titleText = (TextView) getView().findViewById(R.id.songTitle);		
		artistText = (TextView) getView().findViewById(R.id.songArtist);
		
		if (MainActivity.bmp!=null) {
			ImageView image = (ImageView) getView().findViewById(R.id.albumCover);
			image.setImageBitmap(MainActivity.bmp);
		}
		
		if (!MainActivity.mp.isPlaying()) {
        	btnPlay.setImageResource(R.drawable.btn_play);
        }
        else {
        	btnPlay.setImageResource(R.drawable.btn_pause);
        }
        
        if (!MainActivity.isShuffle) {
        	btnShuffle.setImageResource(R.drawable.btn_shuffle);
        }
        else {
        	btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
        }
        
        if (!MainActivity.isRepeat) {
        	btnRepeat.setImageResource(R.drawable.btn_repeat);
        }
        else {
        	btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
        }
		
		if (songHashString.charAt(0)=='%' && songHashString.charAt(1)=='%') {

			MainActivity.songProgressBar.setOnSeekBarChangeListener(this);
			changeLayout();
			
		}
		else if (songHashString.charAt(0)=='*' && songHashString.charAt(1)=='*') {
			PlayNextAllSong();
		}
		else {
			
			if (MainActivity.currentSongHash!=null && MainActivity.Hash.get(songHashString)!=null && MainActivity.currentSongHash.equals(MainActivity.Hash.get(songHashString))) {
				Log.w("helol", "que pasa");
				MainActivity.songProgressBar.setOnSeekBarChangeListener(this);
				changeLayout();
			}
			else {
				MainActivity.songProgressBar = (SeekBar) getView().findViewById(R.id.songProgressBar);
				
				MainActivity.currentSongHash = MainActivity.Hash.get(songHashString);
				
				MainActivity.songProgressBar.setOnSeekBarChangeListener(this); // Important
							
		      	if (MainActivity.currentSongHash!=-1 && !MainActivity.fromComments) {
		      		playSong(MainActivity.currentSongHash);
		      	}
		      	else MainActivity.fromComments = false;
			}
		}
		
		/**
		 * Play button click event
		 * plays a song and changes button to pause image
		 * pauses a song and changes button to play image
		 * */
		btnPlay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// check for already playing
				if(MainActivity.mp.isPlaying()){
					if(MainActivity.mp!=null){
						MainActivity.am.abandonFocus();
						MainActivity.mp.pause();
						MainActivity.isPlaying = false;
						// Changing button image to play button
						btnPlay.setImageResource(R.drawable.btn_play);
					}
				}else{
					// Resume song
					if(MainActivity.mp!=null){
						MainActivity.am.requestFocus();
						MainActivity.mp.start();
						MainActivity.isPlaying = true;
						// Changing button image to pause button
						btnPlay.setImageResource(R.drawable.btn_pause);
					}
				}
			}
		});
	
		
		/**
		 * Next button click event
		 * Plays next song by taking currentSongIndex + 1
		 * */
		btnNext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// check if next song is there or not
				btnPlay.setImageResource(R.drawable.btn_pause);
				if (MainActivity.isShuffle) {
					Random rand = new Random();
					MainActivity.currentSongIndex = rand.nextInt(MainActivity.SongHashListArray.size()-1);
					playSong(MainActivity.SongHashListArray.get(MainActivity.currentSongIndex));
				}
				else {
					if(MainActivity.currentSongIndex < MainActivity.SongHashListArray.size()-1){
						playSong(MainActivity.SongHashListArray.get(MainActivity.currentSongIndex + 1));
						MainActivity.currentSongIndex = MainActivity.currentSongIndex + 1;
					}else{
						// play first song
						playSong(MainActivity.SongHashListArray.get(0));
						MainActivity.currentSongIndex = 0;
					}
				}
			}
		});
		
		/**
		 * Back button click event
		 * Plays previous song by currentSongIndex - 1
		 * */
		btnPrevious.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				btnPlay.setImageResource(R.drawable.btn_pause);
				if (MainActivity.isShuffle) {
					Random rand = new Random();
					MainActivity.currentSongIndex = rand.nextInt(MainActivity.SongHashListArray.size()-1);
					playSong(MainActivity.SongHashListArray.get(MainActivity.currentSongIndex));
				}
				else {
					if(MainActivity.currentSongIndex > 0){
						playSong(MainActivity.SongHashListArray.get(MainActivity.currentSongIndex - 1));
						MainActivity.currentSongIndex = MainActivity.currentSongIndex - 1;
					}else{
						// play last song
						playSong(MainActivity.SongHashListArray.get(MainActivity.SongHashListArray.size()-1));
						MainActivity.currentSongIndex = MainActivity.SongHashListArray.size()-1;
					}
				}
				
			}
		});
		
		/**
		 * Button Click event for Repeat button
		 * Enables repeat flag to true
		 * */
		btnRepeat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(MainActivity.isRepeat){
					MainActivity.isRepeat = false;
					Toast.makeText(getActivity(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
					btnRepeat.setImageResource(R.drawable.btn_repeat);
				}else{
					// make repeat to true
					MainActivity.isRepeat = true;
					Toast.makeText(getActivity(), "Repeat is ON", Toast.LENGTH_SHORT).show();
					// make shuffle to false
					MainActivity.isShuffle = false;
					btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
					btnShuffle.setImageResource(R.drawable.btn_shuffle);
				}	
			}
		});
		
		/**
		 * Button Click event for Shuffle button
		 * Enables shuffle flag to true
		 * */
		btnShuffle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(MainActivity.isShuffle){
					MainActivity.isShuffle = false;
					Toast.makeText(getActivity(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
					btnShuffle.setImageResource(R.drawable.btn_shuffle);
					String title = MainActivity.Name.get(MainActivity.currentSongHash);
					currentSongIndex = MainActivity.SongListMap.get(title);
				}else{
					// make repeat to true
					MainActivity.isShuffle= true;
					Toast.makeText(getActivity(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
					// make shuffle to false
					MainActivity.isRepeat = false;
					btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
					btnRepeat.setImageResource(R.drawable.btn_repeat);
				}	
			}
		});
		
//		btnComments.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallback.onArticleSelected(20,"");
//			}
//		});
//		
//		btnAddPlaylist.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mCallback.onArticleSelected(60,"");
//			}
//		});
		
		/**
		 * Button Click event for Play list click event
		 * Launches list activity which displays list of songs
		 * */
		btnPlaylist.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mCallback.onArticleSelected(5, "");
			}
		});
		
		songTitleLabel.setText(MainActivity.songPlayingTitle);
		artistText.setText(MainActivity.songPlayingArtist + " - " + MainActivity.songPlayingAlbum);
	}
	
	public void playNextSong() {
		btnPlay.setImageResource(R.drawable.btn_pause);
		if (MainActivity.isShuffle) {
			Random rand = new Random();
			playSong(MainActivity.SongHashListArray.get(rand.nextInt(MainActivity.SongHashListArray.size()-1)));
		}
		else {
			if(MainActivity.currentSongIndex < MainActivity.SongHashListArray.size()-1){
				playSong(MainActivity.SongHashListArray.get(MainActivity.currentSongIndex + 1));
				MainActivity.currentSongIndex = MainActivity.currentSongIndex + 1;
			}else{
				// play first song
				playSong(MainActivity.SongHashListArray.get(0));
				MainActivity.currentSongIndex = 0;
			}
		}
		if (songTitleLabel!=null) songTitleLabel.setText(MainActivity.songPlayingTitle);
		if (artistText!=null) artistText.setText(MainActivity.songPlayingArtist + " - " + MainActivity.songPlayingAlbum);
	}
	
	// Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position, String str);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
	
    public void PlayNextAllSong() {
    	String Title = MainActivity.SongListArraySorted.get(MainActivity.allSongsIndex);
		String Song = "";
		String Album = "";
		for (Integer i=0; i<Title.length(); i++) {
			if (Title.charAt(i)=='/' && Title.charAt(i+1)=='*' && Title.charAt(i+2)=='/') {
				Song = Title.substring(0,i);
				Album = Title.substring(i+3,Title.length());
				break;
			}
		}
		MainActivity.songProgressBar = (SeekBar) getView().findViewById(R.id.songProgressBar);
		
		MainActivity.currentSongHash = MainActivity.Hash.get(Album+"/"+Song);
		
		MainActivity.songProgressBar.setOnSeekBarChangeListener(this); // Important
					
      	if (MainActivity.currentSongHash!=-1) {
      		playSong(MainActivity.currentSongHash);
      	}
      	
      	if (songTitleLabel!=null) songTitleLabel.setText(MainActivity.songPlayingTitle);
		if (artistText!=null) artistText.setText(MainActivity.songPlayingArtist + " - " + MainActivity.songPlayingAlbum);
    }
	
	public void changeLayout() {
		try {
			// Displaying Song title
        	//songTitleLabel.setText(MainActivity.songPlayingTitle);
			
        	// Changing Button Image to pause image
			//btnPlay.setImageResource(R.drawable.btn_pause);
			
			// set Progress bar values
			long totalDuration = MainActivity.mp.getDuration();
			long currentDuration = MainActivity.mp.getCurrentPosition();
		  
			// Displaying Total Duration time
			songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
			// Displaying time completed playing
			songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));
		   
			// Updating progress bar
			int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
			//Log.d("Progress", ""+progress);
			MainActivity.songProgressBar.setProgress(progress);
			
			if (MainActivity.bmp!=null) {
				ImageView image = (ImageView) getView().findViewById(R.id.albumCover);
				image.setImageBitmap(MainActivity.bmp);
			}
			
			//isPlaying = true;
			
			// Updating progress bar
			updateProgressBar();			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Function to play a song
	 * @param songIndex - index of song
	 * */
	public void  playSong(int songHash){
		// Play song
		MainActivity.isPlaying = true;
		try {
			
			MainActivity.am.requestFocus();
			MainActivity.mp.reset();
			MainActivity.mp.setDataSource(MainActivity.SongPath.get(songHash));
			MainActivity.mp.prepare();
			MainActivity.mp.start();
			MainActivity.isPlaying = true;
			// Displaying Song title
			String songTitle = MainActivity.Name.get(songHash);
			mCallback.onArticleSelected(8, songTitle);
			curSong = songHash;
			MainActivity.currentSongHash = curSong;
			Integer AlbumHash = MainActivity.Parent.get(curSong);
			Integer ArtistHash = MainActivity.Parent.get(AlbumHash);
			
			//Cambiar Titulo
        	//songTitleLabel.setText(songTitle);
        	
        	//Log.w("hola", songTitle);
        	
        	MainActivity.songPlayingTitle = songTitle;
        	MainActivity.songPlayingAlbum = MainActivity.Name.get(AlbumHash);
        	MainActivity.songPlayingArtist = MainActivity.Name.get(ArtistHash);
        	
        	//Update song
			
        	// Changing Button Image to pause image
			btnPlay.setImageResource(R.drawable.btn_pause);
			
			// set Progress bar values
			MainActivity.songProgressBar.setProgress(0);
			MainActivity.songProgressBar.setMax(100);
			
			songPath = MainActivity.SongPath.get(songHash);
			MainActivity.bmp = null;
			ImageView image = (ImageView) getView().findViewById(R.id.albumCover);
			image.setImageResource(R.drawable.adele);
			try {
				Mp3File mp3file = new Mp3File(songPath);
				if (mp3file.hasId3v2Tag()) {
				  	ID3v2 id3v2Tag = mp3file.getId3v2Tag();
					byte[] albumImageData = id3v2Tag.getAlbumImage();
					if (albumImageData != null) {
						Bitmap bmp=BitmapFactory.decodeByteArray(albumImageData,0,albumImageData.length);
						MainActivity.bmp = bmp;
						if (bmp!=null) {
							if (getView()!=null){
								image.setImageBitmap(bmp);
							}
						}
					  }
				}
			} catch (UnsupportedTagException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Updating progress bar
			updateProgressBar();			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (songTitleLabel!=null) songTitleLabel.setText(MainActivity.songPlayingTitle);
		if (artistText!=null) artistText.setText(MainActivity.songPlayingArtist + " - " + MainActivity.songPlayingAlbum);
	}
	
	
	/**
	 * Update timer on seekbar
	 * */
	public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);        
    }	
	
	/**
	 * Background Runnable thread
	 * */
	private Runnable mUpdateTimeTask = new Runnable() {
		   public void run() {
			   if (MainActivity.mp==null) {
				   MainActivity.mp = new MediaPlayer();
			   }
			   long totalDuration = MainActivity.mp.getDuration();
			   long currentDuration = MainActivity.mp.getCurrentPosition();
			  
			   // Displaying Total Duration time
			   songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
			   // Displaying time completed playing
			   songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));
			   
			   // Updating progress bar
			   int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
			   //Log.d("Progress", ""+progress);
			   MainActivity.songProgressBar.setProgress(progress);
			   
			   // Running this thread after 100 milliseconds
		       mHandler.postDelayed(this, 100);
		   }
		};
		
	/**
	 * 
	 * */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
		
	}

	/**
	 * When user starts moving the progress handler
	 * */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// remove message Handler from updating progress bar
		mHandler.removeCallbacks(mUpdateTimeTask);
    }
	
	/**
	 * When user stops moving the progress handler
	 * */
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = MainActivity.mp.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
		
		// forward or backward to certain seconds
		MainActivity.mp.seekTo(currentPosition);
		
		// update timer progress again
		updateProgressBar();
    }

}