package com.tativo.app.tativo.LogIn.Fragmentos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tativo.app.tativo.Bloques.Actividades.Act_B1_Referencias;
import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.Globals;
import com.tativo.app.tativo.Utilidades.ServiciosSoap;
import com.tativo.app.tativo.Utilidades.Utilerias;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SISTEMAS1 on 29/02/2016.
 */
public class Frg_Registro extends Fragment{

    public Frg_Registro()
    {

    }

    ProgressDialog progressDialog;
    AutoCompleteTextView txtNombre, txtApellidoPaterno, txtApellidoMaterno, txtCorreo,txtContrasena, txtConfirmaContrasena;
    Button btnRegistro;
    Globals g;

    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.frg_registro,container,false);
        g = (Globals)getActivity ().getApplicationContext();
        progressDialog = new ProgressDialog(v.getContext());
        LoadFormControls(v);
        FocusManager(v);
        EventManager();
        return v;
    }

    private void LoadFormControls(View v)
    {
        txtNombre = (AutoCompleteTextView) v.findViewById(R.id.txtNombre);
        txtApellidoPaterno = (AutoCompleteTextView) v.findViewById(R.id.txtApellidoPaterno);
        txtApellidoMaterno = (AutoCompleteTextView) v.findViewById(R.id.txtApellidoMaterno);
        txtCorreo = (AutoCompleteTextView) v.findViewById(R.id.txtCorreo);
        txtContrasena = (AutoCompleteTextView) v.findViewById(R.id.txtContrasena);
        txtConfirmaContrasena = (AutoCompleteTextView) v.findViewById(R.id.txtConfirmaContrasena);
        btnRegistro = (Button) v.findViewById(R.id.btnRegistro);
    }

    public void FocusManager(View v)
    {
        FocusNextControl(R.id.txtNombre, "T", R.id.txtApellidoPaterno, "T", v);
        FocusNextControl(R.id.txtApellidoPaterno, "T", R.id.txtApellidoMaterno, "T", v);
        FocusNextControl(R.id.txtApellidoMaterno, "T", R.id.txtCorreo, "T", v);
        FocusNextControl(R.id.txtCorreo, "T", R.id.txtContrasena, "T", v);
        FocusNextControl(R.id.txtContrasena, "T", R.id.txtConfirmaContrasena, "T", v);
    }

    private void EventManager()
    {
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidaGuardar()) {
                    new AsyncSaveData().execute();
                }
            }
        });

        txtCorreo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    if (txtCorreo.getText().toString().toString().length() > 5 && isEmailValid(txtCorreo.getText().toString().trim()))
                    {
                        new AsyncValidaCorreo().execute();
                    }
                    else
                        txtCorreo.setError(getText(R.string.msjFormatoCorreoInvalido));
                }
            }
        });
    }

    private boolean ValidaGuardar()
    {

        if (txtNombre.getText().toString().trim().length() == 0) {
            txtNombre.setError(getString(R.string.msjRequerido));
            txtNombre.requestFocus();
            return false;
        }
        if (txtApellidoPaterno.getText().toString().trim().length() == 0) {
            txtApellidoPaterno.setError(getString(R.string.msjRequerido));
            txtApellidoPaterno.requestFocus();
            return false;
        }
        if (txtApellidoMaterno.getText().toString().trim().length() == 0) {
            txtApellidoMaterno.setError(getString(R.string.msjRequerido));
            txtApellidoMaterno.requestFocus();
            return false;
        }
        if (txtCorreo.getText().toString().trim().length() == 0) {
            txtCorreo.setError(getString(R.string.msjRequerido));
            txtCorreo.requestFocus();
            return false;
        }
        if (!isEmailValid(txtCorreo.getText().toString().trim()))
        {
            txtCorreo.setError(getText(R.string.msjFormatoCorreoInvalido));
            txtCorreo.requestFocus();
            return false;
        }
        if (txtContrasena.getText().toString().trim().length() < 8) {
            txtContrasena.setError(getString(R.string.msjContrasena));
            txtContrasena.requestFocus();
            return false;
        }
        if (txtConfirmaContrasena.getText().toString().trim().length() < 8) {
            txtConfirmaContrasena.setError(getString(R.string.msjContrasenaConfirma));
            txtConfirmaContrasena.requestFocus();
            return false;
        }
        if (!txtConfirmaContrasena.getText().toString().trim().matches(txtContrasena.getText().toString().trim()))
        {
            txtConfirmaContrasena.setError(getText(R.string.msjConfirmacionContrasena));
            txtConfirmaContrasena.requestFocus();
            return false;
        }

        return true;
    }

    public void FocusNextControl(int o,String ot, int d,String dt, View v )
    {
        final EditText destino = (dt.toUpperCase()=="T"?(EditText) v.findViewById(d):null);
        final Spinner destinoS = (dt.toUpperCase()=="S"?(Spinner) v.findViewById(d):null);

        if(ot.toUpperCase()=="T"){
            EditText origen = (EditText) v.findViewById(o);
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
        if(ot.toUpperCase()=="S"){
            Spinner origen = (Spinner) v.findViewById(o);
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

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    //GUARDAR
    private class AsyncSaveData extends  AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            SaveInfoBloque();
            SaveRegistraSolicitud();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            Intent i = new Intent(getActivity(), Act_B1_Referencias.class);
            startActivity(i);
            getActivity().finish();
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

    private void SaveInfoBloque(){
        String SOAP_ACTION = "http://tempuri.org/IService1/RegistrarCliente";
        String METHOD_NAME = "RegistrarCliente";
        String NAMESPACE = "http://tempuri.org/";

        ArrayList<PropertyInfo> valores =  new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("Nombre");
        pi1.setValue(txtNombre.getText().toString());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        pi1 = new PropertyInfo();
        pi1.setName("ApellidoPaterno");
        pi1.setValue(txtApellidoPaterno.getText().toString());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        pi1 = new PropertyInfo();
        pi1.setName("ApellidoMaterno");
        pi1.setValue(txtApellidoMaterno.getText().toString());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        pi1 = new PropertyInfo();
        pi1.setName("Mail");
        pi1.setValue(txtCorreo.getText().toString());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        pi1 = new PropertyInfo();
        pi1.setName("Password");
        pi1.setValue(Utilerias.MD5(txtContrasena.getText().toString()));
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);


        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE,valores);
        if(respuesta != null) {
            try {
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
                if (ev)
                {
                    SoapObject item = (SoapObject) respuesta.getProperty(0);
                    g.setCliendeID(item.getProperty("Clienteid").toString());
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void SaveRegistraSolicitud(){
        String SOAP_ACTION = "http://tempuri.org/IService1/RegistrarSolicitudEfectivo";
        String METHOD_NAME = "RegistrarSolicitudEfectivo";
        String NAMESPACE = "http://tempuri.org/";

        ArrayList<PropertyInfo> valores =  new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("ClienteID");
        pi1.setValue(g.getCliendeID());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        pi1 = new PropertyInfo();
        pi1.setName("FechaVencimiento");
        pi1.setValue(android.text.format.DateFormat.format("yyyy-MM-dd",g.getFechaPago()).toString());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        pi1 = new PropertyInfo();
        pi1.setName("Importe");
        pi1.setValue(String.valueOf(g.getImporteSolicitado()));
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE,valores);
        if(respuesta != null) {
            try {
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
                if (ev)
                {
                    SoapObject item = (SoapObject) respuesta.getProperty(0);
                    g.setSolicitudID(item.getProperty("Solicitudid").toString());
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    //GUARDAR

    //VALIDAR CORREO
    private class AsyncValidaCorreo extends  AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean CorreoValido = true;
            CorreoValido = ValidaCorreo();
            return CorreoValido;
        }

        @Override
        protected void onPostExecute(Boolean CorreoValido) {
            progressDialog.dismiss();
            if (CorreoValido)
            {
                txtCorreo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ok, 0);
            }
            else
            {
                //Drawable d = (Drawable) getContext().getResources().getDrawable(R.drawable.error);
                txtCorreo.setError(getText(R.string.msjCorreoDuplicado));
                txtCorreo.requestFocus();
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage(getText(R.string.msjValidandoCorreo));
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private Boolean ValidaCorreo(){
        String SOAP_ACTION = "http://tempuri.org/IService1/ValidaCorreoCliente";
        String METHOD_NAME = "ValidaCorreoCliente";
        String NAMESPACE = "http://tempuri.org/";
        Boolean r = true;
        ArrayList<PropertyInfo> valores =  new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("correo");
        pi1.setValue(txtCorreo.getText().toString());
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
                    if (Integer.parseInt(item.getProperty("UltimaAct").toString().trim()) == 0) {
                        r = true;
                    }
                    else {
                        r = false;
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return  r;
    }
    //VALIDAR CORREO
}
