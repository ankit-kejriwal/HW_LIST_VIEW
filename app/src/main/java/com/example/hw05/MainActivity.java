package com.example.hw05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetSourceListAsyncTask.IData {

    ArrayList<Source> listData;
    ListView listViewSource;
    public static final int REQ_NEWS_CODE = 111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewSource = findViewById(R.id.ListVIewSource);
        if(isConnected()){
            String url = "https://newsapi.org/v2/sources?" + "apiKey="+ getString(R.string.api_key);
            new GetSourceListAsyncTask(MainActivity.this).execute(url);

        } else {
            Toast.makeText(MainActivity.this, "No Active connection", Toast.LENGTH_SHORT).show();
        }

        listViewSource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,NewsActivity.class);
                intent.putExtra("source",listData.get(i));
                startActivityForResult(intent,REQ_NEWS_CODE);
            }
        });
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

    @Override
    public void handleListData(ArrayList<Source> lists) {
        this.listData = lists;
        ArrayAdapter<Source> adapter = new ArrayAdapter<Source>(this,android.R.layout.simple_list_item_1
        ,android.R.id.text1,this.listData);

        listViewSource.setAdapter(adapter);

    }
}
