package com.example.whoisolder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ServicioFirebase extends FirebaseMessagingService {
    public ServicioFirebase() {
    }

    public void onMessageRecieved(RemoteMessage remoteMessage){
        if(remoteMessage.getData().size() > 0){
            //Si el mensaje viene con datos
        }
        if(remoteMessage.getNotification() != null){
            //Si el mensaje es una notificacion
        }

    }

    public void onNetworkToken(String s){
        super.onNewToken(s);
    }
}