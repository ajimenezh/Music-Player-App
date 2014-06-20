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

public class PlaylistsAdapter extends BaseAdapter {


    private List<Playlist> playlists;
    private Context context;


    public PlaylistsAdapter(Context context, List<Playlist> topics){
        this.playlists = topics;
        this.context = context;
    }


    @Override
    public int getCount() {
        return playlists.size();
    }


    @Override
    public Object getItem(int position) {
        return playlists.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 100;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    	View rootView = LayoutInflater.from(context).inflate(R.layout.playlist_itemv2, parent, false);

    	TextView title = (TextView) rootView.findViewById(R.id.playlistTitle);
    	
    	title.setText(playlists.get(position).getTitle());
       
        return rootView;
    }
}
