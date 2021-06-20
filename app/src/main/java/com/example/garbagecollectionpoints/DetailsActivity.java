package com.example.garbagecollectionpoints;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    private TextView markerName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        markerName = findViewById(R.id.markerName);

        String title = getIntent().getStringExtra("title");

        markerName.setText(title);
    }
}