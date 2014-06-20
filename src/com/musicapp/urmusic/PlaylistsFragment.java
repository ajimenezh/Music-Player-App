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
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemLongClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.SimpleAdapter;
//import android.widget.TextView;
//import android.widget.TwoLineListItem;
//
//public class PlaylistsFragment extends ListFragment {
//		
//	OnHeadlineSelectedListener mCallback;
//	
//	private String URL_GET_COMMENTS = "http://192.168.1.34:5000/";
//	
//	Button btnCreatePlaylist;
//    private ArrayList<HashMap<String,String>> arrayList = new ArrayList<HashMap<String,String>>();
//    private Set<String> playlistsSet = new HashSet<String>();
//    public static PlaylistsAdapter adapter;
//    
//    private TwoLineListItem btnCurSongPlayingText;
//	
//	private ImageButton btnCurSongPlaying;
//	
//	private DatabaseHelper db;
//	private ArrayList<Playlist> playlists;
//	private ListView myListView;
//
//	public PlaylistsFragment(){}
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
//			rootView = inflater.inflate(R.layout.playlists_playing, container, false);
//		} else {
//			rootView = inflater.inflate(R.layout.playlists, container, false);
//		}
//		
//		arrayList.clear();
//		playlistsSet.clear();
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
////		String FILENAME = "playlists";
////  	  	
////  	  	FileInputStream fos;
////		try {
////			fos = getActivity().openFileInput(FILENAME);
////			
////			DataInputStream in = new DataInputStream(fos);
////			BufferedReader br = new BufferedReader(new InputStreamReader(in));
////			String line = "";
////	        while ((line = br.readLine()) != null) {
////	        	playlists.add(line);
////	        	playlistsSet.add(line);
////	        	HashMap<String,String> playlistHashMap = new HashMap<String,String>();
////	        	playlistHashMap.put("id", line);
////	        	arrayList.add(playlistHashMap);
////	        }
////			
////  	  		fos.close();
////		} catch (FileNotFoundException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////		
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
//		//if (MainActivity.userID!=null) {
//			btnCreatePlaylist = (Button) getView().findViewById(R.id.btn_create_playlist);
//			
//			btnCreatePlaylist.setOnClickListener(new View.OnClickListener() {
//				 
//	            @Override
//	            public void onClick(View v) {
//	            	
//	            	AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//
//	            	alert.setTitle("Nombre");
//
//	            	// Set an EditText view to get user input 
//	            	final EditText input = new EditText(getActivity());
//	            	alert.setView(input);
//
//	            	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//	            	public void onClick(DialogInterface dialog, int whichButton) {
//	            		Editable value = input.getText();
//	            		
//	            		String name = value.toString();
//	            		
////	            		String FILENAME = "playlists";
////
////	            		FileOutputStream fos;
////						try {
////							fos = getActivity().openFileOutput(FILENAME, Context.MODE_APPEND);
////							
////							if (!playlistsSet.contains(name)) {
////		            			playlists.add(name);
////		            			playlistsSet.add(name);
////		        	        	HashMap<String,String> playlistHashMap = new HashMap<String,String>();
////		        	        	playlistHashMap.put("id", name);
////		        	        	arrayList.add(playlistHashMap);
////		        	        	fos.write(name.getBytes());
////		            		}
////		            		adapter.notifyDataSetChanged();
////		            		
////		            		fos.close();
////		            		
////						} catch (FileNotFoundException e) {
////							// TODO Auto-generated catch block
////							e.printStackTrace();
////						} catch (IOException e) {
////							// TODO Auto-generated catch block
////							e.printStackTrace();
////						}
//	            		
//	            		Playlist pl = new Playlist(name);
//	            		
//	            		long id = db.createPlaylist(pl);
//	            		
//	            		pl.setId(id);
//	            		
//	            		playlists.add(pl);
//	            		
//	            		adapter.notifyDataSetChanged();
//	            		
//	            		//myListView = (ListView) getView().findViewById(android.R.id.list);
//	            		myListView.setAdapter(adapter);
//	            	  }
//	            	});
//
//	            	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//	            	  public void onClick(DialogInterface dialog, int whichButton) {
//	            	    // Canceled.
//	            	  }
//	            	});
//
//	            	alert.show();
//	            }
//	        });  
//		//}
//			
//			if (MainActivity.songPlayingTitle!=null) {
//				Log.w("hola","high");
//				btnCurSongPlaying = (ImageButton) getView().findViewById(R.id.btn_cur_song_playing);
//				btnCurSongPlayingText = (TwoLineListItem) getView().findViewById(R.id.cur_song_playing);
//				
//				if (!MainActivity.mp.isPlaying()) {
//					btnCurSongPlaying.setImageResource(R.drawable.btn_play_smaller);
//		        }
//		        else {
//		        	btnCurSongPlaying.setImageResource(R.drawable.btn_pause_smaller);
//		        }
//				
//				btnCurSongPlaying.setOnClickListener(new View.OnClickListener() {
//					public void onClick(View arg0) {
//						if(MainActivity.mp.isPlaying()){
//							if(MainActivity.mp!=null){
//								MainActivity.am.abandonFocus();
//								MainActivity.mp.pause();
//								MainActivity.isPlaying = false;
//								// Changing button image to play button
//								btnCurSongPlaying.setImageResource(R.drawable.btn_play_smaller);
//							}
//						}else{
//							// Resume song
//							if(MainActivity.mp!=null){
//								MainActivity.am.requestFocus();
//								MainActivity.mp.start();
//								MainActivity.isPlaying = true;
//								// Changing button image to pause button
//								btnCurSongPlaying.setImageResource(R.drawable.btn_pause_smaller);
//							}
//						}
//					};
//				});
//				
//				btnCurSongPlayingText.setOnClickListener(new View.OnClickListener() {
//					public void onClick(View arg0) {
//						db.closeDB();
//						mCallback.onArticleSelected(4, "");
//					};
//				});
//				
//			}
//			
//			myListView.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1,
//						int pos, long id) {
//					
//					db.closeDB();
//					mCallback.onArticleSelected(65,""+playlists.get(pos).getId());
//					
//				}
//				
//			});
//			
//			myListView.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//	            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//	                    final int pos, long id) {
//	                // TODO Auto-generated method stub
//
//	            	AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//	            	
//	            	    	alert.setTitle("Eliminar?");
//	            	
//	            	    	// Set an EditText view to get user input 
//	            	    	//final EditText input = new EditText(getActivity());
//	            	    	//alert.setView(input);
//	            	
//	            	    	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//	            	    	public void onClick(DialogInterface dialog, int whichButton) {
//	            	    		//Editable value = input.getText();
//	            	    		
//	            	    		//String name = value.toString();
//	            	    		//name += "\n";
//	            	    		
//	            	    		Log.w("hello", "" + playlists.size());
//	            	    		
//	            	    		Log.w("hello", "" + playlists.get(pos).getTitle());
//	            	    		
//	            	    		db.deletePlaylist(playlists.get(pos).getId());
//	            	    		
//	            	    		playlists.remove(pos);
//	            	    		
//	            	    		adapter.notifyDataSetChanged();
//	            	    		
//	            	    		myListView.setAdapter(adapter);
//	            	    	 
//	            	    	  }
//	            	    	});
//	            	
//	            	    	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//	            	    	  public void onClick(DialogInterface dialog, int whichButton) {
//	            	    	    // Canceled.
//	            	    	  }
//	            	    	});
//	            	
//	            	    	alert.show();
//
//	                return true;
//	            }
//	        }); 
//			
//			mCallback.onArticleSelected(9, "");
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
////		Log.w("hello", "hola");
////		mCallback.onArticleSelected(65,""+playlists.get(position).getId());
////    }
//	
////	adapter.setOnItemLongClickListener (new OnItemLongClickListener() {
////		  public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
////			//mCallback.onArticleSelected(65,playlists.get(position));
////			
////			AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
////	
////	    	alert.setTitle("Eliminar?");
////	
////	    	// Set an EditText view to get user input 
////	    	//final EditText input = new EditText(getActivity());
////	    	//alert.setView(input);
////	
////	    	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
////	    	public void onClick(DialogInterface dialog, int whichButton) {
////	    		//Editable value = input.getText();
////	    		
////	    		//String name = value.toString();
////	    		//name += "\n";
////	    		
////	    		db.deletePlaylist(Long.parseLong(arrayList.get(position).get("Id")));
////	    	 
////	    	  }
////	    	});
////	
////	    	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
////	    	  public void onClick(DialogInterface dialog, int whichButton) {
////	    	    // Canceled.
////	    	  }
////	    	});
////	
////	    	alert.show();
////	    }
////	});
//}