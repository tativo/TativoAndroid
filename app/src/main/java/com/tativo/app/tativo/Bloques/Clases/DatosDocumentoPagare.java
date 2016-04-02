package com.tativo.app.tativo.Bloques.Clases;

import java.util.Date;

/**
 * Created by SISTEMAS1 on 01/04/2016.
 */
public class DatosDocumentoPagare {
    private String Folio;
    private int Codigo;
    private String CodigoLargo;
    private String ClienteID;
    private String NombreCompleto;
    private Date FechaSolicitud;
    private Double Financiamiento;
    private String FechaDocu;
    private Date FechaVence;
    private Double tasa;
    private Double TasaMoratoria;
    private String Domicilio;

    public String getFolio() {
        return Folio;
    }

    public void setFolio(String folio) {
        Folio = folio;
    }

    public int getCodigo() {
        return Codigo;
    }

    public void setCodigo(int codigo) {
        Codigo = codigo;
    }

    public String getCodigoLargo() {
        return CodigoLargo;
    }

    public void setCodigoLargo(String codigoLargo) {
        CodigoLargo = codigoLargo;
    }

    public String getClienteID() {
        return ClienteID;
    }

    public void setClienteID(String clienteID) {
        ClienteID = clienteID;
    }

    public String getNombreCompleto() {
        return NombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        NombreCompleto = nombreCompleto;
    }

    public Date getFechaSolicitud() {
        return FechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        FechaSolicitud = fechaSolicitud;
    }

    public Double getFinanciamiento() {
        return Financiamiento;
    }

    public void setFinanciamiento(Double financiamiento) {
        Financiamiento = financiamiento;
    }

    public String getFechaDocu() {
        return FechaDocu;
    }

    public void setFechaDocu(String fechaDocu) {
        FechaDocu = fechaDocu;
    }

    public Date getFechaVence() {
        return FechaVence;
    }

    public void setFechaVence(Date fechaVence) {
        FechaVence = fechaVence;
    }

    public Double getTasa() {
        return tasa;
    }

    public void setTasa(Double tasa) {
        this.tasa = tasa;
    }

    public Double getTasaMoratoria() {
        return TasaMoratoria;
    }

    public void setTasaMoratoria(Double tasaMoratoria) {
        TasaMoratoria = tasaMoratoria;
    }

    public String getDomicilio() {
        return Domicilio;
    }

    public void setDomicilio(String domicilio) {
        Domicilio = domicilio;
    }
}
