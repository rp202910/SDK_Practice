package com.example.maxtap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.exoplayer2.ExoPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.nio.channels.ScatteringByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Maxtap {
   Activity activity;
    View playerView;
      int pos=0;
    ArrayList<JSONObject> arrayList;
    public void getAds(Activity activity, View playerView) {

        this.activity=activity;
        this.playerView=playerView;

        arrayList=new ArrayList<JSONObject>();
        //reading from JSON.
        new GetImageJson(arrayList).execute();



    }
//
//
//
//
//        image.setVisibility(View.GONE);
//        final ArrayList<String>arr=new ArrayList<String>();
//        final ArrayList<JSONObject> arrayList=new ArrayList<JSONObject>();
//        //reading from JSON.
//        new GetImageJson(arr,arrayList).execute();
//
//
//        player = player1;
//        final Integer[] var = {new Integer(0)};
//            //for seeing every second.
//                for (int i = 0; i < 100; i++) {
//            int mill = 1000 * (i + 1);
//
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if(arrayList.size()> var[0].intValue()){
//                    long x = player.getCurrentPosition() / 1000;
//                    if(x==1){
//                        try {
//                            //getting the mitmap.
//                            new DownLoadImageTask(image).execute((String)arrayList.get(var[0].intValue()).get("img_url"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                    else {
//                        try {
//
//                            //Log.e("start-----",arrayList.size()+" "+ var[0].intValue());
//
//                            if (x == (((Integer)(arrayList.get(var[0].intValue()).get("start")))).intValue()) {
////
////
////                                image.setVisibility(View.VISIBLE);
////                            } else if (x ==(Integer)(arrayList.get(var[0].intValue()).get("end"))) {
////
//                                image.setVisibility(View.GONE);
//                                var[0] =new Integer(var[0].intValue()+1);
//
//                                new DownLoadImageTask(image).execute(arr.get(1));
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }
//                }
//            }, mill);
//
//
//    }
//
//    }

private static class GetImageJson extends AsyncTask<Void, Void, Void> {
//        ArrayList<String> arr;
        ArrayList<JSONObject> arr1;
        String url1;
        public GetImageJson (ArrayList<JSONObject> object){

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



                }




                }
            catch (final JSONException e) {

            }

        } else {


        }


        return null;
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
    boolean viewInserted =false,firstLoad=true;
    ImageView img ;
   RelativeLayout ads;
    public void updateAds(long position) {
        try {
            if (arrayList.size() > 0 && pos<arrayList.size()) {

                if(firstLoad) {
                    img = new ImageView(activity);
                    //img.setMaxWidth(100);
                    new DownLoadImageTask(img).execute((String) arrayList.get(pos).get("img_url"));
                    firstLoad=false;
                }


                long startTime = (((Integer) (arrayList.get(pos).get("start")))).intValue()*1000, endTime = (((Integer) (arrayList.get(pos).get("end")))).intValue()*1000;

                    Log.e("Coming....",startTime+" "+endTime+"\n");

                if (position >= startTime && (!viewInserted)) {
                    Context context;
                    viewInserted = true;
                    int x=150;
                    int y=150;

                  LinearLayout.MarginLayoutParams imageLayout =new LinearLayout.MarginLayoutParams(x,y);
//                    //img.setLayoutParams(parms);
//                    parms1.setMargins(400,500,20,200);
                    img.setLayoutParams(imageLayout);
                    RelativeLayout mainConatiner=new RelativeLayout(activity);
                    RelativeLayout.LayoutParams parms2 = new RelativeLayout.LayoutParams(400, 200);
                    mainConatiner.setLayoutParams(parms2);
                    mainConatiner.setBackgroundColor(Color.RED);



//
                    mainConatiner.addView(img);

                    TextView txt = new TextView(activity);
                    txt.setText("Myhyhjy");
//                    LinearLayout.LayoutParams textLa = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//                    txt.setLayoutParams(textLa);
                    mainConatiner.addView(txt);
                    RelativeLayout.LayoutParams params =
                            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
//                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
//                    img.setLayoutParams(params);


//                  parms1.width=(int) playerView.getLayoutParams().width*(int)(10.0/100);
//                    parms1.height=(int)playerView.getLayoutParams().height*(int)(5.0/100);

                    Log.e("widht...",imageLayout.width+" "+imageLayout.height);

//
//                   ads=new RelativeLayout(activity);
//                   ads.addView(img);
//
//
////                    ad=new RelativeLayout(activity);
////                    ad.addView(img);
//                    TextView txt = new TextView(activity);
//                    txt.setText("Myantra");
//                    LinearLayout.MarginLayoutParams parm1 = (ViewGroup.MarginLayoutParams) playerView.getLayoutParams();







                    ViewGroup rootview = (ViewGroup) activity.findViewById(android.R.id.content).getRootView();

                    RelativeLayout.LayoutParams params1 =
                            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    mainConatiner.setLayoutParams(params1);
                    rootview.addView(mainConatiner);
                }
                if (position >= endTime && (viewInserted)) {
                    ViewGroup rootview = (ViewGroup) activity.findViewById(android.R.id.content).getRootView();
                    rootview.removeView(img);
                    pos++;
                    viewInserted = false;
                    if(pos<arrayList.size()) {
                        img = new ImageView(activity);
                        new DownLoadImageTask(img).execute((String) arrayList.get(pos).get("img_url"));
                    }
                }


            }
        }
        catch (Exception e){
            e.printStackTrace();

        }
    }







}
