package com.tativo.app.tativo.Bloques.Actividades;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tativo.app.tativo.Bloques.Clases.CatBloqueoCliente;
import com.tativo.app.tativo.Bloques.Clases.DatosDocumentoCaratula;
import com.tativo.app.tativo.Bloques.Clases.DatosDocumentoPagare;
import com.tativo.app.tativo.Bloques.Clases.DatosDocumentosContrato;
import com.tativo.app.tativo.Bloques.Clases.DatosDocumentosInfoPago;
import com.tativo.app.tativo.Bloques.Clases.DatosDocumentosOperacion;
import com.tativo.app.tativo.Bloques.Clases.DatosSolicitud;
import com.tativo.app.tativo.Operaciones.Actividades.Act_Perfil;
import com.tativo.app.tativo.Operaciones.Fragmentos.Frg_Contrato;
import com.tativo.app.tativo.Operaciones.Fragmentos.Frg_Cotizador;
import com.tativo.app.tativo.Operaciones.Fragmentos.Frg_Perfil;
import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.Config;
import com.tativo.app.tativo.Utilidades.Globals;
import com.tativo.app.tativo.Utilidades.ServiciosSoap;
import com.tativo.app.tativo.Utilidades.Utilerias;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class Act_Documentos extends AppCompatActivity implements Frg_Contrato.DialogResponseContrato {

    TextView lblNobreCompleto, lblDireccion, lblTelefono, lblCorreo, lblNombreBanco, lblNumeroTarjetaCLABE, lblMonto;
    TextView lblFechaInicio, lblPlazo_Compromiso, lblPlazo_Limite, lblFecha_Compromiso, lblFecha_Limite, lblInteres_Compromiso, lblInteres_Limite, lblIVA_Compromiso, lblIVA_Limite, lblTotal_Compromiso, lblTotal_Limite;
    Button btnVerDocumentos, btnFotoFrontal, btnFotoTrasera, btnAceptarDocumentos;
    LinearLayout lybtnVerDocumentos, lyCargaDocumentos;
    AutoCompleteTextView txtFirmaDocumentos;
    ScrollView svDocumentos;

    ImageView imgIfeFrontal,imgIfeTrasera;
    LinearLayout progresFrontal,progresTrasera;

    Globals Sesion;
    ProgressDialog progressDialog;

    DatosDocumentoPagare datosPagare;
    DatosDocumentoCaratula datosCaratula;
    DatosDocumentosContrato datosContrato;
    DatosDocumentosOperacion datosOperacion;
    DatosDocumentosInfoPago datosInfoPago;

    AsyncEstatusSolicitud EstatusSolicitud = new AsyncEstatusSolicitud();
    boolean CancelaEstatusSolicitud = false ;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_documentos);
        LoadFormControls();
        FocusManager();
        EventManager();
        Sesion = (Globals) getApplicationContext();
        //Sesion.setCliendeID("BA984F15-FE77-47A3-BCE0-0667D57AFA24");
        //Sesion.setSolicitudID("21F227A4-31D1-499F-A665-D66226DD6CA1");
        new AsyncInfoBloque().execute();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            EstatusSolicitud.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            EstatusSolicitud.execute();
        }
    }

    private void LoadFormControls()
    {
        progressDialog = new ProgressDialog(this);

        datosPagare = new DatosDocumentoPagare();
        datosCaratula = new DatosDocumentoCaratula();
        datosContrato = new DatosDocumentosContrato();
        datosOperacion = new DatosDocumentosOperacion();
        datosInfoPago = new DatosDocumentosInfoPago();

        svDocumentos = (ScrollView) findViewById(R.id.svDocumentos);

        lblNobreCompleto = (TextView) findViewById(R.id.lblNobreCompleto);
        lblDireccion = (TextView) findViewById(R.id.lblDireccion);
        lblTelefono = (TextView) findViewById(R.id.lblTelefono);
        lblCorreo = (TextView) findViewById(R.id.lblCorreo);
        lblNombreBanco = (TextView) findViewById(R.id.lblNombreBanco);
        lblNumeroTarjetaCLABE = (TextView) findViewById(R.id.lblNumeroTarjetaCLABE);
        lblMonto = (TextView) findViewById(R.id.lblMonto);
        lblFechaInicio = (TextView) findViewById(R.id.lblFechaInicio);
        lblFecha_Compromiso = (TextView) findViewById(R.id.lblFechaCompromiso);
        lblFecha_Limite = (TextView) findViewById(R.id.lblFechaLimite);
        lblPlazo_Compromiso = (TextView) findViewById(R.id.lblPlazoCompromiso);
        lblPlazo_Limite = (TextView) findViewById(R.id.lblPlazoLimite);
        lblInteres_Compromiso = (TextView) findViewById(R.id.lblInteresCompromiso);
        lblInteres_Limite = (TextView) findViewById(R.id.lblInteresLimite);
        lblIVA_Compromiso = (TextView) findViewById(R.id.lblIVACompromiso);
        lblIVA_Limite = (TextView) findViewById(R.id.lblIVALimite);
        lblTotal_Compromiso = (TextView) findViewById(R.id.lblTotalCompromiso);
        lblTotal_Limite = (TextView) findViewById(R.id.lblTotalLimite);


        btnVerDocumentos = (Button) findViewById(R.id.btnVerDocumentos);
        btnFotoFrontal = (Button) findViewById(R.id.btnFotoFrontal);
        btnFotoTrasera = (Button) findViewById(R.id.btnFotoTrasera);
        btnAceptarDocumentos = (Button) findViewById(R.id.btnAceptarDocumentos);

        lybtnVerDocumentos = (LinearLayout) findViewById(R.id.lybtnVerDocumentos);
        lyCargaDocumentos = (LinearLayout) findViewById(R.id.lyCargaDocumentos);

        txtFirmaDocumentos = (AutoCompleteTextView) findViewById(R.id.txtFirmaDocumentos);

        imgIfeFrontal = (ImageView) findViewById(R.id.imgIfeFrontal);
        progresFrontal = (LinearLayout) findViewById(R.id.progresFrontal);

        imgIfeTrasera = (ImageView) findViewById(R.id.imgIfeTrasera);
        progresTrasera = (LinearLayout) findViewById(R.id.progresTrasera);
    }

    public void FocusManager() {

    }

    private void EventManager() {
        btnVerDocumentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyCargaDocumentos.setVisibility(View.VISIBLE);
                FragmentManager fragmento = getFragmentManager();
                new Frg_Contrato().show(fragmento, "frmContrato");
            }
        });
        btnFotoFrontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage(true);
            }
        });

        btnFotoTrasera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage(false);
            }
        });

        btnAceptarDocumentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidaGuardar())
                {
                    if (Solicitud.getNombreCompleto().toString() == null || Solicitud.getNombreCompleto().equals("") || Solicitud.getNombreCompleto().toString().isEmpty()) {
                        /*
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new AsyncEstatusSolicitud().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            new AsyncEstatusSolicitud().execute();
                        }
                        */
                    }
                    else
                    {
                        if(txtFirmaDocumentos.getText().toString().toLowerCase().equals(Solicitud.getNombreCompleto().toLowerCase()))
                        {
                            if (!fileNameFrontal.equals(""))
                            {
                                if (!fileNameTrasera.equals("")) {
                                    EstatusSolicitud.cancel(true);
                                    new AsyncGuardar().execute();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Favor de cargar imagen trasera", Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Favor de cargar imagen frontal", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        });
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
    private boolean ValidaGuardar() {
        ArrayList<Object> Objetos = new ArrayList<Object>();
        Objetos.add(txtFirmaDocumentos);
        Collections.reverse(Objetos);
        boolean requeridos = false;
        for (Object item:Objetos) {
            if(item instanceof EditText){
                if (((EditText) item).getText().toString().trim().length() == 0) {
                    ((EditText) item).setError(getString(R.string.msjRequerido));
                    ((EditText) item).requestFocus();
                    requeridos = true;
                }
            }
            if(item instanceof Spinner){
                if(((Spinner) item).getSelectedItemPosition() == 0){
                    ((TextView)((Spinner) item).getSelectedView()).setError(getString(R.string.msjRequerido));
                    ((Spinner) item).requestFocus();
                    requeridos = true;
                }
            }
            if(item instanceof CheckBox){
                ((CheckBox) item).setError(null);
                if(!((CheckBox) item).isChecked()){
                    ((CheckBox) item).setError(getString(R.string.msjRequerido));
                    ((CheckBox) item).requestFocus();
                    requeridos = true;
                }
            }
        }
        return !requeridos;
    }
    private class AsyncGuardar extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GuardarDatos();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            Intent i = new Intent(getApplicationContext(), Act_Perfil.class);
            startActivity(i);
            finish();
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage(getText(R.string.msjGuardando));
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    private void GuardarDatos() {
        String SOAP_ACTION = "http://tempuri.org/IService1/SetCatPagareTelefono";
        String METHOD_NAME = "SetCatPagareTelefono";
        String NAMESPACE = "http://tempuri.org/";

        ArrayList<PropertyInfo> valores =  new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("value");
        pi1.setValue(getEntityToSave());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);
        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE,valores);
        if(respuesta != null) {
            try {
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    private String getEntityToSave() {
        JSONObject DatosEntidad = new JSONObject();
        try {
            DatosEntidad.put("Pagareid", "");
            DatosEntidad.put("Solicitudid", Sesion.getSolicitudID());
            DatosEntidad.put("Clienteid", Sesion.getCliendeID());
            DatosEntidad.put("NombreCompleto", Solicitud.getNombreCompleto());
            DatosEntidad.put("MontoSolicitado", Double.parseDouble("0.0"));
            DatosEntidad.put("MontoPagar", Double.parseDouble("0.0"));
            DatosEntidad.put("FechaVence", new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTimeInMillis()));
            DatosEntidad.put("Fechaaceptacion", new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTimeInMillis()));
            DatosEntidad.put("Estatusaceptacion", 1);
            DatosEntidad.put("Firmaaceptacion", txtFirmaDocumentos.getText());
            DatosEntidad.put("Pin", "");
            DatosEntidad.put("TieneIFE", true);
            DatosEntidad.put("UltimaAct", 0);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }
        return DatosEntidad.toString();
    }
    //GUARDAR


    //Info del BLOQUE
    private class AsyncInfoBloque extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetInfoBloque();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            SetInfoBloque();
        }
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Cargando...");
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    private void GetInfoBloque(){
        String SOAP_ACTION = "http://tempuri.org/IService1/GetDatosDocumentos";
        String METHOD_NAME = "GetDatosDocumentos";
        String NAMESPACE = "http://tempuri.org/";

        ArrayList<PropertyInfo> valores =  new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("SolicitudID");
        pi1.setValue(Sesion.getSolicitudID());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION,METHOD_NAME,NAMESPACE,valores);
        if(respuesta != null) {
            try
            {
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
                if (ev)
                {
                    String[] listaRespuesta;
                    listaRespuesta = new String[respuesta.getPropertyCount()];
                    SoapObject iOperacion = (SoapObject) respuesta.getProperty(0);
                    SoapObject iPagare = (SoapObject) iOperacion.getProperty("DatosPagare");
                    SoapObject iCaratula = (SoapObject) iOperacion.getProperty("DatosCaratula");
                    SoapObject iContrato = (SoapObject) iOperacion.getProperty("DatosContrato");
                    SoapObject iInfoPago = (SoapObject) iOperacion.getProperty("DatosInfPago");

                    if(iPagare.getProperty("ClienteID") != null)
                    {
                        datosPagare.setFolio(iPagare.getProperty("Folio").toString());
                        datosPagare.setCodigo(Integer.parseInt(iPagare.getProperty("Codigo").toString()));
                        datosPagare.setCodigoLargo(iPagare.getProperty("CodigoLargo").toString());
                        datosPagare.setClienteID(iPagare.getProperty("ClienteID").toString());
                        datosPagare.setNombreCompleto(iPagare.getProperty("NombreCompleto").toString());
                        datosPagare.setFechaSolicitud(new SimpleDateFormat("yyyy-MM-dd").parse(iPagare.getProperty("FechaSolicitud").toString().substring(0, 10)));
                        datosPagare.setFinanciamiento(Double.parseDouble(iPagare.getProperty("Financiamiento").toString()));
                        datosPagare.setFechaDocu(iPagare.getProperty("FechaDocu").toString());
                        datosPagare.setFechaVence(new SimpleDateFormat("yyyy-MM-dd").parse(iPagare.getProperty("FechaVence").toString().substring(0, 10)));
                        datosPagare.setTasa(Double.parseDouble(iPagare.getProperty("tasa").toString()));
                        datosPagare.setTasaMoratoria(Double.parseDouble(iPagare.getProperty("TasaMoratoria").toString()));
                        datosPagare.setDomicilio(iPagare.getProperty("Domicilio").toString());
                    }

                    if(iCaratula.getProperty("ClienteID") != null)
                    {
                        datosCaratula.setClienteID(iCaratula.getProperty("ClienteID").toString());
                        datosCaratula.setNombreCompleto(iCaratula.getProperty("NombreCompleto").toString());
                        datosCaratula.setDomicilio(iCaratula.getProperty("Domicilio").toString());
                        datosCaratula.setTelefono(iCaratula.getProperty("Telefono").toString());
                        datosCaratula.setCorreo(iCaratula.getProperty("Correo").toString());
                        datosCaratula.setBanco(iCaratula.getProperty("Banco").toString());
                        datosCaratula.setNumeroDeTarjeta(iCaratula.getProperty("NumeroDeTarjeta").toString());
                        datosCaratula.setClabe(iCaratula.getProperty("Clabe").toString());
                        datosCaratula.setCapital(Double.parseDouble(iCaratula.getProperty("Capital").toString()));
                        datosCaratula.setInteres(Double.parseDouble(iCaratula.getProperty("Interes").toString()));
                        datosCaratula.setIVA(Double.parseDouble(iCaratula.getProperty("IVA").toString()));
                        datosCaratula.setTotalPagar(Double.parseDouble(iCaratula.getProperty("TotalPagar").toString()));
                        datosCaratula.setFechaSolicitud(new SimpleDateFormat("yyyy-MM-dd").parse(iCaratula.getProperty("FechaSolicitud").toString().substring(0, 10)));
                        datosCaratula.setFechaVence(new SimpleDateFormat("yyyy-MM-dd").parse(iCaratula.getProperty("FechaVence").toString().substring(0, 10)));
                        datosCaratula.setNumeroContrato(iCaratula.getProperty("NumeroContrato").toString());
                        datosCaratula.setFechaContrato(iCaratula.getProperty("FechaContrato").toString());
                        datosCaratula.setPlazo(Integer.parseInt(iCaratula.getProperty("Plazo").toString()));


                        datosInfoPago.setFechaInicio(new SimpleDateFormat("yyyy-MM-dd").parse(iInfoPago.getProperty("FechaInicio").toString().substring(0, 10)));
                        datosInfoPago.setPlazo_Compromiso(Integer.parseInt(iInfoPago.getProperty("Plazo_Compromiso").toString()));
                        datosInfoPago.setPlazo_Limite(Integer.parseInt(iInfoPago.getProperty("Plazo_Limite").toString()));
                        datosInfoPago.setFecha_Compromiso(new SimpleDateFormat("yyyy-MM-dd").parse(iInfoPago.getProperty("Fecha_Compromiso").toString().substring(0, 10)));
                        datosInfoPago.setFecha_Limite(new SimpleDateFormat("yyyy-MM-dd").parse(iInfoPago.getProperty("Fecha_Limite").toString().substring(0, 10)));
                        datosInfoPago.setInteres_Compromiso(Double.parseDouble(iInfoPago.getProperty("Interes_Compromiso").toString()));
                        datosInfoPago.setInteres_Limite(Double.parseDouble(iInfoPago.getProperty("Interes_Limite").toString()));
                        datosInfoPago.setIVA_Compromiso(Double.parseDouble(iInfoPago.getProperty("IVA_Compromiso").toString()));
                        datosInfoPago.setIVA_Limite(Double.parseDouble(iInfoPago.getProperty("IVA_Limite").toString()));
                        datosInfoPago.setTotal_Compromiso(Double.parseDouble(iInfoPago.getProperty("Total_Compromiso").toString()));
                        datosInfoPago.setTotal_Limite(Double.parseDouble(iInfoPago.getProperty("Total_Limite").toString()));
                    }

                    if(iContrato.getProperty("Clienteid") != null)
                    {
                        datosContrato.setClienteid(iContrato.getProperty("Clienteid").toString());
                        datosContrato.setNombreCompleto(iContrato.getProperty("NombreCompleto").toString());
                        datosContrato.setCorreo(iContrato.getProperty("Correo").toString());
                        datosContrato.setBanco(iContrato.getProperty("Banco").toString());
                        datosContrato.setNumeroDeDeposito(iContrato.getProperty("NumeroDeDeposito").toString());
                        datosContrato.setRFC(iContrato.getProperty("RFC").toString());
                        datosContrato.setNacionalidad(iContrato.getProperty("Nacionalidad").toString());
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void SetInfoBloque() {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        nf.setMaximumFractionDigits(2);

        if (datosCaratula.getClienteID() != null)
        {
            lblNobreCompleto.setText(datosCaratula.getNombreCompleto());
            lblDireccion.setText(datosCaratula.getDomicilio());
            lblTelefono.setText(datosCaratula.getTelefono());
            lblCorreo.setText(datosCaratula.getCorreo());
            lblNombreBanco.setText(datosCaratula.getBanco());
            lblNumeroTarjetaCLABE.setText(datosCaratula.getNumeroDeTarjeta());
            lblMonto.setText(nf.format(datosCaratula.getCapital()));

            lblFechaInicio.setText(new SimpleDateFormat("dd/MM/yyyy").format(datosInfoPago.getFechaInicio()));
            lblPlazo_Compromiso.setText(String.valueOf(datosInfoPago.getPlazo_Compromiso()));
            lblPlazo_Limite.setText(String.valueOf(datosInfoPago.getPlazo_Limite()));
            lblInteres_Compromiso.setText(nf.format(datosInfoPago.getInteres_Compromiso()));
            lblInteres_Limite.setText(nf.format(datosInfoPago.getInteres_Limite()));
            lblIVA_Compromiso.setText(nf.format(datosInfoPago.getIVA_Compromiso()));
            lblIVA_Limite.setText(nf.format(datosInfoPago.getIVA_Limite()));
            lblTotal_Compromiso.setText(nf.format(datosInfoPago.getTotal_Compromiso()));
            lblTotal_Limite.setText(nf.format(datosInfoPago.getTotal_Limite()));
            lblFecha_Compromiso.setText(new SimpleDateFormat("dd/MM/yyyy").format(datosInfoPago.getFecha_Compromiso()));
            lblFecha_Limite.setText(new SimpleDateFormat("dd/MM/yyyy").format(datosInfoPago.getFecha_Limite()));
        }




    }
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
                    int Segundos = 0;
                    while(Segundos <=30) {
                        if(isCancelled()){
                            CancelaEstatusSolicitud = true;
                            break;
                        }
                        Thread.sleep(1000);
                        Segundos++;
                    }
                } catch (InterruptedException ex) {
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            if (!CancelaEstatusSolicitud) {
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



    //Region CAPTURA DE IMAGENES
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE_F = 100;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE_T = 200;

    public static final int MEDIA_TYPE_IMAGE_FRONTAL = 1;
    public static final int MEDIA_TYPE_IMAGE_TRASERA = 2;
    private Uri fileUri;
    private String fileNameFrontal="",fileNameTrasera="";
    private static final String TAG = Act_Documentos.class.getSimpleName();
    private boolean isFrontal=false;


    private void captureImage(boolean isFrontal) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri((isFrontal ? MEDIA_TYPE_IMAGE_FRONTAL:MEDIA_TYPE_IMAGE_TRASERA));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, (isFrontal ? CAMERA_CAPTURE_IMAGE_REQUEST_CODE_F:CAMERA_CAPTURE_IMAGE_REQUEST_CODE_T));
    }
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    private File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("HHmmss",
                Locale.getDefault()).format(new Date());

        File mediaFile;

        if (type == MEDIA_TYPE_IMAGE_FRONTAL) {
            fileNameFrontal = Sesion.getCliendeID().replace("-","")+"_T"+String.valueOf(MEDIA_TYPE_IMAGE_FRONTAL)+ "_" + timeStamp + ".jpg";
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + fileNameFrontal);
        } else if (type == MEDIA_TYPE_IMAGE_TRASERA) {
            fileNameTrasera =Sesion.getCliendeID().replace("-","")+"_T"+String.valueOf(MEDIA_TYPE_IMAGE_TRASERA)+ "_" + timeStamp + ".jpg";
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + fileNameTrasera);
        } else {
            return null;
        }
        return mediaFile;
    }
    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }
    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE_T || requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE_F) {
            if (resultCode == RESULT_OK) {
                // bimatp factory
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                if(requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE_F){
                    isFrontal = true;
                    imgIfeFrontal.setImageBitmap(bitmap);
                }else{
                    isFrontal = false;
                    imgIfeTrasera.setImageBitmap(bitmap);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new UploadFileToServer(requestCode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    new UploadFileToServer(requestCode).execute();
                }

                // successfully captured the image
                // launching upload activity
                //launchUploadActivity(true);
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "Se ha cancelado la captura de la imagen", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Algo fallo al capturar la imagen", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        Integer TipoDocto =0;
        public UploadFileToServer(Integer tipoDoc){
            super();
            TipoDocto = tipoDoc;
        }
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            if(isFrontal)
                progresFrontal.setVisibility(View.VISIBLE);
            else
                progresTrasera.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected String doInBackground(Void... params) {
            uploadFile(fileUri.getPath());
            RegistrarDocumento(TipoDocto);
            return "Listo";
        }
        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);
            if(isFrontal)
                progresFrontal.setVisibility(View.GONE);
            else
                progresTrasera.setVisibility(View.GONE);
            super.onPostExecute(result);
        }
    }
    public int uploadFile(String sourceFileUri) {
        int serverResponseCode = 0;

        String fileName = sourceFileUri;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {
            return 0;
        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(Config.FILE_UPLOAD_URL);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("image", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                //return ("Upload file to server", "error: " + ex.getMessage());
            } catch (Exception e) {
                //return("Upload file to server Exception", "Exception : " + e.getMessage(), e);
            }
            //dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }

    private void RegistrarDocumento(Integer TipoDocumento){
        String SOAP_ACTION = "http://tempuri.org/IService1/RegistrarDocumento";
        String METHOD_NAME = "RegistrarDocumento";
        String NAMESPACE = "http://tempuri.org/";

        ArrayList<PropertyInfo> valores =  new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("clienteid");
        pi1.setValue(Sesion.getCliendeID());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        PropertyInfo  pi2 = new PropertyInfo();
        pi2.setName("tipo");
        pi2.setValue((TipoDocumento == CAMERA_CAPTURE_IMAGE_REQUEST_CODE_F?"1":"2"));
        pi2.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi2);

        PropertyInfo  pi3 = new PropertyInfo();
        pi3.setName("fileName");
        pi3.setValue((TipoDocumento == CAMERA_CAPTURE_IMAGE_REQUEST_CODE_F?fileNameFrontal:fileNameTrasera));
        pi3.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi3);

        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE,valores);
        if(respuesta != null) {
            try {
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }


    //Endregion




    @Override
    public void onPossitiveButtonClick() {
        btnVerDocumentos.setBackgroundColor(getResources().getColor(R.color.colorAmarillo));
        svDocumentos.scrollTo(0,svDocumentos.getScrollY() + 600);
    }

    @Override
    public void onNegativeButtonClick() {
        Toast.makeText(
                this,
                "No se aceptaron los terminos",
                Toast.LENGTH_LONG)
                .show();
    }
}
