package com.example.maxtap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

public class Maxtap {
    ExoPlayer player;

    public void getAds(ExoPlayer player1, final ImageView image) {
        image.setVisibility(View.GONE);

        new GetImageJson(image).execute();


        player = player1;
        for (int i = 0; i < 100; i++) {
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


                }
            }, mill);


        }

    }

private static class GetImageJson extends AsyncTask<Void, Void, Void> {
        ImageView imageView;
        String url1;
        public GetImageJson (ImageView imageView){
        this.imageView = imageView;
    }


        @Override
    protected Void doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();
        // Making a request to url and getting response
        String url = "https://firebasestorage.googleapis.com/v0/b/maxtap-adserver-dev.appspot.com/o/content1.json?alt=media&token=4464ee3b-8463-4ede-a6df-8e717a7d7aae";
        String jsonStr = sh.makeServiceCall(url);


        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                JSONObject ads = jsonObj.getJSONObject("Ad2");
                JSONObject img = ads.getJSONObject("image");
                url1 = img.getString("src");

                }
            catch (final JSONException e) {

            }

        } else {


        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        new DownLoadImageTask(imageView).execute(url1);
    }
}

private static class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
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
