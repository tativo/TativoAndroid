package com.tativo.app.tativo.Bloques.Clases;

import java.util.Date;

/**
 * Created by AlfonsoM on 08/03/2016.
 */
public class Catanio {
    private int Anioid;
    private String Descripcion;
    private Date Fecha;

    public int getEstatus() {
        return Estatus;
    }

    public void setEstatus(int estatus) {
        Estatus = estatus;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    private int Estatus;
    private String Usuario;

    public Catanio() {
    }


    public Catanio(int anioid, String descripcion, Date fecha, int estatus, String usuario) {
        Anioid = anioid;
        Descripcion = descripcion;
        Fecha = fecha;
        Estatus = estatus;
        Usuario = usuario;
    }

    public int getAnioid() {
        return Anioid;
    }

    public void setAnioid(int anioid) {
        Anioid = anioid;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date fecha) {
        Fecha = fecha;
    }
}
