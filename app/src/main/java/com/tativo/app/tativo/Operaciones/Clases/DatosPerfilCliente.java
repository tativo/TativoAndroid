package com.tativo.app.tativo.Operaciones.Clases;

/**
 * Created by sistemasprimos on 04/03/16.
 */
public class DatosPerfilCliente {
    private String NombreCompleto;
    private String NumeroTarjeta;
    private String Banco;
    private String Calificacion;
    private boolean SolicitudActiva;

    public String getNombreCompleto() {
        return NombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        NombreCompleto = nombreCompleto;
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

    public String getCalificacion() {
        return Calificacion;
    }

    public void setCalificacion(String calificacion) {
        Calificacion = calificacion;
    }

    public boolean isSolicitudActiva() {
        return SolicitudActiva;
    }

    public void setSolicitudActiva(boolean solicitudActiva) {
        SolicitudActiva = solicitudActiva;
    }
}
