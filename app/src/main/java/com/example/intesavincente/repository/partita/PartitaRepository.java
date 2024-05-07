package com.example.intesavincente.repository.partita;

import android.app.Application;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.intesavincente.model.Gruppo;
import com.example.intesavincente.model.Partita;
import com.example.intesavincente.model.Utente;
import com.example.intesavincente.repository.user.UserRepository;
import com.example.intesavincente.repository.utente.UtenteRepository;
import com.example.intesavincente.utils.Constants;
import com.example.intesavincente.utils.ResponseCallback;
import com.example.intesavincente.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PartitaRepository implements IPartitaRepository{

    private static final String TAG = "PartitaRepository";

    private DatabaseReference dbGruppi;
    private DatabaseReference dbPartite;
    private DatabaseReference dbUtenti;

    private UtenteRepository mUtenteRepository = new UtenteRepository();

    private PartitaResponse mPartitaResponse;

    private Application mApplication;

    public PartitaRepository(Application application, PartitaResponse mPartitaResponse) {
       this.mApplication = application;
       this.mPartitaResponse = mPartitaResponse;

    }

    public PartitaRepository() {

    }

    public void inserisciGruppoInPartita(String gruppoID) {

        dbPartite = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("partite");
        String partitaID=dbPartite.push().getKey();
        Partita p=new Partita(gruppoID,partitaID);
        dbPartite.child(partitaID).setValue(p);
        mUtenteRepository.aggiungiIDPartita(partitaID);
    }

    public void inserisciPartitaInUtente(String gruppoID){
        DatabaseReference dbPartite = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("partite");
        dbPartite.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> keysPartite = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    Log.d(TAG, "KeyNode " + keyNode);
                    keysPartite.add(keyNode.getKey());
                    if (keyNode.child("gruppoID").getValue().equals(gruppoID)) {
                        String chiave=keyNode.getKey();
                        keysPartite.add(keyNode.getKey());
                        Log.d(TAG, "chiave " + chiave);
                        DatabaseReference dbUtenti = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("utenti");
                        dbUtenti.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                List<String> keysUtenti = new ArrayList<>();
                                for (DataSnapshot keyNode : snapshot.getChildren()) {
                                    Log.d(TAG, "KeyNode " + keyNode);
                                    keysUtenti.add(keyNode.getKey());
                                    if (keyNode.child("idUtente").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        keysUtenti.add(keyNode.getKey());
                                        Log.d(TAG, "KeysPartite " + keysPartite.size());
                                        for (int i = 0; i < keysPartite.size(); i++) {
                                            if(!keyNode.child("partite").child(String.valueOf(i)).getValue(String.class).equals(chiave)){
                                                String utenteID= FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                Log.d(TAG, "tipo Partite" + keyNode.child("idUtente").getClass());
                                                String nickname= (String) keyNode.child("nickname").getValue();
                                                String mail= (String)keyNode.child("mail").getValue();
                                                String password= (String) keyNode.child("password").getValue();
                                                ArrayList <String> partite=new ArrayList<>();
                                                partite= (ArrayList<String>) keyNode.child("partite").getValue();
                                                Utente u=new Utente(utenteID,nickname,mail,password);
                                                if(partite.size()==1&&partite.get(0).equals("prova")){
                                                    Log.d(TAG, "dentro if22" );
                                                    partite.remove(0);
                                                    partite.add(0,chiave);
                                                    u.setPartite(partite);
                                                }
                                                else{
                                                    Log.d(TAG, "dentro if223");
                                                    u.setPartite(partite);
                                                    u.aggiungiPartita(chiave);
                                                }
                                                Log.d(TAG, "valori utente"+ u.toString1());
                                                Log.d(TAG, "valori utente"+ u.getPartite());
                                                String idUtente = keyNode.getKey().toString();
                                                System.out.println(idUtente);
                                                dbUtenti.child(idUtente).setValue(u);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void trovaPartita() {
        ArrayList<String> partiteUtente= new ArrayList<String>();
        dbUtenti = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("utenti");
        dbUtenti.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> keysUtenti = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    keysUtenti.add(keyNode.getKey());
                    if (keyNode.child("idUtente").getValue(String.class).equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        for (int i = 0; i < 100; i++) {
                            if (keyNode.child("partite").child(String.valueOf(i)).exists()) {
                                partiteUtente.add(keyNode.child("partite").child(String.valueOf(i)).getValue(String.class));
                            }
                        }
                    }
                }
                dbPartite = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("partite");
                dbPartite.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> keysPartite = new ArrayList<>();
                        for (DataSnapshot keyNode : snapshot.getChildren()) {
                            keysPartite.add(keyNode.getKey());
                            for(int i = 0; i < partiteUtente.size(); i++) {
                                if (keyNode.child("idPartita").getValue(String.class).equals(partiteUtente.get(i)) && keyNode.child("attiva").getValue(Boolean.class)) {
                                    Log.d(TAG, "Partita isAttiva " + keyNode.child("attiva").getValue(Boolean.class));
                                    Partita partita = (Partita) keyNode.getValue(Partita.class);
                                    Log.d(TAG, "Partita attiva " + partita.getIdPartita());
                                    if(partita != null){
                                        mPartitaResponse.onDataFound(partita);
                                    }
                                }
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

    public void cancellaDatiButton(){
        ArrayList<String> partiteUtente= new ArrayList<String>();
        dbUtenti = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("utenti");
        dbUtenti.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> keysUtenti = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    keysUtenti.add(keyNode.getKey());
                    if (keyNode.child("idUtente").getValue(String.class).equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        for (int i = 0; i < 100; i++) {
                            if (keyNode.child("partite").child(String.valueOf(i)).exists()) {
                                partiteUtente.add(keyNode.child("partite").child(String.valueOf(i)).getValue(String.class));
                            }
                        }
                        Log.d(TAG, "codice partita" + keyNode.child("partite").child(String.valueOf(partiteUtente.size()-1)));
                        if(keyNode.child("partite").child(String.valueOf(partiteUtente.size()-1)).getKey().equals(String.valueOf(0))){
                            dbUtenti.child(keyNode.getKey()).child("partite").child("0").setValue("prova");
                        } else {
                            dbUtenti.child(keyNode.getKey()).child("partite").child(String.valueOf(partiteUtente.size() - 1)).removeValue();
                            Log.d(TAG, "Partite dell'utente " + partiteUtente);
                        }
                    }
                }
                String idPartita = partiteUtente.get(partiteUtente.size()-1);
                Log.d(TAG, "ID Partita " + idPartita);
                dbPartite = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("partite");
                dbPartite.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> keysPartite = new ArrayList<>();
                        String idGruppo = "";
                        for (DataSnapshot keyNode : snapshot.getChildren()) {
                            keysPartite.add(keyNode.getKey());
                            if (keyNode.child("idPartita").getValue(String.class).equals(idPartita)) {
                                idGruppo = keyNode.child("gruppoID").getValue(String.class);
                                dbPartite.child(keyNode.getKey()).removeValue();
                            }
                        }
                        String idGruppoCancelled = idGruppo;
                        dbGruppi = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("gruppi");
                        dbGruppi.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                List<String> keysGruppi = new ArrayList<>();
                                for (DataSnapshot keyNode : snapshot.getChildren()) {
                                    keysGruppi.add(keyNode.getKey());
                                    if (keyNode.child("id").getValue(String.class).equals(idGruppoCancelled)) {
                                        dbGruppi.child(keyNode.getKey()).removeValue();
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



