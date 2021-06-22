package com.example.garbagecollectionpoints;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.garbagecollectionpoints.db.DBConstants;
import com.example.garbagecollectionpoints.db.DBHelper;
import com.example.garbagecollectionpoints.dto.GarbagePoint;
import com.example.garbagecollectionpoints.enums.GarbageType;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener{
    private GarbagePoint garbagePoint;
    private TextView nameView, typeView, descriptionView, dateView, addressView;
    private static final String DATE_PATTERN = "dd-MM-yyyy";
    private Button btnDelete;
    private boolean isBinLoaded = false;
    private String bin;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        garbagePoint = new GarbagePoint();

        //Intent intent = getIntent();
        /*isBinLoaded = intent.getBooleanExtra("isBinLoaded", false);

        if (!isBinLoaded) {
            LatLng coordinates = intent.getParcelableExtra("coordinate");
            String latitude = Double.toString(coordinates.latitude);
            String longitude = Double.toString(coordinates.longitude);
            String pointId = (latitude + longitude).replaceAll("\\.", "");
            garbagePoint.setId(pointId);
            new PHPExecuteActivity(this).execute(
                    "getBinById",
                    garbagePoint.getId()
            );
            return;
        }*/

        getPointById();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getPointById() {
        Intent intent = getIntent();
        bin = intent.getStringExtra("bin");

        String[] str = bin.split("\\*");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

        garbagePoint.setDate(LocalDateTime.parse(str[7]));
        garbagePoint.setDescription(str[6]);
        garbagePoint.setType(GarbageType.valueOf(str[5]));
        garbagePoint.setLatitude(str[3]);
        garbagePoint.setLongitude(str[4]);
        garbagePoint.setName(str[2]);
        garbagePoint.setId(str[1]);

        String date = garbagePoint.getDate().format(formatter) + " "
                + garbagePoint.getDate().getHour() + " часов "
                + garbagePoint.getDate().getMinute() + " минут";

        String address = getAddress();

        setContentView(R.layout.activity_details);
        nameView = findViewById(R.id.name);
        typeView = findViewById(R.id.type);
        descriptionView = findViewById(R.id.description);
        dateView = findViewById(R.id.date);
        addressView = findViewById(R.id.address);

        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);

        nameView.setText(garbagePoint.getName());
        typeView.setText(garbagePoint.getType().getText());
        descriptionView.setText(garbagePoint.getDescription());
        dateView.setText(date);
        addressView.setText(address);
        System.out.println(garbagePoint.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDelete:
                if (PHPExecuteActivity.getUSER().isAdmin()){
                    deletePoint();
                }
        }
    }

    private String getAddress(){
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        try {
            double longitude = Double.parseDouble(garbagePoint.getLongitude());
            double latitude = Double.parseDouble(garbagePoint.getLatitude());
            addresses = gcd.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0) {
            return  addresses.get(0).getAddressLine(0);
        }
        else {
            return "не установленный адрес";
        }
    }

    private void deletePoint() {
        new PHPExecuteActivity(this).execute(
                "deleteBin",
                garbagePoint.getId()
        );
    }
}