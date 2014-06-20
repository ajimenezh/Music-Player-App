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
//public class GroupsFragment extends ListFragment {
//		
//	OnHeadlineSelectedListener mCallback;
//	
//	Button btnCreateGroup;
//    private ArrayList<HashMap<String,String>> arrayList = new ArrayList<HashMap<String,String>>();
//    
//    private String URL_NEW_GROUP = "http://54.72.14.121/app/new_group.php";
//    private String URL_GET_GROUPS = "http://54.72.14.121/app/get_groups.php";
//	
//	public GroupsFragment(){}
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
//		// start Facebook Login
//		  Session.openActiveSession(getActivity(), true, new Session.StatusCallback() {
//		
//		    // callback when session changes state
//			@Override
//			public void call(Session session, SessionState state, Exception exception) {
//				if (session.isOpened()) {
//					// make request to the /me API
//					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
//			
//					  // callback after Graph API response with user object
//			    		  @Override
//			    		  public void onCompleted(GraphUser user, Response response) {
//			    			  if (user != null) {
//			    				  MainActivity.userID = user.getId();
//			    				  
//			    				}
//			    			  else {
//			    				  mCallback.onArticleSelected(25,"");
//			    			  }
//			    		  }
//			    		});
//			    	}
//			    }
//		  });
//		  
//		  if (MainActivity.userID!=null) {
//			  GetGroups();
//		  }
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
//	                new AddNewGroup().execute("");
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
//    private void GetGroups() {
//            ServiceHandler jsonParser = new ServiceHandler();
//
// 
//            // Preparing post params
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("person_id", MainActivity.userID));
// 
//            ServiceHandler serviceClient = new ServiceHandler();
// 
//            String json = serviceClient.makeServiceCall(URL_GET_GROUPS,
//                    ServiceHandler.GET, 2, params);
// 
//            Log.e("Response: ", "> " + json);
// 
//            if (json != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(json);
//                    if (jsonObj != null) {
//                        JSONArray groups = jsonObj
//                                .getJSONArray("groups");                        
// 
//                        arrayList.clear();
//                        for (int i = 0; i < groups.length(); i++) {
//                            JSONObject catObj = (JSONObject) groups.get(i);
//                            String id1 = catObj.getString("id1");
//                            String id2 = catObj.getString("id2");
//                            HashMap<String, String> group = new HashMap<String, String>();
//                            if (id2.equals("-1")) group.put("id", id1);
//                            else group.put("id", id2);
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
//    private class AddNewGroup extends AsyncTask<String, Void, Void> {
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
//            params.add(new BasicNameValuePair("groupId", "-1"));
//            params.add(new BasicNameValuePair("personId", MainActivity.userID));
// 
//            ServiceHandler serviceClient = new ServiceHandler();
// 
//            String json = serviceClient.makeServiceCall(URL_NEW_GROUP,
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
//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        
//		MainActivity.group = arrayList.get(position).get("id");
//		
//		mCallback.onArticleSelected(40,"");
//    }
//}
