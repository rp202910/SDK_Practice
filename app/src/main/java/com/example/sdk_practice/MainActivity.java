package com.example.sdk_practice;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

import com.example.maxtap.Maxtap;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
            ExoPlayer player;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                // creating an instance of ExoPlayer.
                player = new ExoPlayer.Builder(this).build();
                final ImageView image =findViewById(R.id.imageView);
                PlayerView playerView=findViewById(R.id.exoPlayer);
                //Binding the player with the view that is there in our xml.
                playerView.setPlayer(player);
                //Building the media Item.
                MediaItem mediaItem = MediaItem.fromUri("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4" );
                //Setting the media item that is to be played.
                player.setMediaItem(mediaItem);
                //preparing the player
                player.prepare();
        //playing the video.
        player.play();

        Maxtap maxtap =new Maxtap();
        maxtap.getAds(player,image);



    }




}