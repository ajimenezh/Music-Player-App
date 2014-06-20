package com.musicapp.urmusic;
//package com.android.musicplayer;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Set;
//
//import com.android.musicplayer.MainActivity;
//import com.android.musicplayer.R;
//import com.facebook.Session;
//import android.support.v4.app.ListFragment;
//import android.text.Editable;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TwoLineListItem;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemLongClickListener;
//
//public class PlaylistFragment extends ListFragment {
//		
//	OnHeadlineSelectedListener mCallback;
//	
//	private String URL_GET_COMMENTS = "http://192.168.1.34:5000/";
//	
//	Button btnCreatePlaylist;
//    private ArrayList<HashMap<String,String>> arrayList = new ArrayList<HashMap<String,String>>();
//    private ArrayList<String> playlists = new ArrayList<String>();
//    private ArrayList<String> ArtistList = new ArrayList<String>();
//    private ArrayList<String> AlbumList = new ArrayList<String>();
//    private ArrayList<String> SongList = new ArrayList<String>();
//    private Set<String> playlistsSet = new HashSet<String>();
//    private String Title;
//    
//    private TwoLineListItem btnCurSongPlayingText;
//	
//	private ImageButton btnCurSongPlaying;
//
//	private DatabaseHelper db;
//
//	private long playlistId;
//
//	private Playlist playlist = new Playlist();
//
//	private ListView myListView;
//	public static SongAdapter adapter;
//	
//	private ArrayList<Song> songs = new ArrayList<Song>();
//	
//	private int activeButton = 0;
//	
//	LinearLayout newButton;
//
//	public PlaylistFragment(){}
//	
//	@Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState) {
//
//		View rootView;
//		
//		db = new DatabaseHelper(getActivity());
//			
//		if (MainActivity.songPlayingTitle!=null) {
//			rootView = inflater.inflate(R.layout.playlist_song_playing, container, false);
//		} else {
//			rootView = inflater.inflate(R.layout.playlist_song, container, false);
//		}
//				
//		// start Facebook Login
////		  Session.openActiveSession(getActivity(), true, new Session.StatusCallback() {
////		
////		    // callback when session changes state
////			@Override
////			public void call(Session session, SessionState state, Exception exception) {
////				if (session.isOpened()) {
////					// make request to the /me API
////					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
////			
////					  // callback after Graph API response with user object
////			    		  @Override
////			    		  public void onCompleted(GraphUser user, Response response) {
////			    			  if (user != null) {
////			    				  MainActivity.userID = user.getId();
////			    				  
////			    				}
////			    			  else {
////			    				  mCallback.onArticleSelected(25,"");
////			    			  }
////			    		  }
////			    		});
////			    	}
////			    }
////		  });
//		  
//		  if (MainActivity.userID!=null) {
//			  //GetGroups();
//		  }
//		
//		playlistId = Long.parseLong(getArguments().getString("playlistId")); 
//		
//		playlist  = db.getPlaylist(playlistId);
//		
//		songs = db.getAllSongs(playlist);
//         
//        return rootView;
//    }
//	
//	public void onActivityCreated (Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//
//		adapter = new SongAdapter(getActivity(), songs);
//		
//		myListView = (ListView) getView().findViewById(android.R.id.list);
//		
//		myListView.setAdapter(adapter);
//		
//		activeButton = 1;
//		
//		LinearLayout button1 = (LinearLayout) getView().findViewById(R.id.button1);
//		LinearLayout button2 = (LinearLayout) getView().findViewById(R.id.button2);
//		
//		final View line1 = (View) getView().findViewById(R.id.line1);
//		final View line2 = (View) getView().findViewById(R.id.line2);
//		
//		final View header = getActivity().getLayoutInflater().inflate(R.layout.topics_list_header, null);
//		
//		newButton = null;
//		
//		final View.OnClickListener newMemberClick = new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				  if (MainActivity.userID!=null) {
//					  mCallback.onArticleSelected(25, ""+playlistId);
//				  }
//				  
//			}
//		};
//		
//		button1.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View arg0) {
//				if (activeButton == 2) {
//					activeButton = 1;
//					
//					setListAdapter(null);
//					myListView.removeHeaderView(header);
//					
//					adapter = new SongAdapter(getActivity(), songs);
//										
//					myListView.setAdapter(adapter);
//					
//					line1.setBackgroundColor(0xFF2E9AFE);
//					line2.setBackgroundColor(0xFF151515);
//					
//					ViewGroup.MarginLayoutParams margin1 = (ViewGroup.MarginLayoutParams) line1.getLayoutParams();
//					margin1.setMargins(0, 0, 0, 0);
//			        line1.requestLayout();
//			        ViewGroup.MarginLayoutParams margin2 = (ViewGroup.MarginLayoutParams) line2.getLayoutParams();
//					margin2.setMargins(0, 3, 0, 0);
//			        line2.requestLayout();
//			        
//			        ViewGroup.LayoutParams params1 = (ViewGroup.LayoutParams) line1.getLayoutParams();
//			        params1.height = 5;
//			        line1.requestLayout();
//			        ViewGroup.LayoutParams params2 = (ViewGroup.LayoutParams) line2.getLayoutParams();
//			        params2.height = 2;
//			        line2.requestLayout();
//			     
//				}
//			};
//		});
//		
//		button2.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View arg0) {
//				if (activeButton == 1) {
//					activeButton = 2;
//					
//					setListAdapter(null);
//					myListView.addHeaderView(header);
//					
//					newButton = (LinearLayout) getView().findViewById(R.id.new_button);
//					newButton.setOnClickListener(newMemberClick);
//					
//					adapter = new SongAdapter(getActivity(), new ArrayList<Song>());
//					
//					myListView.setAdapter(adapter);
//					
//					line2.setBackgroundColor(0xFF2E9AFE);
//					line1.setBackgroundColor(0xFF151515);
//					
//					ViewGroup.MarginLayoutParams margin1 = (ViewGroup.MarginLayoutParams) line1.getLayoutParams();
//					margin1.setMargins(0, 3, 0, 0);
//			        line1.requestLayout();
//			        ViewGroup.LayoutParams params1 = (ViewGroup.LayoutParams) line1.getLayoutParams();
//			        params1.height = 2;
//			        line1.requestLayout();
//			        ViewGroup.MarginLayoutParams margin2 = (ViewGroup.MarginLayoutParams) line2.getLayoutParams();
//					margin2.setMargins(0, 0, 0, 0);
//			        line2.requestLayout();
//			        ViewGroup.LayoutParams params2 = (ViewGroup.LayoutParams) line2.getLayoutParams();
//			        params2.height = 5;
//			        line2.requestLayout();
//				}
//			};
//		});
//		
//		
//		if (MainActivity.songPlayingTitle!=null) {
//			Log.w("hola","high");
//			btnCurSongPlaying = (ImageButton) getView().findViewById(R.id.btn_cur_song_playing);
//			btnCurSongPlayingText = (TwoLineListItem) getView().findViewById(R.id.cur_song_playing);
//			
//			if (!MainActivity.mp.isPlaying()) {
//				btnCurSongPlaying.setImageResource(R.drawable.btn_play_smaller);
//	        }
//	        else {
//	        	btnCurSongPlaying.setImageResource(R.drawable.btn_pause_smaller);
//	        }
//			
//			btnCurSongPlaying.setOnClickListener(new View.OnClickListener() {
//				public void onClick(View arg0) {
//					if(MainActivity.mp.isPlaying()){
//						if(MainActivity.mp!=null){
//							MainActivity.am.abandonFocus();
//							MainActivity.mp.pause();
//							MainActivity.isPlaying = false;
//							// Changing button image to play button
//							btnCurSongPlaying.setImageResource(R.drawable.btn_play_smaller);
//						}
//					}else{
//						// Resume song
//						if(MainActivity.mp!=null){
//							MainActivity.am.requestFocus();
//							MainActivity.mp.start();
//							MainActivity.isPlaying = true;
//							// Changing button image to pause button
//							btnCurSongPlaying.setImageResource(R.drawable.btn_pause_smaller);
//						}
//					}
//				};
//			});
//			
//			btnCurSongPlayingText.setOnClickListener(new View.OnClickListener() {
//				public void onClick(View arg0) {
//					db.closeDB();
//					mCallback.onArticleSelected(4, "");
//				};
//			});
//			
//		}
//		
//		myListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1,
//					int pos, long id) {
//					
//				MainActivity.SongListMap.clear();
//				MainActivity.SongHashListArray.clear();
//
//				
//				Integer cnt = 0;
//				for(int i=0; i<songs.size(); i++) {
//					String key = songs.get(i).getAlbum() + "/" + songs.get(i).getTitle();
//					Log.w("hello", songs.get(i).getAlbum() + "  " + songs.get(i).getTitle());
//					if (MainActivity.Hash.containsKey(key)) {
//						MainActivity.SongHashListArray.add(MainActivity.Hash.get(key));
//						MainActivity.SongListMap.put(MainActivity.Name.get(MainActivity.Hash.get(key)), cnt);
//						cnt++;
//					}
//				}
//				
//				MainActivity.currentSongIndex = pos;
//				MainActivity.isInAllSongs = false;
//				db.closeDB();
//				mCallback.onArticleSelected(3, songs.get(pos).getAlbum() + "/" + songs.get(pos).getTitle());
//				
//			}
//			
//		});
//		
//		myListView.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//                    final int pos, long id) {
//                // TODO Auto-generated method stub
//
//            	AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//            	
//            	    	alert.setTitle("Eliminar?");
//            	
//            	    	// Set an EditText view to get user input 
//            	    	//final EditText input = new EditText(getActivity());
//            	    	//alert.setView(input);
//            	
//            	    	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            	    	public void onClick(DialogInterface dialog, int whichButton) {
//            	    		//Editable value = input.getText();
//            	    		
//            	    		//String name = value.toString();
//            	    		//name += "\n";
//            	    		
//            	    		
//            	    		db.deleteSong(songs.get(pos).getPlaylist());
//            	    		
//            	    		songs.remove(pos);
//            	    		
//            	    		adapter.notifyDataSetChanged();
//            	    		
//            	    		myListView.setAdapter(adapter);
//            	    	 
//            	    	  }
//            	    	});
//            	
//            	    	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            	    	  public void onClick(DialogInterface dialog, int whichButton) {
//            	    	    // Canceled.
//            	    	  }
//            	    	});
//            	
//            	    	alert.show();
//
//                return true;
//            }
//        });
//		
//		mCallback.onArticleSelected(9, "");
//        
//	}
//	
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//	  super.onActivityResult(requestCode, resultCode, data);
//	  Session.getActiveSession().onActivityResult(getActivity(), requestCode, resultCode, data);
//	}
//	
//	// Container Activity must implement this interface
//    public interface OnHeadlineSelectedListener {
//        public void onArticleSelected(int position, String str);
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        
//        // This makes sure that the container activity has implemented
//        // the callback interface. If not, it throws an exception
//        try {
//            mCallback = (OnHeadlineSelectedListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnHeadlineSelectedListener");
//        }
//    }
//    
//
////	@Override
////    public void onListItemClick(ListView l, View v, int position, long id) {
////		
////		MainActivity.SongListMap.clear();
////		MainActivity.SongHashListArray.clear();
////
////		
////		Integer cnt = 0;
////		for(int i=0; i<SongList.size(); i++) {
////			String key = AlbumList.get(i) + "/" + SongList.get(i);
////			if (MainActivity.Hash.containsKey(key)) {
////				MainActivity.SongHashListArray.add(MainActivity.Hash.get(key));
////				MainActivity.SongListMap.put(MainActivity.Name.get(MainActivity.Hash.get(key)), cnt);
////				cnt++;
////			}
////		}
////		
////		MainActivity.currentSongIndex = position;
////		MainActivity.isInAllSongs = false;
////		mCallback.onArticleSelected(3, AlbumList.get(position)+"/"+SongList.get(position));
////    }
//}