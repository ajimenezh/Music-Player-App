package com.musicapp.urmusic;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.musicapp.urmusic.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

public class CommentsFragment extends ListFragment {
	
	OnHeadlineSelectedListener mCallback;
	
	private Button btnAddNewComment;
    private TextView txtMessage;
    // array list for spinner adapter
    ProgressDialog pDialog;
    private ArrayList<HashMap<String,String>> arrayList = new ArrayList<HashMap<String,String>>();
 
    // API urls
    // Url to create new category
    private String URL_NEW_COMMENT = "http://54.72.14.121/app/new_comment.php";
    private String URL_GET_COMMENTS = "http://54.72.14.121/app/get_comments.php";
    
    private ImageButton btnCurSongPlaying;
	private TwoLineListItem btnCurSongPlayingText;
		
	public CommentsFragment(){
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.comments_fragment, container, false);

        GetComments();
        
        //Log.w("hola", ""+arrayList.size());
        
        ListAdapter adapter = new SimpleAdapter(getActivity(), arrayList,
				R.layout.playlist_item, new String[] { "message" }, new int[] {
						R.id.songTitle });

		setListAdapter(adapter);
        
        return rootView;
    }
	
	public void onActivityCreated (Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		btnAddNewComment = (Button) getView().findViewById(R.id.btnAddNewComment);
		txtMessage = (TextView) getView().findViewById(R.id.txtMessage);
		
		MainActivity.fromComments = true;
		
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
		
        btnAddNewComment.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
                if (txtMessage.getText().toString().trim().length() > 0) {
                     
                    // new category name
                    String newComment = txtMessage.getText().toString();
 
                    // Call Async task to create new category
                    new AddNewComment().execute(newComment);
                    
                    InputMethodManager inputManager = (InputMethodManager)
                            MainActivity.mContext.getSystemService(Context.INPUT_METHOD_SERVICE); 

                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                               InputMethodManager.HIDE_NOT_ALWAYS);
                    
                    txtMessage.setText("");
                } else {
                    //Message toast
                }
            }
        });  
        
        mCallback.onArticleSelected(9, "");
		
//		/**
//		 * Button Click event for Play list click event
//		 * Launches list activity which displays list of songs
//		 * */
//		btnPlaylist.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				mCallback.onArticleSelected(5, "");
//			}
//		});
	}
	
	/**
     * Async task to get all food categories
     * */
    private void GetComments() {
            ServiceHandler jsonParser = new ServiceHandler();
            
            String text = MainActivity.songPlayingArtist + "/" + MainActivity.songPlayingAlbum + "/" + MainActivity.songPlayingTitle;
            
            MessageDigest m;
            String hashtext = "";
			try {
				m = MessageDigest.getInstance("MD5");
				m.reset();
	            m.update(text.getBytes());
	            byte[] digest = m.digest();
	            BigInteger bigInt = new BigInteger(1,digest);
	            hashtext = bigInt.toString(10);
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
            String id = hashtext;
            
            Log.w("hola",""+hashtext);
 
            // Preparing post params
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("hash", id));
 
            ServiceHandler serviceClient = new ServiceHandler();
 
            String json = serviceClient.makeServiceCall(URL_GET_COMMENTS,
                    ServiceHandler.GET, 2, params);
 
            Log.e("Response: ", "> " + json);
 
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray comments = jsonObj
                                .getJSONArray("comments");                        
 
                        arrayList.clear();
                        for (int i = 0; i < comments.length(); i++) {
                            JSONObject catObj = (JSONObject) comments.get(i);
                            String tmp = catObj.getString("message");
                            HashMap<String, String> comment = new HashMap<String, String>();
                            comment.put("message", tmp);
                			arrayList.add(comment);
                        }
                        
                        ListAdapter adapter = new SimpleAdapter(getActivity(), arrayList,
                				R.layout.playlist_item, new String[] { "message" }, new int[] {
                						R.id.songTitle });

                		setListAdapter(adapter);
                    }
 
                } catch (JSONException e) {
                    e.printStackTrace();
                }
 
            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
 
    }
	
	/**
     * Async task to create a new food category
     * */
    private class AddNewComment extends AsyncTask<String, Void, Void> {
 
        boolean isNewCommentCreated = false;
 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pDialog = new ProgressDialog(MainActivity.this);
            //pDialog.setMessage("Creating new category..");
            //pDialog.setCancelable(false);
            //pDialog.show();
 
        }
 
        @Override
        protected Void doInBackground(String... arg) {
 
            String newComment = arg[0];

            String text = MainActivity.songPlayingArtist + "/" + MainActivity.songPlayingAlbum + "/" + MainActivity.songPlayingTitle;
            
            MessageDigest m;
            String hashtext = "";
			try {
				m = MessageDigest.getInstance("MD5");
				m.reset();
	            m.update(text.getBytes());
	            byte[] digest = m.digest();
	            BigInteger bigInt = new BigInteger(1,digest);
	            hashtext = bigInt.toString(10);
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
            String id = hashtext;
            
            Log.w("hola",""+hashtext);
 
            // Preparing post params
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("message", newComment));
            params.add(new BasicNameValuePair("hash", id));
 
            ServiceHandler serviceClient = new ServiceHandler();
 
            String json = serviceClient.makeServiceCall(URL_NEW_COMMENT,
                    ServiceHandler.POST, 1, params);
             
            Log.w("Create Response: ", "> " + json);
 
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    boolean error = jsonObj.getBoolean("error");
                    // checking for error node in json
                    if (!error) {   
                        // new category created successfully
                        isNewCommentCreated = true;
                    } else {
                        Log.e("Create Category Error: ", "> " + jsonObj.getString("message"));
                    }
 
                } catch (JSONException e) {
                    e.printStackTrace();
                }
 
            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
 
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //if (pDialog.isShowing())
            //    pDialog.dismiss();
            if (isNewCommentCreated) {
            	getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // fetching all categories
                        GetComments();
                    }
                });
            }
        }
 
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
}