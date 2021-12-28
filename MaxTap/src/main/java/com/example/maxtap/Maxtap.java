package com.example.maxtap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.google.android.exoplayer2.ExoPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Maxtap {
    ExoPlayer player;

    public void getAds(ExoPlayer player1, final ImageView image)  {
        image.setVisibility(View.GONE);

        new DownLoadImageTask(image).execute("https://static.toiimg.com/thumb/82004611.cms?width=200&height=200&imgsize=360172");


        player = player1;
        for (int i = 0; i < 50; i++) {
            int mill = 1000 * (i + 1);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {


                    long x = player.getCurrentPosition() / 1000;
                    if (x == 10) {


                        image.setVisibility(View.VISIBLE);
                    } else if (x == 18) {

                        image.setVisibility(View.GONE);

                    } else if (x == 25) {
                        image.setVisibility(View.VISIBLE);
                    } else if (x == 35) {
                        image.setVisibility(View.GONE);
                    }

//
                }
            }, mill);


        }


    }
    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }




}
