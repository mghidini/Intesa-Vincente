package com.example.intesavincente.repository.gruppo;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.example.intesavincente.R;
import com.example.intesavincente.model.Gruppo;
import com.example.intesavincente.model.Partita;
import com.example.intesavincente.model.Utente;
import com.example.intesavincente.utils.Constants;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GruppoRepository {

    private DatabaseReference db1;
    private static final String TAG ="GruppoRepository" ;

    public GruppoRepository(){

    }

    public void inserisciGruppo(String gruppoID,String nome) {
        Log.d(TAG,"fuori chiamata");
        db1 = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("utenti");
        DatabaseReference db = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference();
        db1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    if (keyNode.child("idUtente").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        keys.add(keyNode.getKey());
                        Utente utente =(Utente) keyNode.getValue(Utente.class);
                        Log.d(TAG, "Utente stampa query if " + keyNode.child("idUtente"));
                        Log.d(TAG, "Utente chiave  if " + keyNode.getKey());
                        Log.d(TAG, "Utente nome if" + keyNode.child("nickname").getValue());
                        Log.d(TAG, " gruppo id" + gruppoID);
                        Log.d(TAG, " nome" + nome);

                        Gruppo gruppo = new Gruppo(gruppoID, nome, utente);
                        Log.d(TAG, "componenti del gruppo ");
                        Log.d(TAG, "componenti del gruppo ");
                        Log.d(TAG, "componenti del gruppo " + gruppo.stampaLista());

                        db.child("gruppi").child(gruppoID).setValue(gruppo);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void cancellaPartita(Partita partita){
        Log.d(TAG, "partitaid44" );

        String idGruppo = partita.getGruppoID();
        String idPartita = partita.getIdPartita();
        Log.d(TAG, "partitaid89" + idPartita);
        DatabaseReference dbPartite = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("partite");
        dbPartite.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    if (keyNode.getKey().equals(idPartita))
                        dbPartite.child(keyNode.getKey()).removeValue();
                }

                DatabaseReference dbGruppi = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("gruppi");
                dbGruppi.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> keys = new ArrayList<>();
                        for (DataSnapshot keyNode : snapshot.getChildren()) {
                            keys.add(keyNode.getKey());
                            if (keyNode.getKey().equals(idGruppo))
                                dbGruppi.child(keyNode.getKey()).removeValue();
                        }
                        DatabaseReference dbUtenti = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("utenti");
                        ArrayList<String> partiteUtente = new ArrayList<String>();
                        dbUtenti.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                List<String> keys = new ArrayList<>();
                                for (DataSnapshot keyNode : snapshot.getChildren()) {
                                    keys.add(keyNode.getKey());
                                    if (keyNode.child("idUtente").getValue(String.class).equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                        for (int i = 0; i < 100; i++) {
                                            if (keyNode.child("partite").child(String.valueOf(i)).exists()) {
                                                partiteUtente.add(keyNode.child("partite").child(String.valueOf(i)).getValue(String.class));
                                            }
                                        }
                                    }
                                    if (keyNode.child("partite").child(String.valueOf(partiteUtente.size() - 1)).getKey().equals(String.valueOf(0))) {
                                        dbUtenti.child(keyNode.getKey()).child("partite").child("0").setValue("prova");
                                    } else {
                                        dbUtenti.child(keyNode.getKey()).child("partite").child(String.valueOf(partiteUtente.size() - 1)).removeValue();
                                        Log.d(TAG, "Partite dell'utente " + partiteUtente);
                                    }
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}