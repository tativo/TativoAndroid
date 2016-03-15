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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tativo.app.tativo.Bloques.Clases.Catanio;
import com.tativo.app.tativo.Bloques.Clases.Catreferenciaspersonal;
import com.tativo.app.tativo.Bloques.Clases.Catrelacionespersonal;
import com.tativo.app.tativo.LogIn.Actividades.Act_Cotizador;
import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.ServiciosSoap;
import com.tativo.app.tativo.Utilidades.Utilerias;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
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
    CheckBox ckReferenciasAcepta;
    TextView hidenClienteID,hidenImporteSolicitado,hidenFechaPago,hidenSolicitudID,hidenReferenciaLaboralid,hidenReferenciaPersonalidRef1,hidenReferenciaPersonalidRef2,hidenUltimaActEmpresa,hidenUltimaActRef1,hidenUltimaActRef2;
     Catreferenciaspersonal EntityReferenciaPersonal;
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
        //new AsyncEstatusSolicitud().execute();
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
        if (bundle != null) {
            hidenClienteID.setText(bundle.getString("ClienteID"));
            hidenImporteSolicitado.setText(bundle.getString("ImporteSolicitado"));
            hidenFechaPago.setText(bundle.getString("FechaPago"));
            hidenSolicitudID.setText(bundle.getString("SolicitudID"));
        } else {
            //Campos dumi cuando no se manda nada en el intent
            hidenClienteID.setText("6ACDFF99-2070-41F9-B712-A2761134BAE1");
            hidenImporteSolicitado.setText("2000");
            hidenFechaPago.setText("31/03/2016");
            hidenSolicitudID.setText("769073AF-0AC1-4536-ACDB-AB01BE649E72");
        }
    }
    private void LoadFormControls(){
        //Ocultos
        EntityReferenciaPersonal = new Catreferenciaspersonal();
        EntityReferenciaPersonal.setUltimaActRef1(0);
        EntityReferenciaPersonal.setUltimaActRef2(0);
        EntityReferenciaPersonal.setUltimaActEmpresa(0);

        hidenClienteID = (TextView) findViewById(R.id.hidenClienteID);
        hidenImporteSolicitado = (TextView) findViewById(R.id.hidenImporteSolicitado);
        hidenFechaPago = (TextView) findViewById(R.id.hidenFechaPago);
        hidenSolicitudID = (TextView) findViewById(R.id.hidenSolicitudID);
        hidenReferenciaLaboralid = (TextView) findViewById(R.id.hidenReferenciaLaboralid);
        hidenReferenciaPersonalidRef1 =(TextView) findViewById(R.id.hidenReferenciaPersonalidRef1);
        hidenReferenciaPersonalidRef2=(TextView) findViewById(R.id.hidenReferenciaPersonalidRef2);
        hidenUltimaActEmpresa=(TextView) findViewById(R.id.hidenUltimaActEmpresa);
        hidenUltimaActRef1=(TextView) findViewById(R.id.hidenUltimaActRef1);
        hidenUltimaActRef2=(TextView) findViewById(R.id.hidenUltimaActRef2);

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

        //CheckBox
        ckReferenciasAcepta = (CheckBox) findViewById(R.id.ckReferenciasAcepta);

        //Botones
        btnReferencias = (Button) findViewById(R.id.btnReferencias);
        btnReferencias.setFocusable(true);
        btnReferencias.setFocusableInTouchMode(true);

        btnFocoInicial = (Button) findViewById(R.id.btnFocoInicial);
        btnFocoInicial.setFocusable(true);
        btnFocoInicial.setFocusableInTouchMode(true);

    }
    private void EventManager() {
        txtTelefonoCelularRef1.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        txtTelefonoCelularRef2.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        txtTelefonoRefLaboral.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        btnReferencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidaGuardar())
                    new AsyncSaveData().execute();
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
            spnCatanioAdapter = new AdapterAnio(listaCatanio);
            spnRelacionRef1.setAdapter(spnRelacionRefAdapter);
            spnRelacionRef2.setAdapter(spnRelacionRefAdapter);
            spnAmistadRef1.setAdapter(spnCatanioAdapter);
            spnAmistadRef2.setAdapter(spnCatanioAdapter);
            spnTrabajando.setAdapter(spnCatanioAdapter);
            new AsyncInfoBloque().execute();
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

    //Region Existe Info del BLOQUE
    private class AsyncInfoBloque extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetInfoBloque();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            if(EntityReferenciaPersonal.getUltimaActRef1() != 0){
                SetInfoBloque();
            }
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
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatRelacionesPersonal";
        String METHOD_NAME = "GetCatRelacionesPersonal";
        String NAMESPACE = "http://tempuri.org/";

        ArrayList<PropertyInfo> valores =  new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("clienteid");
        pi1.setValue(hidenClienteID.getText().toString());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION,METHOD_NAME,NAMESPACE,valores);
        if(respuesta != null) {
            try {
                String[] listaRespuesta;
                listaRespuesta = new String[respuesta.getPropertyCount()];
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);
                    EntityReferenciaPersonal.setReferenciaPersonalidRef1(item.getProperty("ReferenciaPersonalidRef1").toString());
                    EntityReferenciaPersonal.setClienteid(item.getProperty("Clienteid").toString());
                    EntityReferenciaPersonal.setNombreRef1(item.getProperty("NombreRef1").toString());
                    EntityReferenciaPersonal.setApellidosRef1(item.getProperty("ApellidosRef1").toString());
                    EntityReferenciaPersonal.setTelefonoRef1(item.getProperty("TelefonoRef1").toString());
                    EntityReferenciaPersonal.setRelacionidRef1(Integer.parseInt(item.getProperty("RelacionidRef1").toString()));
                    EntityReferenciaPersonal.setAñoidRef1(Integer.parseInt(item.getProperty("AñoidRef1").toString()));
                    EntityReferenciaPersonal.setUltimaActRef1(Integer.parseInt(item.getProperty("UltimaActRef1").toString()));

                    EntityReferenciaPersonal.setReferenciaPersonalidRef2(item.getProperty("ReferenciaPersonalidRef2").toString());
                    EntityReferenciaPersonal.setNombreRef2(item.getProperty("NombreRef2").toString());
                    EntityReferenciaPersonal.setApellidosRef2(item.getProperty("ApellidosRef2").toString());
                    EntityReferenciaPersonal.setTelefonoRef2(item.getProperty("TelefonoRef2").toString());
                    EntityReferenciaPersonal.setRelacionidRef2(Integer.parseInt(item.getProperty("RelacionidRef2").toString()));
                    EntityReferenciaPersonal.setAñoidRef2(Integer.parseInt(item.getProperty("AñoidRef2").toString()));
                    EntityReferenciaPersonal.setUltimaActRef2(Integer.parseInt(item.getProperty("UltimaActRef2").toString()));

                    EntityReferenciaPersonal.setReferenciaLaboralid(item.getProperty("ReferenciaLaboralid").toString());
                    EntityReferenciaPersonal.setNombreEmpresa(item.getProperty("NombreEmpresa").toString());
                    EntityReferenciaPersonal.setTelefonoEmpresa(item.getProperty("TelefonoEmpresa").toString());
                    EntityReferenciaPersonal.setAñoidEmpresa(Integer.parseInt(item.getProperty("AñoidEmpresa").toString()));
                    EntityReferenciaPersonal.setUltimaActEmpresa(Integer.parseInt(item.getProperty("UltimaActEmpresa").toString()));
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    private void SetInfoBloque() {
        hidenReferenciaLaboralid.setText(EntityReferenciaPersonal.getReferenciaLaboralid());
        hidenReferenciaPersonalidRef1.setText(EntityReferenciaPersonal.getReferenciaPersonalidRef1());
        hidenReferenciaPersonalidRef2.setText(EntityReferenciaPersonal.getReferenciaPersonalidRef2());
        hidenUltimaActRef1.setText(EntityReferenciaPersonal.getUltimaActRef1());
        hidenUltimaActRef2.setText(EntityReferenciaPersonal.getUltimaActRef2());
        hidenUltimaActEmpresa.setText(EntityReferenciaPersonal.getUltimaActEmpresa());

        txtNombreRef1.setText(EntityReferenciaPersonal.getNombreRef1());
        txtApellidosRef1.setText(EntityReferenciaPersonal.getApellidosRef1());
        spnRelacionRef1.setSelection(getIndex(spnRelacionRef1, String.valueOf(EntityReferenciaPersonal.getRelacionidRef1())));
        txtTelefonoCelularRef1.setText(EntityReferenciaPersonal.getTelefonoRef1());
        spnAmistadRef1.setSelection(getIndex(spnAmistadRef1, String.valueOf(EntityReferenciaPersonal.getAñoidRef1())));

        txtNombreRef2.setText(EntityReferenciaPersonal.getNombreRef2());
        txtApellidosRef2.setText(EntityReferenciaPersonal.getApellidosRef2());
        spnRelacionRef2.setSelection(getIndex(spnRelacionRef2, String.valueOf(EntityReferenciaPersonal.getRelacionidRef2())));
        txtTelefonoCelularRef2.setText(EntityReferenciaPersonal.getTelefonoRef2());
        spnAmistadRef2.setSelection(getIndex(spnAmistadRef2, String.valueOf(EntityReferenciaPersonal.getAñoidRef2())));

        txtNombreEmpresa.setText(EntityReferenciaPersonal.getNombreEmpresa());
        txtTelefonoRefLaboral.setText(EntityReferenciaPersonal.getTelefonoEmpresa());
        spnTrabajando.setSelection(getIndex(spnTrabajando, String.valueOf(EntityReferenciaPersonal.getAñoidEmpresa())));
    }
    private int getIndex(Spinner spinner, String value) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                index = i;
                break;
            }
        }
        return index;
    }
    //Endregion



    //Region Guardar
    private boolean ValidaGuardar() {
        ArrayList<Object> Objetos = new ArrayList<Object>();
        Objetos.add(txtNombreRef1);
        Objetos.add(txtApellidosRef1);
        Objetos.add(spnRelacionRef1);
        Objetos.add(txtTelefonoCelularRef1);
        Objetos.add(spnAmistadRef1);
        Objetos.add(txtNombreRef2);
        Objetos.add(txtApellidosRef2);
        Objetos.add(spnRelacionRef2);
        Objetos.add(txtTelefonoCelularRef2);
        Objetos.add(spnAmistadRef2);
        Objetos.add(txtNombreEmpresa);
        Objetos.add(txtTelefonoRefLaboral);
        Objetos.add(spnTrabajando);
        Objetos.add(ckReferenciasAcepta);
        Collections.reverse(Objetos);
        boolean requeridos = false;
        for (Object item:Objetos) {
            if(item instanceof EditText){
                if (((EditText) item).getText().toString().trim().length() == 0) {
                    ((EditText) item).setError(getString(R.string.txtRequerido));
                    ((EditText) item).requestFocus();
                    requeridos = true;
                }
            }
            if(item instanceof Spinner){
                if(((Spinner) item).getSelectedItemPosition() == 0){
                    ((TextView)((Spinner) item).getSelectedView()).setError(getString(R.string.txtRequerido));
                    ((Spinner) item).requestFocus();
                    requeridos = true;
                }
            }
            if(item instanceof CheckBox){
                if(!((CheckBox) item).isChecked()){
                    ((CheckBox) item).requestFocus();
                    requeridos = true;
                }
            }
        }
        return !requeridos;
    }
    private class AsyncSaveData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            SaveInfoBloque();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            Intent i = new Intent(getApplicationContext(), Act_B2_Personal.class);
            i.putExtra("ClienteID", hidenClienteID.getText().toString());
            i.putExtra("ImporteSolicitado", hidenImporteSolicitado.getText().toString());
            i.putExtra("FechaPago", hidenFechaPago.getText().toString());
            i.putExtra("SolicitudID", hidenSolicitudID.getText().toString());
            startActivity(i);
            finish();
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Guardando...");
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    private void SaveInfoBloque(){
        String SOAP_ACTION = "http://tempuri.org/IService1/SetCatreferenciaspersonalTelefono";
        String METHOD_NAME = "SetCatreferenciaspersonalTelefono";
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
            DatosEntidad.put("NombreRef1", txtNombreRef1.getText().toString());
            DatosEntidad.put("ApellidosRef1",txtApellidosRef1.getText().toString());
            DatosEntidad.put("RelacionidRef1",  ((Catrelacionespersonal) spnRelacionRef1.getSelectedItem()).getRelacionid());
            DatosEntidad.put("TelefonoRef1", txtTelefonoCelularRef1.getText().toString());
            DatosEntidad.put("AñoidRef1", ((Catanio) spnAmistadRef1.getSelectedItem()).getAnioid());

            DatosEntidad.put("NombreRef2", txtNombreRef2.getText().toString());
            DatosEntidad.put("ApellidosRef2",txtApellidosRef2.getText().toString());
            DatosEntidad.put("RelacionidRef2", ((Catrelacionespersonal) spnRelacionRef2.getSelectedItem()).getRelacionid());
            DatosEntidad.put("TelefonoRef2", txtTelefonoCelularRef2.getText().toString());
            DatosEntidad.put("AñoidRef2", ((Catanio) spnAmistadRef2.getSelectedItem()).getAnioid());


            DatosEntidad.put("NombreEmpresa", txtNombreEmpresa.getText().toString());
            DatosEntidad.put("TelefonoEmpresa", txtTelefonoRefLaboral.getText().toString());
            DatosEntidad.put("AñoidEmpresa", ((Catanio) spnTrabajando.getSelectedItem()).getAnioid());

            //Campos de los controles ocultos debemos considerar que tienen valor para el momento en que
            //la app lo posicione sobre el bloque actual carge los datos que guardo con anterioridad
            DatosEntidad.put("Clienteid", hidenClienteID.getText().toString());
            DatosEntidad.put("ReferenciaLaboralid", hidenReferenciaLaboralid.getText().toString());
            DatosEntidad.put("ReferenciaPersonalidRef1", hidenReferenciaPersonalidRef1.getText().toString());
            DatosEntidad.put("ReferenciaPersonalidRef2", hidenReferenciaPersonalidRef2.getText().toString());
            DatosEntidad.put("UltimaActEmpresa", Integer.parseInt(hidenUltimaActEmpresa.getText().toString()));
            DatosEntidad.put("UltimaActRef1", Integer.parseInt(hidenUltimaActRef1.getText().toString()));
            DatosEntidad.put("UltimaActRef2", Integer.parseInt(hidenUltimaActRef2.getText().toString()));
        } catch (Exception ex) {

        }
        return "";
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
