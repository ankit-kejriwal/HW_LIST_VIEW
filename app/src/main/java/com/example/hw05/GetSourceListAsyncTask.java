package com.example.hw05;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetSourceListAsyncTask extends AsyncTask<String, Void, ArrayList<Source>> {

    IData iData;
    ProgressDialog progressDialog;

    public GetSourceListAsyncTask(IData iData) {
        this.iData = iData;
    }

    @Override
    protected ArrayList<Source> doInBackground(String... strings) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        ArrayList<Source> result = new ArrayList<Source>();
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
                JSONArray lists = root.getJSONArray("sources");
                for(int i=0;i<lists.length();i++){
                    JSONObject listJSON = lists.getJSONObject(i);
                    Source src = new Source();
                    src.id = listJSON.getString("id");
                    src.name = listJSON.getString("name");
                    result.add(src);
                }
            }
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
        progressDialog = new ProgressDialog((Context) iData);
        progressDialog.setMessage("Loading");
        progressDialog.setMax(10);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<Source> strings) {
        iData.handleListData(strings);
        progressDialog.dismiss();
    }

    public static interface IData{
        public void handleListData(ArrayList<Source> lists);
    }
}
