package com.musicapp.urmusic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.musicapp.urmusic.MainActivity;
import com.musicapp.urmusic.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TwoLineListItem;

public class ArtistFragment extends Fragment {
	
	public static ArrayList<HashMap<String,String>> AlbumListArray;
	public static ArrayList<String> AlbumListArraySorted;
	OnHeadlineSelectedListener mCallback;
	private ImageButton btnCurSongPlaying;
	private TwoLineListItem btnCurSongPlayingText;
	private int activeButton = 0;
	
	public static String Artist;
	
	public ArtistFragment(){
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		View rootView;
		 
		if (MainActivity.songPlayingTitle!=null) {
			rootView = inflater.inflate(R.layout.artist_fragment_playing, container, false);
		} else {
			rootView = inflater.inflate(R.layout.artist_fragment, container, false);
		}
        
        Artist = getArguments().getString("Artist");

        
        Integer ArtistHash = MainActivity.Hash.get(Artist);
		
         
        return rootView;
    }
	
	public void onActivityCreated (Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		TextView ArtistText = (TextView) getView().findViewById(R.id.artist);
		ArtistText.setText(Artist);
		
		Bundle args = new Bundle();

		args.putString("Artist", Artist);

		FragmentManager manager = getChildFragmentManager();

	    if(manager.getBackStackEntryCount() > 0)
	    {
	        manager.popBackStack(null, 
	                             FragmentManager.POP_BACK_STACK_INCLUSIVE);
	    }
	    

		final Fragment albumsFragment = new AlbumsListFragment();
		final Fragment artistTopicsFragment = new ArtistTopicsFragment();
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		
		artistTopicsFragment.setArguments(args);
		albumsFragment.setArguments(args);
		
		FragmentTransaction transaction2 = getChildFragmentManager().beginTransaction();
		transaction2.remove(albumsFragment).commitAllowingStateLoss();
		
		transaction.add(R.id.albums, albumsFragment).commit();
		//transaction.hide(artistTopicsFragment);
		
		activeButton = 1;
		
		LinearLayout button1 = (LinearLayout) getView().findViewById(R.id.button1);
		LinearLayout button2 = (LinearLayout) getView().findViewById(R.id.button2);
		
		final View line1 = (View) getView().findViewById(R.id.line1);
		final View line2 = (View) getView().findViewById(R.id.line2);
		
		
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if (activeButton == 2) {
					activeButton = 1;
					
					//transaction.detach(albumsFragment)
					FragmentTransaction transaction2 = getChildFragmentManager().beginTransaction();
					transaction2.remove(artistTopicsFragment);
					transaction2.show(albumsFragment).commitAllowingStateLoss();
					//transaction2.add(R.id.albums, albumsFragment).commitAllowingStateLoss();
					
					line1.setBackgroundColor(0xFF2E9AFE);
					line2.setBackgroundColor(0xFF151515);
					
					ViewGroup.MarginLayoutParams margin1 = (ViewGroup.MarginLayoutParams) line1.getLayoutParams();
					margin1.setMargins(0, 0, 0, 0);
			        line1.requestLayout();
			        ViewGroup.MarginLayoutParams margin2 = (ViewGroup.MarginLayoutParams) line2.getLayoutParams();
					margin2.setMargins(0, 3, 0, 0);
			        line2.requestLayout();
			        
			        ViewGroup.LayoutParams params1 = (ViewGroup.LayoutParams) line1.getLayoutParams();
			        params1.height = 5;
			        line1.requestLayout();
			        ViewGroup.LayoutParams params2 = (ViewGroup.LayoutParams) line2.getLayoutParams();
			        params2.height = 2;
			        line2.requestLayout();
			        
			     
				}
			};
		});
		
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if (activeButton == 1) {
					activeButton = 2;
					
					//transaction.detach(albumsFragment)
					FragmentTransaction transaction2 = getChildFragmentManager().beginTransaction();
					//transaction2.remove(albumsFragment);
					transaction2.hide(albumsFragment);
				   // transaction2.detach((Fragment) albumsFragment);
					transaction2.add(R.id.albums, artistTopicsFragment).commitAllowingStateLoss();
					//transaction2.detach((Fragment) albumsFragment);
					
					line2.setBackgroundColor(0xFF2E9AFE);
					line1.setBackgroundColor(0xFF151515);
					
					ViewGroup.MarginLayoutParams margin1 = (ViewGroup.MarginLayoutParams) line1.getLayoutParams();
					margin1.setMargins(0, 3, 0, 0);
			        line1.requestLayout();
			        ViewGroup.LayoutParams params1 = (ViewGroup.LayoutParams) line1.getLayoutParams();
			        params1.height = 2;
			        line1.requestLayout();
			        ViewGroup.MarginLayoutParams margin2 = (ViewGroup.MarginLayoutParams) line2.getLayoutParams();
					margin2.setMargins(0, 0, 0, 0);
			        line2.requestLayout();
			        ViewGroup.LayoutParams params2 = (ViewGroup.LayoutParams) line2.getLayoutParams();
			        params2.height = 5;
			        line2.requestLayout();
			        
				}
			};
		});
		
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
    public void onStop() { 

        super.onStop();
    }
	
}