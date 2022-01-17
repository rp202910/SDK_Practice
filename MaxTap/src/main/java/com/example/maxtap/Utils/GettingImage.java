package com.example.maxtap.Utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class GettingImage extends AsyncTask<String, Void, Bitmap> {
    @SuppressLint("StaticFieldLeak")

    ImageView imageView;

    public GettingImage(ImageView imageView) {
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String... urls) {
        String urlOfImage = urls[0];
        Bitmap logo = null;
        Log.i("my_tag", urlOfImage);
        try {
            InputStream is = new URL(urlOfImage).openStream();

            logo = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logo;
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);

    }
}