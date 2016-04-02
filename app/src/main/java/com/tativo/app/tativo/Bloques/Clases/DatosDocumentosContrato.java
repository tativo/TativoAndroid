package com.tativo.app.tativo.Bloques.Clases;

/**
 * Created by SISTEMAS1 on 01/04/2016.
 */
public class DatosDocumentosContrato {
    private String Clienteid;
    private String NombreCompleto;
    private String Correo;
    private String Banco;
    private String NumeroDeDeposito;
    private String RFC;
    private String Nacionalidad;

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

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getBanco() {
        return Banco;
    }

    public void setBanco(String banco) {
        Banco = banco;
    }

    public String getNumeroDeDeposito() {
        return NumeroDeDeposito;
    }

    public void setNumeroDeDeposito(String numeroDeDeposito) {
        NumeroDeDeposito = numeroDeDeposito;
    }

    public String getRFC() {
        return RFC;
    }

    public void setRFC(String RFC) {
        this.RFC = RFC;
    }

    public String getNacionalidad() {
        return Nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        Nacionalidad = nacionalidad;
    }
}
