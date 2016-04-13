package com.tativo.app.tativo.LogIn.Clases;

import java.util.Date;

/**
 * Created by AlfonsoM on 13/04/2016.
 */
public class CatFechasPago {
    public Date getFechaPago() {
        return FechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        FechaPago = fechaPago;
    }

    public int getDiasDeUso() {
        return DiasDeUso;
    }

    public void setDiasDeUso(int diasDeUso) {
        DiasDeUso = diasDeUso;
    }

    private Date FechaPago;
    private int DiasDeUso;
}
