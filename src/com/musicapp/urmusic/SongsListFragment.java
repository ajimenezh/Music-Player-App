package com.musicapp.urmusic;

import java.util.ArrayList;
import java.util.HashMap;


import com.musicapp.urmusic.MainActivity;
import com.musicapp.urmusic.PlayerFragment;
import com.musicapp.urmusic.R;

import android.support.v4.app.ListFragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TwoLineListItem;

public class SongsListFragment extends ListFragment {
	
	public static ArrayList<HashMap<String,String>> SongListArray;
	public static ArrayList<Integer> SongListArraySorted;
	public String Album;
	public Integer AlbumHash;
	OnHeadlineSelectedListener mCallback;
	
	private ImageButton btnCurSongPlaying;
	private TwoLineListItem btnCurSongPlayingText;
	
	public static String Artist;
	
	public SongsListFragment(){
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View rootView;
 
		if (MainActivity.songPlayingTitle!=null) {
			rootView = inflater.inflate(R.layout.artists, container, false);
		} else {
			rootView = inflater.inflate(R.layout.playlist, container, false);
		}
        
        AlbumHash = MainActivity.Hash.get(getArguments().getString("AlbumHash"));
		
		Album = MainActivity.Name.get(AlbumHash);
		
		//SongList = MainActivity.SongList.get(Album);

		// get all songs from sdcard
		//this.songsList = plm.getPlayList();
		
		SongListArray = new ArrayList<HashMap<String,String>>();
		//MainActivity.SongListMap.clear();
		//MainActivity.SongHashListArray.clear();
		SongListArraySorted = new ArrayList<Integer>();
		
		for(Integer Song : MainActivity.Next.get(AlbumHash)) {
			SongListArraySorted.add(Song);
		}
		
		for (Integer i=0; i<SongListArraySorted.size(); i++) {
			for (Integer j = i+1; j<SongListArraySorted.size(); j++) {
				if (MainActivity.Track.get(SongListArraySorted.get(i))>MainActivity.Track.get(SongListArraySorted.get(j))) {
					Integer tmp = SongListArraySorted.get(i);
					SongListArraySorted.set(i, SongListArraySorted.get(j));
					SongListArraySorted.set(j, tmp);
				}
			}
		}
		
		Integer cnt = 0;
		for(Integer Song : SongListArraySorted) {
			HashMap<String, String> song = new HashMap<String, String>();
			song.put("songTitle", MainActivity.Name.get(Song));
			SongListArray.add(song);
			//MainActivity.SongHashListArray.add(Song);
			//MainActivity.SongListMap.put(MainActivity.Name.get(Song), cnt);
			cnt++;
		}
		
		// Adding menuItems to ListView
		ListAdapter adapter = new SimpleAdapter(getActivity(), SongListArray,
				R.layout.playlist_item, new String[] { "songTitle" }, new int[] {
						R.id.songTitle });

		setListAdapter(adapter);

		// selecting single ListView item
         
        return rootView;
    }
	
	public void onActivityCreated (Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		getListView().setFastScrollEnabled(true);
		
		if (MainActivity.songPlayingTitle!=null) {
			Log.w("hola","high");
			btnCurSongPlaying = (ImageButton) getView().findViewById(R.id.btn_cur_song_playing);
			btnCurSongPlayingText = (TwoLineListItem) getView().findViewById(R.id.cur_song_playing);
			
			if (!MainActivity.mp.isPlaying()) {
				btnCurSongPlaying.setImageResource(R.drawable.btn_play_smaller);
	        }
	        else {
	        	btnCurSongPlaying.setImageResource(R.drawable.btn_pause_smaller);
	        }
			
			btnCurSongPlaying.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {
					if(MainActivity.mp.isPlaying()){
						if(MainActivity.mp!=null){
							MainActivity.am.abandonFocus();
							MainActivity.mp.pause();
							MainActivity.isPlaying = false;
							// Changing button image to play button
							btnCurSongPlaying.setImageResource(R.drawable.btn_play_smaller);
						}
					}else{
						// Resume song
						if(MainActivity.mp!=null){
							MainActivity.am.requestFocus();
							MainActivity.mp.start();
							MainActivity.isPlaying = true;
							// Changing button image to pause button
							btnCurSongPlaying.setImageResource(R.drawable.btn_pause_smaller);
						}
					}
				};
			});
			
			btnCurSongPlayingText.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {
					mCallback.onArticleSelected(4, "");
				};
			});
			
		}
		
		mCallback.onArticleSelected(9, "");
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
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
		MainActivity.currentSongIndex = position;
		MainActivity.isInAllSongs = false;
		
		//Log.w("hello", "" + position);
		
		SongListArray.clear();
		MainActivity.SongListMap.clear();
		MainActivity.SongHashListArray.clear();
		SongListArraySorted.clear();
		
		for(Integer Song : MainActivity.Next.get(AlbumHash)) {
			SongListArraySorted.add(Song);
		}
		
		for (Integer i=0; i<SongListArraySorted.size(); i++) {
			for (Integer j = i+1; j<SongListArraySorted.size(); j++) {
				if (MainActivity.Track.get(SongListArraySorted.get(i))>MainActivity.Track.get(SongListArraySorted.get(j))) {
					Integer tmp = SongListArraySorted.get(i);
					SongListArraySorted.set(i, SongListArraySorted.get(j));
					SongListArraySorted.set(j, tmp);
				}
			}
		}
		
		Integer cnt = 0;
		for(Integer Song : SongListArraySorted) {
			HashMap<String, String> song = new HashMap<String, String>();
			song.put("songTitle", MainActivity.Name.get(Song));
			SongListArray.add(song);
			MainActivity.SongHashListArray.add(Song);
			MainActivity.SongListMap.put(MainActivity.Name.get(Song), cnt);
			//Log.w("hello", "" + cnt + " " + Song);
			cnt++;
		}
		
		mCallback.onArticleSelected(3, Album+"/"+SongListArray.get(position).get("songTitle"));
		
    }
}