package com.example.intesavincente.ui.home;

import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.intesavincente.utils.MyApplication;
import com.example.intesavincente.R;
import com.example.intesavincente.adapter.ListaAmiciAdapter;
import com.example.intesavincente.model.Partita;
import com.example.intesavincente.model.Utente;
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
import java.util.stream.Collectors;

/**
 *
 * A simple {@link Fragment} subclass.
 * Use the {@link AmiciFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AmiciFragment extends Fragment implements PartitaResponse {




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView listaAmici;
    PartitaRepository mPartitaRepository;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "AmiciFragment";
    public AmiciFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AmiciFragment newInstance(String param1, String param2) {
        AmiciFragment fragment = new AmiciFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        View v= inflater.inflate(R.layout.fragment_amici, container, false);
        mPartitaRepository = new PartitaRepository((Application) MyApplication.getAppContext(), this);
        mPartitaRepository.trovaPartita();
        listaAmici = v.findViewById(R.id.amici_listView);
        cercaInGruppo();
        return v;
    }
    public void cercaInGruppo(){
        DatabaseReference db = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference("gruppi");

        List<String> amici= new ArrayList<>();

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> keys = new ArrayList<>();
                Boolean flag=true;

                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    List<String> gruppoCorrente = new ArrayList<>();
                    keys.add(keyNode.getKey());
                    Log.d(TAG, "trovaPartita3" + keyNode.getKey());
                    int count=0;
                    flag=false;
                    for (int i = 0; i < 3; i++) {
                        //Log.d(TAG, "idutente1"+keyNode.child("componenti").child(String.valueOf(i)).getValue(Utente.class).getIdUtente() );
                        Log.d(TAG, "idutente12" + FirebaseAuth.getInstance().getCurrentUser().getUid());


                        //controllo se esiste il ramo componente di i
                        if (keyNode.child("componenti").child(String.valueOf(i)).exists()) {
                            //se sono io
                            if (keyNode.child("componenti").child(String.valueOf(i)).getValue(Utente.class).getIdUtente().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                flag = true;
                            } else {
                                gruppoCorrente.add(keyNode.child("componenti").child(String.valueOf(i)).getValue(Utente.class).getNickname());
                            }
                        }
                    }
                    if(flag){
                        amici.addAll(gruppoCorrente);
                    }

                }
                Log.d(TAG, "componenti34" + amici.toString());
                if (amici != null) {
                    if (getActivity()!=null) {
                        List<String> noduplicate = amici.stream().distinct().collect(Collectors.toList());

                        final ListaAmiciAdapter myArrayAdapter = new ListaAmiciAdapter(getActivity(), R.layout.amici_list_item, (ArrayList<String>) noduplicate);

                        listaAmici.setAdapter(myArrayAdapter);
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
    public void onDataFound(Partita partita) {
        GruppoRepository g=new GruppoRepository();
        g.cancellaPartita(partita);
    }
}