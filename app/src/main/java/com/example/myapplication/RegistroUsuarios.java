package com.example.myapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ComandosSQL;
import com.example.myapplication.ConexionBBDDUsuarios;
import com.example.myapplication.R;

public class RegistroUsuarios extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mNameEditText;
    private ConexionBBDDUsuarios mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.registro_land);
        }
        else{
            setContentView(R.layout.registro);
        }

        // Inicializar EditText views
        mEmailEditText = findViewById(R.id.email_edit_text);
        mPasswordEditText = findViewById(R.id.password_edit_text);
        mNameEditText = findViewById(R.id.name_edit_text);

        // Inicializar database helper
        mDatabaseHelper = new ConexionBBDDUsuarios(this, "usuarios", null, 1);

        // montar un listener para el botón de registro
        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obtener los campos rellenados de la información del nuevo usuario
                String name = mNameEditText.getText().toString();
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();

                //necesario rellenar todos los campos
                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegistroUsuarios.this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Añadir usuario a db
                    long rowId = mDatabaseHelper.anadir_user(name, email, password);
                    if (rowId == -1) {
                        Toast.makeText(RegistroUsuarios.this, "Registro fallido", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegistroUsuarios.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }
}

