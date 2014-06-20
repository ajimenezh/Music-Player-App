package com.musicapp.urmusic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.musicapp.urmusic.MainActivity;
import com.musicapp.urmusic.R;
import com.musicapp.urmusic.AllSongsListFragment.OnHeadlineSelectedListener;

import android.support.v4.app.ListFragment;
import android.app.Activity;
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

public class AllAlbumsListFragment extends ListFragment {
	
	// Songs list
		public static ArrayList<HashMap<String,String>> AlbumsListArray;
		public static ArrayList<String> AlbumsListArraySorted;
		public static HashMap<String,Integer> Hash;
		private TwoLineListItem btnCurSongPlayingText;

		
		OnHeadlineSelectedListener mCallback;
		
		private ImageButton btnCurSongPlaying;
	
	public AllAlbumsListFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		View rootView;
		 
		if (MainActivity.songPlayingTitle!=null) {
			rootView = inflater.inflate(R.layout.artists, container, false);
		} else {
			rootView = inflater.inflate(R.layout.playlist, container, false);
		}
        
        AlbumsListArray = new ArrayList<HashMap<String,String>>();
		AlbumsListArraySorted = new ArrayList<String>();
		Hash = new HashMap<String,Integer>();
		
		for(Integer index : MainActivity.AllAlbums) {
			AlbumsListArraySorted.add(MainActivity.Name.get(index));
			Hash.put(MainActivity.Name.get(index), index);
		}
		Collections.sort(AlbumsListArraySorted);
		
		for(String Artist : AlbumsListArraySorted) {
			HashMap<String, String> song = new HashMap<String, String>();
			song.put("songAlbum", Artist);
			AlbumsListArray.add(song);
		}
		
		// Adding menuItems to ListView
		ListAdapter adapter = new SimpleAdapter(getActivity(), AlbumsListArray,
				R.layout.playlist_item, new String[] { "songAlbum" }, new int[] {
						R.id.songTitle });

		setListAdapter(adapter);

		// selecting single ListView item
		//ListView lv = getListView();
         
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
		String Album = AlbumsListArray.get(position).get("songAlbum");
		Integer AlbumHash = Hash.get(Album);
		String Artist = MainActivity.Name.get(MainActivity.Parent.get(AlbumHash));
		mCallback.onArticleSelected(2, Artist+"/"+Album);
		
    }
}
