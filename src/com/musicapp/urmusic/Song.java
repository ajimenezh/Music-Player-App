package com.musicapp.urmusic;

public class Song {

    private long Playlist;
	private String Title;
    private String Album;
    private String Artist;

    public Song(String title, String album, String artist) {
    	this.Title = title;
    	this.Artist = artist;
    	this.Album= album;
    }
    
    public Song() {
    }


    public String getTitle() {
    	return this.Title;
    }
    
    public String getAlbum() {
    	return this.Album;
    }
    
    public String getArtist() {
    	return this.Artist;
    }
    
    public void setTitle(String title) {
    	this.Title = title;
    }
    
    public void setAlbum(String album) {
    	this.Album = album;
    }
    
    public void setArtist(String artist) {
    	this.Artist = artist;
    }

	public long getPlaylist() {
		return this.Playlist;
	}

	public void setPlaylist(long id) {
		this.Playlist = id;
	}

}