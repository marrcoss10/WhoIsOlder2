package com.example.whoisolder;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ServicioFirebase extends FirebaseMessagingService {
    public ServicioFirebase() {
    }

    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData().size() > 0){
            //Si el mensaje viene con datos
            Log.d("Prueba_Mensaje", "El mensaje en el if es --> " + remoteMessage.getData());
        }
        if(remoteMessage.getNotification() != null){
            //Si el mensaje es una notificacion
            Log.d("Prueba_Mensaje", "El mensaje es --> " + remoteMessage.getNotification().getBody());
            //Se genera la notificacion
            NotificationManager elManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(ServicioFirebase.this, "id_canal");
            //Se comprueba si hay que solicitar permisos
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel elCanal = new NotificationChannel("id_canal", "Mensajeria_FCM", NotificationManager.IMPORTANCE_DEFAULT);
                elManager.createNotificationChannel(elCanal);
            }
            //Se configura el contenido de la notificacion
            elBuilder.setSmallIcon(android.R.drawable.arrow_up_float)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setVibrate(new long[] {0, 1000, 500, 1000})
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.logoapppeque))
                    .setAutoCancel(false);
            elManager.notify(1, elBuilder.build());
        }

    }

    public void onNewToken(String s){
        super.onNewToken(s);
    }
}