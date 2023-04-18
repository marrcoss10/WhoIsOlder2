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

public class cargarFamosos extends Worker{
    public cargarFamosos(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("ERRORES","ENTRA DOWORK");
        //Se establece la direccion con el fichero php del servidor
        String direccion = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/mmartinez218/WEB/selectFamosos.php";
        HttpURLConnection urlConnection = null;
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            int status = urlConnection.getResponseCode();
            Log.d("ERRORES",status + "");
            //Si se ha conectado correctamente -> 200, OK
            if(status == 200){
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(inputStream, "UTF-8"));
                String line, result="";
                //Se recogen las lineas devueltas por el resultado
                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                inputStream.close();
                //Se convierte el resultado a un JSON, al ser varios, se crea un JSONArray
                JSONArray jsonArray = new JSONArray(result);
                ArrayList<String> lista1 = new ArrayList<>();
                ArrayList<String> lista2 = new ArrayList<>();
                //Se recoge el nombre y la edad de cada famoso devuelto
                for(int i = 0; i < jsonArray.length(); i++){
                    String nombre = jsonArray.getJSONObject(i).getString("nombre");
                    String edad = jsonArray.getJSONObject(i).getString("edad");
                    lista1.add(nombre);
                    lista2.add(edad);
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
                        .putStringArray("array1",array1) //Nombres
                        .putStringArray("array2",array2) //Edades
                        .build();
                return Result.success(datos);
            }
            return Result.success();
        }
        //Si ha surgido cualquier error
        catch (Exception e){
            Log.d("ERRORES","Error: " + e);
            return Result.failure();
        }
    }
}
