package com.example.garbagecollectionpoints;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class SignInActivity extends AsyncTask<String, String, String> {
    private static final String path = "http://10.0.2.2:80/";
    private Context context;

    public SignInActivity(Context context) {
        this.context = context;
    }

    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String type = (String)arg0[0];
            String
                    link = "",
                    data = "";

            if (type.equals("login")) {
                String email = (String) arg0[1];
                String password = (String) arg0[2];

                link = this.path + "login_user.php";
                data = URLEncoder.encode("email", "UTF-8") + "=" +
                        URLEncoder.encode(email, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                        URLEncoder.encode(password, "UTF-8");
            } else if (type.equals("register")) {
                String name = (String) arg0[1];
                String email = (String) arg0[2];
                String password = (String) arg0[3];

                link = this.path + "register_user.php";
                data = URLEncoder.encode("name", "UTF-8") + "=" +
                        URLEncoder.encode(name, "UTF-8");
                data += "&" + URLEncoder.encode("email", "UTF-8") + "=" +
                        URLEncoder.encode(email, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                        URLEncoder.encode(password, "UTF-8");
            }

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            return sb.toString();
        } catch(Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result){
        if (result.equals("login:success")) {
            Intent map = new Intent(this.context, MapsActivity.class);
            map.putExtra("isLogged", true);
            context.startActivity(map);
        } else if (result.equals("register:success")) {
            Intent login = new Intent(this.context, LoginActivity.class);
            context.startActivity(login);
        }

    }
}