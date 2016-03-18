package com.tativo.app.tativo.Bloques.Clases;

/**
 * Created by sistemasprimos on 18/03/16.
 */
public class Catidentidadcliente {

    private String Identidadclienteid;
    private String Clienteid;
    private Boolean Tarjetacredito;
    private String Ultimoscuatrodigitos;
    private Boolean Creditohipotecario;
    private Boolean Creditoautomotriz;
    private int UltimaAct;


    public String getIdentidadclienteid() {
        return Identidadclienteid;
    }

    public void setIdentidadclienteid(String identidadclienteid) {
        Identidadclienteid = identidadclienteid;
    }

    public String getClienteid() {
        return Clienteid;
    }

    public void setClienteid(String clienteid) {
        Clienteid = clienteid;
    }

    public Boolean getTarjetacredito() {
        return Tarjetacredito;
    }

    public void setTarjetacredito(Boolean tarjetacredito) {
        Tarjetacredito = tarjetacredito;
    }

    public String getUltimoscuatrodigitos() {
        return Ultimoscuatrodigitos;
    }

    public void setUltimoscuatrodigitos(String ultimoscuatrodigitos) {
        Ultimoscuatrodigitos = ultimoscuatrodigitos;
    }

    public Boolean getCreditohipotecario() {
        return Creditohipotecario;
    }

    public void setCreditohipotecario(Boolean creditohipotecario) {
        Creditohipotecario = creditohipotecario;
    }

    public Boolean getCreditoautomotriz() {
        return Creditoautomotriz;
    }

    public void setCreditoautomotriz(Boolean creditoautomotriz) {
        Creditoautomotriz = creditoautomotriz;
    }

    public int getUltimaAct() {
        return UltimaAct;
    }

    public void setUltimaAct(int ultimaAct) {
        UltimaAct = ultimaAct;
    }
}
