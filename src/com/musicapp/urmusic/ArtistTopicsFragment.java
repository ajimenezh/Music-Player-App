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
import com.musicapp.urmusic.AllAlbumsListFragment.OnHeadlineSelectedListener;

import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

public class ArtistTopicsFragment extends ListFragment {
    // array list for spinner adapter
    ProgressDialog pDialog;
    private ArrayList<HashMap<String,String>> arrayList = new ArrayList<HashMap<String,String>>();
    
    private String URL_NEW_TOPIC = "http://54.72.14.121/app/new_topic.php";
    private String URL_GET_TOPICS = "http://54.72.14.121/app/get_topics.php";
    
    String Artist;

    private ListAdapter adapter;
    
    OnHeadlineSelectedListener mCallback;
		
	public ArtistTopicsFragment(){
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.topics_fragment, container, false);
        
        Artist = getArguments().getString("Artist");
        
        GetTopics();
        
        //Log.w("hola", ""+arrayList.size());
        
        adapter = new SimpleAdapter(getActivity(), arrayList,
				R.layout.playlist_item, new String[] { "title" }, new int[] {
						R.id.songTitle });

		setListAdapter(adapter);
        
        return rootView;
    }
	
	public void onActivityCreated (Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		setListAdapter(null);
        View header = getActivity().getLayoutInflater().inflate(R.layout.topics_list_header, null);
	    ListView listView = getListView();
	    listView.addHeaderView(header);
	    setListAdapter(adapter);
		
		LinearLayout newButton = (LinearLayout) getView().findViewById(R.id.new_button);
		
		LinearLayout fr2 = (LinearLayout) getView().findViewById(R.id.topics_fragment);
        
        fr2.setBackgroundColor(0xFF000000);
		
		newButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
				
				View promptView = layoutInflater.inflate(R.layout.prompts, null);
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

				alertDialogBuilder.setView(promptView);

				final EditText input = (EditText) promptView.findViewById(R.id.userInput);

				// setup a dialog window
				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										// get user input and set it to result
										
										Editable value = input.getText();
										
										String newTopic = value.toString();
										 
					                    // Call Async task to create new category
					                    new AddNewTopic().execute(newTopic);
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,	int id) {
										
										//
										
										dialog.cancel();
									}
								});

				// create an alert dialog
				AlertDialog alertD = alertDialogBuilder.create();

				alertD.show();
				
				
			}
		});

	}
	
	/**
     * Async task to get all food categories
     * */
    private void GetTopics() {
            ServiceHandler jsonParser = new ServiceHandler();
            
            String text = Artist + "/" + "__topics__";
            
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
 
            String json = serviceClient.makeServiceCall(URL_GET_TOPICS,
                    ServiceHandler.GET, 2, params);
 
            Log.e("Response: ", "> " + json);
 
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray topics = jsonObj
                                .getJSONArray("topics");                        
 
                        arrayList.clear();
                        for (int i = 0; i < topics.length(); i++) {
                            JSONObject catObj = (JSONObject) topics.get(i);
                            String tmp = catObj.getString("title");
                            HashMap<String, String> comment = new HashMap<String, String>();
                            comment.put("title", tmp);
                			arrayList.add(comment);
                        }
                        
                        ListAdapter adapter = new SimpleAdapter(getActivity(), arrayList,
                				R.layout.playlist_item, new String[] { "title" }, new int[] {
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
    private class AddNewTopic extends AsyncTask<String, Void, Void> {
 
        boolean isNewTopicCreated = false;
 
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
 
            String newTopic = arg[0];

            String text = Artist + "/" + "__topics__";
            
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
            Log.w("hola",""+newTopic);
 
            // Preparing post params
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("title", newTopic));
            params.add(new BasicNameValuePair("hash", id));
 
            ServiceHandler serviceClient = new ServiceHandler();
 
            String json = serviceClient.makeServiceCall(URL_NEW_TOPIC,
                    ServiceHandler.POST, 1, params);
             
            Log.w("Create Response: ", "> " + json);
 
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    boolean error = jsonObj.getBoolean("error");
                    // checking for error node in json
                    if (!error) {   
                        // new category created successfully
                        isNewTopicCreated = true;
                    } else {
                        Log.w("Create Category Error: ", "> " + jsonObj.getString("message"));
                    }
 
                } catch (JSONException e) {
                    e.printStackTrace();
                }
 
            } else {
                Log.w("JSON Data", "Didn't receive any data from server!");
            }
 
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //if (pDialog.isShowing())
            //    pDialog.dismiss();
            if (isNewTopicCreated) {
            	getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // fetching all categories
                        //
                    	GetTopics();
                    }
                });
            }
        }
 
    }
    
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
		String Title = arrayList.get(position-1).get("title");
		Title = Artist + "/" + "__topics__" + "/" + Title;
		mCallback.onArticleSelected(50, Title);
		
    }
	
	@Override
	public void onStop() {
		LinearLayout fr2 = (LinearLayout) getView().findViewById(R.id.topics_fragment);
        
        fr2.setBackgroundColor(0x00000000);
        
        super.onStop();
	}

}