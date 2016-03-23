package com.tativo.app.tativo.Bloques.Clases;

import java.util.Date;

/**
 * Created by AlfonsoM on 22/03/2016.
 */
public class CatBloqueoCliente {
    private int Bloqueoid;
    private String Clienteid;
    private int Bloque;
    private int Estatus;
    private String Datos;
    private String Usuario;
    private Date Fecha;

    public int getBloqueoid() {
        return Bloqueoid;
    }

    public void setBloqueoid(int bloqueoid) {
        Bloqueoid = bloqueoid;
    }

    public String getClienteid() {
        return Clienteid;
    }

    public void setClienteid(String clienteid) {
        Clienteid = clienteid;
    }

    public int getBloque() {
        return Bloque;
    }

    public void setBloque(int bloque) {
        Bloque = bloque;
    }

    public int getEstatus() {
        return Estatus;
    }

    public void setEstatus(int estatus) {
        Estatus = estatus;
    }

    public String getDatos() {
        return Datos;
    }

    public void setDatos(String datos) {
        Datos = datos;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date fecha) {
        Fecha = fecha;
    }
}
