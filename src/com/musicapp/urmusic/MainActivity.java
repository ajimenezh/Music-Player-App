package com.musicapp.urmusic;


import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.musicapp.urmusic.R;
import com.musicapp.urmusic.Utilities;
import com.musicapp.urmusic.adapter.NavDrawerListAdapter;
import com.musicapp.urmusic.model.NavDrawerItem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Audio;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity 
	implements 	AllArtistsListFragment.OnHeadlineSelectedListener, 
				AlbumsListFragment.OnHeadlineSelectedListener, 
				SongsListFragment.OnHeadlineSelectedListener,
				PlayerFragment.OnHeadlineSelectedListener,
				AllSongsListFragment.OnHeadlineSelectedListener,
				AllAlbumsListFragment.OnHeadlineSelectedListener,
				CommentsFragment.OnHeadlineSelectedListener,
//				GroupsFragment.OnHeadlineSelectedListener,
//				SessionLoginFragment.OnHeadlineSelectedListener,
//				GroupPlaylistFragment.OnHeadlineSelectedListener,
//				PlaylistsFragment.OnHeadlineSelectedListener,
				ArtistFragment.OnHeadlineSelectedListener,
				ArtistTopicsFragment.OnHeadlineSelectedListener,
				TopicFragment.OnHeadlineSelectedListener,
