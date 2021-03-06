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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tativo.app.tativo.Bloques.Clases.Catanio;
import com.tativo.app.tativo.Bloques.Clases.Catreferenciaspersonal;
import com.tativo.app.tativo.Bloques.Clases.Catrelacionespersonal;
import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.Globals;
import com.tativo.app.tativo.Utilidades.ServiciosSoap;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Act_B1_Referencias extends AppCompatActivity {
    ProgressDialog progressDialog;
    Button btnReferencias,btnFocoInicial;
    Spinner spnRelacionRef1,spnRelacionRef2,spnAmistadRef1,spnAmistadRef2,spnTrabajando;
    EditText txtNombreRef1,txtApellidosRef1,txtTelefonoCelularRef1,txtNombreRef2,txtApellidosRef2,txtTelefonoCelularRef2,txtNombreEmpresa,txtTelefonoRefLaboral;
    //AdapterRelacionPersonal spnRelacionRefAdapter;
    //AdapterAnio spnCatanioAdapter;
    ArrayList<Catrelacionespersonal> listaCatrelacionespersonal = new ArrayList<Catrelacionespersonal>();
    ArrayList<Catanio> listaCatanio = new ArrayList<Catanio>();
    CheckBox ckReferenciasAcepta;
    Catreferenciaspersonal EntityReferenciaPersonal;
    Globals Sesion;
    LinearLayout lyReferencia1,lyReferencia2,lyReferenciaLaboral;
    boolean r1 = false, r2 = false, rl = false;

    ArrayList<String> arrayCatRalacionPersonal, arrayCatAnio;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_b1_referencias);
        progressDialog = new ProgressDialog(Act_B1_Referencias.this);
        LoadFormControls();
        FocusManager();
        EventManager();
        Sesion = (Globals) getApplicationContext();
        btnFocoInicial.requestFocus();
        new AsyncLoadData().execute();
        //Sesion.setCliendeID("014B2F7C-EF3D-4251-8439-BA73EC0AC0AA");
    }

    public void FocusManager(){
        FocusNextControl(R.id.txtNombreRef1, "T", R.id.txtApellidosRef1, "T");
        FocusNextControl(R.id.txtApellidosRef1, "T", R.id.spnRelacionRef1, "S");
        FocusNextControl(R.id.spnRelacionRef1, "S", R.id.txtTelefonoCelularRef1, "T");
        FocusNextControl(R.id.txtTelefonoCelularRef1, "T", R.id.spnAmistadRef1, "S");
        //FocusNextControl(R.id.spnAmistadRef1, "S", R.id.txtNombreRef2, "T");

        FocusNextControl(R.id.txtNombreRef2, "T", R.id.txtApellidosRef2, "T");
        FocusNextControl(R.id.txtApellidosRef2, "T", R.id.spnRelacionRef2, "S");
        FocusNextControl(R.id.spnRelacionRef2, "S", R.id.txtTelefonoCelularRef2, "T");
        FocusNextControl(R.id.txtTelefonoCelularRef2, "T", R.id.spnAmistadRef2, "S");
        //FocusNextControl(R.id.spnAmistadRef2, "S", R.id.txtNombreEmpresa, "T");

        FocusNextControl(R.id.txtNombreEmpresa, "T", R.id.txtTelefonoRefLaboral, "T");
        FocusNextControl(R.id.txtTelefonoRefLaboral, "T", R.id.spnTrabajando, "S");
    }

    public void FocusNextControl(int o,String ot, int d,String dt ) {
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
    private void LoadFormControls(){
        //Layout
        lyReferencia1 = (LinearLayout) findViewById(R.id.lyReferencia1);
        lyReferencia2= (LinearLayout) findViewById(R.id.lyReferencia2);
        lyReferenciaLaboral= (LinearLayout) findViewById(R.id.lyReferenciaLaboral);


        //Entidad
        EntityReferenciaPersonal = new Catreferenciaspersonal();
        EntityReferenciaPersonal.setUltimaActRef1(0);
        EntityReferenciaPersonal.setUltimaActRef2(0);
        EntityReferenciaPersonal.setUltimaActEmpresa(0);

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
        spnRelacionRef1.setOnTouchListener(new spOcultaTeclado());
        spnRelacionRef1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0)
                {
                    if (listaCatrelacionespersonal.get(spnRelacionRef1.getSelectedItemPosition() - 1).getDescripcion().equals("Familiar"))
                    {
                        spnAmistadRef1.setSelection(getIndexAnio("Mas de 10 años"));
                        spnAmistadRef1.setEnabled(false);
                    }
                    else {
                        spnAmistadRef1.setSelection(0);
                        spnAmistadRef1.setEnabled(true);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnRelacionRef2.setOnTouchListener(new spOcultaTeclado());
        spnRelacionRef2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0)
                {
                    if (listaCatrelacionespersonal.get(spnRelacionRef2.getSelectedItemPosition() - 1).getDescripcion().equals("Familiar"))
                    {
                        spnAmistadRef2.setSelection(getIndexAnio("Mas de 10 años"));
                        spnAmistadRef2.setEnabled(false);
                    }
                    else {
                        spnAmistadRef2.setSelection(0);
                        spnAmistadRef2.setEnabled(true);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnAmistadRef1.setOnTouchListener(new spOcultaTeclado());
        spnAmistadRef2.setOnTouchListener(new spOcultaTeclado());
        spnTrabajando.setOnTouchListener(new spOcultaTeclado());

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
            //progressDialog.dismiss();
            //spnRelacionRefAdapter = new AdapterRelacionPersonal(listaCatrelacionespersonal);
            //spnCatanioAdapter = new AdapterAnio(listaCatanio);

            ArrayAdapter<String> adapterRelacionPersonal = adapterSpinner(arrayCatRalacionPersonal);
            spnRelacionRef1.setAdapter(adapterRelacionPersonal);
            spnRelacionRef2.setAdapter(adapterRelacionPersonal);

            ArrayAdapter<String> adapterAnios = adapterSpinner(arrayCatAnio);
            spnAmistadRef1.setAdapter(adapterAnios);
            spnAmistadRef2.setAdapter(adapterAnios);

            spnTrabajando.setAdapter(adapterAnios);

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
                arrayCatRalacionPersonal = new ArrayList<String>();
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);
                    Catrelacionespersonal entidad = new Catrelacionespersonal();
                    entidad.setRelacionid(Integer.parseInt(item.getProperty("Relacionid").toString()));
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    arrayCatRalacionPersonal.add(item.getProperty("Descripcion").toString());
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
                arrayCatAnio = new ArrayList<String>();
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);
                    Catanio entidad = new Catanio();
                    entidad.setAnioid(Integer.parseInt(item.getProperty("Anioid").toString()));
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    arrayCatAnio.add(item.getProperty("Descripcion").toString());
                    listaCatanio.add(entidad);
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    /*
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
    */
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
            if (EntityReferenciaPersonal.getUltimaActRef1() != 0) {
                SetInfoBloque();
            }
            ValidaCargaPorBloqueo();
        }
        @Override
        protected void onPreExecute() {
            //progressDialog.setMessage("Cargando...");
            //progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    private void GetInfoBloque(){
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatreferenciaspersonal";
        String METHOD_NAME = "GetCatreferenciaspersonal";
        String NAMESPACE = "http://tempuri.org/";

        ArrayList<PropertyInfo> valores =  new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("clienteid");
        pi1.setValue(Sesion.getCliendeID());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION,METHOD_NAME,NAMESPACE,valores);
        if(respuesta != null) {
            try {
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());

                if (ev)
                {


                String[] listaRespuesta;
                listaRespuesta = new String[respuesta.getPropertyCount()];
                SoapObject item = (SoapObject) respuesta.getProperty(0);
                if(Integer.parseInt(item.getProperty("UltimaActRef1").toString())!=0) {
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
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    private void SetInfoBloque() {

        txtNombreRef1.setText(EntityReferenciaPersonal.getNombreRef1());
        txtApellidosRef1.setText(EntityReferenciaPersonal.getApellidosRef1());
        spnRelacionRef1.setSelection(EntityReferenciaPersonal.getRelacionidRef1());
        txtTelefonoCelularRef1.setText(EntityReferenciaPersonal.getTelefonoRef1());
        spnAmistadRef1.setSelection(EntityReferenciaPersonal.getAñoidRef1());

        txtNombreRef2.setText(EntityReferenciaPersonal.getNombreRef2());
        txtApellidosRef2.setText(EntityReferenciaPersonal.getApellidosRef2());
        spnRelacionRef2.setSelection(EntityReferenciaPersonal.getRelacionidRef2());
        txtTelefonoCelularRef2.setText(EntityReferenciaPersonal.getTelefonoRef2());
        spnAmistadRef2.setSelection(EntityReferenciaPersonal.getAñoidRef2());

        txtNombreEmpresa.setText(EntityReferenciaPersonal.getNombreEmpresa());
        txtTelefonoRefLaboral.setText(EntityReferenciaPersonal.getTelefonoEmpresa());
        spnTrabajando.setSelection(EntityReferenciaPersonal.getAñoidEmpresa());
    }
    private void ValidaCargaPorBloqueo() {
        if (Sesion.isBloqueoReferencia()) {
            String[] Bloqueos = Sesion.getBloqueoCliente().getDatos().split(",");
            for (int i = 0; i < Bloqueos.length; i++) {
                switch (Bloqueos[i]) {
                    case "blocRefL":
                        rl = true;
                        break;
                    case "blocRef1":
                        r1 = true;
                        break;
                    case "blocRef2":
                        r2 = true;
                        break;
                }
            }
            if (!r1)
                lyReferencia1.setVisibility(LinearLayout.GONE);
            if (!r2)
                lyReferencia2.setVisibility(LinearLayout.GONE);
            if (!rl)
                lyReferenciaLaboral.setVisibility(LinearLayout.GONE);
        }
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
            Intent i = new Intent();
            switch (Sesion.getBloqueActual()) {
                case 2:
                    i = new Intent(getApplicationContext(), Act_B2_Personal.class);
                    break;
                case 3:
                    i = new Intent(getApplicationContext(), Act_B3_InfDeposito.class);
                    break;
                case 4:
                    i = new Intent(getApplicationContext(), Act_B4_Laboral.class);
                    break;
                case 5:
                    i = new Intent(getApplicationContext(), Act_B5_General.class);
                    break;
                default:
                    i = new Intent(getApplicationContext(), Act_B2_Personal.class);
                    break;
            }
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
            //DatosEntidad.put("RelacionidRef1",  ((Catrelacionespersonal) spnRelacionRef1.getSelectedItem()).getRelacionid());
            DatosEntidad.put("RelacionidRef1",  listaCatrelacionespersonal.get(spnRelacionRef1.getSelectedItemPosition() - 1).getRelacionid());
            DatosEntidad.put("TelefonoRef1", txtTelefonoCelularRef1.getText().toString());
            //DatosEntidad.put("AñoidRef1", ((Catanio) spnAmistadRef1.getSelectedItem()).getAnioid());
            DatosEntidad.put("AñoidRef1", listaCatanio.get(spnAmistadRef1.getSelectedItemPosition() - 1).getAnioid());

            DatosEntidad.put("NombreRef2", txtNombreRef2.getText().toString());
            DatosEntidad.put("ApellidosRef2",txtApellidosRef2.getText().toString());
            //DatosEntidad.put("RelacionidRef2", ((Catrelacionespersonal) spnRelacionRef2.getSelectedItem()).getRelacionid());
            DatosEntidad.put("RelacionidRef2", listaCatrelacionespersonal.get(spnRelacionRef2.getSelectedItemPosition() - 1).getRelacionid());
            DatosEntidad.put("TelefonoRef2", txtTelefonoCelularRef2.getText().toString());
            //DatosEntidad.put("AñoidRef2", ((Catanio) spnAmistadRef2.getSelectedItem()).getAnioid());
            DatosEntidad.put("AñoidRef2", listaCatanio.get(spnAmistadRef2.getSelectedItemPosition() - 1).getAnioid());


            DatosEntidad.put("NombreEmpresa", txtNombreEmpresa.getText().toString());
            DatosEntidad.put("TelefonoEmpresa", txtTelefonoRefLaboral.getText().toString());
            //DatosEntidad.put("AñoidEmpresa", ((Catanio) spnTrabajando.getSelectedItem()).getAnioid());
            DatosEntidad.put("AñoidEmpresa", listaCatanio.get(spnTrabajando.getSelectedItemPosition() - 1).getAnioid());

            //Campos de los controles ocultos debemos considerar que tienen valor para el momento en que
            //la app lo posicione sobre el bloque actual carge los datos que guardo con anterioridad
            DatosEntidad.put("Clienteid", Sesion.getCliendeID());
            DatosEntidad.put("ReferenciaLaboralid", EntityReferenciaPersonal.getReferenciaLaboralid());
            DatosEntidad.put("ReferenciaPersonalidRef1", EntityReferenciaPersonal.getReferenciaPersonalidRef1());
            DatosEntidad.put("ReferenciaPersonalidRef2", EntityReferenciaPersonal.getReferenciaPersonalidRef2());
            DatosEntidad.put("UltimaActEmpresa", EntityReferenciaPersonal.getUltimaActEmpresa());
            DatosEntidad.put("UltimaActRef1", EntityReferenciaPersonal.getUltimaActRef1());
            DatosEntidad.put("UltimaActRef2", EntityReferenciaPersonal.getUltimaActRef2());
            if(Sesion.isBloqueoReferencia())
                DatosEntidad.put("BloqueSiguiente",0);

        } catch (Exception ex) {

        }
        return DatosEntidad.toString();
    }
    //EndGuardar

    private class spOcultaTeclado implements View.OnTouchListener {
        public boolean onTouch(View v, MotionEvent event) {
            if (getCurrentFocus() != null)
            {
                InputMethodManager method = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                method.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            return false;
        }
    }

    public ArrayAdapter<String> adapterSpinner(ArrayList<String> arrayList ) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Act_B1_Referencias.this, android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return  adapter;
    }

    private int getIndexAnio(String myString) {
        int index = 0;
        for (int i = 0; i < listaCatanio.size(); i++) {
            if (listaCatanio.get(i).getDescripcion().toUpperCase().trim().equals(myString.toUpperCase())) {
                index = i + 1;
            }
        }
        return index;
    }
}
