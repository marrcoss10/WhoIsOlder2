package com.example.whoisolder;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class insertarPuntuaciones extends Worker {
    public insertarPuntuaciones(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        //Se recogen las variables de la clase principal para subirlas al servidor
        String nombre = getInputData().getString("nombre");
        int puntos = getInputData().getInt("puntos",0);
        //Se establece la direccion con el fichero php del servidor
        String direccion = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/mmartinez218/WEB/insertPuntos.php";
        HttpURLConnection urlConnection = null;
        try{
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            //Se establecen las variables para pasarselos al php
            String parametros = "nombre="+nombre+"&puntos="+puntos;

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            out.flush(); // Agregar esta línea para asegurarse de que los datos se envíen correctamente

            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            };

            reader.close();
            inputStream.close();
            out.close();
            urlConnection.disconnect();

            return Result.success();
        }
        //Si ha surgido cualquier error
        catch (Exception e){
            Log.d("DAS", "Error: " + e);
            return Result.failure();
        }
    }
}
