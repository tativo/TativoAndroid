package com.tativo.app.tativo.Bloques.Clases;

import java.util.Date;

/**
 * Created by AlfonsoM on 08/03/2016.
 */
public class Catrelacionespersonal {
    private int Relacionid;
    private String Descripcion;
    private Date Fecha;
    private int Estatus;
    private String Usuario;

    public Catrelacionespersonal() {
    }

    public Catrelacionespersonal(int relacionid, String descripcion, Date fecha, int estatus, String usuario) {
        Relacionid = relacionid;
        Descripcion = descripcion;
        Fecha = fecha;
        Estatus = estatus;
        Usuario = usuario;
    }


    public int getRelacionid() {
        return Relacionid;
    }

    public void setRelacionid(int relacionid) {
        Relacionid = relacionid;
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
}
