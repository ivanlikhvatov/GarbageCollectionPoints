package com.example.garbagecollectionpoints;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.garbagecollectionpoints.db.DBConstants;
import com.example.garbagecollectionpoints.db.DBHelper;
import com.example.garbagecollectionpoints.dto.GarbagePoint;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener{
    private GoogleMap map;
    private ActivityMapsBinding binding;
    private DBHelper dbHelper;
    private Button btnFind, createButton;
    private EditText etPlace;
    private Marker findPlace;
    private boolean isLogged = false;
    private boolean isAdmin = false;
    private boolean isBinsLoaded = false;
    private ArrayList<String> gb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        isLogged = PHPExecuteActivity.getUSER().isLogged();
        isAdmin = PHPExecuteActivity.getUSER().isAdmin();
        gb = intent.getStringArrayListExtra("binsArray");
        isBinsLoaded = intent.getBooleanExtra("isBinsLoaded", false);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //dbHelper = new DBHelper(this);

        btnFind = findViewById(R.id.btnFind);
        btnFind.setOnClickListener(this);

        etPlace = findViewById(R.id.et_place);
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onMarkerClick(Marker marker) {
                getAllInfoAboutGarbagePoint(marker);
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getAllInfoAboutGarbagePoint(Marker marker) {
        if (marker.getTitle().equals("Найденное место")){
            if (isLogged) {
                createNewPoint(marker.getPosition());
            } else {
                Intent i = new Intent(MapsActivity.this, LoginActivity.class);
                startActivity(i);
            }

            return;
        }

        //Intent intent = new Intent(MapsActivity.this, DetailsActivity.class);
        //intent.putExtra("coordinate", marker.getPosition());
        //startActivity(intent);
        String latitude = Double.toString(marker.getPosition().latitude);
        String longitude = Double.toString(marker.getPosition().longitude);
        String pointId = (latitude + longitude).replaceAll("\\.", "");

        new PHPExecuteActivity(this).execute(
                "getBinById",
                pointId
        );
        return;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNewPoint(LatLng coordinates) {
        Intent intent = new Intent(MapsActivity.this, CreateNewPointActivity.class);
        intent.putExtra("coordinates", coordinates);
        startActivity(intent);
    }

    private void printAllGarbage(GoogleMap googleMap) throws IOException {
        map = googleMap;

        if (!isBinsLoaded) {
            new PHPExecuteActivity(this).execute("getAllBins");
            return;
        }

        for (String g : gb) {
            System.out.println(g);
            String p = "\\*";
            String[] str = g.split(p);
            LatLng coordinates = new LatLng(Double.parseDouble(str[2]), Double.parseDouble(str[3]));
            MarkerOptions markerOptions = new MarkerOptions().position(coordinates).title(str[1]);

            Marker marker = map.addMarker(markerOptions);

            if (GarbageType.COMPOST_BIN.equals(GarbageType.valueOf(str[4]))) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.green));
            }

            if (GarbageType.GLASS_BIN.equals(GarbageType.valueOf(str[4]))) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.blue));
            }

            if (GarbageType.PACKING_MATERIAL_BIN.equals(GarbageType.valueOf(str[4]))) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.red));
            }

            if (GarbageType.PAPER_BIN.equals(GarbageType.valueOf(str[4]))) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.yellow));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFind:
                Geocoder geocoder = new Geocoder(this);
                List<Address> addresses1;
                try {
                    addresses1 = geocoder.getFromLocationName(etPlace.getText().toString(), 1);

                    if(addresses1.size() > 0) {
                        if (findPlace != null){
                            findPlace.remove();
                        }

                        double latitude = addresses1.get(0).getLatitude();
                        double longitude = addresses1.get(0).getLongitude();

                        LatLng coordinates = new LatLng(latitude,longitude);
                        findPlace = map.addMarker(new MarkerOptions().position(coordinates).title("Найденное место"));
                        map.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
        }
    }
}