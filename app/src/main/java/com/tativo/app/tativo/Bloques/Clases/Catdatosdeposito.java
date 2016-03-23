package com.tativo.app.tativo.Bloques.Clases;

import java.util.Date;

/**
 * Created by AlfonsoM on 22/03/2016.
 */
public class Catdatosdeposito {
    private String Datodepositoid;
    private String Clienteid;
    private String Bancoid;
    private String NumeroDeDeposito;
    private boolean Recibenomina;
    private int Antiguedadid;
    private int Periododepagoid;
    private int Formadepagoid;
    private Date Fechaproxpago;
    private int UltimaAct;

    public String getDatodepositoid() {
        return Datodepositoid;
    }

    public void setDatodepositoid(String datodepositoid) {
        Datodepositoid = datodepositoid;
    }

    public String getClienteid() {
        return Clienteid;
    }

    public void setClienteid(String clienteid) {
        Clienteid = clienteid;
    }

    public String getBancoid() {
        return Bancoid;
    }

    public void setBancoid(String bancoid) {
        Bancoid = bancoid;
    }

    public String getNumeroDeDeposito() {
        return NumeroDeDeposito;
    }

    public void setNumeroDeDeposito(String numeroDeDeposito) {
        NumeroDeDeposito = numeroDeDeposito;
    }

    public boolean isRecibenomina() {
        return Recibenomina;
    }

    public void setRecibenomina(boolean recibenomina) {
        Recibenomina = recibenomina;
    }

    public int getAntiguedadid() {
        return Antiguedadid;
    }

    public void setAntiguedadid(int antiguedadid) {
        Antiguedadid = antiguedadid;
    }

    public int getPeriododepagoid() {
        return Periododepagoid;
    }

    public void setPeriododepagoid(int periododepagoid) {
        Periododepagoid = periododepagoid;
    }

    public int getFormadepagoid() {
        return Formadepagoid;
    }

    public void setFormadepagoid(int formadepagoid) {
        Formadepagoid = formadepagoid;
    }

    public Date getFechaproxpago() {
        return Fechaproxpago;
    }

    public void setFechaproxpago(Date fechaproxpago) {
        Fechaproxpago = fechaproxpago;
    }

    public int getUltimaAct() {
        return UltimaAct;
    }

    public void setUltimaAct(int ultimaAct) {
        UltimaAct = ultimaAct;
    }
}
