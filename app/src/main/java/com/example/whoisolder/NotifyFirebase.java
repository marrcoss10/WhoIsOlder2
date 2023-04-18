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
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NotifyFirebase extends Worker {
    public NotifyFirebase(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork() {
        //Se recogen las variables de la clase principal para subirlas al servidor
        String token = getInputData().getString("token");
        Log.d("NOTIFY","Token: " + token);
        //Se establece la direccion con el fichero php del servidor
        String direccion = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/mmartinez218/WEB/firebase.php";
        HttpURLConnection urlConnection = null;
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            //Se establecen las variables para pasarselos al php
            String parametros = "token="+token;

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            out.flush(); //Agregar esta línea para asegurarse de que los datos se envíen correctamente

            int status = urlConnection.getResponseCode();
            Log.d("BASEDATOS", status + "");
            //Si se ha conectado correctamente -> 200, OK
            if(status == 200) {
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                //Se devuelve el resultado a la clase principal
                Data datos = new Data.Builder()
                        .putString("result",result)
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

