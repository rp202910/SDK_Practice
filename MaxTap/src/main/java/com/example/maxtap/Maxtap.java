package com.example.maxtap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.Map;

public class Maxtap {
    Activity activity;
    View playerView;
    ArrayList<JSONObject> arrayList;
    HashMap<Pair<Integer, Integer>, JSONObject> hashMap;
    HashMap<JSONObject, ImageView> imageViewHashMap;

    String clientId;/*getting the client id*/
    static String contentId;/*getting the content played from particular client*/

    //For initializing
    public void init(Activity activity, View playerView, String clientContentId, String client) {

        this.activity = activity;
        this.playerView = playerView;
        contentId = clientContentId;
        this.clientId = client;

        arrayList = new ArrayList<JSONObject>();
        hashMap = new HashMap<>();
        imageViewHashMap = new HashMap<>();
        // reading from JSON.
        new ReadJson(arrayList, hashMap, imageViewHashMap).execute();

    }
//    static <T> void  sortingAds(List<T> objects){
//        Collections.sort(objects , new Comparator<T>() {
//
//            public int compare(T ad1, T ad2) {
//                Integer getStartA = 0;
//                Integer getStartB = 0;
//                try {
//                    getStartA = (Integer) ad1.get("start");
//                    getStartB = (Integer) ad2.get("start");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                return getStartA.compareTo(getStartB);
//            }
//        });
//
//
//    }


    @SuppressLint("StaticFieldLeak")
    private class ReadJson extends AsyncTask<Void, Void, Void> {

        ArrayList<JSONObject> adList;
        HashMap<Pair<Integer, Integer>, JSONObject> hashMap;
        HashMap<JSONObject, ImageView> imageViewHashMap;

