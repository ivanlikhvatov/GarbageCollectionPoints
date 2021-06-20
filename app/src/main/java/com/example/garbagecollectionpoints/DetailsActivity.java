package com.example.garbagecollectionpoints;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener{
    private GarbagePoint garbagePoint;
    private TextView nameView, typeView, descriptionView, dateView;
    private DBHelper dbHelper;
    private static final String DATE_PATTERN = "dd-MM-yyyy";
    private Button btnDelete;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        dbHelper = new DBHelper(this);
        nameView = findViewById(R.id.name);
        typeView = findViewById(R.id.type);
        descriptionView = findViewById(R.id.description);
        dateView = findViewById(R.id.date);

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

        nameView.setText(garbagePoint.getName());
        typeView.setText(garbagePoint.getType().getText());
        descriptionView.setText(garbagePoint.getDescription());

        dateView.setText(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private GarbagePoint getPointById(){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        GarbagePoint garbagePoint = new GarbagePoint();

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
        return garbagePoint;

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

    private void deletePoint() {
        //logic for delete from db
    }
}