package com.tativo.app.tativo.Bloques.Actividades;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tativo.app.tativo.Bloques.Clases.CatBloqueoCliente;
import com.tativo.app.tativo.Bloques.Clases.Catareaslaborales;
import com.tativo.app.tativo.Bloques.Clases.Catcolonia;
import com.tativo.app.tativo.Bloques.Clases.Catdatosempleo;
import com.tativo.app.tativo.Bloques.Clases.DatosCodigoPostal;
import com.tativo.app.tativo.Bloques.Clases.DatosSolicitud;
import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.Globals;
import com.tativo.app.tativo.Utilidades.ServiciosSoap;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Act_B4_Laboral extends AppCompatActivity {

    AutoCompleteTextView txtCodigoPostal, txtCalle, txtNumeroExt, txtNumeroInt, txtSueldoMensual, txtPaginaEmpresa, txtActividades, txtNuevaColonia;
    MaterialSpinner spnColonia, spnAreaLaboral;
    Button btnLaboral, btnCancelNuevacolonia, btnFocoInicialB4;

    ProgressDialog progressDialog;
    Globals Sesion;
    Boolean NuevaColonia,edicionCP=false;
    DatosCodigoPostal datosCP;
    ArrayList<Catcolonia> lstCatColonia = new ArrayList<Catcolonia>();
    ArrayList<String> s_lstCatColonia= new ArrayList<String>();
    ArrayAdapter<String> ColoniasAdapter;
    TextView hnEstadoMunicipioTexto, txtAgregarColonia;
    LinearLayout lyNuevaColonia;

    ArrayAdapter<String> AreasLaboralesAdapter;
    ArrayList<String> s_lstCatAreasLaborales= new ArrayList<String>();
    ArrayList<Catareaslaborales> lstCatAreasLaborales = new ArrayList<Catareaslaborales>();

    Catdatosempleo catdatosempleo;

    AsyncEstatusSolicitud EstatusSolicitud = new AsyncEstatusSolicitud();
    boolean CancelaEstatusSolicitud = false ;


    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_b4_laboral);
        LoadFormControls();
        FocusManager();
        EventManager();
        Sesion = (Globals) getApplicationContext();
        new AsyncLoadData().execute();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            EstatusSolicitud.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            EstatusSolicitud.execute();
        }
    }

    private void LoadFormControls()
    {
        progressDialog = new ProgressDialog(this);
        datosCP = new DatosCodigoPostal();
        NuevaColonia = false;

        catdatosempleo = new Catdatosempleo();
        catdatosempleo.setDatosempleoid("");
        catdatosempleo.setUltimaAct(0);

        btnFocoInicialB4 = (Button) findViewById(R.id.btnFocoInicialB4);
        btnLaboral = (Button) findViewById(R.id.btnLaboral);

        txtAgregarColonia = (TextView) findViewById(R.id.txtAgregarColonia);
        hnEstadoMunicipioTexto = (TextView) findViewById(R.id.hnEstadoMunicipioTexto);

        lyNuevaColonia = (LinearLayout) findViewById(R.id.lyNuevaColonia);

        btnCancelNuevacolonia = (Button) findViewById(R.id.btnCancelNuevacolonia);

        txtCodigoPostal = (AutoCompleteTextView) findViewById(R.id.txtCodigoPostal);
        txtNuevaColonia = (AutoCompleteTextView) findViewById(R.id.txtNuevaColonia);
        txtCalle = (AutoCompleteTextView) findViewById(R.id.txtCalle);
        txtNumeroExt = (AutoCompleteTextView) findViewById(R.id.txtNumeroExt);
        txtNumeroInt = (AutoCompleteTextView) findViewById(R.id.txtNumeroInt);
        txtSueldoMensual = (AutoCompleteTextView) findViewById(R.id.txtSueldoMensual);
        txtPaginaEmpresa = (AutoCompleteTextView) findViewById(R.id.txtPaginaEmpresa);
        txtActividades = (AutoCompleteTextView) findViewById(R.id.txtActividades);

        spnColonia = (MaterialSpinner) findViewById(R.id.spnColonia);
        spnAreaLaboral = (MaterialSpinner) findViewById(R.id.spnAreaLaboral);

        spnColonia.setEnabled(false);
    }

    public void FocusManager()
    {
        FocusNextControl(R.id.txtCodigoPostal, "T", R.id.spnColonia, "S");
        FocusNextControl(R.id.spnColonia, "S", R.id.txtCalle, "T");
        FocusNextControl(R.id.txtNuevaColonia, "T", R.id.txtCalle, "T");
        FocusNextControl(R.id.txtCalle, "T", R.id.txtNumeroExt, "T");
        FocusNextControl(R.id.txtNumeroExt, "T", R.id.txtNumeroInt, "T");
        FocusNextControl(R.id.txtNumeroInt, "T", R.id.spnAreaLaboral, "S");
        FocusNextControl(R.id.spnAreaLaboral, "S", R.id.txtSueldoMensual, "T");
        FocusNextControl(R.id.txtSueldoMensual, "T", R.id.txtPaginaEmpresa, "T");
        FocusNextControl(R.id.txtPaginaEmpresa, "T", R.id.txtActividades, "T");
    }

    private void EventManager()
    {
        spnColonia.setOnTouchListener(new spOcultaTeclado());
        spnAreaLaboral.setOnTouchListener(new spOcultaTeclado());

        btnLaboral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidaGuardar()) {
                    EstatusSolicitud.cancel(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new AsyncGuardar().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        new AsyncGuardar().execute();
                    }
                }
            }
        });

        txtCodigoPostal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && edicionCP) {
                    if (txtCodigoPostal.getText().toString().trim().length() == 5) {
                        new AsyncTraerDatosCodigoPostal().execute();
                    } else {
                        if (txtCodigoPostal.getText().toString().trim().length() > 0)
                            txtCodigoPostal.setError(getText(R.string.msjFormatoCP));
                        else {
                            hnEstadoMunicipioTexto.setText("");
                            spnColonia.setSelection(0);
                            spnColonia.setEnabled(false);
                        }
                    }
                    edicionCP = false;
                }
            }
        });

        txtCodigoPostal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edicionCP = true;
                return false;
            }
        });

        /*
        txtCodigoPostal.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                edicionCP = true;
                return false;
            }
        });
        */

        txtAgregarColonia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyNuevaColonia.setVisibility(View.VISIBLE);
                spnColonia.setSelection(0);
                spnColonia.setEnabled(false);
                NuevaColonia = true;
                txtNuevaColonia.requestFocus();
            }
        });

        btnCancelNuevacolonia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyNuevaColonia.setVisibility(View.GONE);
                spnColonia.setEnabled(true);
                NuevaColonia = false;
                txtCalle.requestFocus();
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


    //CODIGO POSTAL
    private class AsyncTraerDatosCodigoPostal extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean encontroCP = false;
            encontroCP = TraerDatosCodigoPostal();
            if (encontroCP)
            {
                GetCatColonia();
            }
            return encontroCP;
        }

        @Override
        protected void onPostExecute(Boolean encontroCP) {
            progressDialog.dismiss();
            if (encontroCP)
            {
                hnEstadoMunicipioTexto.setText(datosCP.getMunicipio() + "/" + datosCP.getEstado());
                spnColonia.setEnabled(true);
                ColoniasAdapter = adapterSpinner(s_lstCatColonia);
                spnColonia.setAdapter(ColoniasAdapter);
            }
            else
            {
                hnEstadoMunicipioTexto.setText("");
                txtCodigoPostal.setError(getText(R.string.msjCPNoEncontrado));
                txtCodigoPostal.requestFocus();
                spnColonia.setEnabled(false);
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

    private Boolean TraerDatosCodigoPostal(){
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCodigoPostal";
        String METHOD_NAME = "GetCodigoPostal";
        String NAMESPACE = "http://tempuri.org/";
        Boolean r = false;

        ArrayList<PropertyInfo> valores =  new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("cp");
        pi1.setValue(txtCodigoPostal.getText().toString());
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
                    if (item.getProperty("CP") != null )
                    {
                        datosCP.setPaisID(item.getProperty("PaisID").toString().trim());
                        datosCP.setPais(item.getProperty("Pais").toString().trim());
                        datosCP.setEstadoID(item.getProperty("EstadoID").toString().trim());
                        datosCP.setEstado(item.getProperty("Estado").toString().trim());
                        datosCP.setMunicipioID(item.getProperty("MunicipioID").toString().trim());
                        datosCP.setMunicipio(item.getProperty("Municipio").toString().trim());
                        datosCP.setCiudadID(item.getProperty("CiudadID").toString().trim());
                        datosCP.setCiudad(item.getProperty("Ciudad").toString().trim());
                        datosCP.setColoniaID(item.getProperty("ColoniaID").toString().trim());
                        datosCP.setColonia(item.getProperty("Colonia").toString().trim());
                        r = true;
                    }

                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return r;
    }
    //CODIGO POSTAL


    //LLENA SPINNER
    //Adapter generico para los spiner
    private ArrayAdapter<String> adapterSpinner(ArrayList<String> arrayList ) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Act_B4_Laboral.this, android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
        private class AsyncLoadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetCatAreasLaborales();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            AreasLaboralesAdapter = adapterSpinner(s_lstCatAreasLaborales);
            spnAreaLaboral.setAdapter(AreasLaboralesAdapter);
            new AsyncInfoBloque().execute();
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage(getText(R.string.msjCargando));
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void GetCatAreasLaborales(){
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatAreasLaborales";
        String METHOD_NAME = "GetCatAreasLaborales";
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
                    Catareaslaborales entidad = new Catareaslaborales();
                    entidad.setArealaboralid(Integer.parseInt(item.getProperty("Arealaboralid").toString()));
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    lstCatAreasLaborales.add(entidad);
                    s_lstCatAreasLaborales.add(item.getProperty("Descripcion").toString());
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    private class AdapterAreasLaborales extends BaseAdapter implements SpinnerAdapter {
        private final List<Catareaslaborales> data;

        public AdapterAreasLaborales(List<Catareaslaborales> data){
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

    private void GetCatColonia(){
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatcolonia";
        String METHOD_NAME = "GetCatcolonia";
        String NAMESPACE = "http://tempuri.org/";

        ArrayList<PropertyInfo> valores =  new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("codigopostal");
        pi1.setValue(txtCodigoPostal.getText().toString());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE,valores);
        if(respuesta != null) {
            try {
                lstCatColonia = new ArrayList<Catcolonia>();
                String[] listaRespuesta;
                listaRespuesta = new String[respuesta.getPropertyCount()];
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);
                    Catcolonia entidad = new Catcolonia();
                    entidad.setCiudadid(item.getProperty("Ciudadid").toString());
                    entidad.setColoniaid(item.getProperty("Coloniaid").toString());
                    entidad.setColonia(item.getProperty("Colonia").toString());
                    lstCatColonia.add(entidad);
                    s_lstCatColonia.add(item.getProperty("Colonia").toString());
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    private class AdapterColonias extends BaseAdapter implements SpinnerAdapter {
        private final List<Catcolonia> data;

        public AdapterColonias(List<Catcolonia> data){
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
            text.setText(data.get(position).getColonia());
            return text;
        }
    }
    //LLENA SPINNER

    //GUARDAR
    private boolean ValidaGuardar() {
        ArrayList<Object> Objetos = new ArrayList<Object>();
        Objetos.add(txtCodigoPostal);
        if (NuevaColonia)
            Objetos.add(txtNuevaColonia);
        else
            Objetos.add(spnColonia);
        Objetos.add(txtCalle);
        Objetos.add(txtNumeroExt);
        Objetos.add(spnAreaLaboral);
        Objetos.add(txtSueldoMensual);
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
    private class AsyncGuardar extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GuardarDatos();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            Intent i = new Intent(getApplicationContext(), Act_B5_General.class);
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
    private void GuardarDatos()
    {
        String SOAP_ACTION = "http://tempuri.org/IService1/SetCatdatosempleoTelefono";
        String METHOD_NAME = "SetCatdatosempleoTelefono";
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
            DatosEntidad.put("Datosempleoid", catdatosempleo.getDatosempleoid().toString());
            DatosEntidad.put("clienteid", Sesion.getCliendeID());
            DatosEntidad.put("Calle", txtCalle.getText().toString());
            DatosEntidad.put("Numeroext", txtNumeroExt.getText().toString());
            DatosEntidad.put("numeroint", txtNumeroInt.getText().toString());
            if (NuevaColonia)
                DatosEntidad.put("Colonia", txtNuevaColonia.getText().toString());
            else
                DatosEntidad.put("Colonia", lstCatColonia.get(spnColonia.getSelectedItemPosition()-1).getColonia().toString());
            DatosEntidad.put("Codigopostal", txtCodigoPostal.getText().toString());
            DatosEntidad.put("Paisid", datosCP.getPaisID().toString());
            DatosEntidad.put("Estadoid", datosCP.getEstadoID().toString());
            DatosEntidad.put("Municipioid", datosCP.getMunicipioID().toString());
            DatosEntidad.put("Ciudadid", datosCP.getCiudadID().toString());
            DatosEntidad.put("Arealaboralid", lstCatAreasLaborales.get(spnAreaLaboral.getSelectedItemPosition()-1).getArealaboralid());
            DatosEntidad.put("Descripcionactividad", txtActividades.getText().toString());
            DatosEntidad.put("SueldoMensual", txtSueldoMensual.getText().toString());
            DatosEntidad.put("Paginaweb", txtPaginaEmpresa.getText().toString());
            DatosEntidad.put("UltimaAct", catdatosempleo.getUltimaAct());

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }
        return DatosEntidad.toString();
    }
    //GUARDAR


    //Info del BLOQUE
    String indexColonias ="0";
    private class AsyncInfoBloque extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetInfoBloque();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            if (catdatosempleo.getUltimaAct() != 0) {
                SetInfoBloque();
            }
            btnFocoInicialB4.requestFocus();
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
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatdatosempleo";
        String METHOD_NAME = "GetCatdatosempleo";
        String NAMESPACE = "http://tempuri.org/";

        ArrayList<PropertyInfo> valores =  new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("Clienteid");
        pi1.setValue(Sesion.getCliendeID());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION,METHOD_NAME,NAMESPACE,valores);
        if (respuesta != null) {
            try {
                if (Boolean.parseBoolean(respuesta.getProperty("EsValido").toString())) {
                    SoapObject Datos = (SoapObject) respuesta.getProperty("Datos");
                    SoapObject DatosCodigoPostal = (SoapObject) Datos.getProperty("CP");
                    SoapObject DatosColonias = (SoapObject) Datos.getProperty("Colonias");
                    indexColonias = Datos.getProperty("IndexColonia").toString().toUpperCase().trim();

                    if (Integer.parseInt(Datos.getProperty("UltimaAct").toString()) != 0) {
                        catdatosempleo.setDatosempleoid(Datos.getProperty("Datosempleoid").toString());
                        catdatosempleo.setClienteid(Datos.getProperty("Clienteid").toString());
                        catdatosempleo.setTieneempleo(Boolean.parseBoolean(Datos.getProperty("Tieneempleo").toString()));
                        catdatosempleo.setCalle(Datos.getProperty("Calle").toString());
                        catdatosempleo.setNumeroext(Datos.getProperty("Numeroext").toString());
                        catdatosempleo.setNumeroint(Datos.getProperty("numeroint").toString());
                        catdatosempleo.setColonia(Datos.getProperty("Colonia").toString());
                        catdatosempleo.setCodigopostal(Datos.getProperty("Codigopostal").toString());
                        catdatosempleo.setPaisid(Datos.getProperty("Paisid").toString());
                        catdatosempleo.setEstadoid(Datos.getProperty("Estadoid").toString());
                        catdatosempleo.setMunicipioid(Datos.getProperty("Municipioid").toString());
                        catdatosempleo.setCiudadid(Datos.getProperty("Ciudadid").toString());
                        catdatosempleo.setArealaboralid(Integer.parseInt(Datos.getProperty("Arealaboralid").toString()));
                        catdatosempleo.setDescripcionactividad(Datos.getProperty("Descripcionactividad").toString());
                        catdatosempleo.setSueldoMensual(Double.parseDouble(Datos.getProperty("Sueldomensual").toString()));
                        catdatosempleo.setPaginaweb(Datos.getProperty("Paginaweb").toString());
                        catdatosempleo.setUltimaAct(Integer.parseInt(Datos.getProperty("UltimaAct").toString()));
                    }

                    lstCatColonia = new ArrayList<Catcolonia>();
                    for (int i = 0; i < DatosColonias.getPropertyCount(); i++) {
                        SoapObject item = (SoapObject) DatosColonias.getProperty(i);
                        Catcolonia entidad = new Catcolonia();
                        entidad.setCiudadid(item.getProperty("Ciudadid").toString());
                        entidad.setColoniaid(item.getProperty("Coloniaid").toString());
                        entidad.setColonia(item.getProperty("Colonia").toString());
                        lstCatColonia.add(entidad);
                        s_lstCatColonia.add(item.getProperty("Colonia").toString());
                    }
                    if (DatosCodigoPostal.getProperty("PaisID") != null ) {
                        datosCP.setPaisID(DatosCodigoPostal.getProperty("PaisID").toString().trim());
                        datosCP.setPais(DatosCodigoPostal.getProperty("Pais").toString().trim());
                        datosCP.setEstadoID(DatosCodigoPostal.getProperty("EstadoID").toString().trim());
                        datosCP.setEstado(DatosCodigoPostal.getProperty("Estado").toString().trim());
                        datosCP.setMunicipioID(DatosCodigoPostal.getProperty("MunicipioID").toString().trim());
                        datosCP.setMunicipio(DatosCodigoPostal.getProperty("Municipio").toString().trim());
                        datosCP.setCiudadID(DatosCodigoPostal.getProperty("CiudadID").toString().trim());
                        datosCP.setCiudad(DatosCodigoPostal.getProperty("Ciudad").toString().trim());
                        datosCP.setColoniaID(DatosCodigoPostal.getProperty("ColoniaID").toString().trim());
                        datosCP.setColonia(DatosCodigoPostal.getProperty("Colonia").toString().trim());
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    private void SetInfoBloque() {
        txtCodigoPostal.setText(catdatosempleo.getCodigopostal());
        txtCalle.setText(catdatosempleo.getCalle());
        txtNumeroExt.setText(catdatosempleo.getNumeroext());
        txtNumeroInt.setText(catdatosempleo.getNumeroint());

        //Preguntamos si el usuario ya ingreso los datos del codigo postal para mostrarlos en pantalla
        if (catdatosempleo.getCodigopostal().trim().length() > 0) {
            hnEstadoMunicipioTexto.setText(datosCP.getMunicipio() + "/" + datosCP.getEstado());
            spnColonia.setEnabled(true);
            ColoniasAdapter = adapterSpinner(s_lstCatColonia);
            spnColonia.setAdapter(ColoniasAdapter);
        }
        //Preguntamos si el usuario ya ingreso o selecciono una colonia
        if (catdatosempleo.getColonia().trim().length() > 0) {
            if (indexColonias.trim().toUpperCase().equals("0")) {
                lyNuevaColonia.setVisibility(View.VISIBLE);
                spnColonia.setSelection(0);
                spnColonia.setEnabled(false);
                NuevaColonia = true;
                txtNuevaColonia.setText(catdatosempleo.getColonia().trim());
            } else {
                spnColonia.setSelection(getIndexColonia(indexColonias));
            }
        }

        spnAreaLaboral.setSelection(catdatosempleo.getArealaboralid());
        txtActividades.setText(catdatosempleo.getDescripcionactividad());
        txtSueldoMensual.setText(catdatosempleo.getSueldoMensual().toString());
        txtPaginaEmpresa.setText(catdatosempleo.getPaginaweb());
    }
    private int getIndexColonia(String myString) {
        int index = 0;
        for (int i = 0; i < lstCatColonia.size(); i++) {
            if (lstCatColonia.get(i).getColoniaid().toUpperCase().trim().equals(myString)) {
                index = i + 1;
            }
        }
        return index;
    }
    //Info del BLOQUE



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
                    int Segundos = 0;
                    while(Segundos <=30) {
                        if(isCancelled()){
                            CancelaEstatusSolicitud = true;
                            break;
                        }
                        Thread.sleep(1000);
                        Segundos++;
                    }
                } catch (InterruptedException ex) {
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            if (!CancelaEstatusSolicitud) {
                //Si salio del ciclo quiere decir que encontro bloqueos
                if (Bloqueos.getBloqueoid() != 0) {
                    new AlertDialog.Builder(Act_B4_Laboral.this)
                            .setTitle(R.string.msgRefTitulo)
                            .setMessage(R.string.msgRefNoContesto)
                            .setCancelable(false)
                            .setPositiveButton(R.string.msgRefOk, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Sesion.setBloqueoReferencia(true);
                                    Sesion.setBloqueoCliente(Bloqueos);
                                    Sesion.setSolicitud(Solicitud);
                                    Sesion.setBloqueActual(4);
                                    Intent i = new Intent(getApplicationContext(), Act_B1_Referencias.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).create().show();
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
}
