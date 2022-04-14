package com.project.lasalle.communitybank;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Pair;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4000;

    Animation topAnim,bottomAnim;
    ImageView imgLogo;
    TextView txtAppName,txtAppDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_login);
        setContentView(R.layout.activity_splash_screen);
        init();
    }

    public void init(){
        imgLogo = findViewById(R.id.imageLogo);
        txtAppName = findViewById(R.id.txtTitleSplash);
        txtAppDesc = findViewById(R.id.txtDescSplash);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        imgLogo.setAnimation(topAnim);
        txtAppName.setAnimation(bottomAnim);
        txtAppDesc.setAnimation(bottomAnim);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this,LoginActivity.class);

            Pair[] pair = new Pair[3];
            pair[0] = new Pair(imgLogo,"imageTrans");
            pair[1] = new Pair(txtAppName,"txtTrans");
            pair[2] = new Pair(txtAppDesc,"txtDescTrans");

            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this, pair);
            startActivity(intent,activityOptions.toBundle());
            finish();
        },SPLASH_SCREEN);
    }
}