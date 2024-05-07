package com.example.intesavincente.ui.partita;

import android.app.Application;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.intesavincente.utils.MyApplication;
import com.example.intesavincente.R;
import com.example.intesavincente.model.Partita;
import com.example.intesavincente.repository.gruppo.GruppoRepository;
import com.example.intesavincente.repository.partita.PartitaRepository;
import com.example.intesavincente.repository.partita.PartitaResponse;

public class CreaUniscitiFragment extends Fragment implements PartitaResponse {
    private final String TAG="CreaUniscitiFragment";
    private PartitaRepository mPartitaRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crea_unisciti, container, false);
        mPartitaRepository = new PartitaRepository((Application) MyApplication.getAppContext(), this);
        mPartitaRepository.trovaPartita();
        Button creaGruppo = v.findViewById(R.id.crea_gruppo_button);
        creaGruppo.setOnClickListener(view -> {
            Navigation.findNavController(v).navigate(R.id.action_creaUniscitiFragment2_to_creaGruppoFragment);
            mPartitaRepository = new PartitaRepository((Application) MyApplication.getAppContext(), this);
            mPartitaRepository.trovaPartita();
        });

        Button unisciti = v.findViewById(R.id.unisciti_button);
        unisciti.setOnClickListener(view -> {
            Navigation.findNavController(v).navigate(R.id.action_creaUniscitiFragment2_to_listaGruppiFragment);
            mPartitaRepository = new PartitaRepository((Application) MyApplication.getAppContext(), this);
            mPartitaRepository.trovaPartita();
        });
        return v;
    }

    @Override
    public void onDataFound(Partita partita) {
         Log.d(TAG,partita.getIdPartita());
         GruppoRepository g=new GruppoRepository();
         g.cancellaPartita(partita);
    }

}