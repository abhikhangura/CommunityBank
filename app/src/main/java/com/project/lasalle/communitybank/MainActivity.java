package com.project.lasalle.communitybank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;

import android.os.Bundle;



import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @SuppressLint("NonConstantResourceId")
    public void init(){
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.home);
        HomeFragment fragment = new HomeFragment();
        changeFragment(fragment);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    HomeFragment homeFragment = new HomeFragment();
                    changeFragment(homeFragment);
                    return true;
                case R.id.menu:
                    MenuFragment menuFragment = new MenuFragment();
                    changeFragment(menuFragment);
                    return true;
                case R.id.transfer:
                    TransferFragment transferFragment = new TransferFragment();
                    changeFragment(transferFragment);
                    return true;
                case R.id.payee:
                    PayeeFragment payeeFragment = new PayeeFragment();
                    changeFragment(payeeFragment);
                    return true;
            }
            return false;
        });
    }

    public void changeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contentContainer,fragment);
        fragmentTransaction.commit();

    }
}