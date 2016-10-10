package com.tativo.app.tativo.Bloques.Fragmentos;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tativo.app.tativo.Bloques.Clases.Catcolonia;
import com.tativo.app.tativo.Bloques.Clases.Catdatospersonal;
import com.tativo.app.tativo.LogIn.Actividades.Act_LogIn;
import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.Globals;
import com.tativo.app.tativo.Utilidades.ServiciosSoap;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by AlfonsoM on 27/04/2016.
 */
public class frg_confirmar_telefono extends DialogFragment {
    LinearLayout lyConfirmarNumero,lyCambiarNumero;
    Button btnEnviarPin,btnCambiarNumero,btnGuardarNumeroCelular;
    TextView lblMensajeConfirmacion, lblMensajeConfirmacion2;
    EditText txtNumeroCelular,txtConfirmaNumeroCelular;
    Globals Sesion;
    Catdatospersonal catdatospersonal;
    private ProgressDialog progressDialog;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.frg_confirmar_telefono, null);
        builder.setView(v);
        Sesion = (Globals) getActivity().getApplicationContext();
        LoadFormControls(v);
        EventManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new AsyncLoadData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new AsyncLoadData().execute();
        }
        return builder.create();
    }

    private void LoadFormControls( View v){
        btnEnviarPin = (Button) v.findViewById(R.id.btnEnviarPin);
        btnCambiarNumero = (Button) v.findViewById(R.id.btnCambiarNumero);
        btnGuardarNumeroCelular = (Button) v.findViewById(R.id.btnGuardarNumeroCelular);
        lblMensajeConfirmacion = (TextView) v.findViewById(R.id.lblMensajeConfirmacion);
        lblMensajeConfirmacion2 = (TextView) v.findViewById(R.id.lblMensajeConfirmacion2);
        txtNumeroCelular = (EditText) v.findViewById(R.id.txtNumeroCelular);
        txtConfirmaNumeroCelular = (EditText) v.findViewById(R.id.txtConfirmaNumeroCelular);
        lyConfirmarNumero = (LinearLayout)  v.findViewById(R.id.lyConfirmarNumero);
        lyCambiarNumero = (LinearLayout)  v.findViewById(R.id.lyCambiarNumero);
        catdatospersonal = new Catdatospersonal();
        progressDialog = new ProgressDialog(getActivity());
    }


    private void EventManager(){
        btnEnviarPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new AsyncEnviarPIN().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    new AsyncEnviarPIN().execute();
                }
            }
        });
        btnCambiarNumero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyConfirmarNumero.setVisibility(View.GONE);
                lyCambiarNumero.setVisibility(View.VISIBLE);
            }
        });
        btnGuardarNumeroCelular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidaGuardar())
                    Guardar();
            }
        });
    }

    //Cambiar numero de telefono y enviar PIN
    private boolean ValidaGuardar() {
        ArrayList<Object> Objetos = new ArrayList<Object>();
        Objetos.add(txtNumeroCelular);
        Objetos.add(txtConfirmaNumeroCelular);
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
    private void Guardar() {
        if (!txtNumeroCelular.getText().toString().trim().toUpperCase().equals(txtConfirmaNumeroCelular.getText().toString().trim().toUpperCase())) {
            Toast.makeText(getActivity(), "Los numeros de telefono ingresados no son iguales", Toast.LENGTH_LONG).show();
            txtConfirmaNumeroCelular.requestFocus();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new AsyncGuardarTelefono().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new AsyncGuardarTelefono().execute();
        }
    }
    private class AsyncGuardarTelefono extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GuardarTelefono();
            EnviarPIN();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            dismiss();
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
    private void GuardarTelefono() {
        String SOAP_ACTION = "http://tempuri.org/IService1/ActTelefonoCliente";
        String METHOD_NAME = "ActTelefonoCliente";
        String NAMESPACE = "http://tempuri.org/";

        ArrayList<PropertyInfo> valores = new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("clienteid");
        pi1.setValue(Sesion.getCliendeID());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);


        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("telefono");
        pi2.setValue(txtNumeroCelular.getText().toString());
        pi2.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi2);


        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE, valores);

        if (respuesta != null) {
            try {
                if (Boolean.parseBoolean(respuesta.getProperty("EsValido").toString())) {
                    SoapObject Datos = (SoapObject) respuesta.getProperty("Datos");

                } else {
                    //Toast.makeText(getApplicationContext(),"Error: "+respuesta.getProperty("Mensaje").toString(),Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    //Reenviar PIN
    private class AsyncEnviarPIN extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            EnviarPIN();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            dismiss();
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Enviando mensaje");
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    private void EnviarPIN() {
        String SOAP_ACTION = "http://tempuri.org/IService1/EnviarPIN";
        String METHOD_NAME = "EnviarPIN";
        String NAMESPACE = "http://tempuri.org/";

        ArrayList<PropertyInfo> valores = new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("Clienteid");
        pi1.setValue(Sesion.getCliendeID());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE, valores);

        if (respuesta != null) {
            try {
                if (Boolean.parseBoolean(respuesta.getProperty("EsValido").toString())) {
                    SoapObject Datos = (SoapObject) respuesta.getProperty("Datos");

                } else {
                    //Toast.makeText(getApplicationContext(),"Error: "+respuesta.getProperty("Mensaje").toString(),Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    //Traer la informacion del dato personal
    private class AsyncLoadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetInfoBloque();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            lblMensajeConfirmacion.setText("Pasó el tiempo suficiente para que te llegara el código y vemos que no has ingresado el PIN, ¿recibiste el SMS al celular "+catdatospersonal.getTelefono()+"?");
            lblMensajeConfirmacion2.setText("Si el celular anterior no es el tuyo puedes cambiarlo dando clic en el botón \"Ingresar otro número celular\". Si en efecto ese es tu celular podemos enviarte de nuevo el pin, dando clic en \"Volver a enviar pin\"");
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
    private void GetInfoBloque() {
        String SOAP_ACTION = "http://tempuri.org/IService1/getCatDatosPersonales";
        String METHOD_NAME = "getCatDatosPersonales";
        String NAMESPACE = "http://tempuri.org/";

        ArrayList<PropertyInfo> valores = new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("clienteid");
        pi1.setValue(Sesion.getCliendeID());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE, valores);

        if (respuesta != null) {
            try {
                if (Boolean.parseBoolean(respuesta.getProperty("EsValido").toString())) {
                    SoapObject DatosPersonales = (SoapObject) respuesta.getProperty("Datos");
                    if(Integer.parseInt(DatosPersonales.getProperty("UltimaAct").toString())!=0){
                        catdatospersonal.setDatopersonalid(DatosPersonales.getProperty("Datopersonalid").toString());
                        catdatospersonal.setClienteid(DatosPersonales.getProperty("Clienteid").toString());
                        catdatospersonal.setGenero(DatosPersonales.getProperty("Genero").toString());
                        catdatospersonal.setFechanacimiento(new SimpleDateFormat("yyyy-MM-dd").parse(DatosPersonales.getProperty("Fechanacimiento").toString().substring(0, 10)));
                        catdatospersonal.setEstadocivilid(Integer.parseInt(DatosPersonales.getProperty("Estadocivilid").toString()));
                        catdatospersonal.setDependientes(Integer.parseInt(DatosPersonales.getProperty("Dependientes").toString()));
                        catdatospersonal.setTelefono(DatosPersonales.getProperty("Telefono").toString());
                        catdatospersonal.setMarcaid(Integer.parseInt(DatosPersonales.getProperty("Marcaid").toString()));
                        catdatospersonal.setCalle(DatosPersonales.getProperty("Calle").toString());
                        catdatospersonal.setNumeroext(DatosPersonales.getProperty("Numeroext").toString());
                        catdatospersonal.setNumeroint(DatosPersonales.getProperty("Numeroint").toString());
                        catdatospersonal.setColonia(DatosPersonales.getProperty("Colonia").toString());
                        catdatospersonal.setCodigopostal(DatosPersonales.getProperty("Codigopostal").toString());
                        catdatospersonal.setPaisid(DatosPersonales.getProperty("Paisid").toString());
                        catdatospersonal.setEstadoid(DatosPersonales.getProperty("Estadoid").toString());
                        catdatospersonal.setMunicipioid(DatosPersonales.getProperty("Municipioid").toString());
                        catdatospersonal.setCiudadid(DatosPersonales.getProperty("Ciudadid").toString());
                        catdatospersonal.setUltimaAct(Integer.parseInt(DatosPersonales.getProperty("UltimaAct").toString()));
                    }

                } else {
                    //Toast.makeText(getApplicationContext(),"Error: "+respuesta.getProperty("Mensaje").toString(),Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }


}
