package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import com.example.myapplication.Adaptadores.prendasOverview;
import com.example.myapplication.Entidades.Prenda;

public class MainActivity extends AppCompatActivity {
    //Inicialización de atributos
    ArrayList<Prenda> listaPrendas = new ArrayList<Prenda>();
    prendasOverview prendaOverview ;
    ConexionBBDDLocal ddbb;


    // cuando se inicializa la clase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //dependiendo de la orientación del teléfono se muestra un layout u otro
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_main_land);
        }
        else{
            setContentView(R.layout.activity_main);
        }

        // se llama al método crearLista que muestra en la interfaz la lista de prendas que hay
        //ya registradas anteriormente
        crearLista();
        prendaOverview = new prendasOverview(listaPrendas,getApplicationContext());

        ListView prendas = (ListView) findViewById(R.id.lPrendas);

        // utilizamos un listener para saber cuando se desea eliminar una prenda de la lista
        prendaOverview.setOnItemDeleteListener(new prendasOverview.OnItemDeleteListener() {
            @Override
            public void onItemDelete(int pos) {
                ConexionBBDDLocal bbdd = new ConexionBBDDLocal(getApplicationContext(), "prendas", null, 1);
                SQLiteDatabase sql = bbdd.getWritableDatabase();
                // se borran las prendas que tengan la misma fecha, tanto de la interfaz como de la BD
                sql.delete("prendas", "fecha = ?", new String[]{listaPrendas.get(pos).getFechaColgado()});
                listaPrendas.remove(pos);
                prendaOverview.notifyDataSetChanged();
            }
        });
        // se adapta la lista de Prendas a la manera en la que queremos que se muestre por pantalla
        prendas.setAdapter(prendaOverview);

        Button nuevaPrenda = findViewById(R.id.botonCrearPrenda);
        // cuando deseamos introducir una nueva prenda nos lleva a la clase para introducirla
        nuevaPrenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, IntroducirPrendas.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void crearLista(){
        ListView prendas = (ListView) findViewById(R.id.lPrendas);
        listaPrendas = new ArrayList<Prenda>();
        // se conecta con una instancia de la clase que conecta con la BD, en este caso "prendas"
        ddbb = new ConexionBBDDLocal(getApplicationContext(), "prendas", null, 1);
        // se llama al método en el que se obtienen todas las prendas de la DB prendas
        obtenerTodasLasPrendas();
    }

    public void obtenerTodasLasPrendas(){
        SQLiteDatabase db = ddbb.getReadableDatabase();
        Prenda p = null;
        // se obtienen todos los datos de las prendas
        Cursor c = db.rawQuery("SELECT * FROM prendas", null);
        while(c.moveToNext()){
            // se crean nuevas instancias de prenda con los datos obtenidos
            p = new Prenda(c.getString(1), c.getString(2), c.getString(0), R.drawable.iconoprendas);
            // se añaden en la lista que vamos a mostrar por pantalla en la interfaz
            listaPrendas.add(p);
        }

    }

    public void onClick(View view) {
            Intent miIntent = new Intent(MainActivity.this, IntroducirPrendas.class);
            startActivity(miIntent);
            finish();
    }

}