package com.tativo.app.tativo.Bloques.Actividades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tativo.app.tativo.Bloques.Clases.CatBloqueoCliente;
import com.tativo.app.tativo.Bloques.Clases.DatosSolicitud;
import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.Globals;
import com.tativo.app.tativo.Utilidades.ServiciosSoap;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Act_Documentos extends AppCompatActivity {

    TextView lblNobreCompleto, lblDireccion, lblTelefono, lblCorreo, lblNombreBanco, lblNumeroTarjetaCLABE, lblMonto, lblInteres, lblIVA, lblTotalPagar, lblFechaInicio, lblPlazo, lblFechaLimite;
    Button btnVerDocumentos, btnFotoFrontal, btnFotoTrasera, btnAceptarDocumentos;
    LinearLayout lybtnVerDocumentos, lyCargaDocumentos;
    AutoCompleteTextView txtFirmaDocumentos;

    Globals Sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_documentos);
    }

    private void LoadFormControls()
    {
        Sesion = new Globals();

        lblNobreCompleto = (TextView) findViewById(R.id.lblNobreCompleto);
        lblDireccion = (TextView) findViewById(R.id.lblDireccion);
        lblTelefono = (TextView) findViewById(R.id.lblTelefono);
        lblCorreo = (TextView) findViewById(R.id.lblCorreo);
        lblNombreBanco = (TextView) findViewById(R.id.lblNombreBanco);
        lblNumeroTarjetaCLABE = (TextView) findViewById(R.id.lblNumeroTarjetaCLABE);
        lblMonto = (TextView) findViewById(R.id.lblMonto);
        lblInteres = (TextView) findViewById(R.id.lblInteres);
        lblIVA = (TextView) findViewById(R.id.lblIVA);
        lblTotalPagar = (TextView) findViewById(R.id.lblTotalPagar);
        lblFechaInicio = (TextView) findViewById(R.id.lblFechaInicio);
        lblPlazo = (TextView) findViewById(R.id.lblPlazo);
        lblFechaLimite = (TextView) findViewById(R.id.lblFechaLimite);

        btnVerDocumentos = (Button) findViewById(R.id.btnVerDocumentos);
        btnFotoFrontal = (Button) findViewById(R.id.btnFotoFrontal);
        btnFotoTrasera = (Button) findViewById(R.id.btnFotoTrasera);
        btnAceptarDocumentos = (Button) findViewById(R.id.btnAceptarDocumentos);

        lybtnVerDocumentos = (LinearLayout) findViewById(R.id.lybtnVerDocumentos);
        lyCargaDocumentos = (LinearLayout) findViewById(R.id.lyCargaDocumentos);

        txtFirmaDocumentos = (AutoCompleteTextView) findViewById(R.id.txtFirmaDocumentos);
    }

    public void FocusManager() {

    }

    private void EventManager() {

    }

    public void FocusNextControl(int o, String ot, int d, String dt) {
        final EditText destino = (dt.toUpperCase() == "T" ? (EditText) findViewById(d) : null);
        final Spinner destinoS = (dt.toUpperCase() == "S" ? (Spinner) findViewById(d) : null);

        if (ot.toUpperCase() == "T") {
            EditText origen = (EditText) findViewById(o);
            origen.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE ||
                            actionId == EditorInfo.IME_ACTION_NEXT) {
                        if (destino != null)
                            destino.requestFocus();
                        else {
                            destinoS.requestFocus();
                            destinoS.performClick();
                        }
                        return false;
                    }
                    return false;
                }
            });
        }
        if (ot.toUpperCase() == "S") {
            Spinner origen = (Spinner) findViewById(o);
            origen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != -1) {
                        if (destino != null)
                            destino.requestFocus();
                        else {
                            destinoS.requestFocus();
                            destinoS.performClick();
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }


    //GUARDAR

    //GUARDAR


    //Info del BLOQUE

    //Info del BLOQUE

    //Region Estatus Solicitud y Bloqueos
    CatBloqueoCliente Bloqueos = new CatBloqueoCliente();
    DatosSolicitud Solicitud = new DatosSolicitud();
    private class AsyncEstatusSolicitud extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            while (true) {
                try {
                    GetEstatusSolicitud();
                    if(Bloqueos.getBloqueoid()!=0)
                        break;
                    Thread.sleep(30000);
                } catch (InterruptedException ex) {
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            //Si salio del ciclo quiere decir que encontro bloqueos
            if (Bloqueos.getBloqueoid() != 0) {
                new AlertDialog.Builder(Act_Documentos.this)
                        .setTitle(R.string.msgRefTitulo)
                        .setMessage(R.string.msgRefNoContesto)
                        .setCancelable(false)
                        .setPositiveButton(R.string.msgRefOk, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Sesion.setBloqueoReferencia(true);
                                Sesion.setBloqueoCliente(Bloqueos);
                                Sesion.setSolicitud(Solicitud);
                                Sesion.setBloqueActual(6);
                                Intent i = new Intent(getApplicationContext(), Act_B1_Referencias.class);
                                startActivity(i);
                                finish();
                            }
                        }).create().show();
            }
        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    private void GetEstatusSolicitud(){
        String SOAP_ACTION = "http://tempuri.org/IService1/GetEstatusSolicitud";
        String METHOD_NAME = "GetEstatusSolicitud";
        String NAMESPACE = "http://tempuri.org/";

        ArrayList<PropertyInfo> valores =  new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("ClienteID");
        pi1.setValue(Sesion.getCliendeID());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION,METHOD_NAME,NAMESPACE,valores);
        if(respuesta != null) {
            try {

                if(Boolean.parseBoolean(respuesta.getProperty("EsValido").toString())){
                    SoapObject Datos = (SoapObject) respuesta.getProperty("Datos");
                    SoapObject datosSolicitud = (SoapObject) Datos.getProperty("datosSolicitud");
                    SoapObject bloqueoCliente = (SoapObject) Datos.getProperty("bloqueoCliente");

                    //Llenamos los datos del bloqueo de la solicitud
                    if(Integer.parseInt(bloqueoCliente.getProperty("Bloqueoid").toString())!=0){
                        Bloqueos.setBloqueoid(Integer.parseInt(bloqueoCliente.getProperty("Bloqueoid").toString()));
                        Bloqueos.setClienteid(bloqueoCliente.getProperty("Clienteid").toString());
                        Bloqueos.setBloque(Integer.parseInt(bloqueoCliente.getProperty("Bloque").toString()));
                        Bloqueos.setEstatus(Integer.parseInt(bloqueoCliente.getProperty("Estatus").toString()));
                        Bloqueos.setDatos(bloqueoCliente.getProperty("Datos").toString());
                    }
                    //Llenamos los datos de la solicitud
                    Solicitud.setEstatusCliente(datosSolicitud.getProperty("EstatusCliente").toString());
                    Solicitud.setPagareEnviado(Boolean.parseBoolean(datosSolicitud.getProperty("PagareEnviado").toString()));
                    Solicitud.setDentroDeHorario(Boolean.parseBoolean(datosSolicitud.getProperty("DentroDeHorario").toString()));
                    Solicitud.setSolicitudid(datosSolicitud.getProperty("Solicitudid").toString());
                    Solicitud.setImporteSolicitud(Double.parseDouble(datosSolicitud.getProperty("ImporteSolicitud").toString()));
                    Solicitud.setImporteSolicitud(Double.parseDouble(datosSolicitud.getProperty("ImporteSolicitud").toString()));
                    Solicitud.setIntereses(Double.parseDouble(datosSolicitud.getProperty("Intereses").toString()));
                    Solicitud.setIVA(Double.parseDouble(datosSolicitud.getProperty("IVA").toString()));
                    Solicitud.setTotalPagar(Double.parseDouble(datosSolicitud.getProperty("TotalPagar").toString()));
                    //Solicitud.setFechaSolicitud(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datosSolicitud.getProperty("FechaSolicitud").toString()));
                    Solicitud.setFechaSolicitud(new SimpleDateFormat("yyyy-MM-dd").parse(datosSolicitud.getProperty("FechaSolicitud").toString().substring(0, 10)));
                    Solicitud.setDiasUso(Integer.parseInt(datosSolicitud.getProperty("DiasUso").toString()));
                    Solicitud.setFechaVence(new SimpleDateFormat("yyyy-MM-dd").parse(datosSolicitud.getProperty("FechaVence").toString().substring(0, 10)));
                    Solicitud.setNombreCompleto(datosSolicitud.getProperty("NombreCompleto").toString());
                    Solicitud.setBloqueCliente(Integer.parseInt(datosSolicitud.getProperty("BloqueCliente").toString()));
                }else{
                    //Toast.makeText(getApplicationContext(),"Error: "+respuesta.getProperty("Mensaje").toString(),Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    //Endregion
}
