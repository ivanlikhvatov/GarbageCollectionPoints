package com.example.garbagecollectionpoints;

import androidx.fragment.app.FragmentActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.garbagecollectionpoints.db.DBConstants;
import com.example.garbagecollectionpoints.db.DBHelper;
import com.example.garbagecollectionpoints.enums.GarbageTypes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.garbagecollectionpoints.databinding.ActivityMapsBinding;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        printAllGarbage(googleMap);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onMapClick(LatLng latLng) {
               createNewPoint(latLng);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                getAllInfoAboutGarbagePoint(marker);
                return false;
            }
        });
    }

    private void getAllInfoAboutGarbagePoint(Marker marker) {
        String markerTitle = marker.getTitle();
        Intent i = new Intent(MapsActivity.this, DetailsActivity.class);
        i.putExtra("title", markerTitle);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNewPoint(LatLng latLng) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        MarkerOptions newPoint = new MarkerOptions().position(latLng).title("name");

        String latitude = Double.toString(newPoint.getPosition().latitude);
        String longitude = Double.toString(newPoint.getPosition().longitude);
        String date = LocalDate.now().toString();
        String id = (latitude + longitude).replaceAll("\\.", "");

        ContentValues contentValues = new ContentValues();

        contentValues.put(DBConstants.KEY_ID.getName(), id);
        contentValues.put(DBConstants.KEY_NAME.getName(), "name");
        contentValues.put(DBConstants.KEY_LATITUDE.getName(), latitude);
        contentValues.put(DBConstants.KEY_LONGITUDE.getName(), longitude);
        contentValues.put(DBConstants.KEY_DESCRIPTION.getName(), "bla-bla-bla");
        contentValues.put(DBConstants.KEY_DATE.getName(), date);
        contentValues.put(DBConstants.KEY_TYPE.getName(), GarbageTypes.GLASS_BIN.toString());

        database.insert(DBConstants.TABLE_POINTS.getName(), null, contentValues);

        Marker marker = mMap.addMarker(newPoint);
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.blue));
    }

    private void printAllGarbage(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        LatLng sydney = new LatLng(51.539482738188,46.01493146270514);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Saratov"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        SQLiteDatabase database = dbHelper.getWritableDatabase();


        Cursor cursor = database.query(
                DBConstants.TABLE_POINTS.getName(),
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBConstants.KEY_NAME.getName());
            int latitudeIndex = cursor.getColumnIndex(DBConstants.KEY_LATITUDE.getName());
            int longitudeIndex = cursor.getColumnIndex(DBConstants.KEY_LONGITUDE.getName());
            int typeIndex = cursor.getColumnIndex(DBConstants.KEY_TYPE.getName());

            do {
                double latitude = Double.parseDouble(cursor.getString(latitudeIndex));
                double longitude = Double.parseDouble(cursor.getString(longitudeIndex));
                String name = cursor.getString(nameIndex);
                String type = cursor.getString(typeIndex);

                LatLng coordinates = new LatLng(latitude, longitude);
                MarkerOptions markerOptions = new MarkerOptions().position(coordinates).title(name);

                Marker marker = mMap.addMarker(markerOptions);

                if (GarbageTypes.COMPOST_BIN.toString().equals(type)){
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.green));
                }

                if (GarbageTypes.GLASS_BIN.toString().equals(type)){
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.blue));
                }

                if (GarbageTypes.PACKING_MATERIAL_BIN.toString().equals(type)){
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.red));
                }

                if (GarbageTypes.PAPER_BIN.toString().equals(type)){
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.yellow));
                }

            } while (cursor.moveToNext());
        } else {
            Log.d("mLog", "0 rows");
        }

        //
        cursor.close();
    }
}