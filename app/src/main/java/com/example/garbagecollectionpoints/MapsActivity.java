package com.example.garbagecollectionpoints;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.garbagecollectionpoints.db.DBConstants;
import com.example.garbagecollectionpoints.db.DBHelper;
import com.example.garbagecollectionpoints.enums.GarbageType;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private ActivityMapsBinding binding;
    private DBHelper dbHelper;
    private boolean isLogged = false;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        isLogged = intent.getBooleanExtra("isLogged", false);
        isAdmin = intent.getBooleanExtra("isAdmin", false);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            printAllGarbage(googleMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onMapClick(LatLng latLng) {
                if (isLogged) {
                    createNewPoint(latLng);
                } else {
                    Intent i = new Intent(MapsActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                getAllInfoAboutGarbagePoint(marker);
                return false;
            }
        });
    }

    private void getAllInfoAboutGarbagePoint(Marker marker) {
        if (marker.getTitle().equals("Saratov")){
            return;
        }

        Intent intent = new Intent(MapsActivity.this, DetailsActivity.class);
        intent.putExtra("coordinate", marker.getPosition());
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNewPoint(LatLng coordinates) {
        Intent intent = new Intent(MapsActivity.this, CreateNewPointActivity.class);
        intent.putExtra("coordinates", coordinates);
        startActivity(intent);
    }

    private void printAllGarbage(GoogleMap googleMap) throws IOException {
        map = googleMap;
//        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        LatLng saratov = new LatLng(51.539482738188,46.01493146270514);
        map.addMarker(new MarkerOptions().position(saratov).title("Saratov"));
        map.moveCamera(CameraUpdateFactory.newLatLng(saratov));

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

                Marker marker = map.addMarker(markerOptions);

                if (GarbageType.COMPOST_BIN.toString().equals(type)){
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.green));
                }

                if (GarbageType.GLASS_BIN.toString().equals(type)){
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.blue));
                }

                if (GarbageType.PACKING_MATERIAL_BIN.toString().equals(type)){
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.red));
                }

                if (GarbageType.PAPER_BIN.toString().equals(type)){
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.yellow));
                }

            } while (cursor.moveToNext());
        } else {
            Log.d("mLog", "0 rows");
        }

        cursor.close();
    }
}