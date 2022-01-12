package com.example.sdk_practice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

import com.example.maxtap.Maxtap;

public class MainActivity extends AppCompatActivity {
    ExoPlayer exoPlayer;
    /*Add this to the client side*/
    //This is intializing the plugin instance.
    final Maxtap maxtap =new Maxtap();
    final Handler handler=new Handler();
    final Runnable runnable =new Runnable() {
        @Override
        public void run() {
            maxtap.updateAds(exoPlayer.getCurrentPosition());
            handler.postDelayed(runnable,1000);
        }
    };
    /*TILL HERE*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // creating an instance of ExoPlayer.
        exoPlayer = new ExoPlayer.Builder(this).build();

        PlayerView playerView=findViewById(R.id.exoPlayer);
        //Binding the player with the view that is there in our xml.
        playerView.setPlayer(exoPlayer);
        //Building the media Item.
        MediaItem mediaItem = MediaItem.fromUri("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4" );
        //Setting the media item that is to be played.
        exoPlayer.setMediaItem(mediaItem);
        //preparing the player
        exoPlayer.prepare();
        //playing the video.
        exoPlayer.play();



        String clientContentId="spiderman-5";

        String clientId="hotstar";

        /*Add these line to the ott side*/
        maxtap.init(this,(View)playerView,clientContentId,clientId);
        //This will intialiize the feilds.
        handler.postDelayed(runnable,1000);
        /*Till here*/



    }




}



//// Initialize the Mobile Ads SDK
//        MaxtapAds.initialize(this, new OnInitializationCompleteListener() {
//@Override
//public void onInitializationComplete(InitializationStatus initializationStatus) {
//        Toast.makeText(this, " successful ", Toast.LENGTH_SHORT).show();
//        }
//        });
//
//        // AdView mAdView;
//        // mAdView = findViewById(R.id.adView);
//
//        // maxtapAdView = findViewById(R.id.adView);
//
//        MaxtapAdView maxtapAdView = new MaxtapAdView();
//
//        clientId = "hotstar";
//        oTTContentId = "spiderman-4";
//
//        Exoplayer videoPlayer = new Exoplayer();
//
//        MaxtapAdRequest maxtapAdRequest = new MaxtapAdRequest.Builder().build(oTTContentId, clientId); // list of 20 ads to show on spiderman-4
//        maxtapAdView.loadAd(videoPlayer, maxtapAdRequest, anyAdditionalOptions);
//        }
//        }
//
//        maxtapAdObj = new singleton MaxtapAdObj()
//        maxtapAdObj.setContent("content-id") // prefetch ads also
//        maxtapObj.renderAds(videoPlayer)
////
////
/////*
////Request builder should be the part of initialization
////// promise related architecture in android.
////Kotlin has a good implementation of promises
////
////Events
////1) Hotstar App open --> basic initialization. Singleton object. To be used in in init. NOT in resume functionality
////2) User selected a movie ->
////3) user playing a movie -> fetch ads list.  + ads render
//
////Client Side
////public class MaxtapAdRequest extends AppCompatActivity {
////    protected void builder(oTTContentId, clientId)
////    {
////        ads = maxtap.api.loadjson(oTTContentId, clientId);
////        checkAds(ads);
////        return ads
////    }
////}
////
////public class MaxtapAdView extends AppCompatActivity {
////    protected void loadAd(videoPlayer, maxtapAdRequest, anyAdditionalOptions);
////    {
////        videoPlayerType = getObjType(videoPlayer);
////        if (videoPlayerType=="Exoplayer"){
////            //show maxtapAdRequest on Exoplayer
////        }
////        else if (videoPlayerType=="SomeRandomVideoPlayer"){
////            //show maxtapAdRequest on Exoplayer
////        }
////        else{
////            //show maxtapAdRequest on a generic video player
////        }
////    }
////}