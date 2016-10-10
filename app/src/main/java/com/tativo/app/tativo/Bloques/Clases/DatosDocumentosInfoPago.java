package com.tativo.app.tativo.Bloques.Clases;

import java.util.Date;

/**
 * Created by sistemasprimos on 23/03/16.
 */
public class DatosDocumentosInfoPago {

    private Date FechaInicio;
    private int Plazo_Compromiso;
    private int Plazo_Limite;
    private Date Fecha_Compromiso;
    private Date Fecha_Limite;
    private Double Interes_Compromiso;
    private Double Interes_Limite;
    private Double IVA_Compromiso;
    private Double IVA_Limite;
    private Double Total_Compromiso;
    private Double Total_Limite;

    public Date getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        FechaInicio = fechaInicio;
    }

    public int getPlazo_Compromiso() {
        return Plazo_Compromiso;
    }

    public void setPlazo_Compromiso(int plazo_Compromiso) {
        Plazo_Compromiso = plazo_Compromiso;
    }

    public int getPlazo_Limite() {
        return Plazo_Limite;
    }

    public void setPlazo_Limite(int plazo_Limite) {
        Plazo_Limite = plazo_Limite;
    }

    public Date getFecha_Compromiso() {
        return Fecha_Compromiso;
    }

    public void setFecha_Compromiso(Date fecha_Compromiso) {
        Fecha_Compromiso = fecha_Compromiso;
    }

    public Date getFecha_Limite() {
        return Fecha_Limite;
    }

    public void setFecha_Limite(Date fecha_Limite) {
        Fecha_Limite = fecha_Limite;
    }

    public Double getInteres_Compromiso() {
        return Interes_Compromiso;
    }

    public void setInteres_Compromiso(Double interes_Compromiso) {
        Interes_Compromiso = interes_Compromiso;
    }

    public Double getInteres_Limite() {
        return Interes_Limite;
    }

    public void setInteres_Limite(Double interes_Limite) {
        Interes_Limite = interes_Limite;
    }

    public Double getIVA_Compromiso() {
        return IVA_Compromiso;
    }

    public void setIVA_Compromiso(Double IVA_Compromiso) {
        this.IVA_Compromiso = IVA_Compromiso;
    }

    public Double getIVA_Limite() {
        return IVA_Limite;
    }

    public void setIVA_Limite(Double IVA_Limite) {
        this.IVA_Limite = IVA_Limite;
    }

    public Double getTotal_Compromiso() {
        return Total_Compromiso;
    }

    public void setTotal_Compromiso(Double total_Compromiso) {
        Total_Compromiso = total_Compromiso;
    }

    public Double getTotal_Limite() {
        return Total_Limite;
    }

    public void setTotal_Limite(Double total_Limite) {
        Total_Limite = total_Limite;
    }
}
