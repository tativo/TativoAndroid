package com.tativo.app.tativo.Operaciones.Clases;

/**
 * Created by sistemasprimos on 04/03/16.
 */
public class DatosEmpresaCuenta {
    private String Banco;
    private String Empresa;
    private String Cuenta;
    private String ClabeInterbancaria;
    private String ConvenioCIE;
    private String Referencias;
    private String ConceptoDescripcion;


    public String getBanco() {
        return Banco;
    }

    public void setBanco(String banco) {
        Banco = banco;
    }

    public String getEmpresa() {
        return Empresa;
    }

    public void setEmpresa(String empresa) {
        Empresa = empresa;
    }

    public String getCuenta() {
        return Cuenta;
    }

    public void setCuenta(String cuenta) {
        Cuenta = cuenta;
    }

    public String getClabeInterbancaria() {
        return ClabeInterbancaria;
    }

    public void setClabeInterbancaria(String clabeInterbancaria) {
        ClabeInterbancaria = clabeInterbancaria;
    }

    public String getConvenioCIE() {
        return ConvenioCIE;
    }

    public void setConvenioCIE(String convenioCIE) {
        ConvenioCIE = convenioCIE;
    }

    public String getReferencias() {
        return Referencias;
    }

    public void setReferencias(String referencias) {
        Referencias = referencias;
    }

    public String getConceptoDescripcion() {
        return ConceptoDescripcion;
    }

    public void setConceptoDescripcion(String conceptoDescripcion) {
        ConceptoDescripcion = conceptoDescripcion;
    }
}
