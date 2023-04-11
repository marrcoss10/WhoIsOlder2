package com.example.whoisolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorListView extends BaseAdapter {
    private Context contexto;
    private LayoutInflater inflater;
    private ArrayList<String> datos;
    private int[] imagenes;

    public AdaptadorListView(Context pcontext, ArrayList<String> pdatos, int[] pimagenes){
        contexto = pcontext;
        datos = pdatos;
        imagenes = pimagenes;
        inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.usuarios,null);
        TextView nombre = (TextView) view.findViewById(R.id.user);
        ImageView img = (ImageView) view.findViewById(R.id.userimg);
        nombre.setText(datos.get(i));
        img.setImageResource(imagenes[0]);
        return view;
    }

    @Override
    public Object getItem(int i) {
        return datos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}