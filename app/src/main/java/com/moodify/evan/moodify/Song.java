package com.moodify.evan.moodify;

/**
 * Created by White on 2/26/2018.
 */

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Metadata;
import com.spotify.sdk.android.player.Spotify;

public class Song {
   private String title;
   private String artist;
   private String album;
   private int image; // TODO - Figure out how Spotify deals with album images to make things look nice
   private long id;

   public Song(String title, String artist, String album, int image, long id) {
       this.title = title;
       this.artist = artist;
       this.album = album;
       this.image = image;
       this.id = id;
   }

   //TODO - fuck spotify's api for not making this native, but I need to figure out how to use the web api to build a song object from a song URI
   public Song(String uri){
      // implement here
   }

   public String getTitle() {
       return this.title;
   }

   public String getArtist() {
       return this.artist;
   }

   public String getAlbum() {
       return this.album;
   }

   public int getImage() {
       return this.image;
   }

   public long getId() {
       return this.id;
   }
}
