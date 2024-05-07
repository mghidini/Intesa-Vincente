package com.example.intesavincente.ui.partita;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.intesavincente.ui.home.MainActivity;
import com.example.intesavincente.R;
import com.example.intesavincente.repository.partita.PartitaRepository;
import com.example.intesavincente.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityScegliRuolo extends AppCompatActivity {

    private static final String TAG = "ActivityScegliRuolo";

    private DatabaseReference dbPartite;
    private DatabaseReference dbUtenti;
    private DatabaseReference dbGruppi;

    private Button avantiButton;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private RadioButton r;
    private boolean isBackground = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scegli_ruolo);

        avantiButton = findViewById(R.id.avantiButton);
        radioGroup = findViewById(R.id.radioGroupScelta);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                Log.d(TAG, "ID radio button selezionato " + radioButtonID);
                radioButton = findViewById(radioButtonID);

                int idx = radioGroup.indexOfChild(radioButton);
                Log.d(TAG, "idx " + idx);

                r = (RadioButton) radioGroup.getChildAt(idx);
                Log.d(TAG, "Selezionato" + r);

                String selectedText = r.getText().toString();

                avantiButton.setOnClickListener(view -> {
                    if (selectedText.equals("SUGGERITORE")) {
                        partitaEsiste();
                        Intent iSuggeritore = new Intent(ActivityScegliRuolo.this, SuggeritoreActivity.class);
                        startActivity(iSuggeritore);
                    } else if (selectedText.equals("INDOVINATORE")) {
                        partitaEsiste();
                        Intent iIndovinatore = new Intent(ActivityScegliRuolo.this, Indovinatore.class);
                        startActivity(iIndovinatore);
                    }
                });
            }
        });
    }

    public void finish() {
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Home button pressed78");
        //PartitaRepository p = new PartitaRepository();
        //p.cancellaDatiButton();

        super.onDestroy();
        Log.d(TAG, "Home button pressed789");
        //finish();
    }

    @Override
    public void onBackPressed() {
        PartitaRepository p = new PartitaRepository();
        p.cancellaDatiButton();
        startActivity(new Intent(ActivityScegliRuolo.this, MainActivity.class));
        finish();
    }

    public void partitaEsiste() {

        dbUtenti = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("utenti");
        dbUtenti.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String partiteUtente = null;
                List<String> keysUtenti = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    keysUtenti.add(keyNode.getKey());
                    if (keyNode.child("idUtente").getValue(String.class).equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        for (int i = 0; i < 100; i++) {
                            if (keyNode.child("partite").child(String.valueOf(i)).exists()) {
                                partiteUtente=keyNode.child("partite").child(String.valueOf(i)).getValue(String.class);
                            }
                        }

                    }
                }
                Log.d(TAG, "IDpartitaUtente " + partiteUtente);
                dbPartite = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("partite");
                String finalPartiteUtente = partiteUtente;
                dbPartite.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d(TAG, "IDpartitaUtente1 " + finalPartiteUtente);
                        boolean flag = true;
                        List<String> keysPartite = new ArrayList<>();
                        for (DataSnapshot keyNode : snapshot.getChildren()) {
                            keysPartite.add(keyNode.getKey());
                            Log.d(TAG, "ifcondizione " + keyNode.child("idPartita").getValue(String.class).equals(finalPartiteUtente));
                            if (keyNode.child("idPartita").getValue(String.class).equals(finalPartiteUtente)) {
                                flag = false;
                            }
                        }
                        if (flag) {
                            dbUtenti.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ArrayList<String> partiteUtente1= new ArrayList<String>();
                                    List<String> keysUtenti = new ArrayList<>();
                                    for (DataSnapshot keyNode : snapshot.getChildren()) {
                                        keysUtenti.add(keyNode.getKey());
                                        if (keyNode.child("idUtente").getValue(String.class).equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                            for (int i = 0; i < 100; i++) {
                                                if (keyNode.child("partite").child(String.valueOf(i)).exists()) {
                                                    partiteUtente1.add(keyNode.child("partite").child(String.valueOf(i)).getValue(String.class));
                                                }
                                            }
                                            Log.d(TAG, "codice partita" + keyNode.child("partite").child(String.valueOf(partiteUtente1.size()-1)));
                                            if(keyNode.child("partite").child(String.valueOf(partiteUtente1.size()-1)).getKey().equals(String.valueOf(0))){
                                                dbUtenti.child(keyNode.getKey()).child("partite").child("0").setValue("prova");
                                            } else {
                                                dbUtenti.child(keyNode.getKey()).child("partite").child(String.valueOf(partiteUtente1.size() - 1)).removeValue();
                                                Log.d(TAG, "Partite dell'utente " + partiteUtente1);
                                            }

                                        }
                                    }
                                    startActivity(new Intent(ActivityScegliRuolo.this, MainActivity.class));
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }

                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
