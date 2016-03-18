package com.tativo.app.tativo.Bloques.Actividades;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tativo.app.tativo.Bloques.Clases.Catcolonia;
import com.tativo.app.tativo.Bloques.Clases.Catestadoscivil;
import com.tativo.app.tativo.Bloques.Clases.Catmarcastelefonos;
import com.tativo.app.tativo.Bloques.Clases.DatosCodigoPostal;
import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.Globals;
import com.tativo.app.tativo.Utilidades.ServiciosSoap;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Act_B2_Personal extends AppCompatActivity {

    Button btnInfPersonal, btnFechaNacimiento;
    AutoCompleteTextView txtFechaNacimiento, txtDependientes, txtCodigoPostal, txtCalle, txtNumeroExt, txtNumeroInt, txtTelefonoCelular, txt4DigitosTarjeta;
    MaterialSpinner spnGenero, spnEstadoCivil, spnColonia, spnMarcaCelular;
    Switch swtTarjetaCredito, swtCreditoHipotecario, swtCreditoAutomotriz;
    CheckBox ckTerminosCondiciones1, ckTerminosCondiciones2, ckTerminosCondiciones3, ckTerminosCondiciones4;
    TextView hnEstadoMunicipioTexto;
    ProgressDialog progressDialog;
    Globals g;
    DatosCodigoPostal datosCP;
    AdapterEstadosCivil EstadosCivilAdapter;
    AdapterMarcaTelefono MarcaTelefonoAdapter;
    AdapterColonias ColoniasAdapter;
    ArrayList<Catestadoscivil> lstCatestadoscivil = new ArrayList<Catestadoscivil>();
    ArrayList<Catmarcastelefonos> lstCatMarcaTelefono = new ArrayList<Catmarcastelefonos>();
    ArrayList<Catcolonia> lstCatColonia = new ArrayList<Catcolonia>();


    int year_x, month_x, day_x;
    static final int DILOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_b2_personal);
        g = (Globals) getApplicationContext();
        progressDialog = new ProgressDialog(this);
        datosCP = new DatosCodigoPostal();
        LoadFormControls();
        FocusManager();
        EventManager();
        new AsyncLoadData().execute();

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        spnColonia.setEnabled(false);

        txtFechaNacimiento.requestFocus();
    }

    private void LoadFormControls()
    {
        btnInfPersonal = (Button) findViewById(R.id.btnInfPersonal);
        txtFechaNacimiento = (AutoCompleteTextView) findViewById(R.id.txtFechaNacimiento);
        btnFechaNacimiento = (Button) findViewById(R.id.btnFechaNacimiento);
        txtDependientes = (AutoCompleteTextView) findViewById(R.id.txtDependientes);
        txtCodigoPostal = (AutoCompleteTextView) findViewById(R.id.txtCodigoPostal);
        txtCalle = (AutoCompleteTextView) findViewById(R.id.txtCalle);
        txtNumeroExt = (AutoCompleteTextView) findViewById(R.id.txtNumeroExt);
        txtNumeroInt = (AutoCompleteTextView) findViewById(R.id.txtNumeroInt);
        txtTelefonoCelular = (AutoCompleteTextView) findViewById(R.id.txtTelefonoCelular);
        txt4DigitosTarjeta = (AutoCompleteTextView) findViewById(R.id.txt4DigitosTarjeta);

        spnGenero = (MaterialSpinner) findViewById(R.id.spnGenero);
        spnEstadoCivil = (MaterialSpinner) findViewById(R.id.spnEstadoCivil);
        spnColonia = (MaterialSpinner) findViewById(R.id.spnColonia);
        spnMarcaCelular = (MaterialSpinner) findViewById(R.id.spnMarcaCelular);

        swtTarjetaCredito = (Switch) findViewById(R.id.swtTarjetaCredito);
        swtCreditoHipotecario = (Switch) findViewById(R.id.swtCreditoHipotecario);
        swtCreditoAutomotriz = (Switch) findViewById(R.id.swtCreditoAutomotriz);

        ckTerminosCondiciones1 = (CheckBox) findViewById(R.id.ckTerminosCondiciones1);
        ckTerminosCondiciones2 = (CheckBox) findViewById(R.id.ckTerminosCondiciones2);
        ckTerminosCondiciones3 = (CheckBox) findViewById(R.id.ckTerminosCondiciones3);
        ckTerminosCondiciones4 = (CheckBox) findViewById(R.id.ckTerminosCondiciones4);

        hnEstadoMunicipioTexto = (TextView) findViewById(R.id.hnEstadoMunicipioTexto);


    }

    public void FocusManager()
    {
        FocusNextControl(R.id.txtFechaNacimiento, "T", R.id.txtDependientes, "T");
        FocusNextControl(R.id.txtDependientes, "T", R.id.spnGenero, "S");
        FocusNextControl(R.id.spnGenero, "S", R.id.spnEstadoCivil, "S");
        FocusNextControl(R.id.spnEstadoCivil, "S", R.id.txtCodigoPostal, "T");
        FocusNextControl(R.id.txtCodigoPostal, "T", R.id.spnColonia, "S");
        FocusNextControl(R.id.spnColonia, "S", R.id.txtCalle, "T");
        FocusNextControl(R.id.txtNumeroExt, "T", R.id.txtNumeroInt, "T");
        FocusNextControl(R.id.txtNumeroInt, "T", R.id.txtTelefonoCelular, "T");
        FocusNextControl(R.id.txtTelefonoCelular, "T", R.id.spnEstadoCivil, "S");
    }

    private void EventManager()
    {
        btnFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DILOG_ID);
            }
        });

        swtTarjetaCredito.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txt4DigitosTarjeta.setEnabled(true);
                    txt4DigitosTarjeta.requestFocus();
                } else {
                    txt4DigitosTarjeta.setEnabled(false);
                    txtTelefonoCelular.requestFocus();
                }
            }
        });

        btnInfPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Act_B3_InfDeposito.class);
                startActivity(i);
                finish();
            }
        });

        txtCodigoPostal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (txtCodigoPostal.getText().toString().trim().length() == 5) {
                        new AsyncTraerDatosCodigoPostal().execute();
                    } else
                        txtCodigoPostal.setError(getText(R.string.FormatoCP));
                }
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
            GetCatEstadosCivil();
            GetCatMarcaTelefono();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            EstadosCivilAdapter = new AdapterEstadosCivil(lstCatestadoscivil);
            spnEstadoCivil.setAdapter(EstadosCivilAdapter);

            MarcaTelefonoAdapter = new AdapterMarcaTelefono(lstCatMarcaTelefono);
            spnMarcaCelular.setAdapter(MarcaTelefonoAdapter);
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

    private void GetCatEstadosCivil(){
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatEstadosCivil";
        String METHOD_NAME = "GetCatEstadosCivil";
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
                    Catestadoscivil entidad = new Catestadoscivil();
                    entidad.setEstadocivilid(Integer.parseInt(item.getProperty("Estadocivilid").toString()));
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    lstCatestadoscivil.add(entidad);
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private class AdapterEstadosCivil extends BaseAdapter implements SpinnerAdapter {
        private final List<Catestadoscivil> data;

        public AdapterEstadosCivil(List<Catestadoscivil> data){
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


    private void GetCatMarcaTelefono(){
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatMarcasTelefonos";
        String METHOD_NAME = "GetCatMarcasTelefonos";
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
                    Catmarcastelefonos entidad = new Catmarcastelefonos();
                    entidad.setMarcaid(Integer.parseInt(item.getProperty("Marcaid").toString()));
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    lstCatMarcaTelefono.add(entidad);
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private class AdapterMarcaTelefono extends BaseAdapter implements SpinnerAdapter {
        private final List<Catmarcastelefonos> data;

        public AdapterMarcaTelefono(List<Catmarcastelefonos> data){
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












    //Para el Date
    @Override
    protected Dialog onCreateDialog(int id)
    {
        if (id == DILOG_ID)
            return new DatePickerDialog(this, dpickerListner, year_x, month_x, day_x);

        return  null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            year_x = year;
            month_x = monthOfYear+1;
            day_x = dayOfMonth;

            String sfecha = day_x+"/"+month_x+"/"+year_x;
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            ParsePosition pp = new ParsePosition(0);
            Date d = format.parse(sfecha, pp);
            txtFechaNacimiento.setText(format.format(d).toString());
        }
    };
}
