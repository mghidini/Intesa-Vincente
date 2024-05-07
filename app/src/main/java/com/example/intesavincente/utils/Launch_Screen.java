package com.example.intesavincente.utils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intesavincente.R;
import com.example.intesavincente.ui.authentication.AuthenticationActivity;
import com.example.intesavincente.ui.home.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Launch_Screen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    //private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launch_screen);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Handler handler  = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser != null) {

                    Intent i = new Intent(Launch_Screen.this, MainActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    Intent j = new Intent(Launch_Screen.this, AuthenticationActivity.class);
                    startActivity(j);
                    finish();
                }
           }
        }, 2000);
    }
}
