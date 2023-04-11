package com.example.whoisolder;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Registrarse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        //Recoger variables del layout
        Button comenzar = findViewById(R.id.btnStart);
        //Crear el nuevo intent y pasarle el nombre a (Juego)
        comenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nombre = findViewById(R.id.editName);
                String name = nombre.getText().toString();

                Intent intentjuego = new Intent(Registrarse.this, Juego.class);
                intentjuego.putExtra("nombre",name.replace(" ","")); //Guardamos en variable global y quitamos los epacios
                startActivity(intentjuego);
                finish();
            }
        });
    }
}