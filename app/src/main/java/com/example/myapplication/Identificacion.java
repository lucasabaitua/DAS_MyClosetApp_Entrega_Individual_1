package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Identificacion extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //dependiendo de la orientación del teléfono
        if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.inicio_sesion_land);
        }
        else{
            setContentView(R.layout.inicio_sesion);
        }
        Button iniciarSesion = findViewById(R.id.botonLogin);
        // si se pulsa el botón de iniciar sesión
        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //nos comprueba si existe el usuario
                EditText user = findViewById(R.id.usuarioEditText);
                EditText pass = findViewById(R.id.contraEditText);
                comprobarIniSes(user.getText().toString(), pass.getText().toString());
            }
        });

        Button registrarse = findViewById(R.id.botonRegistro);
        // si se pulsa el botón de registrarse
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // nos lleva al menú de registro
                Intent intent = new Intent(Identificacion.this, RegistroUsuarios.class);
                startActivity(intent);
            }
        });
    }

    public void comprobarIniSes(String usuario, String contra){
        //se busca en la BD si el usuario existe
        ConexionBBDDUsuarios bdusers = new ConexionBBDDUsuarios(this, "usuarios",null, 1);
        SQLiteDatabase sql = bdusers.getReadableDatabase();
        String[] arguments = new String[] {usuario};
        Cursor c = sql.rawQuery("SELECT * FROM usuarios WHERE email = ? ", arguments);
        if (c.moveToNext()){
            //si existe nos lleva al menú principal
            Intent intent = new Intent(Identificacion.this, MainActivity.class);
            startActivity(intent);
        }
        else{
            // si no existe nos lo notifica
            Toast.makeText(this, "No existe el usuario", Toast.LENGTH_SHORT).show();
        }
    }
}