package com.moodify.evan.moodify;

        //Stuff for music player tutorial
        import java.util.*;

        import android.Manifest;
        import android.content.ComponentName;
        import android.content.Context;
        import android.content.ServiceConnection;
        import android.content.pm.PackageManager;
        import android.net.Uri;
        import android.content.ContentResolver;
        import android.database.Cursor;
        import android.os.Build;
        import android.os.IBinder;
        import android.provider.ContactsContract;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ListView;


        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;

        import com.spotify.sdk.android.authentication.AuthenticationClient;
        import com.spotify.sdk.android.authentication.AuthenticationRequest;
        import com.spotify.sdk.android.authentication.AuthenticationResponse;
        import com.spotify.sdk.android.player.Config;
        import com.spotify.sdk.android.player.ConnectionStateCallback;
        import com.spotify.sdk.android.player.Error;
        import com.spotify.sdk.android.player.Player;
        import com.spotify.sdk.android.player.PlayerEvent;
        import com.spotify.sdk.android.player.Spotify;
        import com.spotify.sdk.android.player.SpotifyPlayer;

public class MainActivity extends Activity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback
{

    private static final String CLIENT_ID = "021703a983cd4ea0939366c49fa98d52";
    private static final String REDIRECT_URI = "moodify-481://callback";

    private Player mPlayer;
    private static final int REQUEST_CODE = 1337;
    
    private DataApi api;

    // Stuff for music player tutorial
    private ArrayList<Song> songList;
    private ListView songView;
    
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                                                                                   AuthenticationResponse.Type.TOKEN,
                                                                                   REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private","streaming"});
        AuthenticationRequest request = builder.build();

        songView = (ListView)findViewById(R.id.song_list);
        songList = new ArrayList<Song>();
   
       ActivityCompat.requestPermissions(MainActivity.this,
               new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        
       // now done in override function after ^ that happens
        //getSongList();
        
        Collections.sort(songList, new Comparator<Song>() {
           @Override
           public int compare(Song a, Song b) {
              return a.getTitle().compareTo(b.getTitle());
           }
        });
//
//        SongAdapter songAdapter = new SongAdapter(this, songList);
//        int num = songAdapter.getCount();
//        Log.d("SongAdapter check", "Song adapter count == " + num);
//        songView.setAdapter(songAdapter);
        
        
   
       AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }
   
   private ServiceConnection musicConnection = new ServiceConnection(){
      
      @Override
      public void onServiceConnected(ComponentName name, IBinder service) {
         MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
         //get service
         musicSrv = binder.getService();
         //pass list
         musicSrv.setList(songList);
         musicBound = true;
      }
      
      @Override
      public void onServiceDisconnected(ComponentName name) {
         musicBound = false;
      }
   };
   
   @Override
   protected void onStart() {
      super.onStart();
      if(playIntent==null){
         playIntent = new Intent(this, MusicService.class);
         bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
         startService(playIntent);
      }
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
   
       SongAdapter songAdapter = new SongAdapter(this, songList);
       int num = songAdapter.getCount();
       Log.d("SongAdapter check", "Song adapter count == " + num);
       songView.setAdapter(songAdapter);

        if(requestCode == REQUEST_CODE) {
            final AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if(response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addNotificationCallback(MainActivity.this);
                        Log.d("MainActivity", "Initialized Player");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity","Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
      Log.d("onDestroy", "RIP");
        Spotify.destroyPlayer(this);
        
        //
       stopService(playIntent);
       musicSrv = null;
       
        super.onDestroy();
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
           
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");

        //mPlayer.playUri(null, "spotify:track:6qAAOu6mkt8T0GTS7FGr9a", 0, 0);
        
        //open main menu activity here
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error i) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }
    
    //My override for permission request
   @Override
   public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
       Log.d("MainActivity", "Recieved permission result");
      switch (requestCode) {
         case 1: {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               // permission granted and now can proceed
               getSongList(); //a sample method called
            
            } else {
            
               // permission denied, boo! Disable the
               // functionality that depends on this permission.
               //Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
            return;
         }
         // add other cases for more permissions
      }
   }
   
   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case R.id.action_shuffle:
            //shuffle
            break;
         case R.id.action_end:
            stopService(playIntent);
            musicSrv=null;
            System.exit(0);
            break;
      }
      return super.onOptionsItemSelected(item);
   }
   
    
    //Non-override stuff
   public void getSongList() {
       Log.d("Main Activity", "Getting song list");
      ContentResolver musicResolver = getContentResolver();
      Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
      Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
   
      int i = 0;
      if(musicCursor!=null && musicCursor.moveToFirst()){
         //get columns
         int titleColumn = musicCursor.getColumnIndex
                 (android.provider.MediaStore.Audio.Media.TITLE);
         int idColumn = musicCursor.getColumnIndex
                 (android.provider.MediaStore.Audio.Media._ID);
         int artistColumn = musicCursor.getColumnIndex
                 (android.provider.MediaStore.Audio.Media.ARTIST);
         //add songs to list
         do {
            long thisId = musicCursor.getLong(idColumn);
            String thisTitle = musicCursor.getString(titleColumn);
            String thisArtist = musicCursor.getString(artistColumn);
            songList.add(new Song(thisId, thisTitle, thisArtist));
            i++;
         }
         while (musicCursor.moveToNext());
      }
      Log.d("MainActivity", "Got " + i + " songs");
      musicCursor.close();
   }
   
   public void songPicked(View view) {
      musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
      musicSrv.playSong();
      
   }
}