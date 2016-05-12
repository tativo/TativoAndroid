package com.tativo.app.tativo.LogIn.Fragmentos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tativo.app.tativo.Bloques.Actividades.Act_B1_Referencias;
import com.tativo.app.tativo.Bloques.Actividades.Act_B2_Personal;
import com.tativo.app.tativo.Bloques.Actividades.Act_B3_InfDeposito;
import com.tativo.app.tativo.Bloques.Actividades.Act_B4_Laboral;
import com.tativo.app.tativo.Bloques.Actividades.Act_B5_General;
import com.tativo.app.tativo.Bloques.Actividades.Act_Documentos;
import com.tativo.app.tativo.Bloques.Actividades.Act_Mensajes;
import com.tativo.app.tativo.Bloques.Clases.CatBloqueoCliente;
import com.tativo.app.tativo.Bloques.Clases.DatosSolicitud;
import com.tativo.app.tativo.LogIn.Actividades.Act_LogIn;
import com.tativo.app.tativo.LogIn.Clases.Catcliente;
import com.tativo.app.tativo.Operaciones.Actividades.Act_Perfil;
import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.Globals;
import com.tativo.app.tativo.Utilidades.ServiciosSoap;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by SISTEMAS1 on 29/02/2016.
 */
public class Frg_LogIn extends Fragment {

    Globals Sesion;
    Button btnLogIn, btnPrueba;
    View v;
    ProgressDialog progressDialog;
    TextView lblMsjError;

    AutoCompleteTextView txtUsuario, textPassword;
    Catcliente catCliente;

    public Frg_LogIn()
    {}

    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.frg_login,container,false);
        Sesion = (Globals) getActivity().getApplicationContext();
        LoadFormControls();
        EventManager();
        return v;
    }

    private void LoadFormControls()
    {
        catCliente = new Catcliente();
        progressDialog = new ProgressDialog(v.getContext());
        btnLogIn = (Button) v.findViewById(R.id.btnLogIn);
        txtUsuario = (AutoCompleteTextView) v.findViewById(R.id.txtUsuario);
        textPassword = (AutoCompleteTextView) v.findViewById(R.id.txtPassword);
        lblMsjError = (TextView) v.findViewById(R.id.lblMsjError);

        btnPrueba = (Button) v.findViewById(R.id.btnPrueba);
    }

    private void EventManager()
    {
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblMsjError.setVisibility(View.GONE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new AsyncValidaUsuario().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    new AsyncValidaUsuario().execute();
                }
            }
        });

        btnPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Act_Perfil.class);
                startActivity(i);
                getActivity().finish();
            }
        });
    }




    private class AsyncValidaUsuario extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean UsuarioValido = false;
            UsuarioValido = validaCliente();
            if (UsuarioValido)
                GetEstatusSolicitud();

            return UsuarioValido;
        }

        @Override
        protected void onPostExecute(Boolean UsuarioValido) {
            progressDialog.dismiss();
            if (UsuarioValido)
            {
                Sesion.setSolicitudID(Solicitud.getSolicitudid());
                Intent i = null;
                switch (catCliente.getBloque())
                {
                    case 1:
                        i = new Intent(getActivity(), Act_B1_Referencias.class);
                        break;
                    case 2:
                        i = new Intent(getActivity(), Act_B2_Personal.class);
                        break;
                    case 3:
                        i = new Intent(getActivity(), Act_B3_InfDeposito.class);
                        break;
                    case 4:
                        i = new Intent(getActivity(), Act_B4_Laboral.class);
                        break;
                    case 5:
                        i = new Intent(getActivity(), Act_B5_General.class);
                        break;
                    case 6:
                        i = new Intent(getActivity(), Act_Documentos.class);
                        break;
                    case 7:
                        i = new Intent(getActivity(), Act_Perfil.class);
                        break;
                    default:
                        Toast.makeText(getActivity().getApplicationContext(),"Inicia tu registro",Toast.LENGTH_LONG).show();
                        i = new Intent(getActivity(), Act_LogIn.class);
                        break;
                }
                startActivity(i);
                getActivity().finish();
            }
            else
            {
                lblMsjError.setVisibility(View.VISIBLE);
                txtUsuario.requestFocus();
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage(getText(R.string.msjBuscando));
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    private Boolean validaCliente(){
        String SOAP_ACTION = "http://tempuri.org/IService1/validaCliente";
        String METHOD_NAME = "validaCliente";
        String NAMESPACE = "http://tempuri.org/";
        Boolean r = false;

        ArrayList<PropertyInfo> valores =  new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("Mail");
        pi1.setValue(txtUsuario.getText().toString().trim().toLowerCase());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        pi1 = new PropertyInfo();
        pi1.setName("Password");
        pi1.setValue(textPassword.getText().toString().trim());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE,valores);
        if(respuesta != null) {
            try {
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
                if (ev) {
                    SoapObject item = (SoapObject) respuesta.getProperty(0);
                    if (item.getProperty("Clienteid") != null )
                    {
                        Sesion.setCliendeID(item.getProperty("Clienteid").toString());
                        catCliente.setClienteid(item.getProperty("Clienteid").toString());
                        catCliente.setBloque(Integer.parseInt(item.getProperty("Bloque").toString()));
                        r = true;
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return r;
    }

    CatBloqueoCliente Bloqueos = new CatBloqueoCliente();
    DatosSolicitud Solicitud = new DatosSolicitud();
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
}
