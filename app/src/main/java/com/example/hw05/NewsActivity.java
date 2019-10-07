package com.example.hw05;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        listView = findViewById(R.id.listviewNews);

        Source src = (Source) getIntent().getSerializableExtra("source");
        String url = "https://newsapi.org/v2/top-headlines?sources="+src.getId()+"&apiKey="+getString(R.string.api_key);
        if(isConnected()){
            new GetNewsAsync().execute(url);
        } else {
            Toast.makeText(NewsActivity.this, "No Active connection", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    private class GetNewsAsync extends AsyncTask<String ,Void, ArrayList<News>>{

        @Override
        protected ArrayList<News> doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            ArrayList<News> result = new ArrayList<News>();
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    String json = stringBuilder.toString();
                    JSONObject root = new JSONObject(json);
                    JSONArray articles = root.getJSONArray("articles");
                    for(int i=0;i<20;i++){
                        JSONObject articleJSON = articles.getJSONObject(i);
                        News news = new News();
                        news.author = articleJSON.getString("author");
                        news.title = articleJSON.getString("title");
                        news.urlToImage = articleJSON.getString("urlToImage");
                        news.url = articleJSON.getString("url");
                        news.publishedAt = articleJSON.getString("publishedAt");
                        result.add(news);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(NewsActivity.this);
            progressDialog.setMessage("Loading");
            progressDialog.setMax(10);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<News> news) {
            loadListView(news);
        }
    }

    private void loadListView(ArrayList<News> news){
        NewsAdapter adapter = new NewsAdapter(this,R.layout.news_item,news);
        listView.setAdapter(adapter);
        progressDialog.dismiss();

    }
}
