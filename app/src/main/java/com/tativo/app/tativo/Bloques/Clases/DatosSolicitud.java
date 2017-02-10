package com.tativo.app.tativo.Bloques.Clases;

import java.util.Date;

/**
 * Created by AlfonsoM on 22/03/2016.
 */
public class DatosSolicitud {

    private String EstatusCliente;
    private boolean PagareEnviado;
    private boolean PagareAceptado;
    private boolean DentroDeHorario;
    private String Solicitudid;
    private double ImporteSolicitud;
    private double Intereses;
    private double IVA;
    private double TotalPagar;
    private Date FechaSolicitud;
    private int DiasUso;
    private Date FechaVence;
    private String NombreCompleto;
    private int BloqueCliente;
    private boolean PinEnviado;
    private String Pin;

    public String getEstatusCliente() {
        return EstatusCliente;
    }

    public void setEstatusCliente(String estatusCliente) {
        EstatusCliente = estatusCliente;
    }

    public boolean isPagareEnviado() {
        return PagareEnviado;
    }

    public void setPagareEnviado(boolean pagareEnviado) {
        PagareEnviado = pagareEnviado;
    }

    public boolean isDentroDeHorario() {
        return DentroDeHorario;
    }

    public void setDentroDeHorario(boolean dentroDeHorario) {
        DentroDeHorario = dentroDeHorario;
    }

    public String getSolicitudid() {
        return Solicitudid;
    }

    public void setSolicitudid(String solicitudid) {
        Solicitudid = solicitudid;
    }

    public double getImporteSolicitud() {
        return ImporteSolicitud;
    }

    public void setImporteSolicitud(double importeSolicitud) {
        ImporteSolicitud = importeSolicitud;
    }

    public double getIntereses() {
        return Intereses;
    }

    public void setIntereses(double intereses) {
        Intereses = intereses;
    }

    public double getIVA() {
        return IVA;
    }

    public void setIVA(double IVA) {
        this.IVA = IVA;
    }

    public double getTotalPagar() {
        return TotalPagar;
    }

    public void setTotalPagar(double totalPagar) {
        TotalPagar = totalPagar;
    }

    public Date getFechaSolicitud() {
        return FechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        FechaSolicitud = fechaSolicitud;
    }

    public int getDiasUso() {
        return DiasUso;
    }

    public void setDiasUso(int diasUso) {
        DiasUso = diasUso;
    }

    public Date getFechaVence() {
        return FechaVence;
    }

    public void setFechaVence(Date fechaVence) {
        FechaVence = fechaVence;
    }

    public String getNombreCompleto() {
        return NombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        NombreCompleto = nombreCompleto;
    }

    public int getBloqueCliente() {
        return BloqueCliente;
    }

    public void setBloqueCliente(int bloqueCliente) {
        BloqueCliente = bloqueCliente;
    }

    public boolean isPagareAceptado() {
        return PagareAceptado;
    }

    public void setPagareAceptado(boolean pagareAceptado) {
        PagareAceptado = pagareAceptado;
    }

    public boolean isPinEnviado() {
        return PinEnviado;
    }

    public void setPinEnviado(boolean pinEnviado) {
        PinEnviado = pinEnviado;
    }

    public String getPin() {
        return Pin;
    }

    public void setPin(String pin) {
        Pin = pin;
    }
}
