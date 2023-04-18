package com.example.whoisolder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import java.util.AbstractCollection;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Puntuaciones extends AppCompatActivity {

    //ArrayList<String> listapuntos = new ArrayList<>();

    int[] imagenes = {R.drawable.usuario};
    AdaptadorListView adaptador;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuaciones);
        //Se llama a la funcion de cargar las puntuaciones de la base de datos
        cargarPuntos();
        //Se recogen las variables del layout
        Button share = findViewById(R.id.btn_share);
        //Se abre instagram en google para poder compartir la puntuacion
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri web = Uri.parse("https://www.instagram.com/");
                Intent webIntent = new Intent(Intent.ACTION_VIEW,web);
                startActivity(webIntent);
            }
        });
    }
    public void cargarPuntos(){
        //Se inicializa la lista de las puntuaciones
        ArrayList<String> listaPuntos = new ArrayList<>();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(cargarPuntuaciones.class).build();
        WorkManager.getInstance(Puntuaciones.this).getWorkInfoByIdLiveData(otwr.getId()).observe(Puntuaciones.this,new Observer<WorkInfo>() {
            @Override
            public void onChanged (WorkInfo workInfo){
                if (workInfo != null && workInfo.getState().isFinished()) {
                    String[] listaNombres = workInfo.getOutputData().getStringArray("array1");
                    String[] listaPts = workInfo.getOutputData().getStringArray("array2");
                    Log.d("ERR","Nombre: " + listaNombres[0]);

                    for(int i = 0; i < listaNombres.length; i++){
                        String nombre = listaNombres[i];
                        String puntos = listaPts[i];
                        listaPuntos.add(nombre + " " + puntos);
                    }
                    Log.d("ERR","Nombre: " + listaPuntos.get(0));
                    adaptador = new AdaptadorListView(getApplicationContext(),listaPuntos,imagenes);
                    list = findViewById(R.id.lista);
                    list.setAdapter(adaptador);
                }
            }
        });
        WorkManager.getInstance(Puntuaciones.this).enqueue(otwr);

    }
}