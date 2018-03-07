package com.moodify.evan.moodify;

import java.util.ArrayList;
import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.spotify.sdk.android.player.SpotifyPlayer;

// Created by White on 2/28/2018.

public class MusicService extends Service implements
   MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
   
   private MediaPlayer player;
   private SpotifyPlayer spotifyPlayer;
   private ArrayList<Song> songs;
   private int songPosn;
   
   private final IBinder musicBind = new MusicBinder();
   
   public void onCreate() {
      super.onCreate();
      
      songPosn = 0;
      
      player = new MediaPlayer();
   }
   
   public void setList(ArrayList<Song> Songs) {
      songs = Songs;
   }
   
   public void setSong(int songIndex) {
      songPosn = songIndex;
   }
   
   public void playSong() {
      Log.d("Song Player", "Entered playSong()");
      player.reset();
      Song playSong = songs.get(songPosn);
      long currSong = playSong.getDepId();
      Uri trackuri = ContentUris.withAppendedId(
              MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
              currSong);
      try {
         player.setDataSource(getApplicationContext(),trackuri);
      } catch (Exception e) {
         Log.e("GUCK", "ERROR PLZ");
      }
      
      player.prepareAsync();
   }
   
   public class MusicBinder extends Binder {
      MusicService getService() {
         return MusicService.this;
      }
   }
   
   @Override
   public IBinder onBind(Intent arg0) {
       return musicBind;
   }
   
   @Override
   public boolean onUnbind(Intent intent){
      player.stop();
      player.release();
      return false;
   }
   
   @Override
   public void onCompletion(MediaPlayer mediaPlayer) {
   
   }
   
   @Override
   public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
      return false;
   }
   
   @Override
   public void onPrepared(MediaPlayer mediaPlayer) {
      mediaPlayer.start();
   }
}
