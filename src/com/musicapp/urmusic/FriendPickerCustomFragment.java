package com.musicapp.urmusic;
//package com.android.musicplayer;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//import com.facebook.FacebookException;
//import com.facebook.model.GraphUser;
//import com.facebook.widget.FriendPickerFragment;
//import com.facebook.widget.PickerFragment;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//// This class provides an example of an Activity that uses FriendPickerFragment to display a list of
//// the user's friends. It takes a programmatic approach to creating the FriendPickerFragment with the
//// desired parameters -- see PickPlaceActivity in the PlacePickerSample project for an example of an
//// Activity creating a fragment (in this case a PlacePickerFragment) via XML layout rather than
//// programmatically.
//public class FriendPickerCustomFragment extends Fragment {
//    FriendPickerFragment friendPickerFragment;
//    
//    long playlistId;
//    
//    private DatabaseHelper db;
//
//    // A helper to simplify life for callers who want to populate a Bundle with the necessary
//    // parameters. A more sophisticated Activity might define its own set of parameters; our needs
//    // are simple, so we just populate what we want to pass to the FriendPickerFragment.
//    public static void populateParameters(Intent intent, String userId, boolean multiSelect, boolean showTitleBar) {
//        intent.putExtra(FriendPickerFragment.USER_ID_BUNDLE_KEY, userId);
//        intent.putExtra(FriendPickerFragment.MULTI_SELECT_BUNDLE_KEY, multiSelect);
//        intent.putExtra(FriendPickerFragment.SHOW_TITLE_BAR_BUNDLE_KEY, showTitleBar);
//    }
//    
//	@Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState) {
//		
//		playlistId = Long.parseLong(getArguments().getString("playlistId"));
//
//		View rootView;
//		
//		rootView = inflater.inflate(R.layout.pick_friends_activity, container, false);
//		
//        return rootView;
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        FragmentManager fm = getChildFragmentManager();
//
//        if (savedInstanceState == null) {
//
//            friendPickerFragment = new FriendPickerFragment();
//            fm.beginTransaction()
//                    .add(R.id.friend_picker_fragment, friendPickerFragment)
//                    .commit();
//        } else {
//            // Subsequent times, our fragment is recreated by the framework and already has saved and
//            // restored its state, so we don't need to specify args again. (In fact, this might be
//            // incorrect if the fragment was modified programmatically since it was created.)
//            friendPickerFragment = (FriendPickerFragment) fm.findFragmentById(R.id.friend_picker_fragment);
//        }
//        
//        try {
//            friendPickerFragment.loadData(true);
//        } catch (Exception ex) {
//            onError(ex);
//        }
//
//        friendPickerFragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
//            @Override
//            public void onError(PickerFragment<?> fragment, FacebookException error) {
//                
//            }
//        });
//
//        friendPickerFragment.setOnDoneButtonClickedListener(new PickerFragment.OnDoneButtonClickedListener() {
//            @Override
//            public void onDoneButtonClicked(PickerFragment<?> fragment) {
//                // We just store our selection in the Application for other activities to look at.
//            	
//            	List<GraphUser> friendsPicked = friendPickerFragment.getSelection();
//            	
//            	db = new DatabaseHelper(getActivity());
//            	Playlist pl = db.getPlaylist(playlistId);
//            	ArrayList<Song> songs = db.getAllSongs(pl);
//            	
//            	JSONArray array = new JSONArray();
//
//            	for (int i=0; i<songs.size(); i++) {
//            		JSONObject tmp = new JSONObject();
//            		try {
//						tmp.put("title", songs.get(i).getTitle());
//						tmp.put("album", songs.get(i).getAlbum());
//	            		tmp.put("artist", songs.get(i).getArtist());
//	            		
//	            		array.put(tmp);
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//            	}
//            	
//            	JSONObject playlist = new JSONObject();
//            	try {
//					playlist.put("songs", array);
//					playlist.put("title", pl.getTitle());
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//            	
//            	for (int i=0; i<friendsPicked.size(); i++) {
//            		
//            	}
//            	
//            	db.closeDB();
//
//            }
//        });
//    }
//
//    private void onError(Exception error) {
//
//    }
//
//}
