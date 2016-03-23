package com.tativo.app.tativo.Bloques.Clases;

/**
 * Created by AlfonsoM on 17/03/2016.
 */
public class Catbanco {
    private String Bancoid;
    private  int Codigo;
    private String Nombre;
    public Catbanco(){

    }
    public String getBancoid() {
        return Bancoid;
    }

    public void setBancoid(String bancoid) {
        Bancoid = bancoid;
    }

    public int getCodigo() {
        return Codigo;
    }

    public void setCodigo(int codigo) {
        Codigo = codigo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
