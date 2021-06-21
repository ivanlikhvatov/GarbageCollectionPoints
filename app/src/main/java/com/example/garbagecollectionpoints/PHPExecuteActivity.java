package com.example.garbagecollectionpoints;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.garbagecollectionpoints.db.DBConstants;
import com.example.garbagecollectionpoints.dto.GarbagePoint;
import com.example.garbagecollectionpoints.enums.GarbageType;

public class PHPExecuteActivity extends AsyncTask<String, String, String> {
    private static final String path = "http://10.0.2.2:80/";
    private Context context;

    public PHPExecuteActivity(Context context) {
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
            } else if (type.equals("createBin")) {
                String name = (String) arg0[1];
                String latitude = (String) arg0[2];
                String longtitude = (String) arg0[3];
                String binType = (String) arg0[4];
                String desc = (String) arg0[5];
                String date = (String) arg0[6];

                link = this.path + "bin_create.php";
                data = URLEncoder.encode("name", "UTF-8") + "=" +
                        URLEncoder.encode(name, "UTF-8");
                data += "&" + URLEncoder.encode("latitude", "UTF-8") + "=" +
                        URLEncoder.encode(latitude, "UTF-8");
                data += "&" + URLEncoder.encode("longtitude", "UTF-8") + "=" +
                        URLEncoder.encode(longtitude, "UTF-8");
                data += "&" + URLEncoder.encode("binType", "UTF-8") + "=" +
                        URLEncoder.encode(binType, "UTF-8");
                data += "&" + URLEncoder.encode("desc", "UTF-8") + "=" +
                        URLEncoder.encode(desc, "UTF-8");
                data += "&" + URLEncoder.encode("date", "UTF-8") + "=" +
                        URLEncoder.encode(date, "UTF-8");
            } else if (type.equals("getAllBins")) {
                link = this.path + "get_bins.php";
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPostExecute(String result) {
        System.out.println(result);

        if (result.contains("login:success")) {
            Intent map = new Intent(this.context, MapsActivity.class);
            map.putExtra("isLogged", true);

            for (String str : result.split(" ")) {
                if (str.equals("admin")) {
                    map.putExtra("isAdmin", true);
                    break;
                }
            }

            context.startActivity(map);
        } else if (result.equals("register:success")) {
            Intent login = new Intent(this.context, LoginActivity.class);
            context.startActivity(login);
        } else if (result.contains("getBins:success")) {
            Intent map = new Intent(this.context, MapsActivity.class);

            ArrayList<GarbagePoint> gb = new ArrayList<>();

            for (String str : result.split("|")) {
                GarbagePoint g = new GarbagePoint();
                int i = 0;
                for (String st : str.split(" ")) {
                    System.out.println(st);
                    switch (i) {
                        case 0: {
                            g.setId(st);
                        }

                        case 1: {
                            g.setName(st);
                        }

                        case 2: {
                            g.setLatitude(st);
                        }

                        case 3: {
                            g.setLongitude(st);
                        }

                        case 4: {
                            g.setType(GarbageType.getEnumByText(st));
                        }

                        case 5: {
                            g.setDescription(st);
                        }

                        case 6: {
                            g.setDate(LocalDateTime.parse(st));
                        }
                    }

                    i++;
                    gb.add(g);
                }
            }

            map.putExtra("gb", gb);
            map.putExtra("isBinsLoaded", true);
            context.startActivity(map);
        }

    }
}