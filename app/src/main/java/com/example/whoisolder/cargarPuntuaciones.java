package com.example.whoisolder;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class cargarPuntuaciones extends Worker {
    public cargarPuntuaciones(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        //Se establece la direccion con el fichero php del servidor
        String direccion = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/mmartinez218/WEB/selectPuntuaciones.php";
        HttpURLConnection urlConnection = null;
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            int status = urlConnection.getResponseCode();
            Log.d("BASEDATOS", status + "");
            //Si se ha conectado correctamente -> 200, OK
            if(status == 200) {
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                //Se recogen las lineas devueltas por el resultado
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                //Se recoge el nombre y la puntuacion de cada jugador devuelto
                JSONArray jsonArray = new JSONArray(result);
                ArrayList<String> lista1 = new ArrayList<>();
                ArrayList<String> lista2 = new ArrayList<>();
                for(int i = 0; i < jsonArray.length(); i++){
                    String nombre = jsonArray.getJSONObject(i).getString("nombre");
                    String puntos = jsonArray.getJSONObject(i).getString("puntos");
                    lista1.add(nombre);
                    lista2.add(puntos);
                }
                //Se convierten a arrays de strings para poder devolverlos a la clase principal
                String[] array1 = new String[lista1.size()];
                String[] array2 = new String[lista1.size()];
                for(int i = 0; i < lista1.size(); i++){
                    array1[i] = lista1.get(i);
                    array2[i] = lista2.get(i);
                }
                //Se devuelven los dos arrays a la clase principal
                Data datos = new Data.Builder()
                        .putStringArray("array1",array1) //Nombre de usuario
                        .putStringArray("array2",array2) //Puntos conseguidos
                        .build();
                return Result.success(datos);
            }
        }
        //Si ha surgido cualquier error
        catch (Exception e){
            Log.d("DAS","Error: " + e);
        }
        return Result.failure();
    }
}
