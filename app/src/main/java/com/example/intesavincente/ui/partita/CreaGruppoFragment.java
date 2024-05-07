package com.example.intesavincente.ui.partita;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.intesavincente.utils.MyApplication;
import com.example.intesavincente.R;
import com.example.intesavincente.model.Partita;
import com.example.intesavincente.repository.gruppo.GruppoRepository;
import com.example.intesavincente.repository.partita.PartitaRepository;
import com.example.intesavincente.repository.partita.PartitaResponse;
import com.example.intesavincente.utils.Constants;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreaGruppoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreaGruppoFragment extends Fragment implements PartitaResponse {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG ="CreaGruppoFragment" ;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreaGruppoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreaGruppoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreaGruppoFragment newInstance(String param1, String param2) {
        CreaGruppoFragment fragment = new CreaGruppoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    Button creaGruppoButton;
    EditText nomeGruppo;
    Snackbar snackbarCreaGruppo;
    Snackbar snackbarNomeGruppoErrato;

    DatabaseReference db;
    DatabaseReference db1;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crea_gruppo, container, false);

        creaGruppoButton = v.findViewById(R.id.crea);
        nomeGruppo = v.findViewById(R.id.campoNick);

        creaGruppoButton.setOnClickListener(view -> {
        db = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference();
        String nome = nomeGruppo.getText().toString();
        if(!TextUtils.isEmpty(nome)) {
            String gruppoID = db.push().getKey();

            Log.d(TAG,"fuori chiamata1");
            GruppoRepository g= new GruppoRepository();
            g.inserisciGruppo(gruppoID,nome);
            Log.d(TAG,"fuori chiamata2");
            snackbarCreaGruppo = Snackbar.make(v, "GRUPPO " + nome + " CREATO", Snackbar.LENGTH_SHORT);
            snackbarCreaGruppo.show();

            PartitaRepository p = new PartitaRepository((Application) MyApplication.getAppContext(), this);
            p.trovaPartita();
            p.inserisciGruppoInPartita(gruppoID);
            Log.d(TAG,"fuori chiamata3");
            Log.d(TAG,"fuori chiamata4");
            Navigation.findNavController(v).navigate(R.id.action_creaGruppoFragment_to_activityScegliRuolo);

        }
        else{
            snackbarNomeGruppoErrato = Snackbar.make(v, "NOME GRUPPO NON INSERITO", Snackbar.LENGTH_SHORT);
            snackbarNomeGruppoErrato.show();
        }
    });
        return v;
}


    @Override
    public void onDataFound(Partita partita) {
        Log.d(TAG,"fuori partita"+partita.getIdPartita());
        DatabaseReference dbPartite = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("partite");
        dbPartite.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> keysPartite = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    Log.d(TAG, "KeyNode " + keyNode);
                    keysPartite.add(keyNode.getKey());
                    if(partita.getIdPartita().equals(keyNode.getKey())){
                        Log.d(TAG,"dentro partita"+partita.getIdPartita());
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}