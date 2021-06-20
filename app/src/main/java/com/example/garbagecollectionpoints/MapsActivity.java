package com.example.garbagecollectionpoints;

import androidx.fragment.app.FragmentActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

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
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        LatLng sydney = new LatLng(51.539482738188,46.01493146270514);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Saratov"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions newPoint = new MarkerOptions().position(latLng).title("name");

                String latitude = Double.toString(newPoint.getPosition().latitude);
                String longitude = Double.toString(newPoint.getPosition().longitude);
                String date = LocalDate.now().toString();
                String id = latitude + longitude;

                SQLiteDatabase database = dbHelper.getWritableDatabase();
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
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String markerTitle = marker.getTitle();
                Intent i = new Intent(MapsActivity.this, DetailsActivity.class);
                i.putExtra("title", markerTitle);
                startActivity(i);

                return false;
            }
        });
    }
}