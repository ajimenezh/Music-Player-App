package com.musicapp.urmusic;
//package com.android.musicplayer;
//
//import io.socket.IOAcknowledge;
//import io.socket.IOCallback;
//import io.socket.SocketIOException;
//
//import java.io.BufferedReader;
//import java.io.DataInputStream;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.math.BigInteger;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.android.musicplayer.MainActivity;
//import com.android.musicplayer.R;
//import com.android.musicplayer.SongsListFragment.OnHeadlineSelectedListener;
//import com.facebook.Request;
//import com.facebook.Response;
//import com.facebook.Session;
//import com.facebook.SessionState;
//import com.facebook.model.GraphUser;
//
//import android.support.v4.app.Fragment;
//import android.support.v4.app.ListFragment;
//import android.text.Editable;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.SimpleAdapter;
//import android.widget.TextView;
//import android.widget.TwoLineListItem;
//
//public class AddPlaylistFragment extends ListFragment {
//		
//	OnHeadlineSelectedListener mCallback;
//	
//	private String URL_GET_COMMENTS = "http://192.168.1.34:5000/";
//	
//	Button btnCreatePlaylist;
//    private ArrayList<HashMap<String,String>> arrayList = new ArrayList<HashMap<String,String>>();
//    private Set<String> playlistsSet = new HashSet<String>();
//
//	private DatabaseHelper db;
//	private ArrayList<Playlist> playlists;
//	private ListView myListView;
//	public static PlaylistsAdapter adapter;
//
//	public AddPlaylistFragment(){}
//	
//	@Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState) {
//
//		View rootView;
//		
//		db = new DatabaseHelper(getActivity());
//			
//		rootView = inflater.inflate(R.layout.playlists, container, false);
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
//		  playlists = new ArrayList<Playlist>();
//		  
//		  playlists = db.getAllPlaylists();
//		  
//		  for (int i=0; i<playlists.size(); i++) {
//			  HashMap<String,String> playlistHashMap = new HashMap<String,String>();
//			  playlistHashMap.put("id", playlists.get(i).getTitle());
//			  playlistHashMap.put("Id", "" + playlists.get(i).getId());
//			  arrayList.add(playlistHashMap);
//		  }
//		
//         
//        return rootView;
//    }
//	
//	public void onActivityCreated (Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//		
//		adapter = new PlaylistsAdapter(getActivity(), playlists);
//		
//		myListView = (ListView) getView().findViewById(android.R.id.list);
//		
//		myListView.setAdapter(adapter);
//		
//		if (MainActivity.userID!=null) {
//			btnCreatePlaylist = (Button) getView().findViewById(R.id.btn_create_playlist);
//			
//			btnCreatePlaylist.setOnClickListener(new View.OnClickListener() {
//				 
//				 @Override
//		            public void onClick(View v) {
//		            	
//		            	AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//
//		            	alert.setTitle("Nombre");
//
//		            	// Set an EditText view to get user input 
//		            	final EditText input = new EditText(getActivity());
//		            	alert.setView(input);
//
//		            	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//		            	public void onClick(DialogInterface dialog, int whichButton) {
//		            		Editable value = input.getText();
//		            		
//		            		String name = value.toString();
//		            		
//		            		
//		            		Playlist pl = new Playlist(name);
//		            		
//		            		long id = db.createPlaylist(pl);
//		            		
//		            		pl.setId(id);
//		            		
//		            		playlists.add(pl);
//		            		
//		            		adapter.notifyDataSetChanged();
//		            		
//		            		//myListView = (ListView) getView().findViewById(android.R.id.list);
//		            		myListView.setAdapter(adapter);
//		            	  }
//		            	});
//
//		            	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//		            	  public void onClick(DialogInterface dialog, int whichButton) {
//		            	    // Canceled.
//		            	  }
//		            	});
//
//		            	alert.show();
//		            }
//	        });  
//		}
//        
//	}
//	
//	void addSong(int position) {
//
//		Playlist playlist = playlists.get(position);
//		
//		Song song = new Song();
//		
//		song.setPlaylist(playlist.getId());
//		song.setTitle(MainActivity.songPlayingTitle);
//		song.setAlbum(MainActivity.songPlayingAlbum);
//		song.setArtist(MainActivity.songPlayingArtist);
//        
//		db.createSong(song);
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
//	@Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//		addSong(position);
//		mCallback.onArticleSelected(100,"");
//    }
//}