package com.example.myapplication.Entidades;

import android.graphics.Color;
import android.media.Image;

import java.io.Serializable;

public class Prenda implements Serializable {
    private int img;
    private String tituloPrenda;
    private String descripcion;
    private String fechaColgado;

    //Constructora de la clase prenda como entidad
    public Prenda(String pTit, String pDesc,String pFechaC, int pFoto){
        this.tituloPrenda=pTit;
        this.descripcion=pDesc;
        this.fechaColgado=pFechaC;
        this.img=pFoto;
    }

    public String getTituloPrenda(){
        return this.tituloPrenda;
    }

    public String getDescripcion(){
        return this.descripcion;
    }

    public String getFechaColgado(){
        return this.fechaColgado;
    }

    public int getFoto(){
        return this.img;
    }
}
