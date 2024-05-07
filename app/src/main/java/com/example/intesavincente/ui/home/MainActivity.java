package com.example.intesavincente.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.intesavincente.R;
import com.example.intesavincente.repository.partita.PartitaRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private PartitaRepository mPartitaRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NavigationFragmentContainerView);
        NavController navController = navHostFragment.getNavController();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        Toolbar toolbar = findViewById(R.id.toolbar_logo);

        ImageButton imageButtonSettings = findViewById(R.id.tastoSettings);
        imageButtonSettings.setOnClickListener(view -> {
            Log.d(TAG, "Bottone IMPOSTAZIONI premuto");
        });

        ImageButton tasto= (ImageButton) findViewById(R.id.tastoSettings);
        tasto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                // definisco l'intenzione
                Intent modificaProfilo = new Intent(MainActivity.this, ModificaProfilo.class);
                // passo all'attivazione dell'activity Pagina.java
                startActivity(modificaProfilo);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}