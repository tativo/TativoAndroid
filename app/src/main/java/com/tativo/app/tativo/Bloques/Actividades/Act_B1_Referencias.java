package com.tativo.app.tativo.Bloques.Actividades;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tativo.app.tativo.Bloques.Clases.Catanio;
import com.tativo.app.tativo.Bloques.Clases.Catrelacionespersonal;
import com.tativo.app.tativo.LogIn.Actividades.Act_Cotizador;
import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.ServiciosSoap;
import com.tativo.app.tativo.Utilidades.Utilerias;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;

public class Act_B1_Referencias extends AppCompatActivity {
    ProgressDialog progressDialog;
    Button btnReferencias,btnFocoInicial;
    Spinner spnRelacionRef1,spnRelacionRef2,spnAmistadRef1,spnAmistadRef2,spnTrabajando;
    EditText txtNombreRef1,txtApellidosRef1,txtTelefonoCelularRef1,txtNombreRef2,txtApellidosRef2,txtTelefonoCelularRef2,txtNombreEmpresa,txtTelefonoRefLaboral;
    AdapterRelacionPersonal spnRelacionRefAdapter;
    AdapterAnio spnCatanioAdapter;
    ArrayList<Catrelacionespersonal> listaCatrelacionespersonal = new ArrayList<Catrelacionespersonal>();
    ArrayList<Catanio> listaCatanio = new ArrayList<Catanio>();
    TextView hidenClienteID,hidenImporteSolicitado,hidenFechaPago,hidenSolicitudID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_b1_referencias);
        progressDialog = new ProgressDialog(Act_B1_Referencias.this);
        LoadFormControls();
        LlenarCamposOcultos(getIntent().getExtras());
        FocusManager();
        EventManager();
        new AsyncLoadData().execute();
        new AsyncEstatusSolicitud().execute();
        btnFocoInicial.requestFocus();
    }

    public void FocusManager(){
        FocusNextControl(R.id.txtNombreRef1, "T", R.id.txtApellidosRef1, "T");
        FocusNextControl(R.id.txtApellidosRef1, "T", R.id.spnRelacionRef1, "S");
        FocusNextControl(R.id.spnRelacionRef1, "S", R.id.txtTelefonoCelularRef1, "T");
        FocusNextControl(R.id.txtTelefonoCelularRef1, "T", R.id.spnAmistadRef1, "S");
        FocusNextControl(R.id.spnAmistadRef1, "S", R.id.txtNombreRef2, "T");

        FocusNextControl(R.id.txtNombreRef2, "T", R.id.txtApellidosRef2, "T");
        FocusNextControl(R.id.txtApellidosRef2, "T", R.id.spnRelacionRef2, "S");
        FocusNextControl(R.id.spnRelacionRef2, "S", R.id.txtTelefonoCelularRef2, "T");
        FocusNextControl(R.id.txtTelefonoCelularRef2, "T", R.id.spnAmistadRef2, "S");
        FocusNextControl(R.id.spnAmistadRef2, "S", R.id.txtNombreEmpresa, "T");

        FocusNextControl(R.id.txtNombreEmpresa, "T", R.id.txtTelefonoRefLaboral, "T");
        FocusNextControl(R.id.txtTelefonoRefLaboral, "T", R.id.spnTrabajando, "S");
    }

    public void FocusNextControl(int o,String ot, int d,String dt )
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
                        if(destino!=null)
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
                        if(destino!=null)
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

    public void LlenarCamposOcultos(Bundle bundle) {
        if(bundle!=null){
            hidenClienteID.setText(bundle.getString("ClienteID"));
            hidenImporteSolicitado.setText(bundle.getString("ImporteSolicitado"));
            hidenFechaPago.setText(bundle.getString("FechaPago"));
            hidenSolicitudID.setText(bundle.getString("SolicitudID"));
        }
    }
    private void LoadFormControls(){
        //Ocultos

        hidenClienteID = (TextView) findViewById(R.id.hidenClienteID);
        hidenImporteSolicitado = (TextView) findViewById(R.id.hidenImporteSolicitado);
        hidenFechaPago = (TextView) findViewById(R.id.hidenFechaPago);
        hidenSolicitudID = (TextView) findViewById(R.id.hidenSolicitudID);

        //Combos
        spnRelacionRef1 = (Spinner) findViewById(R.id.spnRelacionRef1);
        spnRelacionRef1.setFocusable(true);
        spnRelacionRef1.setFocusableInTouchMode(true);

        spnRelacionRef2 = (Spinner) findViewById(R.id.spnRelacionRef2);
        spnRelacionRef2.setFocusable(true);
        spnRelacionRef2.setFocusableInTouchMode(true);

        spnAmistadRef1 = (Spinner) findViewById(R.id.spnAmistadRef1);
        spnAmistadRef1.setFocusable(true);
        spnAmistadRef1.setFocusableInTouchMode(true);

        spnAmistadRef2 = (Spinner) findViewById(R.id.spnAmistadRef2);
        spnAmistadRef2.setFocusable(true);
        spnAmistadRef2.setFocusableInTouchMode(true);

        spnTrabajando = (Spinner) findViewById(R.id.spnTrabajando);
        spnTrabajando.setFocusable(true);
        spnTrabajando.setFocusableInTouchMode(true);

        //Cajas de texto
        txtNombreRef1 = (EditText) findViewById(R.id.txtNombreRef1);
        txtApellidosRef1 = (EditText) findViewById(R.id.txtApellidosRef1);
        txtTelefonoCelularRef1 = (EditText) findViewById(R.id.txtTelefonoCelularRef1);
        txtNombreRef2 = (EditText) findViewById(R.id.txtNombreRef2);
        txtApellidosRef2 = (EditText) findViewById(R.id.txtApellidosRef2);
        txtTelefonoCelularRef2 = (EditText) findViewById(R.id.txtTelefonoCelularRef2);
        txtNombreEmpresa = (EditText) findViewById(R.id.txtNombreEmpresa);
        txtTelefonoRefLaboral = (EditText) findViewById(R.id.txtTelefonoRefLaboral);

        //Botones
        btnReferencias = (Button) findViewById(R.id.btnReferencias);
        btnReferencias.setFocusable(true);
        btnReferencias.setFocusableInTouchMode(true);

        btnFocoInicial = (Button) findViewById(R.id.btnFocoInicial);
        btnFocoInicial.setFocusable(true);
        btnFocoInicial.setFocusableInTouchMode(true);

    }
    private void EventManager(){
        txtTelefonoCelularRef1.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        txtTelefonoCelularRef2.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        txtTelefonoRefLaboral.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        btnReferencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidaGuardar()){
                    Intent i = new Intent(getApplicationContext(), Act_B2_Personal.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    //Region Llenar datos del formulario
    private class AsyncLoadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetCatRelacionesPersonal();
            GetCatanio();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            spnRelacionRefAdapter = new AdapterRelacionPersonal(listaCatrelacionespersonal);
            spnRelacionRefAdapter = new AdapterRelacionPersonal(listaCatrelacionespersonal);
            spnCatanioAdapter =  new AdapterAnio(listaCatanio);
            spnRelacionRef1.setAdapter(spnRelacionRefAdapter);
            spnRelacionRef2.setAdapter(spnRelacionRefAdapter);
            spnAmistadRef1.setAdapter(spnCatanioAdapter);
            spnAmistadRef2.setAdapter(spnCatanioAdapter);
            spnTrabajando.setAdapter(spnCatanioAdapter);
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
    private void GetCatRelacionesPersonal(){
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatRelacionesPersonal";
        String METHOD_NAME = "GetCatRelacionesPersonal";
        String NAMESPACE = "http://tempuri.org/";
        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION,METHOD_NAME,NAMESPACE,null);
        if(respuesta != null) {
            try {
                String[] listaRespuesta;
                listaRespuesta = new String[respuesta.getPropertyCount()];
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);
                    Catrelacionespersonal entidad = new Catrelacionespersonal();
                    entidad.setRelacionid(Integer.parseInt(item.getProperty("Relacionid").toString()));
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    listaCatrelacionespersonal.add(entidad);
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    private void GetCatanio(){
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatanio";
        String METHOD_NAME = "GetCatanio";
        String NAMESPACE = "http://tempuri.org/";
        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION,METHOD_NAME,NAMESPACE,null);
        if(respuesta != null) {
            try {
                String[] listaRespuesta;
                listaRespuesta = new String[respuesta.getPropertyCount()];
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);
                    Catanio entidad = new Catanio();
                    entidad.setAnioid(Integer.parseInt(item.getProperty("Anioid").toString()));
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    listaCatanio.add(entidad);
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    private class AdapterRelacionPersonal extends BaseAdapter implements SpinnerAdapter {
        private final List<Catrelacionespersonal> data;

        public AdapterRelacionPersonal(List<Catrelacionespersonal> data){
            this.data = data;
        }
        @Override
        public int getCount() {
            return data.size();
        }
        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public View getView(int position, View recycle, ViewGroup parent) {
            TextView text;
            if (recycle != null){
                text = (TextView) recycle;
            } else {
                text = (TextView) getLayoutInflater().inflate(
                        android.R.layout.simple_dropdown_item_1line, parent, false
                );
            }
            text.setTextColor(Color.BLACK);
            text.setText(data.get(position).getDescripcion());
            return text;
        }
    }
    private class AdapterAnio extends BaseAdapter implements SpinnerAdapter {
        private final List<Catanio> data;

        public AdapterAnio(List<Catanio> data){
            this.data = data;
        }
        @Override
        public int getCount() {
            return data.size();
        }
        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public View getView(int position, View recycle, ViewGroup parent) {
            TextView text;
            if (recycle != null){
                text = (TextView) recycle;
            } else {
                text = (TextView) getLayoutInflater().inflate(
                        android.R.layout.simple_dropdown_item_1line, parent, false
                );
            }
            text.setTextColor(Color.BLACK);
            text.setText(data.get(position).getDescripcion());
            return text;
        }
    }
    //Endregion

    //Region Guardar
    private boolean ValidaGuardar() {
        if (txtNombreRef1.getText().toString().trim().length() == 0) {
            txtNombreRef1.setError("Este campo es requerido");
            txtNombreRef1.requestFocus();
            return false;
        }
        return true;
    }
    //EndGuardar

    //Para la revision del estatus de la solicitud utilizaremos una tarea asincrona que se este ejecutando indefinidamente en el servidor
    private class AsyncEstatusSolicitud extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            int veces=0;
            while (true){
                if(veces>5)
                    break;
                try{
                    Thread.sleep(3000);
                    veces++;

                }catch(InterruptedException ex){
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(getApplicationContext(),"Tarea terminada ya pasaron todos los segundos",Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}
