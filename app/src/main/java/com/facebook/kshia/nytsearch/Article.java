package com.facebook.kshia.nytsearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kshia on 6/20/16.
 */
public class Article implements Serializable {

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    String webUrl;
    String headline;
    String thumbNail;
    final int TOP_STORY = 0;
    final int NORMAL = 1;

    public Article(JSONObject jsonObject, int type) {
        try {

            if(type == TOP_STORY) {
                this.webUrl = jsonObject.getString("url");
                this.headline = jsonObject.getString("title");
            }
            else {
                this.webUrl = jsonObject.getString("web_url");
                this.headline = jsonObject.getJSONObject("headline").getString("main");
            }

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if (multimedia.length() > 0) {
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                if (type == NORMAL) {
                    this.thumbNail = "http://www.nytimes.com/" + multimediaJson.getString("url");
                }
                else {
                    this.thumbNail = multimediaJson.getString("url");
                }
            } else {
                this.thumbNail = "";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array, int type) {
        ArrayList<Article> results = new ArrayList<>();

        for (int x = 0; x < array.length(); x++) {
            try {
                results.add(new Article(array.getJSONObject(x), type));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }
}
