package com.tativo.app.tativo.Operaciones.Fragmentos;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tativo.app.tativo.Bloques.Clases.DatosSolicitud;
import com.tativo.app.tativo.Operaciones.Actividades.Act_Perfil;
import com.tativo.app.tativo.Operaciones.Clases.DatosPerfilCliente;
import com.tativo.app.tativo.Operaciones.Clases.HistorialOperacion;
import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.Globals;
import com.tativo.app.tativo.Utilidades.ServiciosSoap;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by SISTEMAS1 on 09/04/2016.
 */
public class Frg_Perfil extends Fragment {

    public static final String ARG_SECTION_TITLE = "section_number";

    public static Frg_Perfil newInstance(String sectionTitle) {
        Frg_Perfil fragment = new Frg_Perfil();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_TITLE, sectionTitle);
        fragment.setArguments(args);
        return fragment;
    }

    public Frg_Perfil() {
    }

    View v;

    Button btnPerfilSolicitarPrestamo, btnPerfilFirmarDocumentos;
    TextView lblPerfilNombreCompleto, lblPerfilNombreBanco, lblPerfilNumTarjetaCLABE,
             lblPerfilClasificacion, lblPerfilFechaCalculada, lblPerfilMontoSolicitado,
             lblPerfilCompromiso, lblPerfilFechaInicio, lblPerfilFechaVencimiento,
            lblPerfilDiasTranscurridos, lblPerfilDiasVencimiento, lblPerfilSandoDelDia;
    LinearLayout lyPerfilSolicitar, lyPerfilDatos, lyPerfilSinOperaciones;

    Globals Sesion;
    DatosPerfilCliente datosPerfilCliente;
    HistorialOperacion datosOperacionActual;
    DatosSolicitud Solicitud = new DatosSolicitud();
    Frg_Perfil.AsyncEstatusSolicitud EstatusSolicitud = new Frg_Perfil.AsyncEstatusSolicitud();

    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frg_perfil, container, false);
        //String title = getArguments().getString(ARG_SECTION_TITLE);
        //TextView titulo = (TextView) v.findViewById(R.id.title);
        //titulo.setText(title);
        //titulo.setTextColor(getResources().getColor(R.color.colorBlanco));
        LoadFormControls();
        EventManager();
        Sesion = (Globals) getActivity().getApplicationContext();


        /*
            consultar GetPerfilHistorialCliente
                -SolicitudActiva == true
                    -consultar EstatusSolicitud
                        -PagareEnviado y Pagare Aceptado
                            -Mostrar Datos
                        -PagareEnviado
                            -Mostrar Mensaje SinOperacion
                                -GetDatosDocumentos
                        -Mostrar Mensaje de solicitar
                -Mostrar Mensaje de solicitar
        */
        new AsyncGetPerfilHistorialCliente().execute();


        return v;
    }

    private void LoadFormControls(){
        datosPerfilCliente = new DatosPerfilCliente();
        datosOperacionActual = new HistorialOperacion();

        lyPerfilSolicitar =(LinearLayout) v.findViewById(R.id.lyPerfilSolicitar);
        lyPerfilDatos = (LinearLayout) v.findViewById(R.id.lyPerfilDatos);
        lyPerfilSinOperaciones = (LinearLayout) v.findViewById(R.id.lyPerfilSinOperaciones);
        btnPerfilSolicitarPrestamo = (Button) v.findViewById(R.id.btnPerfilSolicitarPrestamo);
        btnPerfilFirmarDocumentos = (Button) v.findViewById(R.id.btnPerfilFirmarDocumentos);
        lblPerfilNombreCompleto = (TextView) v.findViewById(R.id.lblPerfilNombreCompleto);
        lblPerfilNombreBanco = (TextView) v.findViewById(R.id.lblPerfilNombreBanco);
        lblPerfilNumTarjetaCLABE = (TextView) v.findViewById(R.id.lblPerfilNumTarjetaCLABE);
        lblPerfilClasificacion = (TextView) v.findViewById(R.id.lblPerfilClasificacion);
        lblPerfilFechaCalculada = (TextView) v.findViewById(R.id.lblPerfilFechaCalculada);
        lblPerfilMontoSolicitado = (TextView) v.findViewById(R.id.lblPerfilMontoSolicitado);
        lblPerfilCompromiso = (TextView) v.findViewById(R.id.lblPerfilCompromiso);
        lblPerfilFechaInicio = (TextView) v.findViewById(R.id.lblPerfilFechaInicio);
        lblPerfilFechaVencimiento = (TextView) v.findViewById(R.id.lblPerfilFechaVencimiento);
        lblPerfilDiasTranscurridos = (TextView) v.findViewById(R.id.lblPerfilDiasTranscurridos);
        lblPerfilDiasVencimiento = (TextView) v.findViewById(R.id.lblPerfilDiasVencimiento);
        lblPerfilSandoDelDia = (TextView) v.findViewById(R.id.lblPerfilSandoDelDia);
    }

    private void EventManager() {
        btnPerfilSolicitarPrestamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NavigationView navigationView = (NavigationView) v.findViewById(R.id.nav_view);
                //navigationView.setCheckedItem(R.id.nav_Cotizador);
                Bundle args = new Bundle();
                Fragment fragment = null;
                args.putString(Frg_Cotizador.ARG_SECTION_TITLE, "Cotizador");
                fragment = Frg_Cotizador.newInstance("Cotizador");

                fragment.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.navconten, fragment)
                        .commit();
            }
        });
    }

    //Region Estatus Solicitud
    private class AsyncEstatusSolicitud extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetEstatusSolicitud();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {

            if (Solicitud.isPagareEnviado()) {
                lyPerfilSolicitar.setVisibility(View.GONE);
                lyPerfilSinOperaciones.setVisibility(View.VISIBLE);
                lyPerfilDatos.setVisibility(View.GONE);
                //Consultar GetDatosDocumentos
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

                    //Llenamos los datos de la solicitud
                    Solicitud.setEstatusCliente(datosSolicitud.getProperty("EstatusCliente").toString());
                    Solicitud.setPagareEnviado(Boolean.parseBoolean(datosSolicitud.getProperty("PagareEnviado").toString()));
                    Solicitud.setPagareAceptado(Boolean.parseBoolean(datosSolicitud.getProperty("PagareAceptado").toString()));
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
                    Solicitud.setPinEnviado(Boolean.parseBoolean(datosSolicitud.getProperty("PinEnviado").toString()));
                    Solicitud.setPin(datosSolicitud.getProperty("Pin").toString());
                }else{
                    //Toast.makeText(getApplicationContext(),"Error: "+respuesta.getProperty("Mensaje").toString(),Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    //Region Estatus Solicitud

    //Consultar GetPerfilHistorialCliente
    private class AsyncGetPerfilHistorialCliente extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetPerfilHistorialCliente();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            SetPerfilHitorialCliente();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    private void GetPerfilHistorialCliente(){
        String SOAP_ACTION = "http://tempuri.org/IService1/GetPerfilHistorialCliente";
        String METHOD_NAME = "GetPerfilHistorialCliente";
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
                    SoapObject datosPefil = (SoapObject) Datos.getProperty("PerfilCliente");
                    SoapObject sOperacionActual = (SoapObject) Datos.getProperty("OperacionActual");

                    //Llenamos los datos de la solicitud
                    datosPerfilCliente.setBanco(datosPefil.getProperty("Banco").toString());
                    datosPerfilCliente.setCalificacion(datosPefil.getProperty("Calificacion").toString());
                    datosPerfilCliente.setNombreCompleto(datosPefil.getProperty("NombreCompleto").toString());
                    datosPerfilCliente.setNumeroTarjeta(datosPefil.getProperty("NumeroTarjeta").toString());
                    datosPerfilCliente.setSolicitudActiva(Boolean.parseBoolean(datosPefil.getProperty("SolicitudActiva").toString()));

                    if (sOperacionActual.getPropertyCount() > 0)
                    {
                        datosOperacionActual.setClienteid(sOperacionActual.getProperty("Clienteid").toString());
                        datosOperacionActual.setSolicitudid(sOperacionActual.getProperty("Solicitudid").toString());
                        datosOperacionActual.setFolio(Integer.parseInt(sOperacionActual.getProperty("Folio").toString()));
                        datosOperacionActual.setFechaInicio(new SimpleDateFormat("yyyy-MM-dd").parse(sOperacionActual.getProperty("FechaInicio").toString().substring(0, 10)));
                        datosOperacionActual.setFechaVencimiento(new SimpleDateFormat("yyyy-MM-dd").parse(sOperacionActual.getProperty("FechaVencimiento").toString().substring(0, 10)));
                        datosOperacionActual.setReferencia(sOperacionActual.getProperty("Referencia").toString());
                        datosOperacionActual.setFinanciamiento(Double.parseDouble(sOperacionActual.getProperty("Financiamiento").toString()));
                        datosOperacionActual.setCapital(Double.parseDouble(sOperacionActual.getProperty("Capital").toString()));
                        datosOperacionActual.setInteres(Double.parseDouble(sOperacionActual.getProperty("Interes").toString()));
                        datosOperacionActual.setIVA(Double.parseDouble(sOperacionActual.getProperty("IVA").toString()));
                        datosOperacionActual.setAbono(Double.parseDouble(sOperacionActual.getProperty("Abono").toString()));
                        datosOperacionActual.setMoratorio(Double.parseDouble(sOperacionActual.getProperty("Moratorio").toString()));
                        datosOperacionActual.setTotal(Double.parseDouble(sOperacionActual.getProperty("Total").toString()));
                        datosOperacionActual.setDiasUso(Integer.parseInt(sOperacionActual.getProperty("DiasUso").toString()));
                        datosOperacionActual.setDiasVencimiento(Integer.parseInt(sOperacionActual.getProperty("DiasVencimiento").toString()));
                        datosOperacionActual.setEstatus(Integer.parseInt(sOperacionActual.getProperty("Estatus").toString()));
                        datosOperacionActual.setNumeroTarjeta(sOperacionActual.getProperty("NumeroTarjeta").toString());
                        datosOperacionActual.setBanco(sOperacionActual.getProperty("Banco").toString());
                        datosOperacionActual.setFecha(new SimpleDateFormat("yyyy-MM-dd").parse(sOperacionActual.getProperty("Fecha").toString().substring(0, 10)));
                        datosOperacionActual.setPlazoSolicitud(sOperacionActual.getProperty("PlazoSolicitud").toString());
                    }

                }else{
                    //Toast.makeText(getApplicationContext(),"Error: "+respuesta.getProperty("Mensaje").toString(),Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    private void SetPerfilHitorialCliente(){
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        nf.setMaximumFractionDigits(2);

        lblPerfilNombreCompleto.setText(datosPerfilCliente.getNombreCompleto());
        lblPerfilNombreBanco.setText(datosPerfilCliente.getBanco());
        lblPerfilNumTarjetaCLABE.setText(datosPerfilCliente.getNumeroTarjeta());
        lblPerfilClasificacion.setText(datosPerfilCliente.getCalificacion());
        if (datosOperacionActual.getSolicitudid() != null)
        {
            lyPerfilSolicitar.setVisibility(View.GONE);
            lyPerfilSinOperaciones.setVisibility(View.GONE);
            lyPerfilDatos.setVisibility(View.VISIBLE);

            lblPerfilFechaCalculada.setText("");
            lblPerfilMontoSolicitado.setText(nf.format(datosOperacionActual.getFinanciamiento()));
            lblPerfilCompromiso.setText(datosOperacionActual.getPlazoSolicitud());
            lblPerfilFechaInicio.setText(new SimpleDateFormat("dd/MM/yyyy").format(datosOperacionActual.getFechaInicio()));
            lblPerfilFechaVencimiento.setText(new SimpleDateFormat("dd/MM/yyyy").format(datosOperacionActual.getFechaVencimiento()));
            lblPerfilDiasTranscurridos.setText(String.valueOf(datosOperacionActual.getDiasUso()));
            lblPerfilDiasVencimiento.setText(String.valueOf(datosOperacionActual.getDiasVencimiento()));
        }
        else
        {
            if(datosPerfilCliente.isSolicitudActiva())
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    EstatusSolicitud.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    EstatusSolicitud.execute();
                }
            }
            else
            {
                lyPerfilSolicitar.setVisibility(View.VISIBLE);
                lyPerfilSinOperaciones.setVisibility(View.GONE);
                lyPerfilDatos.setVisibility(View.GONE);
            }
        }
    }
    //Consultar GetPerfilHistorialCliente



}
