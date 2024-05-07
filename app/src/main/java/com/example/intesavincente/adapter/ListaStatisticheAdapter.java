package com.example.intesavincente.adapter;

import static android.content.Context.MODE_PRIVATE;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.intesavincente.utils.MyApplication;
import com.example.intesavincente.R;
import com.example.intesavincente.model.Partita;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ListaStatisticheAdapter extends ArrayAdapter<Partita>{
    private static final String TAG ="ListaGruppiAdapter" ;

    private ArrayList<Partita> mArrayPartite;
    private int mLayout;

    private TextView textViewParoleIndovinate;
    private TextView textViewNumPasso;
    private TextView textViewNomeGruppo;

    private int nParoleIndovinate;
    private int nPassoUsati;
    private String nomeGruppo;
    private DatabaseReference dbGruppi;

    private SharedPreferences prefStatistiche = MyApplication.getAppContext().getSharedPreferences("MyPref", MODE_PRIVATE);
    private SharedPreferences.Editor editorStatistiche = prefStatistiche.edit();

    public ListaStatisticheAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Partita> partite) {
        super(context, resource, partite);
        this.mArrayPartite = partite;
        this.mLayout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mLayout, parent, false);
        }

        nParoleIndovinate = mArrayPartite.get(position).getParole_indovinate();
        nPassoUsati = mArrayPartite.get(position).getPasso();

        textViewParoleIndovinate = convertView.findViewById(R.id.parole_indovinate);
        textViewParoleIndovinate.setText(String.valueOf(nParoleIndovinate));
        Log.d(TAG, "Numero parole indovinate : "+nParoleIndovinate);
        textViewNumPasso = convertView.findViewById(R.id.nPasso_usati);
        textViewNumPasso.setText(String.valueOf(nPassoUsati));
        Log.d(TAG, "Componenti : "+nPassoUsati);
        nomeGruppo = mArrayPartite.get(position).getParola();
        textViewNomeGruppo=convertView.findViewById(R.id.nome_gruppo_stat);
        textViewNomeGruppo.setText(nomeGruppo.toUpperCase());
        System.out.println("nomegruppo2312"+ nomeGruppo);

        return convertView;
    }
}
