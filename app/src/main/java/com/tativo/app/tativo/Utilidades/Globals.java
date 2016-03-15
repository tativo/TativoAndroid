package com.tativo.app.tativo.Utilidades;

import android.app.Application;

import java.util.Date;

/**
 * Created by SISTEMAS1 on 14/03/2016.
 */
public class Globals extends Application {
    private String CliendeID;
    private double ImporteSolicitado;
    private Date FechaPago;
    private String SolicitudID;

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
