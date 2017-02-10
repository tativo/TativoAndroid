package com.tativo.app.tativo.Operaciones.Clases;

import java.util.Date;

/**
 * Created by sistemasprimos on 04/03/16.
 */
public class HistorialOperacion {
    private String Clienteid;
    private String Solicitudid;
    private int Folio;
    private Date FechaInicio;
    private Date FechaVencimiento;
    private String Referencia;
    private Double Financiamiento;
    private Double Capital;
    private Double Interes;
    private Double IVA;
    private Double Abono;
    private Double Moratorio;
    private Double Total;
    private int DiasUso;
    private int DiasVencimiento;
    private int Estatus;
    private String NumeroTarjeta;
    private String Banco;
    private Date Fecha;
    private String PlazoSolicitud;

    public String getClienteid() {
        return Clienteid;
    }

    public void setClienteid(String clienteid) {
        Clienteid = clienteid;
    }

    public String getSolicitudid() {
        return Solicitudid;
    }

    public void setSolicitudid(String solicitudid) {
        Solicitudid = solicitudid;
    }

    public int getFolio() {
        return Folio;
    }

    public void setFolio(int folio) {
        Folio = folio;
    }

    public Date getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        FechaInicio = fechaInicio;
    }

    public Date getFechaVencimiento() {
        return FechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        FechaVencimiento = fechaVencimiento;
    }

    public String getReferencia() {
        return Referencia;
    }

    public void setReferencia(String referencia) {
        Referencia = referencia;
    }

    public Double getFinanciamiento() {
        return Financiamiento;
    }

    public void setFinanciamiento(Double financiamiento) {
        Financiamiento = financiamiento;
    }

    public Double getCapital() {
        return Capital;
    }

    public void setCapital(Double capital) {
        Capital = capital;
    }

    public Double getInteres() {
        return Interes;
    }

    public void setInteres(Double interes) {
        Interes = interes;
    }

    public Double getIVA() {
        return IVA;
    }

    public void setIVA(Double IVA) {
        this.IVA = IVA;
    }

    public Double getAbono() {
        return Abono;
    }

    public void setAbono(Double abono) {
        Abono = abono;
    }

    public Double getMoratorio() {
        return Moratorio;
    }

    public void setMoratorio(Double moratorio) {
        Moratorio = moratorio;
    }

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double total) {
        Total = total;
    }

    public int getDiasUso() {
        return DiasUso;
    }

    public void setDiasUso(int diasUso) {
        DiasUso = diasUso;
    }

    public int getDiasVencimiento() {
        return DiasVencimiento;
    }

    public void setDiasVencimiento(int diasVencimiento) {
        DiasVencimiento = diasVencimiento;
    }

    public int getEstatus() {
        return Estatus;
    }

    public void setEstatus(int estatus) {
        Estatus = estatus;
    }

    public String getNumeroTarjeta() {
        return NumeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        NumeroTarjeta = numeroTarjeta;
    }

    public String getBanco() {
        return Banco;
    }

    public void setBanco(String banco) {
        Banco = banco;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date fecha) {
        Fecha = fecha;
    }

    public String getPlazoSolicitud() {
        return PlazoSolicitud;
    }

    public void setPlazoSolicitud(String plazoSolicitud) {
        PlazoSolicitud = plazoSolicitud;
    }
}
