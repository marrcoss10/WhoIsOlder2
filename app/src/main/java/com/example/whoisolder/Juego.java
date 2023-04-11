package com.example.whoisolder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class Juego extends AppCompatActivity {
    ArrayList<String> nombres = new ArrayList<String>();
    HashMap<String,Integer> map = new HashMap<String, Integer>();
    Integer x;
    Integer y;
    int points;
    String p;
    TextView pt, name, f1, f2;
    ImageView img1, img2;
    Boolean cargar = true;
    NotificationManager elManager;
    NotificationCompat.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        if (savedInstanceState!= null)
        {
            //Recuperar valores cuando gira
            points = savedInstanceState.getInt("punt");
            cargar = savedInstanceState.getBoolean("bool");
            nombres = savedInstanceState.getStringArrayList("lista");
            x = savedInstanceState.getInt("x");
            y = savedInstanceState.getInt("y");
        }

        //Realizar todo no necesario para las notificaciones
        elManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(Juego.this,"Mi Notificacion");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED) {
                //PEDIR EL PERMISO
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 11);
            }
            NotificationChannel channel = new NotificationChannel("Mi Notificacion","Mi Notificacion", NotificationManager.IMPORTANCE_DEFAULT);
            elManager.createNotificationChannel(channel);
        }


        builder.setContentTitle("Fin del juego");
        builder.setContentText("Has perdido, vuelve a intentarlo!");
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.logoapppeque));
        builder.setSmallIcon(android.R.drawable.stat_sys_warning);
        builder.setAutoCancel(true);
        //Recoger todos las variables del layout
        name = findViewById(R.id.hola);
        pt = findViewById(R.id.puntos);
        pt.setText(String.valueOf(points));

        p = String.valueOf(points);
        pt.setText(p);
        //Se recogen las variables de la pantalla anterior
        Bundle bundle = getIntent().getExtras();
        String dato = bundle.getString("nombre");
        name.setText(dato);
        //Se llama a la funcion de cargar los datos de la bd en el hashmap
        cargarHashmap();
        //Se recogen las variables del layout
        Button boton1 = findViewById(R.id.btn_1);
        Button boton2 = findViewById(R.id.btn_2);
        //Se llama a la funcion para comprobar si el que ha pulsado es correcto
        boton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                comprobar_mayor(1,nombres.get(x),nombres.get(y));
            }
        });
        //Se llama a la funcion para comprobar si el que ha pulsado es correcto
        boton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                comprobar_mayor(2,nombres.get(x),nombres.get(y));
            }
        });

    }

    //Se cargan los nombres junto a las imagens de manera aleatoria (al iniciar y al acertar)
    public void cargarNombresAleatorios(){
        cargar = false;
        f1 = findViewById(R.id.famoso1);
        f2 = findViewById(R.id.famoso2);
        Integer i = nombres.size();
        x = (int) Math.floor(Math.random()*i);
        y = (int) Math.floor(Math.random()*i);
        boolean salir = false;
        Log.d("ERRORES","SIZE: " + i);
        //Se comprueba que ambos nombres no sean iguales
        while(!salir){
            if(y != x){
                salir = true;
            }
            else{
                y = (int) Math.floor(Math.random()*i);
            }
        }
        f1.setText(nombres.get(x));
        f2.setText(nombres.get(y));

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        //Se llama a la funcion para cargar la imagen correspondiente
        actualizarImagen(img1,x);
        actualizarImagen(img2,y);
    }
    //Se comprueba que imagen hay que cargar
    public void actualizarImagen(ImageView img, int i){
        String fam = nombres.get(i);
        switch (fam){
            case "Messi":
                img.setImageResource(R.drawable.messi);
                break;
            case "Cristiano":
                img.setImageResource(R.drawable.cristiano);
                break;
            case "Pen.Cruz":
                img.setImageResource(R.drawable.penelope);
                break;
            case "Ant.Banderas":
                img.setImageResource(R.drawable.antoniobanderas);
                break;
            case "Jen.Lopez":
                img.setImageResource(R.drawable.jl);
                break;
            case "Bel.Rueda":
                img.setImageResource(R.drawable.belenrueda);
                break;
            case "Cr.Pedroche":
                img.setImageResource(R.drawable.pedroche);
                break;
            case "Pablo Motos":
                img.setImageResource(R.drawable.motos);
                break;
            case "Alej.Sanz":
                img.setImageResource(R.drawable.alesanz);
                break;
            case "Coronado":
                img.setImageResource(R.drawable.coronado);
                break;
            case "Sanchez":
                img.setImageResource(R.drawable.sanchez);
                break;
            case "Rajoy":
                img.setImageResource(R.drawable.rajoy);
                break;
            case "Broncano":
                img.setImageResource(R.drawable.broncano);
                break;
            case "Risto":
                img.setImageResource(R.drawable.risto);
                break;
            case "Bustamante":
                img.setImageResource(R.drawable.bustamante);
                break;
            case "Chenoa":
                img.setImageResource(R.drawable.chenoa);
                break;
            case "Beckham":
                img.setImageResource(R.drawable.beckham);
                break;
            default:
                break;
        }
    }
    //Se cargan todos los nombres junto a la edad de la BBDD en un hashmap para poder acceder mas rapido
    public void cargarHashmap(){
        Log.d("ERRORES","Entra a cargar");
        nombres.clear();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(cargarFamosos.class).build();
        WorkManager.getInstance(Juego.this).getWorkInfoByIdLiveData(otwr.getId()).observe(Juego.this,new Observer<WorkInfo>() {
            @Override
            public void onChanged (WorkInfo workInfo){
                if (workInfo != null && workInfo.getState().isFinished()) {
                    String[] listaNombres = workInfo.getOutputData().getStringArray("array1");
                    String[] listaEdad = workInfo.getOutputData().getStringArray("array2");
                    for(int i = 0; i < listaNombres.length; i++){
                        String nombre = listaNombres[i];
                        String edad = listaEdad[i];
                        map.put(nombre,Integer.parseInt(edad));
                        nombres.add(nombre);
                    }
                    if(cargar){
                        cargarNombresAleatorios();
                    }
                    //Si se ha girado se recogen los datos de las variables guardadas
                    else{
                        f1 = findViewById(R.id.famoso1);
                        f2 = findViewById(R.id.famoso2);
                        //Se actualizan los nombres correspondientes
                        f1.setText(nombres.get(x));
                        f2.setText(nombres.get(y));
                        img1 = findViewById(R.id.img1);
                        img2 = findViewById(R.id.img2);
                        //Se llama a la funcion para cargar las imagenes correspondientes
                        actualizarImagen(img1,x);
                        actualizarImagen(img2,y);
                    }
                }
            }
        });
        WorkManager.getInstance(Juego.this).enqueue(otwr);
    }
    //Funcion que comprueba si es correcto lo pulsado (si son iguales las edades se da por correcto cualquiera de los dos)
    public void comprobar_mayor(int i, String x, String y){
        Integer x1 = map.get(x);
        Integer x2 = map.get(y);
        Boolean acierta = false;
        //Famoso 1 edad mayor o igual a famoso 2
        if(x1 >= x2){
            //Acierta
            if(i == 1){
                acierta = true;
            }
        }
        //Famoso 1 edad menor o igual a famoso 2
        else if(x1 <= x2){
            //Acierta
            if(i == 2){
                acierta = true;
            }
        }
        //Si acierta se actualizan los nombres y las imagenes y se suma un punto
        if(acierta){
            pt = findViewById(R.id.puntos);
            points = Integer.parseInt(pt.getText().toString()) + 1;
            pt.setText(String.valueOf(points));
            cargarNombresAleatorios();
        }
        //Si no acierta se llama al siguiente intent (Puntiaciones)
        else{
            Intent intentpuntuaciones = new Intent(Juego.this, Puntuaciones.class);
            TextView n = findViewById(R.id.hola);
            String nom = n.getText().toString();

            Data data = new Data.Builder().putString("nombre",nom).putInt("puntos",points).build();
            OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(insertarPuntuaciones.class)
                    .setInputData(data)
                    .build();
            WorkManager.getInstance(Juego.this).enqueue(otwr);

            //Se crea la noticiacion de fin de juego
            elManager.notify(1,builder.build());

            startActivity(intentpuntuaciones);
            finish();
        }
    }
    //Guardar las variables necesarias para recuperarlas en caso de giro
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("punt", points);
        savedInstanceState.putBoolean("bool",cargar);
        savedInstanceState.putStringArrayList("lista",nombres);
        savedInstanceState.putInt("x",x);
        savedInstanceState.putInt("y",y);
    }
}