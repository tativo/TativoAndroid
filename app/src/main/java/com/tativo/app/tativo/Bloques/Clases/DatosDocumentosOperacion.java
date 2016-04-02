package com.tativo.app.tativo.Bloques.Clases;

/**
 * Created by SISTEMAS1 on 01/04/2016.
 */
public class DatosDocumentosOperacion {
    private DatosDocumentoPagare datosPagare;
    private DatosDocumentoCaratula datosCaratula;
    private DatosDocumentosContrato datosContrato;

    public DatosDocumentoPagare getDatosPagare() {
        return datosPagare;
    }

    public void setDatosPagare(DatosDocumentoPagare datosPagare) {
        this.datosPagare = datosPagare;
    }

    public DatosDocumentoCaratula getDatosCaratula() {
        return datosCaratula;
    }

    public void setDatosCaratula(DatosDocumentoCaratula datosCaratula) {
        this.datosCaratula = datosCaratula;
    }

    public DatosDocumentosContrato getDatosContrato() {
        return datosContrato;
    }

    public void setDatosContrato(DatosDocumentosContrato datosContrato) {
        this.datosContrato = datosContrato;
    }
}
