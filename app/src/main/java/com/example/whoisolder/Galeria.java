package com.example.whoisolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.Manifest;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.security.spec.EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;

//Video -> https://www.youtube.com/watch?v=s1aOlr3vbbk&list=PLlGT4GXi8_8eopz0Gjkh40GG6O5KhL1V1&ab_channel=SmallAcademy
public class Galeria extends AppCompatActivity {
    ImageView fot;
    String currentPhotoPath;
    StorageReference storageReference;
    Uri direccion;
    Boolean vacio = true;
    static String token;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);
        if(savedInstanceState != null){
            vacio = savedInstanceState.getBoolean("vacio");
            Log.d("MARCOS","DIRECCION: " + direccion);
            if(!vacio){
                direccion = Uri.parse(savedInstanceState.getString("direccion"));
                if(direccion != null){
                    fot  = findViewById(R.id.imgGaleria);
                    Picasso.get().load(direccion).into(fot);
                }
            }
        }

        //Se recogen los datos del layout
        Button foto = findViewById(R.id.btnFoto);
        fot  = findViewById(R.id.imgGaleria);

        storageReference = FirebaseStorage.getInstance().getReference();

        //Se abre la camara
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pedirPermisos();
            }
        });

    }

    public void pedirPermisos(){
        // Si no hay permisos de CAMARA y ALMACENAMIENTO, se piden
        if(ContextCompat.checkSelfPermission(Galeria.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(Galeria.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Galeria.this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERM_CODE);
        }
        else{
            dispatchTakePictureIntent();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }
            else{
                Toast.makeText(getApplicationContext(),"Es necesario el permiso para usar la camara",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(direccion != null){
            outState.putString("direccion",direccion.toString());
        }
        outState.putBoolean("vacio",vacio);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            File f = new File(currentPhotoPath);
            //fot.setImageURI(Uri.fromFile(f));
            Log.d("GALERIA","URL: " + Uri.fromFile(f));

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

            uploadImageToFirebase(f.getName(),contentUri);
        }
    }

    private void uploadImageToFirebase(String name, Uri contentUri){
        StorageReference image = storageReference.child("images/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        vacio = false;
                        direccion = uri;
                        Picasso.get().load(direccion).into(fot);
                    }
                });
                Toast.makeText(Galeria.this,"Upload Successfully.",Toast.LENGTH_SHORT).show();
                //Se llama a la función que envia la notificacion del firebase
                onTokenRefresh();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Galeria.this,"Upload Failed.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File createImageFile() throws IOException {
        // Se crea la imagen con un TimeStamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            //Create the file where the photo should go
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }
            catch (IOException e){

            }
            //Continue only if the file was successfully created
            if(photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(this,"com.example.android.juegoDas.fileprovider",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                startActivityForResult(takePictureIntent,CAMERA_REQUEST_CODE);
            }
        }
    }

    //Se crea la función para notificar con el firebase
    public void onTokenRefresh(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("NOTIFY", "Fetching FCM registration token failed", task.getException());
                    return;
                }

                // Conseguir el token
                token = task.getResult();

                Log.d("NOTIFY", "Token: " + token);
                Data data = new Data.Builder().putString("token", token).build();
                OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(NotifyFirebase.class).setInputData(data).build();
                WorkManager.getInstance(Galeria.this).enqueue(otwr);
                WorkManager.getInstance(Galeria.this).getWorkInfoByIdLiveData(otwr.getId()).observe(Galeria.this, new Observer<WorkInfo>() {
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            //Se recoge el resultado devuelto
                            String resultado = workInfo.getOutputData().getString("result");
                            // Si el php devuelve que se ha identificado CORRECTAMENTE
                            Log.d("NOTIFY", "Resultado --> " + resultado);
                        }
                    }
                });
            }
        });
    }
}