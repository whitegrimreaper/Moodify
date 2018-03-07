package com.moodify.evan.moodify;

import android.util.Log;
import android.view.*;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by White on 2/28/2018.
 */

public class SongAdapter extends BaseAdapter {
   
   private ArrayList<Song> songs;
   private LayoutInflater songInf;
   
   public SongAdapter(Context c, ArrayList<Song> Songs) {
      songs = Songs;
      songInf = LayoutInflater.from(c);
   }
   
   @Override
   public int getCount() {
      return songs.size();
   }
   
   @Override
   public Object getItem(int arg0) {
      // TODO Auto-generated method stub
      return null;
   }
   
   @Override
   public long getItemId(int arg0) {
      // TODO Auto-generated method stub
      return 0;
   }
   
   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      //map to song layout
      LinearLayout songLay = (LinearLayout)songInf.inflate
              (R.layout.song, parent, false);
      //get title and artist views
      TextView songView = (TextView)songLay.findViewById(R.id.song_title);
      TextView artistView = (TextView)songLay.findViewById(R.id.song_artist);
      //get song using position
      Song currSong = songs.get(position);
      //get title and artist strings
      songView.setText(currSong.getTitle());
      //Log.d("SongAdapter", "Current song is "+ currSong.getTitle());
      artistView.setText(currSong.getArtist());
      //set position as tag
      songLay.setTag(position);
      return songLay;
   }
}
