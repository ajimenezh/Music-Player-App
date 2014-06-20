package com.musicapp.urmusic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.musicapp.urmusic.MainActivity;
import com.musicapp.urmusic.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TwoLineListItem;

public class AlbumsListFragment extends ListFragment {
	
	public static ArrayList<HashMap<String,String>> AlbumListArray;
	public static ArrayList<String> AlbumListArraySorted;
	OnHeadlineSelectedListener mCallback;
	private ImageButton btnCurSongPlaying;
	private TwoLineListItem btnCurSongPlayingText;
	
	public static String Artist;
	
	public AlbumsListFragment(){
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		View rootView;
		 
		rootView = inflater.inflate(R.layout.playlist, container, false);
        
        Artist = getArguments().getString("Artist");

        
        Integer ArtistHash = MainActivity.Hash.get(Artist);
		
		//this.AlbumList = MainActivity.AlbumList.get(Artist);

		// get all songs from sdcard
		//this.songsList = plm.getPlayList();
		
		AlbumListArray = new ArrayList<HashMap<String,String>>();
		AlbumListArraySorted = new ArrayList<String>();
		
		for(Integer Album: MainActivity.Next.get(ArtistHash)) {
			AlbumListArraySorted.add(MainActivity.Name.get(Album));
		}
		Collections.sort(AlbumListArraySorted);
		
		for(String Album: AlbumListArraySorted) {
			HashMap<String, String> song = new HashMap<String, String>();
			song.put("songAlbum", Album);
			AlbumListArray.add(song);
		}
		
		// Adding menuItems to ListView
		ListAdapter adapter = new SimpleAdapter(getActivity(), AlbumListArray,
				R.layout.playlist_item, new String[] { "songAlbum" }, new int[] {
						R.id.songTitle });

		setListAdapter(adapter);
         
        return rootView;
    }
	
	public void onActivityCreated (Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		getListView().setFastScrollEnabled(true);
		
		
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
        
		mCallback.onArticleSelected(2, Artist+"/"+AlbumListArray.get(position).get("songAlbum"));
		
    }
}
