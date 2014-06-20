package com.musicapp.urmusic;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	// Logcat tag
    private static final String LOG = "DatabaseHelper";
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "test_db";
 
    // Table Names
    private static final String TABLE_PLAYLISTS = "playlists";
    private static final String TABLE_SONGS = "songs";
    private static final String TABLE_PLAYLIST_SONG = "playlist_song";
 
    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";
 
    // PLAYLIST Table - column names
    private static final String KEY_PLAYLIST = "playlist";
    private static final String KEY_STATUS = "status";
 
    // SONGS Table - column names
    private static final String KEY_SONG_TITLE = "song_title";
    private static final String KEY_SONG_PLAYLIST = "song_playlist";
    private static final String KEY_SONG_ALBUM = "song_album";
    private static final String KEY_SONG_ARTIST = "song_artist";
 
    // NOTE_TAGS Table - column names
    private static final String KEY_PLAYLIST_ID = "playlist_id";
    private static final String KEY_SONG_ID = "song_id";
 
    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_PLAYLISTS = "CREATE TABLE "
            + TABLE_PLAYLISTS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PLAYLIST
            + " TEXT" + ")";
 
    // Tag table create statement
    private static final String CREATE_TABLE_SONGS = "CREATE TABLE " + TABLE_SONGS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SONG_TITLE + " TEXT," + KEY_SONG_ALBUM + " TEXT," + KEY_SONG_ARTIST + " TEXT,"
            + KEY_SONG_PLAYLIST + " INTEGER" + ")";
 
    // todo_tag table create statement
    private static final String CREATE_TABLE_PLAYLIST_SONG = "CREATE TABLE "
            + TABLE_PLAYLIST_SONG + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PLAYLIST_ID + " INTEGER," + KEY_SONG_ID + " INTEGER"
            + ")";
 
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
 
        // creating required tables
        db.execSQL(CREATE_TABLE_PLAYLISTS);
        db.execSQL(CREATE_TABLE_SONGS);
        db.execSQL(CREATE_TABLE_PLAYLIST_SONG);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	
    	// on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST_SONG);
 
        // create new tables
        onCreate(db);
    }
    
    /*
     * Creating a playlist
     */
    public long createPlaylist(Playlist playlist) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_PLAYLIST, playlist.getTitle());
     
        // insert row
        long playlist_id = db.insert(TABLE_PLAYLISTS, null, values);
     
        return playlist_id;
    }
    
    /*
     * get single playlist
     */
    public Playlist getPlaylist(long playlist_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selectQuery = "SELECT  * FROM " + TABLE_PLAYLISTS + " WHERE "
                + KEY_ID + " = " + playlist_id;
     
        Log.e(LOG, selectQuery);
     
        Cursor c = db.rawQuery(selectQuery, null);
     
        if (c != null)
            c.moveToFirst();
     
        Playlist pl = new Playlist();
        pl.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        pl.setTitle((c.getString(c.getColumnIndex(KEY_PLAYLIST))));
     
        return pl;
    }
    
    /*
     * getting all playlists
     * */
    public ArrayList<Playlist> getAllPlaylists() {
    	ArrayList<Playlist> playlists = new ArrayList<Playlist>();
        String selectQuery = "SELECT  * FROM " + TABLE_PLAYLISTS;
     
        Log.e(LOG, selectQuery);
     
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
             
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Playlist pl = new Playlist();
                pl.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pl.setTitle((c.getString(c.getColumnIndex(KEY_PLAYLIST))));
     
                // adding to todo list
                playlists.add(pl);
            } while (c.moveToNext());
        }
     
        return playlists;
    }
    
    /*
     * Updating a playlist
     */
    public int updatePlaylist(Playlist playlist) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_PLAYLIST, playlist.getTitle());
     
        // updating row
        return db.update(TABLE_PLAYLISTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(playlist.getId()) });
    }
    
    public long getSong(String Title) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selectQuery = "SELECT  * FROM " + TABLE_SONGS + " WHERE "
                + KEY_SONG_TITLE + " = \"" + Title + "\"";
     
        Log.e(LOG, selectQuery);
     
        Cursor c = db.rawQuery(selectQuery, null);
     
        if (c == null || c.isClosed() || c.getCount() == 0)
            return -1;
        
        c.moveToFirst();
     
        return c.getLong(c.getColumnIndex(KEY_ID));
    }
    
    /*
     * Creating song
     */
    public long createSong(Song song) {
    	
    	long t = getSong(song.getTitle());
    	
    	if (t!=-1) return t;
    	
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_SONG_TITLE, song.getTitle());
        values.put(KEY_SONG_ALBUM, song.getAlbum());
        values.put(KEY_SONG_ARTIST, song.getArtist());
        values.put(KEY_SONG_PLAYLIST, song.getPlaylist());
     
        // insert row
        long tag_id = db.insert(TABLE_SONGS, null, values);
     
        return tag_id;
    }
    
    /*
     * getting all playlists
     * */
    public ArrayList<Song> getAllSongs(Playlist playlist) {
    	ArrayList<Song> playlists = new ArrayList<Song>();
        String selectQuery = "SELECT  * FROM " + TABLE_SONGS  + " WHERE "
                + KEY_SONG_PLAYLIST + " = " + playlist.getId();
     
        Log.e(LOG, selectQuery);
     
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Song song = new Song();
                song.setPlaylist(c.getInt((c.getColumnIndex(KEY_ID))));
                song.setTitle((c.getString(c.getColumnIndex(KEY_SONG_TITLE))));
                song.setAlbum((c.getString(c.getColumnIndex(KEY_SONG_ALBUM))));
                song.setArtist((c.getString(c.getColumnIndex(KEY_SONG_ARTIST))));
     
                // adding to todo list
                playlists.add(song);
            } while (c.moveToNext());
        }
     
        return playlists;
    }
    
    /*
     * Creating playlist_song
     */
    public long createTodoTag(long playlist_id, long song_id) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_PLAYLIST_ID, playlist_id);
        values.put(KEY_SONG_ID, song_id);
 
        long id = db.insert(TABLE_PLAYLIST_SONG, null, values);
 
        return id;
    }
    
    /**
     * Deleting a playlist song
     */
    public void deletePlaylist(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLAYLISTS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }
    
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

	public void deleteSong(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SONGS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
		
	}

}