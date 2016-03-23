package com.tativo.app.tativo.Utilidades;

import android.app.Application;

import com.tativo.app.tativo.Bloques.Clases.CatBloqueoCliente;
import com.tativo.app.tativo.Bloques.Clases.DatosSolicitud;

import java.util.Date;

/**
 * Created by SISTEMAS1 on 14/03/2016.
 */
public class Globals extends Application {
    private String CliendeID;
    private double ImporteSolicitado;
    private Date FechaPago;
    private String SolicitudID;

    //Propiedades para administrar los bloqueos
    private boolean BloqueoReferencia;
    private CatBloqueoCliente BloqueoCliente;
    private DatosSolicitud Solicitud;
    private int BloqueActual;

    public boolean isBloqueoReferencia() {
        return BloqueoReferencia;
    }

    public void setBloqueoReferencia(boolean bloqueoReferencia) {
        BloqueoReferencia = bloqueoReferencia;
    }

    public CatBloqueoCliente getBloqueoCliente() {
        return BloqueoCliente;
    }

    public void setBloqueoCliente(CatBloqueoCliente bloqueoCliente) {
        BloqueoCliente = bloqueoCliente;
    }

    public DatosSolicitud getSolicitud() {
        return Solicitud;
    }

    public void setSolicitud(DatosSolicitud solicitud) {
        Solicitud = solicitud;
    }

    public int getBloqueActual() {
        return BloqueActual;
    }

    public void setBloqueActual(int bloqueActual) {
        BloqueActual = bloqueActual;
    }

    public double getImporteSolicitado() {
        return ImporteSolicitado;
    }

    public void setImporteSolicitado(double importeSolicitado) {
        ImporteSolicitado = importeSolicitado;
    }

    public Date getFechaPago() {
        return FechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        FechaPago = fechaPago;
    }

    public String getSolicitudID() {
        return SolicitudID;
    }

    public void setSolicitudID(String solicitudID) {
        SolicitudID = solicitudID;
    }

    public String getCliendeID() {
        return CliendeID;
    }

    public void setCliendeID(String cliendeID) {
        CliendeID = cliendeID;
    }
}
