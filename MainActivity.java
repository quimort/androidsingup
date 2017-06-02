package com.example.quimortvil.go_to_point;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getpointdata();
    }
    private void getpointdata(){
        String url = "http://172.17.129.113:8080/users_manager/v1/point/1";
        GetRutes getrutes = new GetRutes();
        getrutes.execute(url);
    }
    public void gotogooglemaps(JSONObject json) throws JSONException {
        double longitude=json.getDouble("pointlongitude");
        double latitude = json.getDouble("pointlatitude");
        String output= longitude+","+latitude;
        String url = "https://www.google.com/maps/search/?api=1&query="+output;
        Uri gmmIntentUri = Uri.parse(url);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
    class GetRutes extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            String data = "";
            InputStream istream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                istream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(istream));
                StringBuffer sb = new StringBuffer();
                String line ="";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                data = sb.toString();
                br.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray arr = new JSONArray(s);
                JSONObject json=null;
                for(int i =0; i<arr.length();i++){
                    json= arr.getJSONObject(i);
                }
                gotogooglemaps(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

