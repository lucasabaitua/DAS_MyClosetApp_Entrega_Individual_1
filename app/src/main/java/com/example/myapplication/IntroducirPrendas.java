package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.Entidades.Prenda;

import java.time.temporal.ValueRange;
import java.util.Date;

public class IntroducirPrendas extends AppCompatActivity {

    EditText textoTitulo;
    EditText textoDescrip;
    EditText fecha;
    ImageFilterButton img;

    Prenda prenda;
    private ConexionBBDDLocal mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //en base a la orientación utilizamos un layout u otro
        if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.introducir_prendas_land);
        }
        else{
            setContentView(R.layout.introducir_prendas);
        }
        //en Android 12 +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                //pedir permiso para notificaciones
                ActivityCompat.requestPermissions(this, new
                        String[]{Manifest.permission.POST_NOTIFICATIONS}, 11);
            }
        }

        //Creamos notificationManager y Builder para las notificaciones locales
        NotificationManager elManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(IntroducirPrendas.this, "IdCanal");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel elCanal = new NotificationChannel("IdCanal", "NombreCanal",
                    NotificationManager.IMPORTANCE_DEFAULT);
            elManager.createNotificationChannel(elCanal);
        //editamos el contenido de la notificación
            elBuilder.setSmallIcon(android.R.drawable.stat_sys_warning)
                    .setContentTitle("Llenando tu armario:")
                    .setContentText("Has introducido una nueva prenda!!")
                    .setSubText("Sigue así para llenar tu armario")
                    .setVibrate(new long[]{0, 1000, 500, 1000})
                    .setAutoCancel(true);
        //editamos el canal de la notificación poniendo luces led cuando lleguen
            elCanal.setDescription("Nueva prenda");
            elCanal.enableLights(true);
            elCanal.setLightColor(Color.RED);
            elCanal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            elCanal.enableVibration(true);
        }

        // se declaran variables para los campos donde se introduce texto
        textoTitulo = findViewById(R.id.titulo_edit_text);
        textoDescrip = findViewById(R.id.desc_edit_text);
        fecha = findViewById(R.id.editTextDate);

        mDatabaseHelper = new ConexionBBDDLocal(getApplicationContext(), "prendas", null, 1);
        Button nueva_prenda = findViewById(R.id.button_guardar);
        nueva_prenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pasamos los campos rellenados a String
                String titulo = textoTitulo.getText().toString();
                String desc = textoDescrip.getText().toString();
                String fechaP = fecha.getText().toString();

                //para introducir una prenda nueva hay que rellenar todos los campos
                if (titulo.isEmpty()||fechaP.isEmpty()||desc.isEmpty()){
                    Toast.makeText(IntroducirPrendas.this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                }
                else{
                    // llamamos al método utilizado para añadir una prenda a la BD
                    long rowId = mDatabaseHelper.anadir_prenda(titulo, desc, fechaP);
                    if (rowId==-1){
                        Toast.makeText(IntroducirPrendas.this, "Introduccion de prenda fallida", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //Si se ha añadido la prenda correctamente
                        Toast.makeText(IntroducirPrendas.this, "Se ha introducido la prenda", Toast.LENGTH_SHORT).show();
                        //lanzamos la notificación de que hemos introducido una prenda
                        elManager.notify(1, elBuilder.build());
                        Intent intent = new Intent(IntroducirPrendas.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        });

        Button cancelar = findViewById(R.id.button_cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroducirPrendas.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}


