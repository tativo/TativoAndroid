package com.tativo.app.tativo.Bloques.Actividades;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tativo.app.tativo.Bloques.Clases.Catpin;
import com.tativo.app.tativo.Bloques.Fragmentos.frg_confirmar_telefono;
import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.Globals;
import com.tativo.app.tativo.Utilidades.ServiciosSoap;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Collections;

public class Act_B3_ConfirmarPIN extends AppCompatActivity implements  frg_confirmar_telefono.DialogResponse {

    public static Act_B3_ConfirmarPIN INSTANCE = null;
    private synchronized static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Act_B3_ConfirmarPIN();
        }
    }
    public static Act_B3_ConfirmarPIN getInstance() {
        createInstance();
        return INSTANCE;
    }
    public Act_B3_ConfirmarPIN() {
        INSTANCE = this;
    }

    Button btnConfirmarPIN;
    EditText txtPIN;
    LinearLayout lyProgresBar;
    Globals Sesion;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    AsyncEsperaPIN tareaEsperarPIN= new AsyncEsperaPIN();
    boolean CancelatareaEsperarPIN = false ;
    TextView txtProgressBar;



    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_b3_confirmarpin);
        Sesion = (Globals) getApplicationContext();
        LoadFormControls();
        EventManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            tareaEsperarPIN.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            tareaEsperarPIN.execute();
        }
    }
    private void LoadFormControls() {
        //Progress Bar
        progressDialog = new ProgressDialog(Act_B3_ConfirmarPIN.this);
        //Cajas de texto
        txtPIN = (EditText) findViewById(R.id.txtPIN);
        //Botones
        btnConfirmarPIN = (Button) findViewById(R.id.btnConfirmarPIN);
        //ProgressBar
        progressBar = (ProgressBar) findViewById(R.id.circularProgressbar);

        txtProgressBar = (TextView) findViewById(R.id.txtProgressBar);

    }
    private void EventManager() {
        btnConfirmarPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidaGuardar()){
                    tareaEsperarPIN.cancel(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new AsyncValidarPIN().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }else{
                        new AsyncValidarPIN().execute();
                    }
                }
            }
        });
    }
    private boolean ValidaGuardar() {
        ArrayList<Object> Objetos = new ArrayList<Object>();
        Objetos.add(txtPIN);
        Collections.reverse(Objetos);
        boolean requeridos = false;
        for (Object item : Objetos) {
            if (item instanceof EditText) {
                if (((EditText) item).getText().toString().trim().length() == 0) {
                    ((EditText) item).setError(getString(R.string.msjRequerido));
                    ((EditText) item).requestFocus();
                    requeridos = true;
                }
            }
            if (item instanceof Spinner) {
                if (((Spinner) item).getSelectedItemPosition() == 0) {
                    ((TextView) ((Spinner) item).getSelectedView()).setError(getString(R.string.msjRequerido));
                    ((Spinner) item).requestFocus();
                    requeridos = true;
                }
            }
            if (item instanceof CheckBox) {
                if (!((CheckBox) item).isChecked()) {
                    ((CheckBox) item).requestFocus();
                    requeridos = true;
                }
            }
        }
        return !requeridos;
    }

    private Catpin PIN = new Catpin();
    private class AsyncValidarPIN extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            ValidarPIN();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            //Si salio del ciclo quiere decir que encontro bloqueos
            if (PIN.getPinid().trim().length() == 0) {
                new AlertDialog.Builder(Act_B3_ConfirmarPIN.this)
                        .setTitle("PIN")
                        .setMessage("El pin ingresado no es valido")
                        .setCancelable(false)
                        .setPositiveButton(R.string.msgRefOk, null).create().show();
            }else{
                //Aqui quiere decir que el pin ingresado es valido
                Intent i = new Intent(getApplicationContext(), Act_B4_Laboral.class);
                startActivity(i);
                finish();
            }
        }
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Validando PIN...");
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    private void ValidarPIN(){
        String SOAP_ACTION = "http://tempuri.org/IService1/ValidarPIN";
        String METHOD_NAME = "ValidarPIN";
        String NAMESPACE = "http://tempuri.org/";

        ArrayList<PropertyInfo> valores =  new ArrayList<PropertyInfo>();

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("Clienteid");
        pi1.setValue(Sesion.getCliendeID());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("PIN");
        pi2.setValue(txtPIN.getText().toString());
        pi2.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi2);

        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION,METHOD_NAME,NAMESPACE,valores);
        if(respuesta != null) {
            try {
                if(Boolean.parseBoolean(respuesta.getProperty("EsValido").toString())){
                    SoapObject Datos = (SoapObject) respuesta.getProperty("Datos");
                    //Llenamos los datos del PIN
                    if(Datos.getProperty("Pinid").toString().trim().length() !=0 ) {
                        PIN.setPinid(Datos.getProperty("Pinid").toString());
                        PIN.setPin(Datos.getProperty("Pin").toString());
                        PIN.setClienteid(Datos.getProperty("Clienteid").toString());
                    }
                }else{
                }
            } catch (Exception e) {
            }
        }
    }

    public void verificarCodigoSms(String codigo) {
        lyProgresBar.setVisibility(View.GONE);
        txtPIN.setText(codigo);
        txtPIN.setSelection(txtPIN.getText().length());
    }

    //Region Esperar 60 segundos si aun no se envia el mensaje
    private class AsyncEsperaPIN extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... params) {
                try {
                    int Segundos = 0;
                    Integer ProgressUpdate = 0;
                    while(Segundos <=30) {
                        if (isCancelled()) {
                            CancelatareaEsperarPIN = true;
                            break;
                        }
                        Thread.sleep(1000);
                        Segundos++;
                        publishProgress(ProgressUpdate);
                        ProgressUpdate++;
                    }
                } catch (InterruptedException ex) {
                }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            if(!CancelatareaEsperarPIN) {
                //Si aun no ha validado el sistema debe mostrar un modal
                //if (txtPIN.getText().toString().trim().length() == 0) {
                FragmentManager fragmento = getFragmentManager();
                DialogFragment dialogo =new frg_confirmar_telefono();
                dialogo.show(fragmento, "frmConfirmarTelefono");

                //}
            }
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            Integer contador = 30 -values[0];
            progressBar.setProgress(values[0]);
            txtProgressBar.setText(contador.toString());
        }
    }
    //EndRegion
    @Override
    public void onPossitiveButtonClick() {
        tareaEsperarPIN= new AsyncEsperaPIN();
        CancelatareaEsperarPIN = false ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            tareaEsperarPIN.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            tareaEsperarPIN.execute();
        }
    }
}
