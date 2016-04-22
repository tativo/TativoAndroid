package com.tativo.app.tativo.Bloques.Clases;

/**
 * Created by SISTEMAS1 on 22/04/2016.
 */
public class Catpagare {
    private String Pagareid;
    private String Solicitudid;
    private String Clienteid;
    private String NombreCompleto;
    private Double MontoSolicitado;
    private Double MontoPagar;
    private String FechaVence;
    private String Fechaaceptacion;
    private int Estatusaceptacion;
    private String Firmaaceptacion;
    private String Pin;
    private Boolean TieneIFE;
    private int UltimaAct;

    public String getPagareid() {
        return Pagareid;
    }

    public void setPagareid(String pagareid) {
        Pagareid = pagareid;
    }

    public String getSolicitudid() {
        return Solicitudid;
    }

    public void setSolicitudid(String solicitudid) {
        Solicitudid = solicitudid;
    }

    public String getClienteid() {
        return Clienteid;
    }

    public void setClienteid(String clienteid) {
        Clienteid = clienteid;
    }

    public String getNombreCompleto() {
        return NombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        NombreCompleto = nombreCompleto;
    }

    public Double getMontoSolicitado() {
        return MontoSolicitado;
    }

    public void setMontoSolicitado(Double montoSolicitado) {
        MontoSolicitado = montoSolicitado;
    }

    public Double getMontoPagar() {
        return MontoPagar;
    }

    public void setMontoPagar(Double montoPagar) {
        MontoPagar = montoPagar;
    }

    public String getFechaVence() {
        return FechaVence;
    }

    public void setFechaVence(String fechaVence) {
        FechaVence = fechaVence;
    }

    public String getFechaaceptacion() {
        return Fechaaceptacion;
    }

    public void setFechaaceptacion(String fechaaceptacion) {
        Fechaaceptacion = fechaaceptacion;
    }

    public int getEstatusaceptacion() {
        return Estatusaceptacion;
    }

    public void setEstatusaceptacion(int estatusaceptacion) {
        Estatusaceptacion = estatusaceptacion;
    }

    public String getFirmaaceptacion() {
        return Firmaaceptacion;
    }

    public void setFirmaaceptacion(String firmaaceptacion) {
        Firmaaceptacion = firmaaceptacion;
    }

    public String getPin() {
        return Pin;
    }

    public void setPin(String pin) {
        Pin = pin;
    }

    public Boolean getTieneIFE() {
        return TieneIFE;
    }

    public void setTieneIFE(Boolean tieneIFE) {
        TieneIFE = tieneIFE;
    }

    public int getUltimaAct() {
        return UltimaAct;
    }

    public void setUltimaAct(int ultimaAct) {
        UltimaAct = ultimaAct;
    }
}
