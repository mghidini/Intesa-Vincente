package com.example.intesavincente.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.intesavincente.R;

import java.util.ArrayList;

public class ListaAmiciAdapter extends ArrayAdapter<String> {
    private static final String TAG ="ListaGruppiAdapter" ;

    private ArrayList<String> mArrayComponenti;
    private int mLayout;


    public ListaAmiciAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        this.mArrayComponenti = objects;
        this.mLayout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mLayout, parent, false);
        }

        TextView textViewNomeComponente = convertView.findViewById(R.id.componenti1);
        textViewNomeComponente.setText(mArrayComponenti.get(position));
        Log.d(TAG, "Componenti : "+mArrayComponenti.get(position));

        return convertView;
    }
}
