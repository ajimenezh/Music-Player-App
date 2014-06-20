package com.musicapp.urmusic;
//package com.android.musicplayer;
//
//import java.math.BigInteger;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
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
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.SimpleAdapter;
//import android.widget.TextView;
//import android.widget.TwoLineListItem;
//
//public class GroupPlaylistFragment extends ListFragment {
//		
//	OnHeadlineSelectedListener mCallback;
//	
//	Button btnCreateGroup;
//    private ArrayList<HashMap<String,String>> arrayList = new ArrayList<HashMap<String,String>>();
//    
//    private String URL_NEW_PLAYLIST = "http://54.72.14.121/app/new_playlist.php";
//    private String URL_GET_PLAYLISTS = "http://54.72.14.121/app/get_playlists.php";
//	
//	public GroupPlaylistFragment(){}
//	
//	@Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState) {
// 
//		View rootView;
//		
//			
//		if (MainActivity.songPlayingTitle!=null) {
//			rootView = inflater.inflate(R.layout.groups_playing, container, false);
//		} else {
//			rootView = inflater.inflate(R.layout.groups, container, false);
//		}
//		
//		getPlaylists();
//		
//		ListAdapter adapter = new SimpleAdapter(getActivity(), arrayList,
//				R.layout.playlist_item, new String[] { "id" }, new int[] {
//						R.id.songTitle });
//
//		setListAdapter(adapter);
//         
//        return rootView;
//    }
//	
//	public void onActivityCreated (Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//		
//		if (MainActivity.userID!=null) {
//			btnCreateGroup = (Button) getView().findViewById(R.id.btn_create_group);
//			
//			btnCreateGroup.setOnClickListener(new View.OnClickListener() {
//				 
//	            @Override
//	            public void onClick(View v) {
//	                new AddNewPlaylist().execute("");
//	            }
//	        });  
//		}
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
//    /**
//     * Async task to get all food categories
//     * */
//    private void getPlaylists() {
//            ServiceHandler jsonParser = new ServiceHandler();
// 
//            // Preparing post params
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("groupId", MainActivity.group));
// 
//            ServiceHandler serviceClient = new ServiceHandler();
// 
//            String json = serviceClient.makeServiceCall(URL_GET_PLAYLISTS,
//                    ServiceHandler.GET, 2, params);
// 
//            Log.e("Response: ", "> " + json);
// 
//            if (json != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(json);
//                    if (jsonObj != null) {
//                        JSONArray groups = jsonObj
//                                .getJSONArray("playlists");                        
// 
//                        arrayList.clear();
//                        for (int i = 0; i < groups.length(); i++) {
//                            JSONObject catObj = (JSONObject) groups.get(i);
//                            String id = catObj.getString("id");
//                            HashMap<String, String> group = new HashMap<String, String>();
//                			arrayList.add(group);
//                        }
//                        
//                        ListAdapter adapter = new SimpleAdapter(getActivity(), arrayList,
//                				R.layout.playlist_item, new String[] { "id" }, new int[] {
//                						R.id.songTitle });
//
//                		setListAdapter(adapter);
//                    }
// 
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
// 
//            } else {
//                Log.e("JSON Data", "Didn't receive any data from server!");
//            }
// 
//    }
//    
//	/**
//     * Async task to create a new food category
//     * */
//    private class AddNewPlaylist extends AsyncTask<String, Void, Void> {
// 
//        boolean isNewCommentCreated = false;
// 
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            //pDialog = new ProgressDialog(MainActivity.this);
//            //pDialog.setMessage("Creating new category..");
//            //pDialog.setCancelable(false);
//            //pDialog.show();
// 
//        }
// 
//        @Override
//        protected Void doInBackground(String... arg) {
// 
//            // Preparing post params
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("groupId", MainActivity.group));
// 
//            ServiceHandler serviceClient = new ServiceHandler();
// 
//            String json = serviceClient.makeServiceCall(URL_NEW_PLAYLIST,
//                    ServiceHandler.POST, 1, params);
//             
//            Log.w("Create Response: ", "> " + json);
// 
//            if (json != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(json);
//                    boolean error = jsonObj.getBoolean("error");
//                    // checking for error node in json
//                    if (!error) {   
//                        // new category created successfully
//                        isNewCommentCreated = true;
//                    } else {
//                        Log.e("Create Category Error: ", "> " + jsonObj.getString("message"));
//                    }
// 
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
// 
//            } else {
//                Log.e("JSON Data", "Didn't receive any data from server!");
//            }
// 
//            return null;
//        }
// 
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            //if (pDialog.isShowing())
//            //    pDialog.dismiss();
//            if (isNewCommentCreated) {
//            	getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // fetching all categories
//                        //GetComments();
//                    }
//                });
//            }
//        }
// 
//    }
//	
//	@Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//		
//    }
//}
