package com.example.sdk_practice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

import com.example.maxtap.Maxtap;

public class MainActivity extends AppCompatActivity {
    ExoPlayer exoPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // creating an instance of ExoPlayer.
        exoPlayer = new ExoPlayer.Builder(this).build();

        PlayerView playerView = findViewById(R.id.exoPlayer);
        //Binding the player with the view that is there in our xml.
        playerView.setPlayer(exoPlayer);
        //Building the media Item.
        MediaItem mediaItem = MediaItem.fromUri("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4");
        
        //Setting the media item that is to be played.
        exoPlayer.setMediaItem(mediaItem);
        //preparing the player
        exoPlayer.prepare();
        //playing the video.
        exoPlayer.play();


        String clientContentId = "test_data";

        String clientId = "hotstar";

        /*Add these line to the ott side*/
        Maxtap maxtap = new Maxtap();
        maxtap.init(this, playerView, clientContentId, clientId);
        maxtap.runAds(exoPlayer);
        /*Till here*/
    }

}

