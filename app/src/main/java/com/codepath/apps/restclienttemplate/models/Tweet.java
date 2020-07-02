package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel

public class Tweet {
    public long id;
    public String text;
    public String mediaUrl;
    public String timeStamp;
    public User user;
    public long userId;
    public boolean retweeted;
    public boolean favorited;

    public Tweet() {};

    public static Tweet fromJson(JSONObject data) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.text = data.getString("text");
        tweet.timeStamp = getRelativeTimeAgo(data.getString("created_at"));
        tweet.id = data.getLong("id");
        User user = User.fromJson(data.getJSONObject("user"));
        tweet.user = user;
        tweet.userId = user.id;
        tweet.mediaUrl = getMedia(data.getJSONObject("entities"));

        return tweet;
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_ALL).toString();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String [] time = relativeDate.split(" ");
        relativeDate = time[0] + time[1].charAt(0);

        return relativeDate;
    }

    public static String getMedia(JSONObject json) {
        try {
            JSONArray mediaObjects = json.getJSONArray("media");
            JSONObject firstMedia = mediaObjects.getJSONObject(0);
            String url = firstMedia.getString("media_url_https");
            return url;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Tweet> fromJsonArray(JSONArray data) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            Tweet tweet = fromJson((JSONObject) data.get(i));
            tweets.add(tweet);
        }

        return tweets;
    }
}
