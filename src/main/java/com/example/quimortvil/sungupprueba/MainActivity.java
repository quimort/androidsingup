package com.example.quimortvil.sungupprueba;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText etname,etemail,etpassword;
    public static JSONObject json;
    public  JSONObject intAux ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        traerreferencias();
        keylisener();
    }
    private void traerreferencias(){
        etname=(EditText) findViewById(R.id.txtUserName);
        etemail=(EditText) findViewById(R.id.txtEmail);
        etpassword=(EditText) findViewById(R.id.txtPass);
    }

    public void singupuser(View view) throws JSONException {
        String nombre=etname.getText().toString();
        String email = etemail.getText().toString();
        String password = etpassword.getText().toString();
        json =stringsTojson(nombre,password,email);
        String urls="http://172.17.129.113:8080/users_manager/v1/register";
        new RetrieveFeedTask().execute(urls);
        cleantext();
    }
    private void keylisener(){
        etname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                validatenametext();
                validateemail();
                validatepassword();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatenametext();
                validateemail();
                validatepassword();
            }

            @Override
            public void afterTextChanged(Editable s) {
                validatenametext();
                validateemail();
                validatepassword();
            }
        });
    }
    private void cleantext(){
        etname.setText("");
        etemail.setText("");
        etpassword.setText("");
    }
    private void validatenametext(){
        String name = etname.getText().toString();
        if(name.length()<6){

        }
    }
    private void validateemail(){
        String email= etemail.getText().toString();
    }
    private void validatepassword(){
        String pass=etpassword.getText().toString();
    }
    private JSONObject stringsTojson(String login, String password,String email) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name", login);
        json.put("email",email);
        json.put("Password", password);
        return json;
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {
        public  String aux = "";

        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection)
                        url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.addRequestProperty("Content-Type","application/json");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new
                        OutputStreamWriter(os, "UTF-8"));
                writer.write(MainActivity.json.toString());
                System.out.println(MainActivity.json.toString());
                writer.close();
                os.close();
                conn.connect();
                //int response = conn.getResponseCode();
                InputStream is = conn.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();
                String line;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }
                aux= sBuilder.toString();
                //aux = convertStreamToString(is);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "success";
        }

        protected void onPostExecute(String urls) {
            if (!aux.equals("")) {
                try {
                    JSONObject json = new JSONObject(aux);
                    intAux = json;
                    System.out.println(intAux);
                    getallthecvaluesfromjson();
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        }
        private void getallthecvaluesfromjson(){
            JSONObject arrst = new JSONObject();
            try {
                arrst= intAux.getJSONObject("respuesta");
                String api_key=arrst.getString("apiyek");
                System.out.println(api_key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
    }
}
