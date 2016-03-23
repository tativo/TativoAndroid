package com.tativo.app.tativo.Bloques.Actividades;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
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

import com.tativo.app.tativo.Bloques.Clases.Catareaslaborales;
import com.tativo.app.tativo.Bloques.Clases.Catcolonia;
import com.tativo.app.tativo.Bloques.Clases.Catdatosempleo;
import com.tativo.app.tativo.Bloques.Clases.Catestadoscivil;
import com.tativo.app.tativo.Bloques.Clases.Catmarcastelefonos;
import com.tativo.app.tativo.Bloques.Clases.DatosCodigoPostal;
import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.Globals;
import com.tativo.app.tativo.Utilidades.ServiciosSoap;
import com.tativo.app.tativo.Utilidades.Utilerias;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Act_B4_Laboral extends AppCompatActivity {

    AutoCompleteTextView txtCodigoPostal, txtCalle, txtNumeroExt, txtNumeroInt, txtSueldoMensual, txtPaginaEmpresa, txtActividades, txtNuevaColonia;
    MaterialSpinner spnColonia, spnAreaLaboral;
    Button btnLaboral, btnCancelNuevacolonia;

    ProgressDialog progressDialog;
    Globals Sesion;
    Boolean NuevaColonia;
    DatosCodigoPostal datosCP;
    ArrayList<Catcolonia> lstCatColonia = new ArrayList<Catcolonia>();
    AdapterColonias ColoniasAdapter;
    TextView hnEstadoMunicipioTexto, txtAgregarColonia;
    LinearLayout lyNuevaColonia;

    AdapterAreasLaborales AreasLaboralesAdapter;
    ArrayList<Catareaslaborales> lstCatAreasLaborales = new ArrayList<Catareaslaborales>();

    Catdatosempleo catdatosempleo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_b4_laboral);
        LoadFormControls();
        FocusManager();
        EventManager();
        new AsyncLoadData().execute();
    }

    private void LoadFormControls()
    {
        Sesion = (Globals) getApplicationContext();

        Sesion.setCliendeID("83F0E461-3887-4185-94FB-D992D9AC7E26");

        progressDialog = new ProgressDialog(this);
        datosCP = new DatosCodigoPostal();
        NuevaColonia = false;

        catdatosempleo = new Catdatosempleo();
        catdatosempleo.setDatosempleoid("");
        catdatosempleo.setUltimaAct(0);

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
        btnLaboral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidaGuardar())
                {
                    new AsyncGuardar().execute();
                }
            }
        });

        txtCodigoPostal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (txtCodigoPostal.getText().toString().trim().length() == 5) {
                        new AsyncTraerDatosCodigoPostal().execute();
                    } else {
                        if (txtCodigoPostal.getText().toString().trim().length() > 0)
                            txtCodigoPostal.setError(getText(R.string.FormatoCP));
                        else {
                            hnEstadoMunicipioTexto.setText("");
                            spnColonia.setSelection(0);
                            spnColonia.setEnabled(false);
                        }
                    }
                }
            }
        });

        txtAgregarColonia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyNuevaColonia.setVisibility(View.VISIBLE);
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
                ColoniasAdapter = new AdapterColonias(lstCatColonia);
                spnColonia.setAdapter(ColoniasAdapter);
            }
            else
            {
                hnEstadoMunicipioTexto.setText("");
                txtCodigoPostal.setError(getText(R.string.CPNoEncontrado));
                txtCodigoPostal.requestFocus();
                spnColonia.setEnabled(false);
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage(getText(R.string.Buscando));
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
    private class AsyncLoadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetCatAreasLaborales();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            AreasLaboralesAdapter = new AdapterAreasLaborales(lstCatAreasLaborales);
            spnAreaLaboral.setAdapter(AreasLaboralesAdapter);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage(getText(R.string.Cargando));
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
        Objetos.add(txtNumeroInt);
        Objetos.add(spnAreaLaboral);
        Objetos.add(txtSueldoMensual);
        Objetos.add(txtPaginaEmpresa);
        Objetos.add(txtActividades);

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
                ((CheckBox) item).setError(null);
                if(!((CheckBox) item).isChecked()){
                    ((CheckBox) item).setError(getString(R.string.txtRequerido));
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
            progressDialog.setMessage(getText(R.string.Guardando));
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
                DatosEntidad.put("Colonia", ((Catcolonia) spnColonia.getSelectedItem()).getColonia().toString());
            DatosEntidad.put("Codigopostal", txtCodigoPostal.getText().toString());
            DatosEntidad.put("Paisid", datosCP.getPaisID().toString());
            DatosEntidad.put("Estadoid", datosCP.getEstadoID().toString());
            DatosEntidad.put("Municipioid", datosCP.getMunicipioID().toString());
            DatosEntidad.put("Ciudadid", datosCP.getCiudadID().toString());
            DatosEntidad.put("Arealaboralid", ((Catareaslaborales) spnAreaLaboral.getSelectedItem()).getArealaboralid());
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
}
