package com.musicapp.urmusic;

import java.io.Console;
import java.util.Date;
import java.util.List;

import com.musicapp.urmusic.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SongAdapter extends BaseAdapter {


    private List<Song> songs;
    private Context context;


    public SongAdapter(Context context, List<Song> songs){
        this.songs = songs;
        this.context = context;
    }


    @Override
    public int getCount() {
        return songs.size();
    }


    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 100;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    	View rootView = LayoutInflater.from(context).inflate(R.layout.playlist_itemv2, parent, false);

    	TextView title = (TextView) rootView.findViewById(R.id.playlistTitle);
    	
    	title.setText(songs.get(position).getTitle());
       
        return rootView;
    }
}
