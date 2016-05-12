package com.tativo.app.tativo.Bloques.Actividades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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


public class Act_Mensajes extends AppCompatActivity {

    TextView lblMSJTitulo, lblMSJTexto;
    Button btnIrADocumentos, btnIrAPerfil, btnTerminarRegistro;

    Globals Sesion;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mensajes);
        Sesion = (Globals) getApplicationContext();
        LoadFormControls();
        EventManager();
        //new AsyncEstatusSolicitud().execute();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new AsyncEstatusSolicitud().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new AsyncEstatusSolicitud().execute();
        }
    }

    private void LoadFormControls() {
        lblMSJTitulo = (TextView) findViewById(R.id.lblMSJTitulo);
        lblMSJTexto = (TextView) findViewById(R.id.lblMSJTexto);
        btnIrADocumentos = (Button) findViewById(R.id.btnIrADocumentos);
        btnIrAPerfil = (Button) findViewById(R.id.btnIrAPerfil);
        btnTerminarRegistro = (Button) findViewById(R.id.btnTerminarRegistro);
    }

    private void EventManager() {
        btnIrADocumentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Act_Documentos.class);
                startActivity(i);
                finish();
            }
        });
    }

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
                    if (Solicitud.getSolicitudid() != null)
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
                new AlertDialog.Builder(Act_Mensajes.this)
                        .setTitle(R.string.msgRefTitulo)
                        .setMessage(R.string.msgRefNoContesto)
                        .setCancelable(false)
                        .setPositiveButton(R.string.msgRefOk, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Sesion.setBloqueoReferencia(true);
                                Sesion.setBloqueoCliente(Bloqueos);
                                Sesion.setSolicitud(Solicitud);
                                Sesion.setBloqueActual(5);
                                Intent i = new Intent(getApplicationContext(), Act_B1_Referencias.class);
                                startActivity(i);
                                finish();
                            }
                        }).create().show();
            }
            else
            {
                if (Solicitud.getEstatusCliente().equals("AUTORIZADO")  && Solicitud.isPagareEnviado() && Solicitud.isDentroDeHorario())
                {
                    lblMSJTitulo.setText(R.string.msjAutPagareEnviadoTitulo);
                    lblMSJTexto.setText(R.string.msjAutPagareEnviadoCuerpo);
                    btnIrADocumentos.setVisibility(View.VISIBLE);
                }
                else if (Solicitud.getEstatusCliente().equals("AUTORIZADO") && Solicitud.isPagareEnviado() && !Solicitud.isDentroDeHorario())
                {
                    lblMSJTitulo.setText(R.string.msjPenAutNoEnHoraTitulo);
                    lblMSJTexto.setText(R.string.msjPenAutNoEnHoraCuerpo);
                    btnIrAPerfil.setVisibility(View.VISIBLE);
                }
                else if (Solicitud.getEstatusCliente().equals("AUTORIZADO") && !Solicitud.isPagareEnviado())
                {
                    lblMSJTitulo.setText(R.string.msjAutSinDocumentoTitulo);
                    lblMSJTexto.setText(R.string.msjAutSinDocumentoCuerpo);
                    btnIrAPerfil.setVisibility(View.VISIBLE);
                }
                else if (Solicitud.getEstatusCliente().equals("PENDIENTE") && Solicitud.isDentroDeHorario())
                {
                    lblMSJTitulo.setText(R.string.msjPenAutEnHoraTitulo);
                    lblMSJTexto.setText(R.string.msjPenAutEnHoraCuerpo);
                    btnIrAPerfil.setVisibility(View.VISIBLE);
                    btnIrAPerfil.setVisibility(View.VISIBLE);
                }
                else if (Solicitud.getEstatusCliente().equals("PENDIENTE") && !Solicitud.isDentroDeHorario())
                {
                    lblMSJTitulo.setText(R.string.msjPenAutNoEnHoraTitulo);
                    lblMSJTexto.setText(R.string.msjPenAutNoEnHoraCuerpo);
                    btnIrAPerfil.setVisibility(View.VISIBLE);
                }
                else if (Solicitud.getEstatusCliente().equals("RECHAZADO"))
                {
                    lblMSJTitulo.setText(R.string.msjRechazadoTitulo);
                    lblMSJTexto.setText(R.string.msjRechazadoCuerpo);
                    btnTerminarRegistro.setVisibility(View.VISIBLE);
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
}
