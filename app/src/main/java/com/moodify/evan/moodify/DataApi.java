package com.moodify.evan.moodify;


import com.wrapper.spotify.SpotifyApi;

/**
 * Created by White on 2/28/2018.
 */

public class DataApi {
   private String accessToken;
   private SpotifyApi api;
   
   public DataApi(String accessToken, String clientID, String redirectUri) {
      this.accessToken = accessToken;
      this.api = new SpotifyApi.Builder()
              .setClientId(clientID)
              //.setRedirectUri(redirectUri)
              // TODO The above line is supposed to work, I should investigate
              .setAccessToken(accessToken)
              .build();
   }
}
