package com.example.intesavincente.ui.partita;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.intesavincente.ui.home.MainActivity;
import com.example.intesavincente.utils.Launch_Screen;
import com.example.intesavincente.utils.MyApplication;
import com.example.intesavincente.R;
import com.example.intesavincente.model.Partita;
import com.example.intesavincente.repository.partita.PartitaRepository;
import com.example.intesavincente.repository.partita.PartitaResponse;
import com.example.intesavincente.repository.words.IWordsRepository;
import com.example.intesavincente.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SuggeritoreActivity extends AppCompatActivity implements PartitaResponse {

    private static final String TAG ="Suggeritore";

    private PartitaRepository mPartitaRepository;
    SharedPreferences prefSuggeritore = MyApplication.getAppContext().getSharedPreferences("MyPrefSuggeritore", MODE_PRIVATE);
    SharedPreferences.Editor editorSuggeritore = prefSuggeritore.edit();
    private MediaPlayer soundCambiaParola;
    private MediaPlayer soundGongTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggeritore);
        System.out.println("prova1");

        mPartitaRepository = new PartitaRepository(this.getApplication(), this);
        mPartitaRepository.trovaPartita();
        
        soundCambiaParola = MediaPlayer.create(this, R.raw.cambio_parola);
        soundGongTimer = MediaPlayer.create(this, R.raw.gong_timer);
    }

    @Override
    public void onDataFound(Partita partita) {
        Log.d(TAG, "idPartita12 " + partita.getIdPartita());
        TextView parolaDaIndovinare=findViewById(R.id.parolaDaIndovinare);

        DatabaseReference db = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("partite");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> keys = new ArrayList<>();

                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Log.d(TAG, "idPartita Partite " + keyNode.child("idPartita").getValue());
                    Log.d(TAG, "id partita passata " + partita.getIdPartita());
                    if (keyNode.child("idPartita").getValue().equals(partita.getIdPartita())){
                        if(keyNode.child("attiva").getValue(Boolean.class)){
                            String par= keyNode.child("parola").getValue().toString();
                            Log.d(TAG, "If attiva");
                            Log.d(TAG, "partita123 " + keyNode.child("idPartita").getValue());
                            parolaDaIndovinare.setText(par);
                        }
                        else {
                            Log.d(TAG, "Else non attiva");
                            Intent io = new Intent(SuggeritoreActivity.this, MainActivity.class);
                            startActivity(io);
                        }
                    }

                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}