package com.example.intesavincente.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.intesavincente.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ListaNomiGruppiStatAdapter extends ArrayAdapter<String> {
    private static final String TAG ="ListaNomiGruppiStatAdapter" ;

    private ArrayList<String> mArrayNomiGruppi;
    private int mLayout;

    private TextView textViewNomeGruppo;

    private String nomeGruppo;

    public ListaNomiGruppiStatAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> nomi) {
        super(context, resource, nomi);
        this.mArrayNomiGruppi = nomi;
        this.mLayout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mLayout, parent, false);
        }

        nomeGruppo = mArrayNomiGruppi.get(position);
        textViewNomeGruppo=convertView.findViewById(R.id.nome_gruppo_stat);
        textViewNomeGruppo.setText(nomeGruppo);
        System.out.println("nomegruppo2312"+ nomeGruppo);

        return convertView;
    }
}
