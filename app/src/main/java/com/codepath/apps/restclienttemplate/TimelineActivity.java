package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.restclienttemplate.adapters.TweetsAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    TwitterClient client;
    RecyclerView timelineRv;
    TweetsAdapter adapter;
    List<Tweet> tweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        getSupportActionBar().setTitle("Timeline");

        // initialize tweets so we don't pass null into adapter
        tweets = new ArrayList<>();

        // set up adapter for recyclerview
        timelineRv = findViewById(R.id.timeline);
        adapter = new TweetsAdapter(tweets, getApplicationContext());
        timelineRv.setAdapter(adapter);
        timelineRv.setLayoutManager(new LinearLayoutManager(this));

        // create an instance of the current Twitter client
        client = TwitterApp.getRestClient(this);
        displayHomeTimeline(client);
    }

    private void displayHomeTimeline(TwitterClient client) {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray data = json.jsonArray;
                try {
                    tweets.addAll(Tweet.fromJsonArray(data));
                    // when data is parsed, notify the adapter to update view
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response,
                                  Throwable throwable) {
                Log.e("TimelineActivity", "Error fetching home timeline!" + response);
            }
        });
    }
}