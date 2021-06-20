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
    private DBHelper dbHelper;
    private static final String DATE_PATTERN = "dd-MM-yyyy";
    private Button btnDelete;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        garbagePoint = new GarbagePoint();

        dbHelper = new DBHelper(this);
        nameView = findViewById(R.id.name);
        typeView = findViewById(R.id.type);
        descriptionView = findViewById(R.id.description);
        dateView = findViewById(R.id.date);
        addressView = findViewById(R.id.address);

        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);

        LatLng coordinates = getIntent().getParcelableExtra("coordinate");
        String latitude = Double.toString(coordinates.latitude);
        String longitude = Double.toString(coordinates.longitude);
        String pointId = (latitude + longitude).replaceAll("\\.", "");
        garbagePoint.setId(pointId);

        getPointById();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

        String date = garbagePoint.getDate().format(formatter) + " "
                + garbagePoint.getDate().getHour() + " часов "
                + garbagePoint.getDate().getMinute() + " минут";

        String address = getAddress();

        nameView.setText(garbagePoint.getName());
        typeView.setText(garbagePoint.getType().getText());
        descriptionView.setText(garbagePoint.getDescription());
        dateView.setText(date);
        addressView.setText(address);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getPointById(){
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        Cursor cursor =  database.rawQuery(
                "select * from " + DBConstants.TABLE_POINTS.getName() +
                        " where " + DBConstants.KEY_ID.getName() + "=" + garbagePoint.getId()  , null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndex(DBConstants.KEY_NAME.getName()));
                String latitude = cursor.getString(cursor.getColumnIndex(DBConstants.KEY_LATITUDE.getName()));
                String longitude = cursor.getString(cursor.getColumnIndex(DBConstants.KEY_LONGITUDE.getName()));
                GarbageType type = GarbageType.valueOf(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_TYPE.getName())));
                String description = cursor.getString(cursor.getColumnIndex(DBConstants.KEY_DESCRIPTION.getName()));

                LocalDateTime date = LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(DBConstants.KEY_DATE.getName())));

                garbagePoint.setName(name);
                garbagePoint.setType(type);
                garbagePoint.setDescription(description);
                garbagePoint.setLongitude(longitude);
                garbagePoint.setLatitude(latitude);
                garbagePoint.setDate(date);

            }
            cursor.close();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDelete:
                deletePoint();
                Intent intent = new Intent(DetailsActivity.this, MapsActivity.class);
                startActivity(intent);
                break;
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
        //logic for delete from db
    }
}