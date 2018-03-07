package com.moodify.evan.moodify;

//import com.neovisionaries.il8n.CountryCode;
import android.util.Log;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;

/**
 * Created by White on 2/26/2018.
 */

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Metadata;
import com.spotify.sdk.android.player.Spotify;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Song {
   private String title;
   private String artist;
   private String album;
   private int image; // TODO - Figure out how Spotify deals with album images to make things look nice
   private String id;
   private long deprecated_id;
   private SpotifyApi api;
   private Track track;

   public Song(String title, String artist, String album, int image, String id) {
       this.title = title;
       this.artist = artist;
       this.album = album;
       this.image = image;
       this.id = id;
       
       this.api = new SpotifyApi.Builder()
               .setAccessToken("")
               .build();
   }
   
   //TODO remove once I switch from tutorial based stuff to spotify based stuff
   public Song(long id, String title, String artist) {
      this.deprecated_id = id;
      this.title = title;
      this.artist = artist;
   }

   public Song(String uri) {
      this.api = new SpotifyApi.Builder()
              .setAccessToken("")
              .build();
      
      final GetTrackRequest getTrackRequest = api.getTrack(uri)
              .market(CountryCode.US)
              .build();
      
      //TODO Find out if I should do this asynchronously instead of sync
      // currently done sync
      try {
         final Track track = getTrackRequest.execute();
         
         this.track = track;
         this.title = track.getName();
         this.artist = track.getArtists()[0].getName();
         this.album = track.getAlbum().getName();
         // image i'm gonna ignore for now
         this.id = uri;
         
         
      } catch (IOException | SpotifyWebApiException e) {
         Log.e("SongBuilder", "Error: " + e.getCause().getMessage());
      }
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

   public String getId() {
       return this.id;
   }
   
   public long getDepId() {
      return this.deprecated_id;
   }
}
