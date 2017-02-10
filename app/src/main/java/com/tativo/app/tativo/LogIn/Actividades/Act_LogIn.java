package com.tativo.app.tativo.LogIn.Actividades;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tativo.app.tativo.Bloques.Actividades.Act_B1_Referencias;
import com.tativo.app.tativo.LogIn.Fragmentos.Frg_LogIn;
import com.tativo.app.tativo.LogIn.Fragmentos.Frg_Registro;
import com.tativo.app.tativo.LogIn.Fragmentos.Frg_Requisitos;
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

public class Act_LogIn extends FragmentActivity implements Frg_Requisitos.DialogResponseRequisitos {

    Globals Sesion;
    AutoCompleteTextView txtNombre, txtApellidoPaterno, txtApellidoMaterno, txtCorreo,txtContrasena, txtConfirmaContrasena;
    Button btnRegistrar, btnMiCuenta;
    ProgressDialog progressDialog;
    TextView txtRequisitos;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        Sesion = (Globals) getApplicationContext();
        progressDialog = new ProgressDialog(this);
        LoadFormControls();
        FocusManager();
        EventManager();

        /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Frg_Registro newFragment = new Frg_Registro();
        transaction.replace(R.id.flContenedor, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        btnfrgLogIn.setBackgroundColor(getResources().getColor(R.color.colorAzulOscuro));
        btnfrgRegistrar.setBackgroundColor(getResources().getColor(R.color.colorAzulSeleccion));

        btnfrgLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnfrgLogIn.setBackgroundColor(getResources().getColor(R.color.colorAzulSeleccion));
                btnfrgRegistrar.setBackgroundColor(getResources().getColor(R.color.colorAzulOscuro));

                //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //Frg_LogIn newFragment = new Frg_LogIn();
                //transaction.replace(R.id.flContenedor, newFragment);
                //transaction.addToBackStack(null);
                //transaction.commit();
            }
        });


        btnfrgRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnfrgLogIn.setBackgroundColor(getResources().getColor(R.color.colorAzulOscuro));
                btnfrgRegistrar.setBackgroundColor(getResources().getColor(R.color.colorAzulSeleccion));

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Frg_Registro newFragment = new Frg_Registro();
                transaction.replace(R.id.flContenedor, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });*/
    }

    private void LoadFormControls()
    {
        txtNombre = (AutoCompleteTextView) findViewById(R.id.txtNombre);
        txtApellidoPaterno = (AutoCompleteTextView) findViewById(R.id.txtApellidoPaterno);
        txtApellidoMaterno = (AutoCompleteTextView) findViewById(R.id.txtApellidoMaterno);
        txtCorreo = (AutoCompleteTextView) findViewById(R.id.txtCorreo);
        txtContrasena = (AutoCompleteTextView) findViewById(R.id.txtContrasena);
        txtConfirmaContrasena = (AutoCompleteTextView) findViewById(R.id.txtConfirmaContrasena);
        btnRegistrar = (Button) findViewById(R.id.btnRegistro);
        txtRequisitos = (TextView) findViewById(R.id.txtRequisitos);
        btnMiCuenta = (Button) findViewById(R.id.btnMiCuenta);
    }

    public void FocusManager()
    {
        FocusNextControl(R.id.txtNombre, "T", R.id.txtApellidoPaterno, "T");
        FocusNextControl(R.id.txtApellidoPaterno, "T", R.id.txtApellidoMaterno, "T");
        FocusNextControl(R.id.txtApellidoMaterno, "T", R.id.txtCorreo, "T");
        FocusNextControl(R.id.txtCorreo, "T", R.id.txtContrasena, "T");
        FocusNextControl(R.id.txtContrasena, "T", R.id.txtConfirmaContrasena, "T");
    }

    private void EventManager()
    {
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidaGuardar()) {
                    new Act_LogIn.AsyncSaveData().execute();
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
                        new Act_LogIn.AsyncValidaCorreo().execute();
                    }
                    else
                        txtCorreo.setError(getText(R.string.msjFormatoCorreoInvalido));
                }
            }
        });

        btnMiCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmento = getFragmentManager();
                new Frg_LogIn().show(fragmento,"frmLogIn");
            }
        });

        txtRequisitos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmento = getFragmentManager();
                new Frg_Requisitos().show(fragmento,"frmRequisitos");
            }
        });
    }



    public void FocusNextControl(int o,String ot, int d,String dt)
    {
        final EditText destino = (dt.toUpperCase()=="T"?(EditText) findViewById(d):null);
        final Spinner destinoS = (dt.toUpperCase()=="S"?(Spinner) findViewById(d):null);

        if(ot.toUpperCase()=="T"){
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
        if(ot.toUpperCase()=="S"){
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
        if (txtContrasena.getText().toString().trim().length() == 0) {
            txtContrasena.setError(getString(R.string.msjRequerido));
            txtContrasena.requestFocus();
            return false;
        }
        if (txtConfirmaContrasena.getText().toString().trim().length() == 0) {
            txtConfirmaContrasena.setError(getString(R.string.msjRequerido));
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


    //VALIDAR CORREO
    private class AsyncValidaCorreo extends AsyncTask<Void, Void, Boolean>
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
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return  r;
    }
    //VALIDAR CORREO



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
            Intent i = new Intent(getApplicationContext(), Act_B1_Referencias.class);
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
                    Sesion.setCliendeID(item.getProperty("Clienteid").toString());
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
        pi1.setValue(Sesion.getCliendeID());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        pi1 = new PropertyInfo();
        pi1.setName("FechaVencimiento");
        pi1.setValue(android.text.format.DateFormat.format("yyyy-MM-dd",Sesion.getFechaPago()).toString());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        pi1 = new PropertyInfo();
        pi1.setName("Importe");
        pi1.setValue(String.valueOf(Sesion.getImporteSolicitado()));
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
                    Sesion.setSolicitudID(item.getProperty("Solicitudid").toString());
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    //GUARDAR


    //REQUISITOS -->
    @Override
    public void onPossitiveButtonClick() {

    }

    @Override
    public void onNegativeButtonClick() {
        //Toast.makeText(this,"No se aceptaron los terminos",Toast.LENGTH_LONG).show();
    }
    //<-- REQUISITOS

}
