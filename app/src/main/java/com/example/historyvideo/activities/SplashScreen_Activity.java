package com.example.historyvideo.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.historyvideo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.gray_dark));


    mAuth = FirebaseAuth.getInstance();

    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser != null && currentUser.isEmailVerified()){
                Intent intent1 = new Intent(SplashScreen_Activity.this, Home_Activity.class);
                startActivity(intent1);
            }else{
                Intent intent =new Intent(SplashScreen_Activity.this, Start_Activity.class);
                startActivity(intent);
            }
            finish();
        }
    }, 3500);


}}
