package com.example.maxtap.utils;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReadJson extends AsyncTask<Void, Void, Void> {

    ArrayList<JSONObject> object;
    String content_id;

    public ReadJson(ArrayList<JSONObject> object, String content_id) {

        this.object = object;
        this.content_id = content_id;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();
        // Making a request to url and getting response
        String url;
        if (!this.content_id.contains(".json")) {
            url = "https://storage.googleapis.com/maxtap-adserver-dev.appspot.com/" + this.content_id + ".json";
        } else {
            url = "https://storage.googleapis.com/maxtap-adserver-dev.appspot.com/" + this.content_id;
        }

        String jsonStr = sh.makeServiceCall(url);

        if (jsonStr != null) {
            try {
                JSONArray jsonArr = new JSONArray(jsonStr);
                List<JSONObject> jsonList = new ArrayList<>();
                JSONArray sortedJsonArray = new JSONArray();
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
                            e.printStackTrace();
                        }

                        return valA.compareTo(valB);
                    }
                });
                for (int i = 0; i < jsonArr.length(); i++) {
                    sortedJsonArray.put(jsonList.get(i));
                }

                for (int i = 0; i < sortedJsonArray.length(); i++) {

                    JSONObject ads = sortedJsonArray.getJSONObject(i);
                    object.add(ads);

                }
            } catch (final JSONException e) {
                e.printStackTrace();
            }

        } else {
            // * Handle no data error
        }

        return null;
    }

}