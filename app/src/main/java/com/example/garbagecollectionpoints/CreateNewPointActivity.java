package com.example.garbagecollectionpoints;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.garbagecollectionpoints.db.DBConstants;
import com.example.garbagecollectionpoints.db.DBHelper;
import com.example.garbagecollectionpoints.dto.GarbagePoint;
import com.example.garbagecollectionpoints.enums.GarbageType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CreateNewPointActivity extends AppCompatActivity implements View.OnClickListener {
    private GarbagePoint garbagePoint;
    private DBHelper dbHelper;
    private Button btnAdd;
    private EditText nameInput, description;
    private Spinner spinner;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_point);
        dbHelper = new DBHelper(this);
        garbagePoint = new GarbagePoint();

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        nameInput = findViewById(R.id.markerName);
        description = findViewById(R.id.description);
        spinner = findViewById(R.id.spinner);

        LatLng coordinates = getIntent().getParcelableExtra("coordinates");
        MarkerOptions markerOptions = new MarkerOptions().position(coordinates);

        String latitude = Double.toString(markerOptions.getPosition().latitude);
        String longitude = Double.toString(markerOptions.getPosition().longitude);
        LocalDateTime date = LocalDateTime.now();
        String id = (latitude + longitude).replaceAll("\\.", "");

        garbagePoint.setLatitude(latitude);
        garbagePoint.setLongitude(longitude);
        garbagePoint.setDate(date);
        garbagePoint.setId(id);
    }

    private void savePoint() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBConstants.KEY_ID.getName(), garbagePoint.getId());
        contentValues.put(DBConstants.KEY_NAME.getName(), garbagePoint.getName());
        contentValues.put(DBConstants.KEY_LATITUDE.getName(), garbagePoint.getLatitude());
        contentValues.put(DBConstants.KEY_LONGITUDE.getName(), garbagePoint.getLongitude());
        contentValues.put(DBConstants.KEY_DESCRIPTION.getName(), garbagePoint.getDescription());
        contentValues.put(DBConstants.KEY_DATE.getName(), garbagePoint.getDate().toString());
        contentValues.put(DBConstants.KEY_TYPE.getName(), garbagePoint.getType().toString());

        database.insert(DBConstants.TABLE_POINTS.getName(), null, contentValues);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                String selectedType = spinner.getSelectedItem().toString();
                GarbageType enumType = GarbageType.getEnumByText(selectedType);

                garbagePoint.setName(nameInput.getText().toString());
                garbagePoint.setType(enumType);
                garbagePoint.setDescription(description.getText().toString());

                savePoint();
                Intent intent = new Intent(CreateNewPointActivity.this, MapsActivity.class);
                startActivity(intent);
                break;
        }

    }
}