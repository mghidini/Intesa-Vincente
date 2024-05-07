package com.example.intesavincente.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.intesavincente.R;
import com.example.intesavincente.ui.authentication.AuthenticationActivity;
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

public class ModificaProfilo extends AppCompatActivity {

    private static final String TAG ="ModificaProfilo";

    private Button mButtonLogout;
    private EditText nickname;
    private EditText password;
    private Button salvaModifiche;
    private Snackbar modificaProfilo;

    private DatabaseReference dbUtenti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_profilo);

        mButtonLogout = findViewById(R.id.logout_button);
        nickname=findViewById(R.id.campoNickname);
        password=findViewById(R.id.campoPwd);
        salvaModifiche=findViewById(R.id.salva_modifiche);
        salvaModifiche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbUtenti = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("utenti");
                dbUtenti.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> keysUtenti = new ArrayList<>();
                        for (DataSnapshot keyNode : snapshot.getChildren()) {
                            keysUtenti.add(keyNode.getKey());
                            if (keyNode.child("idUtente").getValue(String.class).equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                Log.d(TAG,"stampa1"+nickname.getText());
                                Log.d(TAG,"stampa1"+nickname.getText());
                                if(!(nickname.getText().toString().equals("")) && !(password.getText().toString().equals(""))){
                                    dbUtenti.child(keyNode.getKey()).child("nickname").setValue(nickname.getText().toString());
                                    FirebaseAuth.getInstance().getCurrentUser().updatePassword(password.getText().toString());
                                    nickname.setText("");
                                    password.setText("");
                                    modificaProfilo = Snackbar.make(v, "PROFILO MODIFICATO CORRETTAMENTE", Snackbar.LENGTH_SHORT);
                                    modificaProfilo.show();
                                }
                                else if(nickname.getText().toString().equals("") && password.getText().toString().equals("")) {
                                    modificaProfilo = Snackbar.make(v, "NESSUN NUOVO DATO INSERITO", Snackbar.LENGTH_SHORT);
                                    modificaProfilo.show();
                                }
                                else {
                                    if (!nickname.getText().toString().equals("")) {
                                        dbUtenti.child(keyNode.getKey()).child("nickname").setValue(nickname.getText().toString());
                                        nickname.setText("");
                                        modificaProfilo = Snackbar.make(v, "NICKNAME MODIFICATO CORRETTAMENTE", Snackbar.LENGTH_SHORT);
                                        modificaProfilo.show();
                                    }
                                    Log.d(TAG, "stampa2" + password.getText().toString());
                                    if (!password.getText().toString().equals("") && password.length() >= 6) {
                                        FirebaseAuth.getInstance().getCurrentUser().updatePassword(password.getText().toString());
                                        password.setText("");
                                        modificaProfilo = Snackbar.make(v, "PASSWORD MODIFICATA CORRETTAMENTE", Snackbar.LENGTH_SHORT);
                                        modificaProfilo.show();
                                    }
                                    else if(!password.getText().toString().equals("") && password.length() < 6){
                                        password.setText("");
                                        modificaProfilo = Snackbar.make(v, "PASSWORD DEVE CONTENERE ALMENO 6 CARATTERI", Snackbar.LENGTH_SHORT);
                                        modificaProfilo.show();
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
        });
        mButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // disconnessione dell'utente
                FirebaseAuth.getInstance().signOut();

                // definisco l'intenzione
                Intent backAutenticazione = new Intent(ModificaProfilo.this, AuthenticationActivity.class);
                // passo all'attivazione dell'activity Pagina.java
                startActivity(backAutenticazione);
            }
        });

    }

}