        public ReadJson(ArrayList<JSONObject> object, HashMap<Pair<Integer, Integer>, JSONObject> hashMap, HashMap<JSONObject, ImageView> imageViewHashMap) {
            this.hashMap = hashMap;
            this.imageViewHashMap = imageViewHashMap;
            adList = object;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response

            // String dataUrl = "https://storage.googleapis.com/maxtap-adserver-dev.appspot.com/"+ contentId +".json";
            String dataUrl = "https://firebasestorage.googleapis.com/v0/b/maxtap-adserver-dev.appspot.com/o/Naagin.json?alt=media&token=7b26b182-2da0-4174-afe0-697fe96ed287";
            String jsonStr = sh.makeServiceCall(dataUrl);

            if (jsonStr != null) {
                try {
                    JSONArray jsonArr = new JSONArray(jsonStr);
                    JSONArray sortedJsonArray = new JSONArray();
                    List<JSONObject> jsonList = new ArrayList<JSONObject>();
                    for (int i = 0; i < jsonArr.length(); i++) {
                        jsonList.add(jsonArr.getJSONObject(i));
                    }
                    //Task1.Put in the file.

                    Collections.sort(jsonList, new Comparator<JSONObject>() {

                        public int compare(JSONObject ad1, JSONObject ad2) {
                            Integer getStartA = 0;
                            Integer getStartB = 0;
                            try {
                                getStartA = (Integer) ad1.get("start_time");
                                getStartB = (Integer) ad2.get("start_time");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            return getStartA.compareTo(getStartB);
                        }
                    });
                    for (int i = 0; i < jsonArr.length(); i++) {
                        sortedJsonArray.put(jsonList.get(i));
                    }

                    for (int i = 0; i < sortedJsonArray.length(); i++) {

                        JSONObject ads = sortedJsonArray.getJSONObject(i);
                        adList.add(ads);
                        Integer getStartA = 0;
                        Integer getEndA = 0;
                        getStartA = (Integer) ads.get("start_time");
                        getEndA = (Integer) ads.get("end_time");
                        Pair<Integer, Integer> p = new Pair<>(getStartA, getEndA);
                        hashMap.put(p, ads);

                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                }

            } else {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (int i = 0; i < adList.size(); i++) {
                Context context;
                ImageView img = new ImageView(activity);
                try {
                    new GettingImage(img).execute((String) adList.get(i).get("image_link"));
                    imageViewHashMap.put(adList.get(i), img);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }
    }

    private static class GettingImage extends AsyncTask<String, Void, Bitmap> {
        @SuppressLint("StaticFieldLeak")
        ImageView imageView;

        public GettingImage(ImageView imageView) {
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

    boolean viewInserted = false;
    ImageView img;

    RelativeLayout topContainer;

    public void updateAds(long position) {

        try {
            if (arrayList.size() > 0 && hashMap.size() > 0) {


                JSONObject jsonObject = null;
                for (Map.Entry<Pair<Integer, Integer>, JSONObject> entry : hashMap.entrySet()) //using map.entrySet() for iteration
                {

                    Pair<Integer, Integer> p = entry.getKey();
                    long startTime = (p.first) * 1000,
                            endTime = (p.second) * 1000;


                    if (startTime <= position && endTime >= position) {
                        jsonObject = entry.getValue();
                        img = imageViewHashMap.get(jsonObject);
                        break;
                    }

                }


                if (jsonObject != null) {
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(100,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    if (topContainer != null) {
                        ViewGroup rootview = (ViewGroup) activity.findViewById(android.R.id.content).getRootView();
                        rootview.removeView(topContainer);
                    }
                    topContainer = new RelativeLayout(activity);
                    img = imageViewHashMap.get(jsonObject);
                    assert img != null;
                    ViewGroup grandparent = (ViewGroup) ((View) img).getParent();
                    if (grandparent != null)
                        grandparent.removeView(img);

                    viewInserted = true;
//                    img.setBackgroundColor(Color.BLUE);

                    String s1 = (String) jsonObject.get("caption_regional_language");
                    final String s2 = (String) jsonObject.get("product_link");
                    final int[] bottom = {playerView.getBottom()};
                    int right = playerView.getRight();


                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    LinearLayout mainConatiner = new LinearLayout(activity);
                    mainConatiner.setOrientation(LinearLayout.HORIZONTAL);
                    Resources r = activity.getResources();
                    int height = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            80,
                            r.getDisplayMetrics()
                    );
//                    int width = (int) TypedValue.applyDimension(
//                            TypedValue.COMPLEX_UNIT_DIP,
//                            150,
//                            r.getDisplayMetrics()
//                    );

                    RelativeLayout.LayoutParams mainParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
                    mainParams.setMargins(0, 0, displayMetrics.widthPixels - right, displayMetrics.heightPixels - bottom[0]);

                    mainParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    mainParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                    mainConatiner.setLayoutParams(mainParams);
                    FrameLayout.LayoutParams viewGrp =
                            new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    mainConatiner.setBackgroundColor(Color.argb(80, 0, 0, 0));


                    TextView txt = new TextView(activity);
                    txt.setText(s1);
                    LinearLayout.LayoutParams textLa = new LinearLayout.LayoutParams(
                            150, ViewGroup.LayoutParams.MATCH_PARENT);
                    txt.setGravity(Gravity.CENTER_VERTICAL);
                    txt.setLayoutParams(textLa);
                    txt.setTextColor(Color.WHITE);
                    textLa.leftMargin = 10;

                    txt.setTypeface(null, Typeface.BOLD);
                    mainConatiner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(s2));
                            activity.startActivity(browserIntent);
                        }
                    });
                    mainConatiner.addView(txt);
                    mainConatiner.addView(img, imageParams);


                    ViewGroup rootview = (ViewGroup) activity.findViewById(android.R.id.content).getRootView();

                    topContainer.addView(mainConatiner);
                    rootview.addView(topContainer);

                    topContainer.setLayoutParams(viewGrp);


                }
                if (jsonObject == null && (viewInserted)) {
                    ViewGroup rootview = (ViewGroup) activity.findViewById(android.R.id.content).getRootView();
                    rootview.removeView(topContainer);

                    viewInserted = false;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    ExoPlayer exoPlayer;
    final Handler handler = new Handler();
    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateAds(exoPlayer.getCurrentPosition());
            handler.postDelayed(runnable, 1000);
        }
    };

    public void runAds(ExoPlayer exoPlayer){
        this.exoPlayer = exoPlayer;
        handler.postDelayed(runnable, 1000);
    }

}