//				AddPlaylistFragment.OnHeadlineSelectedListener,
//				PlaylistFragment.OnHeadlineSelectedListener,
				IOCallback,
				OnCompletionListener
				{
	private static DrawerLayout mDrawerLayout;
	private static ListView mDrawerList;
	private static ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private static CharSequence mDrawerTitle;

	// used to store app title
	private static CharSequence mTitle;

	// slide menu items
	private static String[] navMenuTitles;
	private static TypedArray navMenuIcons;

	private static ArrayList<NavDrawerItem> navDrawerItems;
	private static NavDrawerListAdapter adapter;
	
	public static SeekBar songProgressBar;
	private TextView songTitleLabel;
	private TextView songCurrentDurationLabel;
	private TextView songTotalDurationLabel;
	// Media Player
	public static MediaPlayer mp = new MediaPlayer();
	// Handler to update UI timer, progress bar etc,.
	public static AudioFocusHelper am;
	private Handler mHandler = new Handler();;
	private Utilities utils;
	private int seekForwardTime = 5000; // 5000 milliseconds
	private int seekBackwardTime = 5000; // 5000 milliseconds
	public static int currentSongIndex = 0; 
	public static boolean isShuffle = false;
	public static boolean isRepeat = false;
	
	public static String songPlayingTitle = null;
	public static String songPlayingAlbum = null;
	public static String songPlayingArtist = null;
	public static boolean isPlaying = false;
	public static Integer currentSongHash;
	public static boolean fromComments = false;
	
	public static boolean isOpen = false;
	
	public static Context mContext;
	
	public static ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	
	public static ArrayList<ArrayList<Integer>> Next = new ArrayList<ArrayList<Integer>>();
	public static HashMap<String,Integer> Hash = new HashMap<String,Integer>();
	public static ArrayList<Integer> AllArtists = new ArrayList<Integer>();
	public static ArrayList<Integer> AllAlbums = new ArrayList<Integer>();
	public static ArrayList<Integer> AllSongs = new ArrayList<Integer>();
	public static ArrayList<Integer> Parent = new ArrayList<Integer>();
	public static ArrayList<String> Name = new ArrayList<String>();
	public static SparseArray<String> SongPath = new SparseArray<String>();
	public static SparseIntArray Track = new SparseIntArray();
	public static boolean isInAllSongs = false;
	public static ArrayList<HashMap<String,String>> SongListArray;
	public static ArrayList<String> SongListArraySorted;
	public static HashMap<String,Integer> SongListMap = new HashMap<String,Integer>();
	public static ArrayList<Integer> SongHashListArray = new ArrayList<Integer>();
	public static int allSongsIndex;
	public static LinkedList<String> stack = new LinkedList<String>();
	
	public static Bitmap bmp = null;
	
	public static String userID;
	public static String group;
	
	public static final String PREFS_NAME = "MyPrefsFile";
	
	 private String URL_SERVER = "http://192.168.1.34:5000/";
	 
	 private SocketIO socket;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		isOpen = true;
		  
		mContext = getBaseContext();
		
		{
			utils = new Utilities();
			
			Cursor mCursor = null;
			
			Integer cnt = 0;
			
			Log.w("hello", "hola");
			
			try {
		         mCursor = getContentResolver().query(
		             Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, "_id");
		         
		         mCursor.moveToFirst();

		         if (mCursor.getCount() != 0) {

		        	 do {


		                 //long date = mCursor.getLong(mCursor
		                 //    .getColumnIndexOrThrow(Audio.Media.DATE_ADDED));

		                 //String Duration = mCursor.getString(mCursor
		                 //    .getColumnIndexOrThrow(Audio.Media.DURATION));
		                String DATA = mCursor.getString(mCursor.getColumnIndexOrThrow(Audio.Media.DATA));
		                 
		                 //double TIME_STAMP = mCursor.getInt(mCursor
		                 //    .getColumnIndexOrThrow(Audio.Media.DATE_ADDED));
		                if(DATA.endsWith(".mp3") || DATA.endsWith(".MP3")) {
		                	String ALBUM = mCursor.getString(mCursor.getColumnIndexOrThrow(Audio.Media.ALBUM));
		                	String ARTIST = mCursor.getString(mCursor.getColumnIndexOrThrow(Audio.Media.ARTIST));
		                	//String DURATION = mCursor.getString(mCursor.getColumnIndexOrThrow(Audio.Media.DURATION));
			                //String DISPLAY_NAME = mCursor.getString(mCursor.getColumnIndexOrThrow(Audio.Media.DISPLAY_NAME));
			                String TRACK = mCursor.getString(mCursor.getColumnIndexOrThrow(Audio.Media.TRACK)); 
			                String TITLE = mCursor.getString(mCursor.getColumnIndexOrThrow(Audio.Media.TITLE));				
							
			                ALBUM = utils.ParseName(ALBUM);
			                ARTIST = utils.ParseName(ARTIST);
			                TITLE = utils.ParseTitle(TITLE);
			                
							if (Hash.get(ARTIST)==null) {
								Hash.put(ARTIST, cnt);
								Name.add(ARTIST);
								AllArtists.add(cnt++);
								Parent.add(-1);
								ArrayList<Integer> v = new ArrayList<Integer>();
								Next.add(v);
							}
							
							if (Hash.get(ARTIST+"/"+ALBUM)==null) {
								AllAlbums.add(cnt);
								Hash.put(ARTIST+"/"+ALBUM, cnt++);
								Name.add(ALBUM);
								ArrayList<Integer> v = new ArrayList<Integer>();
								Next.add(v);
								Parent.add(Hash.get(ARTIST));
								Next.get(Hash.get(ARTIST)).add(Hash.get(ARTIST+"/"+ALBUM));
							}
							
							if (Hash.get(ALBUM+"/"+TITLE)==null) {
								AllSongs.add(cnt);
								Hash.put(ALBUM+"/"+TITLE, cnt++);
								Name.add(TITLE);
								ArrayList<Integer> v = new ArrayList<Integer>();
								Next.add(v);
								Parent.add(Hash.get(ARTIST+"/"+ALBUM));
								SongPath.put(cnt-1, DATA);
								Track.put(cnt-1, Integer.parseInt(TRACK));
								Next.get(Hash.get(ARTIST+"/"+ALBUM)).add(Hash.get(ALBUM+"/"+TITLE));
							}
							
		                }
		                 //Log.w("song","Name:" + DATA);
		                 //System.out.println("data " + DATA);
		                 //System.out.println("time " + TIME_STAMP);
		                 //System.out.println("time " + Duration);
		             } while (mCursor.moveToNext());
		        	 
		        	 new MyAsyncTask().execute();

		         }
		     } catch (Exception e) {
		    	 Log.w("Error", "fallo");
		         e.printStackTrace();
		     } finally {
		         if (mCursor != null) {
		             mCursor.close();
		             mCursor = null;
		         }
		     }
		}
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		userID = settings.getString("userID", null);

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Communities, Will add a counter here
		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		// Pages
		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter); 

		// enabling action bar app icon and behaving it as toggle button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		int imageResource = R.drawable.ic_hardware_headphones;
		Drawable image = getResources().getDrawable(imageResource);
		getSupportActionBar().setIcon(image);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				supportInvalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
			stack.add("Artists");
		}
		
		mp.setOnCompletionListener(this);
		
		if (android.os.Build.VERSION.SDK_INT >= 8) {
		    am = new AudioFocusHelper(getApplicationContext());
		} else {
		    am = null;
		}
		
	}
	
	
	private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
		  @Override
		  protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			  	SongListArray = new ArrayList<HashMap<String,String>>();
				SongListArraySorted = new ArrayList<String>();
				
				for(Integer index : MainActivity.AllSongs) {
					SongListArraySorted.add(MainActivity.Name.get(index)+"/*/"+MainActivity.Name.get(MainActivity.Parent.get(index)));
				}
				Collections.sort(SongListArraySorted);
		        return null;
		  }
		 
		  protected void onPostExecute(Double result){
			//
		  } 
		  
		  protected void onProgressUpdate(Integer... progress){
		    //
		  }
		}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	} 

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new AllArtistsListFragment();
			break;
		case 1:
			fragment = new AllAlbumsListFragment();
			break;
		case 2:
			fragment = new AllSongsListFragment();
			break;
		case 3:
			//fragment = new PlaylistsFragment();
			break;
		case 4:
			fragment = new PagesFragment();
			break;
		case 5:
			fragment = new WhatsHotFragment();
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment)
					.addToBackStack(null)
					.commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			
			if (MainActivity.songPlayingTitle!=null) {
				String text;
				TextView tv1 = (TextView)findViewById(R.id.player_title);
				text = songPlayingTitle;
				if (text.length()>30) {
					text = text.substring(0, 30) + "...";
				}
				if (tv1!=null)
					tv1.setText(text);
				TextView tv2 = (TextView)findViewById(R.id.player_artist);
				text = MainActivity.songPlayingArtist+" - "+MainActivity.songPlayingAlbum;
				if (text.length()>30) {
					text = text.substring(0, 30) + "...";
				}
				if (tv2!=null)
					tv2.setText(text);
			}
			
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onArticleSelected(int TYPE, String str) {
		// TODO Auto-generated method stub
		Fragment fragment = null;
		Bundle args = new Bundle();
		CharSequence title = "";
		switch (TYPE) {
		case 1:
			//fragment = new ArtistFragment();
			fragment = new AlbumsListFragment();
			args.putString("Artist", str);
			title = str;
			fragment.setArguments(args);
			stack.add(str);
			break;
		case 2:
			fragment = new SongsListFragment();
			args.putString("AlbumHash", str);
			title = Name.get(Hash.get(str));
			fragment.setArguments(args);
			stack.add(Name.get(Hash.get(str)));
			break;
		case 3:
			fragment = new PlayerFragment();
			args.putString("SongHash", str);
			title = Name.get(Hash.get(str));
			fragment.setArguments(args);
			stack.add(Name.get(Hash.get(str)));
			break;
		case 4:
			fragment = new PlayerFragment();
			args.putString("SongHash", "%%");
			title = Name.get(currentSongHash);
			fragment.setArguments(args);
			stack.add(Name.get(currentSongHash));
			break;
		case 5:
			onBackPressed();
			break;
		case 6:
			fragment = new PlayerFragment();
			args.putString("SongHash", str);
			title = Name.get(Hash.get(str));
			fragment.setArguments(args);
			stack.add(Name.get(Hash.get(str)));
			break;
		case 8:
			setTitle(str);
			stack.add(str);
			return;
		case 9:
			if (MainActivity.songPlayingTitle!=null) {
				String text;
				TextView tv1 = (TextView)findViewById(R.id.player_title);
				text = songPlayingTitle;
				if (text.length()>30) {
					text = text.substring(0, 30) + "...";
				}
				if (tv1!=null) tv1.setText(text);
				TextView tv2 = (TextView)findViewById(R.id.player_artist);
				text = MainActivity.songPlayingArtist+" - "+MainActivity.songPlayingAlbum;
				if (text.length()>30) {
					text = text.substring(0, 30) + "...";
				}
				if (tv2!=null) tv2.setText(text);
			}
			return;
		case 20:
			fragment = new CommentsFragment();
			break;
//		case 25:
//			fragment = new SessionLoginFragment();
//			args.putString("playlistId", str);
//			fragment.setArguments(args);
//			break;
//		case 26:
//			fragment = new FriendPickerCustomFragment();
//			args.putString("playlistId", str);
//			fragment.setArguments(args);
//			break;
//		case 30:
//			fragment = new GroupsFragment();
//			break;
//		case 40:
//			fragment = new GroupPlaylistFragment();
//			break;
		case 50:
			fragment = new TopicFragment();
			args.putString("title", str);
			fragment.setArguments(args);
			break;
//		case 60:
//			fragment = new AddPlaylistFragment();
//			args.putString("song", str);
//			fragment.setArguments(args);
//			break;
//		case 65:
//			fragment = new PlaylistFragment();
//			args.putString("playlistId", str);
//			fragment.setArguments(args);
//			break;
		case 100:
			onBackPressed();

		default:
			break;
		}
		
		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
			.replace(R.id.frame_container, fragment)
			.addToBackStack(null)
			.commit();

			if (MainActivity.isPlaying && TYPE!=20 && TYPE!=60) {
				String text;
				TextView tv1 = (TextView)findViewById(R.id.player_title);
				text = songPlayingTitle;
				if (text.length()>30) {
					text = text.substring(0, 30) + "...";
				}
				if (tv1!=null) tv1.setText(text);
				TextView tv2 = (TextView)findViewById(R.id.player_artist);
				text = MainActivity.songPlayingArtist+" - "+MainActivity.songPlayingAlbum;
				if (text.length()>30) {
					text = text.substring(0, 30) + "...";
				}
				if (tv2!=null) tv2.setText(text);
			}

			// update selected item and title, then close the drawer
			setTitle(title);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void onBackPressed() {

		if (getSupportFragmentManager().getBackStackEntryCount()==1) {
			Intent showOptions = new Intent(Intent.ACTION_MAIN);
			showOptions.addCategory(Intent.CATEGORY_HOME);
			startActivity(showOptions);
		}
		else {
			super.onBackPressed();
			
			if (MainActivity.songPlayingTitle!=null) {
				String text;
				TextView tv1 = (TextView)findViewById(R.id.player_title);
				text = songPlayingTitle;
				if (text.length()>30) {
					text = text.substring(0, 30) + "...";
				}
				if (tv1!=null) tv1.setText(text);
				TextView tv2 = (TextView)findViewById(R.id.player_artist);
				text = MainActivity.songPlayingArtist+" - "+MainActivity.songPlayingAlbum;
				if (text.length()>30) {
					text = text.substring(0, 30) + "...";
				}
				if (tv2!=null) tv2.setText(text);
			}
			
			if (stack.size()>0) {
				Log.w("hola", stack.size()+"");
				stack.poll();
				setTitle(stack.poll());
			}
			else setTitle("Artists");
		}
		
	}
	
	/**
	 * Function to play a song
	 * @param songIndex - index of song
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 * */
	public void  playSong(int songHash) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
		// Play song
		MainActivity.isPlaying = true;
		{
			MainActivity.am.requestFocus();
			MainActivity.mp.reset();
			MainActivity.mp.setDataSource(MainActivity.SongPath.get(songHash));
			MainActivity.mp.prepare();
			MainActivity.mp.start();
			MainActivity.isPlaying = true;
			// Displaying Song title
			String songTitle = MainActivity.Name.get(songHash);

			MainActivity.currentSongHash = songHash;
			Integer AlbumHash = MainActivity.Parent.get(songHash);
			Integer ArtistHash = MainActivity.Parent.get(AlbumHash);
			
			//Cambiar Titulo
        	//songTitleLabel.setText(songTitle);
        	
        	//Log.w("hola", songTitle);
        	
        	MainActivity.songPlayingTitle = songTitle;
        	MainActivity.songPlayingAlbum = MainActivity.Name.get(AlbumHash);
        	MainActivity.songPlayingArtist = MainActivity.Name.get(ArtistHash);
        	
        	if (MainActivity.songPlayingTitle!=null) {
				String text;
				TextView tv1 = (TextView)findViewById(R.id.player_title);
				text = songPlayingTitle;
				if (text.length()>30) {
					text = text.substring(0, 30) + "...";
				}
				if (tv1!=null) tv1.setText(text);
				TextView tv2 = (TextView)findViewById(R.id.player_artist);
				text = MainActivity.songPlayingArtist+" - "+MainActivity.songPlayingAlbum;
				if (text.length()>30) {
					text = text.substring(0, 30) + "...";
				}
				if (tv2!=null) tv2.setText(text);
			}
		} 
        	
	}
	
	public void playNextSong() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
		if (MainActivity.isShuffle) {
			Random rand = new Random();
			currentSongIndex = rand.nextInt(SongHashListArray.size()-1);
			playSong(SongHashListArray.get(currentSongIndex));
		}
		else {
			if(currentSongIndex < SongHashListArray.size()-1){
				playSong(SongHashListArray.get(currentSongIndex + 1));
				currentSongIndex = currentSongIndex + 1;
			}else{
				// play first song
				playSong(SongHashListArray.get(0));
				currentSongIndex = 0;
			}
		}
	}
	
	/**
	 * On Song Playing completed
	 * if repeat is ON play same song again
	 * if shuffle is ON play random song
	 * */
	@Override
	public void onCompletion(MediaPlayer arg0) {
		
		// check for repeat is ON or OFF
		if(MainActivity.isRepeat){
			// repeat is on play same song again
			try {
				playSong(MainActivity.currentSongHash);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				playNextSong();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (!isOpen) {
			Intent i= new Intent(this, NotificationService.class);
	       // potentially add data to the intent
	       i.putExtra("key", "blablabla");
	       this.startService(i);
		}
	}
	
	@Override
    protected void onStop(){
       super.onStop();
       
       isOpen = false;
       
       // use this to start and trigger a service
       Intent i= new Intent(this, NotificationService.class);
       // potentially add data to the intent
       i.putExtra("key", "blablabla");
       this.startService(i); 
       

      // We need an Editor object to make preference changes.
      // All objects are from android.context.Context
      SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
      SharedPreferences.Editor editor = settings.edit();
      editor.putString("userID", userID);

      // Commit the edits!
      editor.commit();
    }

	@Override
	public void on(String arg0, IOAcknowledge arg1, Object... arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(SocketIOException arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(String arg0, IOAcknowledge arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(JSONObject arg0, IOAcknowledge arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void sendMessage(String event, JSONObject JSONObj) {
		try {
			 socket = new SocketIO();
			 socket.connect("http://192.168.1.34:5000/", this);
			 
			socket.emit(event, JSONObj);
			
			socket.disconnect();

	     } catch (Exception e) {
	    	 	e.printStackTrace();
	     }
	}

}
