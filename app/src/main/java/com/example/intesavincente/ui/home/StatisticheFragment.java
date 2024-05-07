package com.example.intesavincente.ui.home;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.intesavincente.utils.MyApplication;
import com.example.intesavincente.R;
import com.example.intesavincente.adapter.ListaStatisticheAdapter;
import com.example.intesavincente.model.Partita;
import com.example.intesavincente.repository.gruppo.GruppoRepository;
import com.example.intesavincente.repository.partita.PartitaRepository;
import com.example.intesavincente.repository.partita.PartitaResponse;
import com.example.intesavincente.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticheFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticheFragment extends Fragment implements PartitaResponse {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG ="StatisticheFragment" ;

    ListView listaStatistiche;
    ArrayList<Partita> arrayPartite = new ArrayList<Partita>();

    DatabaseReference dbPartite;
    DatabaseReference dbUtenti;
    DatabaseReference dbGruppi;
    DatabaseReference db;
    PartitaRepository mPartitaRepository;

    public StatisticheFragment() {
        // Required empty public constructor
    }

    public static StatisticheFragment newInstance(String param1, String param2) {
        StatisticheFragment fragment = new StatisticheFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_statistiche, container, false);
        mPartitaRepository = new PartitaRepository((Application) MyApplication.getAppContext(), this);
        mPartitaRepository.trovaPartita();
        listaStatistiche = v.findViewById(R.id.Statistiche_listView);
        ArrayList<Partita> arrayPartite = new ArrayList<Partita>();
        ArrayList<String> partiteUtente= new ArrayList<String>();
        ArrayList<String> nomiGruppi= new ArrayList<String>();

        dbUtenti = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("utenti");
        dbUtenti.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayPartite.clear();
                List<String> keysUtenti = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    keysUtenti.add(keyNode.getKey());
                    if (keyNode.child("idUtente").getValue(String.class).equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        for (int i = 0; i < 100; i++) {
                            if (keyNode.child("partite").child(String.valueOf(i)).exists()) {
                                partiteUtente.add(keyNode.child("partite").child(String.valueOf(i)).getValue(String.class));
                            }
                        }
                        Log.d(TAG, "Partite dell'utente " + partiteUtente);
                    }
                }

                dbPartite = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("partite");
                dbPartite.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> keysPartite = new ArrayList<>();
                        for (DataSnapshot keyNode1 : dataSnapshot.getChildren()) {
                            keysPartite.add(keyNode1.getKey());
                            for(int i = 0; i < partiteUtente.size(); i++){
                                if(partiteUtente.get(i).equals(keyNode1.getKey())){
                                    Log.d(TAG, "Partite dell'utente6 " + partiteUtente);
                                    Partita partita = keyNode1.getValue(Partita.class);
                                    Log.d(TAG, "Partita " + partita.toString());
                                    arrayPartite.add(partita);
                                    Log.d(TAG, "ArrayPartite " + arrayPartite);
                                }
                            }
                        }

                        dbGruppi = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("gruppi");
                        dbGruppi.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                List<String> keysGruppi = new ArrayList<>();
                                for (DataSnapshot keyNode2 : snapshot.getChildren()) {
                                    keysGruppi.add(keyNode2.getKey());
                                    for(int i = 0; i < arrayPartite.size(); i++){
                                        Log.d(TAG, "Gruppo id in partite " + arrayPartite.get(i).getGruppoID());
                                        Log.d(TAG, "Gruppo id in gruppi " + keyNode2.child("id").getValue(String.class));
                                        if(keyNode2.child("id").getValue(String.class).equals(arrayPartite.get(i).getGruppoID())){
                                            String nomeGruppo = keyNode2.child("nome").getValue(String.class);
                                            Log.d(TAG, "Nome gruppo " + nomeGruppo);
                                            //nomiGruppi.add(nomeGruppo);
                                            arrayPartite.get(i).setParola(nomeGruppo);
                                        }
                                    }
                                }
                                //Log.d(TAG, "ArrayPartite " + arrayPartite.toString());
                                if(arrayPartite!=null){
                                    if (getActivity()!=null) {
                                        final ListaStatisticheAdapter myArrayAdapter = new ListaStatisticheAdapter(getActivity(), R.layout.statistiche_list_item, arrayPartite);
                                        listaStatistiche.setAdapter(myArrayAdapter);
                                        myArrayAdapter.notifyDataSetChanged();
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

        return v;
    }

    @Override
    public void onDataFound(Partita partita) {
        GruppoRepository g=new GruppoRepository();
        g.cancellaPartita(partita);
    }
}