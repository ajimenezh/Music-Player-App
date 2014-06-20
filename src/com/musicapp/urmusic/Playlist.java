package com.musicapp.urmusic;

public class Playlist {

    private String Title;
    private Integer NumberSongs;
    private Integer NumberPeople;
    private long Id;

    public Playlist(String title) {
    	this.Title = title;
    	this.NumberSongs = 0;
    	this.NumberPeople = 1;
    }
    
    public Playlist() {
    }


    public String getTitle() {
    	return this.Title;
    }
    
    public void setTitle(String title) {
    	this.Title = title;
    }
    
    public void setId(long l) {
    	this.Id = l;
    }
    
    public long getId() {
    	return this.Id;
    }
    
    public Integer getNumberSongs() {
    	return this.NumberSongs;
    }
    
    public Integer getNumberPeople() {
    	return this.NumberPeople;
    }
    
    public void addSong() {
    	this.NumberSongs++;
    }
    
    public void addSPerson() {
    	this.NumberPeople++;
    }

}
