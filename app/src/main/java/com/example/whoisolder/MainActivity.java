package com.example.whoisolder;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Recoger variables del layout
        Button jugar = findViewById(R.id.btn_jugar);
        Button instrucciones = findViewById(R.id.btn_instrucciones);
        Button galeria = findViewById(R.id.btnGaleria);
        //Crear el intent (Registrarse)
        jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentregistro = new Intent(MainActivity.this, Registrarse.class);
                startActivity(intentregistro);
            }
        });
        //Llama al dialogo de las intrucciones del juego
        instrucciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogoInstrucciones dialogo = new DialogoInstrucciones();
                dialogo.show(getSupportFragmentManager(),"instrucciones");
            }
        });
        //Llamar al la pantalla de galeria
        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentgaleria = new Intent(MainActivity.this, Galeria.class);
                startActivity(intentgaleria);
            }
        });
    }
}