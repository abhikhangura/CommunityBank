package com.project.lasalle.communitybank;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class ContactActivity extends AppCompatActivity {

    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        init();
    }

    public void init(){
        imgBack = findViewById(R.id.imgBackContact);

        MapFragment mapFragment = new MapFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,mapFragment).commit();
        imgBack.setOnClickListener(view -> {
            finish();
        });
    }
}