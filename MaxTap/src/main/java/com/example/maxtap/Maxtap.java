package com.example.maxtap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
    int pos = 0;
    ArrayList<JSONObject> arrayList;

    public void getAds(Activity activity, View playerView) {

        this.activity = activity;
        this.playerView = playerView;

        arrayList = new ArrayList<JSONObject>();
        // reading from JSON.
        new GetImageJson(arrayList).execute();

    }

    private static class GetImageJson extends AsyncTask<Void, Void, Void> {

        ArrayList<JSONObject> arr1;
        String url1;

        public GetImageJson(ArrayList<JSONObject> object) {

            arr1 = object;
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
                    Collections.sort(jsonList, new Comparator<JSONObject>() {

                        public int compare(JSONObject a, JSONObject b) {
                            Integer valA = 0;
                            Integer valB = 0;
                            try {
                                valA = (Integer) a.get("start");
                                valB = (Integer) b.get("start");

                            } catch (JSONException e) {
                                // do something
                            }

                            return valA.compareTo(valB);
                        }
                    });
                    for (int i = 0; i < jsonArr.length(); i++) {
                        sortedJsonArray.put(jsonList.get(i));
                    }

                    for (int i = 0; i < sortedJsonArray.length(); i++) {

                        JSONObject ads = sortedJsonArray.getJSONObject(i);
                        arr1.add(ads);
                        String s1 = ads.getString("img_url");
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                }

            } else {

            }

            return null;
        }

    }

    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
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

    // Real code starts

    boolean viewInserted = false, firstLoad = true;
    ImageView img;
    RelativeLayout topContainer;
    public void updateAds(long position) {
        try {
            if (arrayList.size() > 0 && pos < arrayList.size()) {

                if (firstLoad) {
                    img = new ImageView(activity);

                    new DownLoadImageTask(img).execute((String) arrayList.get(pos).get("img_url"));
                    firstLoad = false;
                }

                long startTime = (((Integer) (arrayList.get(pos).get("start")))).intValue() * 1000,
                        endTime = (((Integer) (arrayList.get(pos).get("end")))).intValue() * 1000;

                if (position >= startTime && (!viewInserted)) {
                   LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(100,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                     topContainer = new RelativeLayout(activity);

                    Context context;
                    viewInserted = true;
                    img.setBackgroundColor(Color.BLUE);


                    String s1=(String) arrayList.get(pos).get("product_details");
                    final String s2=(String) arrayList.get(pos).get("ad_url");


                    int bottom=playerView.getBottom();
                    int right=playerView.getRight();
                    Log.e("Values...........",bottom+" "+right+" ");

                    DisplayMetrics displayMetrics=new DisplayMetrics();
                    activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    LinearLayout mainConatiner = new LinearLayout(activity);
                    mainConatiner.setOrientation(LinearLayout.HORIZONTAL);
                    Resources r = activity.getResources();
                    int height = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            75,
                            r.getDisplayMetrics()
                    );
                    int width = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            150,
                            r.getDisplayMetrics()
                    );

                    RelativeLayout.LayoutParams parms2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,height);
                    parms2.setMargins(0, 0, displayMetrics.widthPixels-right, displayMetrics.heightPixels-bottom);

                    parms2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    parms2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                    mainConatiner.setLayoutParams(parms2);
                    FrameLayout.LayoutParams viewGrp= new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


                    mainConatiner.setBackgroundColor(Color.argb(60,0,0,0));



                    TextView txt = new TextView(activity);
                    txt.setText(s1);
                    LinearLayout.LayoutParams textLa = new LinearLayout.LayoutParams(
                         150 , ViewGroup.LayoutParams.MATCH_PARENT);
                    txt.setGravity(Gravity.CENTER_VERTICAL);
                    txt.setLayoutParams(textLa);
                    txt.setTextColor(Color.WHITE);
                    textLa.leftMargin=10;

                    txt.setTypeface(null, Typeface.BOLD);
                    mainConatiner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(s2));
                            activity.startActivity(browserIntent);
                        }
                    });
                    mainConatiner.addView(txt);
                    mainConatiner.addView(img,par);


                    ViewGroup rootview = (ViewGroup) activity.findViewById(android.R.id.content).getRootView();

                    topContainer.addView(mainConatiner);
                    rootview.addView(topContainer);

                    topContainer.setLayoutParams(viewGrp);


                }
                if (position >= endTime && (viewInserted)) {
                    ViewGroup rootview = (ViewGroup) activity.findViewById(android.R.id.content).getRootView();
                    rootview.removeView(topContainer);
                    pos++;
                    viewInserted = false;
                    if (pos < arrayList.size()) {
                        img = new ImageView(activity);
                        new DownLoadImageTask(img).execute((String) arrayList.get(pos).get("img_url"));
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
