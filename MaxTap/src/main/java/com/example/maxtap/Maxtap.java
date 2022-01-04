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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Maxtap {
    ExoPlayer player;

    public void getAds(ExoPlayer player1, final ImageView image) {
        image.setVisibility(View.GONE);
        final ArrayList<String>arr=new ArrayList<String>();
        final ArrayList<JSONObject> arrayList=new ArrayList<JSONObject>();
        new GetImageJson(arr,arrayList).execute();




        player = player1;
        final Integer[] var = {new Integer(0)};
        for (int i = 0; i < 100; i++) {
            int mill = 1000 * (i + 1);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(arrayList.size()> var[0].intValue()){
                    long x = player.getCurrentPosition() / 1000;
                    if(x==1){
                        try {
                            new DownLoadImageTask(image).execute((String)arrayList.get(var[0].intValue()).get("img_url"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    else {
                        try {

                            Log.e("start-----",arrayList.size()+" "+ var[0].intValue());

                            if (x == (((Integer)(arrayList.get(var[0].intValue()).get("start")))).intValue()) {


                                image.setVisibility(View.VISIBLE);
                            } else if (x ==(Integer)(arrayList.get(var[0].intValue()).get("end"))) {

                                image.setVisibility(View.GONE);
                                var[0] =new Integer(var[0].intValue()+1);

                                new DownLoadImageTask(image).execute(arr.get(1));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
                }
            }, mill);


    }

    }

private static class GetImageJson extends AsyncTask<Void, Void, Void> {
        ArrayList<String> arr;
        ArrayList<JSONObject> arr1;
        String url1;
        public GetImageJson (ArrayList<String> arrayList,ArrayList<JSONObject> object){
        arr=arrayList;
        arr1=object;
    }


        @Override
    protected Void doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();
        // Making a request to url and getting response
        String url = "https://storage.googleapis.com/maxtap-adserver-dev.appspot.com/spiderman-4.json";
        String jsonStr = sh.makeServiceCall(url);


        if (jsonStr != null) {
            try {
                JSONArray jsonArr = new JSONArray(jsonStr);
                JSONArray sortedJsonArray = new JSONArray();
                List<JSONObject> jsonList = new ArrayList<JSONObject>();
                for (int i = 0; i < jsonArr.length(); i++) {
                    jsonList.add(jsonArr.getJSONObject(i));
                }
                Collections.sort( jsonList, new Comparator<JSONObject>() {

                    public int compare(JSONObject a, JSONObject b) {
                        Integer valA= 0;
                        Integer valB= 0;
                        try {
                            valA =  (Integer) a.get("start");
                            valB = (Integer) b.get("start");
                            Log.i("start",valA+""+valB+" ");
                        }
                        catch (JSONException e) {
                            //do something
                        }

                        return valA.compareTo(valB);
                    }
                });
                for (int i = 0; i < jsonArr.length(); i++) {
                    sortedJsonArray.put(jsonList.get(i));
                }
                Log.e("size....",sortedJsonArray.length()+" ");
                for(int i=0;i<sortedJsonArray.length();i++) {

                    JSONObject ads = sortedJsonArray.getJSONObject(i);
                    arr1.add(ads);
                    String s1 = ads.getString("img_url");


                    arr.add(s1);
                }




                }
            catch (final JSONException e) {

            }

        } else {


        }


        return null;
    }


    protected ArrayList<String> onPostExecute(ArrayList<String> strings) {


        return arr;

    }
}

private  class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
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
