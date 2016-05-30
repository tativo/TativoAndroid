package com.tativo.app.tativo.Bloques.Actividades;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tativo.app.tativo.Bloques.Clases.CatBloqueoCliente;
import com.tativo.app.tativo.Bloques.Clases.Catcolonia;
import com.tativo.app.tativo.Bloques.Clases.Catdatospersonal;
import com.tativo.app.tativo.Bloques.Clases.Catestadoscivil;
import com.tativo.app.tativo.Bloques.Clases.Catidentidadcliente;
import com.tativo.app.tativo.Bloques.Clases.Catmarcastelefonos;
import com.tativo.app.tativo.Bloques.Clases.DatosCodigoPostal;
import com.tativo.app.tativo.Bloques.Clases.DatosSolicitud;
import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.Globals;
import com.tativo.app.tativo.Utilidades.ServiciosSoap;
import com.tativo.app.tativo.Utilidades.Utilerias;

import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Act_B2_Personal extends AppCompatActivity {

    Button btnInfPersonal, btnFechaNacimiento, btnCancelNuevacolonia, btnFocoInicialB2;
    AutoCompleteTextView txtFechaNacimiento, txtDependientes, txtCodigoPostal, txtCalle, txtNumeroExt, txtNumeroInt, txtTelefonoCelular, txt4DigitosTarjeta, txtNuevaColonia;
    MaterialSpinner spnGenero, spnEstadoCivil, spnColonia, spnMarcaCelular;
    Switch swtTarjetaCredito, swtCreditoHipotecario, swtCreditoAutomotriz;
    CheckBox ckTerminosCondiciones1, ckTerminosCondiciones2, ckTerminosCondiciones3, ckTerminosCondiciones4;
    TextView hnEstadoMunicipioTexto, txtAgregarColonia;
    ProgressDialog progressDialog;
    Globals Sesion;
    DatosCodigoPostal datosCP;
    Catdatospersonal catdatospersonal;
    Catidentidadcliente catidentidadcliente;
    //AdapterEstadosCivil EstadosCivilAdapter;
    //AdapterMarcaTelefono MarcaTelefonoAdapter;
    //AdapterColonias ColoniasAdapter;
    ArrayList<Catestadoscivil> lstCatestadoscivil = new ArrayList<Catestadoscivil>();
    ArrayList<Catmarcastelefonos> lstCatMarcaTelefono = new ArrayList<Catmarcastelefonos>();
    ArrayList<Catcolonia> lstCatColonia = new ArrayList<Catcolonia>();
    LinearLayout lyNuevaColonia;
    Boolean NuevaColonia,edicionCP=false;

    ArrayList<String> arrayCatEstadoCivil, arrayCatMarcaTelefono, arrayCatColonia;

    int year_x, month_x, day_x;
    static final int DILOG_ID = 0;

    AsyncEstatusSolicitud EstatusSolicitud = new AsyncEstatusSolicitud();
    boolean CancelaEstatusSolicitud = false ;


    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_b2_personal);
        Sesion = (Globals) getApplicationContext();


        LoadFormControls();
        FocusManager();
        EventManager();
        new AsyncLoadData().execute();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            EstatusSolicitud.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            EstatusSolicitud.execute();
        }


        btnFocoInicialB2 = (Button) findViewById(R.id.btnFocoInicialB2);
    }

    private void LoadFormControls() {
        progressDialog = new ProgressDialog(this);
        datosCP = new DatosCodigoPostal();

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
        txtNuevaColonia = (AutoCompleteTextView) findViewById(R.id.txtNuevaColonia);

        txtAgregarColonia = (TextView) findViewById(R.id.txtAgregarColonia);
        lyNuevaColonia = (LinearLayout) findViewById(R.id.lyNuevaColonia);
        btnCancelNuevacolonia = (Button) findViewById(R.id.btnCancelNuevacolonia);

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

        AplicaFormato(R.id.ckTerminosCondiciones1, R.string.ckTerminosCondiciones1);
        ckTerminosCondiciones1.setClickable(true);
        ckTerminosCondiciones1.setMovementMethod(LinkMovementMethod.getInstance());

        AplicaFormato(R.id.ckTerminosCondiciones2, R.string.ckTerminosCondiciones2);
        ckTerminosCondiciones2.setClickable(true);
        ckTerminosCondiciones2.setMovementMethod(LinkMovementMethod.getInstance());

        AplicaFormato(R.id.ckTerminosCondiciones3, R.string.ckTerminosCondiciones3);
        ckTerminosCondiciones3.setClickable(true);
        ckTerminosCondiciones3.setMovementMethod(LinkMovementMethod.getInstance());

        AplicaFormato(R.id.ckTerminosCondiciones4, R.string.ckTerminosCondiciones4);
        ckTerminosCondiciones4.setClickable(true);
        ckTerminosCondiciones4.setMovementMethod(LinkMovementMethod.getInstance());


        hnEstadoMunicipioTexto = (TextView) findViewById(R.id.hnEstadoMunicipioTexto);

        NuevaColonia = false;
        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        spnColonia.setEnabled(false);

        catdatospersonal = new Catdatospersonal();
        catdatospersonal.setDatopersonalid("");
        catdatospersonal.setUltimaAct(0);

        catidentidadcliente = new Catidentidadcliente();
        catidentidadcliente.setIdentidadclienteid("");
        catidentidadcliente.setUltimaAct(0);

        txt4DigitosTarjeta.setEnabled(false);
    }

    public void FocusManager() {
        FocusNextControl(R.id.txtDependientes, "T", R.id.spnGenero, "S");
        FocusNextControl(R.id.spnGenero, "S", R.id.spnEstadoCivil, "S");
        FocusNextControl(R.id.spnEstadoCivil, "S", R.id.txtCodigoPostal, "T");
        FocusNextControl(R.id.txtCodigoPostal, "T", R.id.spnColonia, "S");
        FocusNextControl(R.id.spnColonia, "S", R.id.txtCalle, "T");
        FocusNextControl(R.id.txtCalle, "T", R.id.txtNumeroExt, "T");
        FocusNextControl(R.id.txtNumeroExt, "T", R.id.txtNumeroInt, "T");
        FocusNextControl(R.id.txtNumeroInt, "T", R.id.txtTelefonoCelular, "T");
        FocusNextControl(R.id.txtTelefonoCelular, "T", R.id.spnMarcaCelular, "S");
    }

    private void EventManager() {
        spnGenero.setOnTouchListener(new spOcultaTeclado());
        spnEstadoCivil.setOnTouchListener(new spOcultaTeclado());
        spnColonia.setOnTouchListener(new spOcultaTeclado());
        spnMarcaCelular.setOnTouchListener(new spOcultaTeclado());

        txtTelefonoCelular.addTextChangedListener(new PhoneNumberFormattingTextWatcher());



        txtFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtFechaNacimiento.setError(null);
                showDialog(DILOG_ID);
                txtDependientes.requestFocus();
            }
        });

        /*
        btnFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtFechaNacimiento.setError(null);
                showDialog(DILOG_ID);
                txtDependientes.requestFocus();
            }
        });
*/

        swtTarjetaCredito.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txt4DigitosTarjeta.setEnabled(true);
                    txt4DigitosTarjeta.requestFocus();
                } else {
                    txt4DigitosTarjeta.setEnabled(false);
                    txt4DigitosTarjeta.setText("");
                    txtTelefonoCelular.requestFocus();
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
                edicionCP=true;
                return false;
            }
        });

        /*
        txtCodigoPostal.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //if (keyCode == KeyEvent.KEYCODE_ENTER)
                edicionCP=true;
                return false;
            }
        });
*/
        btnInfPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidaGuardar()) {
                    EstatusSolicitud.cancel(true);
                    new AsyncGuardar().execute();
                }
            }
        });

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

        ckTerminosCondiciones1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    ckTerminosCondiciones1.setError(null);
            }
        });

        ckTerminosCondiciones2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    ckTerminosCondiciones2.setError(null);
            }
        });

        ckTerminosCondiciones3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    ckTerminosCondiciones3.setError(null);
            }
        });

        ckTerminosCondiciones4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    ckTerminosCondiciones4.setError(null);
            }
        });
    }

    public void FocusNextControl(int o,String ot, int d,String dt) {
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
    private class AsyncTraerDatosCodigoPostal extends AsyncTask<Void, Void, Boolean> {
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
                //ColoniasAdapter = new AdapterColonias(lstCatColonia);
                ArrayAdapter<String> adapterColonia = adapterSpinner(arrayCatColonia);
                spnColonia.setAdapter(adapterColonia);
            }
            else
            {
                lstCatColonia = new ArrayList<Catcolonia>();
                //ColoniasAdapter = new AdapterColonias(lstCatColonia);
                ArrayAdapter<String> adapterColonia = adapterSpinner(arrayCatColonia);
                spnColonia.setAdapter(adapterColonia);
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
            //EstadosCivilAdapter = new AdapterEstadosCivil(lstCatestadoscivil);
            ArrayAdapter<String> adapterEstadoCivil = adapterSpinner(arrayCatEstadoCivil);
            spnEstadoCivil.setAdapter(adapterEstadoCivil);

            //MarcaTelefonoAdapter = new AdapterMarcaTelefono(lstCatMarcaTelefono);
            ArrayAdapter<String> adapterTelefono = adapterSpinner(arrayCatMarcaTelefono);
            spnMarcaCelular.setAdapter(adapterTelefono);

            /*
            spnEstadoCivil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Catestadoscivil cEC = new Catestadoscivil();
                    if (position >= 0) {
                        //cEC = (Catestadoscivil) spnEstadoCivil.getItemAtPosition(position);
                        cEC = (Catestadoscivil) lstCatestadoscivil.get(position);
                    }
                    Toast.makeText(parent.getContext(), String.valueOf(cEC.getEstadocivilid()) + "-" +  cEC.getDescripcion() ,Toast.LENGTH_LONG).show();
                    //Toast.makeText(parent.getContext(),((Catestadoscivil) parent.getItemAtPosition(position)).getEstadocivilid(),Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/

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
                arrayCatEstadoCivil = new ArrayList<String>();
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);
                    Catestadoscivil entidad = new Catestadoscivil();
                    entidad.setEstadocivilid(Integer.parseInt(item.getProperty("Estadocivilid").toString()));
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    arrayCatEstadoCivil.add(item.getProperty("Descripcion").toString());
                    lstCatestadoscivil.add(entidad);
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    /*
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
                        android.R.layout.simple_spinner_item, parent, false
                );
            }
            text.setTextColor(Color.BLACK);
            text.setText(data.get(position).getDescripcion());
            return text;
        }
    }*/

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
                arrayCatMarcaTelefono = new ArrayList<String>();
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);
                    Catmarcastelefonos entidad = new Catmarcastelefonos();
                    entidad.setMarcaid(Integer.parseInt(item.getProperty("Marcaid").toString()));
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    arrayCatMarcaTelefono.add(item.getProperty("Descripcion").toString());
                    lstCatMarcaTelefono.add(entidad);
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    /*
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
    }*/

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
                arrayCatColonia = new ArrayList<String>();
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);
                    Catcolonia entidad = new Catcolonia();
                    entidad.setCiudadid(item.getProperty("Ciudadid").toString());
                    entidad.setColoniaid(item.getProperty("Coloniaid").toString());
                    entidad.setColonia(item.getProperty("Colonia").toString());
                    arrayCatColonia.add(item.getProperty("Colonia").toString());
                    lstCatColonia.add(entidad);
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    /*
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
    }*/
    //LLENA SPINNER



    //GUARDAR
    private boolean ValidaGuardar() {
        ArrayList<Object> Objetos = new ArrayList<Object>();
        Objetos.add(txtFechaNacimiento);
        Objetos.add(txtDependientes);
        Objetos.add(spnGenero);
        Objetos.add(spnEstadoCivil);
        Objetos.add(txtCodigoPostal);
        if (!NuevaColonia)
            Objetos.add(spnColonia);
        if (NuevaColonia)
            Objetos.add(txtNuevaColonia);
        Objetos.add(txtCalle);
        Objetos.add(txtNumeroExt);
        Objetos.add(txtTelefonoCelular);
        if (swtTarjetaCredito.isChecked())
            Objetos.add(txt4DigitosTarjeta);

        Objetos.add(ckTerminosCondiciones1);
        Objetos.add(ckTerminosCondiciones2);
        Objetos.add(ckTerminosCondiciones3);
        Objetos.add(ckTerminosCondiciones4);

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
            Intent i = new Intent(getApplicationContext(), Act_B3_InfDeposito.class);
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
    private void GuardarDatos() {
        String SOAP_ACTION = "http://tempuri.org/IService1/SetCatDatosIdentidadTelefono";
        String METHOD_NAME = "SetCatDatosIdentidadTelefono";
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
        JSONObject DatosPersonales = new JSONObject();
        JSONObject DatosIdentidad = new JSONObject();
        try {
            DatosPersonales.put("Datopersonalid", catdatospersonal.getDatopersonalid().toString());
            DatosPersonales.put("Clienteid", Sesion.getCliendeID());
            DatosPersonales.put("Genero", spnGenero.getSelectedItem().toString().substring(0, 1));
            DatosPersonales.put("Fechanacimiento", Utilerias.getDate(txtFechaNacimiento.getText().toString()));
            //DatosPersonales.put("Estadocivilid", ((Catestadoscivil) spnEstadoCivil.getSelectedItem()).getEstadocivilid());
            DatosPersonales.put("Estadocivilid", lstCatestadoscivil.get(spnEstadoCivil.getSelectedItemPosition() - 1).getEstadocivilid());
            DatosPersonales.put("Dependientes", txtDependientes.getText().toString());
            DatosPersonales.put("Telefono", txtTelefonoCelular.getText().toString());
            //DatosPersonales.put("Marcaid", ((Catmarcastelefonos) spnMarcaCelular.getSelectedItem()).getMarcaid());
            DatosPersonales.put("Marcaid", lstCatMarcaTelefono.get(spnMarcaCelular.getSelectedItemPosition() - 1).getMarcaid());
            DatosPersonales.put("Calle", txtCalle.getText().toString());
            DatosPersonales.put("Numeroext", txtNumeroExt.getText().toString());
            DatosPersonales.put("Numeroint", txtNumeroInt.getText().toString());
            if (NuevaColonia)
                DatosPersonales.put("Colonia", txtNuevaColonia.getText().toString());
            else
                //DatosPersonales.put("Colonia", ((Catcolonia) spnColonia.getSelectedItem()).getColonia().toString());
                DatosPersonales.put("Colonia", lstCatColonia.get(spnColonia.getSelectedItemPosition() - 1).getColonia());
            DatosPersonales.put("Codigopostal", txtCodigoPostal.getText().toString());
            DatosPersonales.put("Paisid", datosCP.getPaisID().toString());
            DatosPersonales.put("Estadoid", datosCP.getEstadoID().toString());
            DatosPersonales.put("Municipioid", datosCP.getMunicipioID().toString());
            DatosPersonales.put("Ciudadid", datosCP.getCiudadID().toString());
            DatosPersonales.put("UltimaAct", catdatospersonal.getUltimaAct());


            DatosIdentidad.put("Identidadclienteid", catidentidadcliente.getIdentidadclienteid());
            DatosIdentidad.put("Clienteid", Sesion.getCliendeID());
            DatosIdentidad.put("Tarjetacredito", swtTarjetaCredito.isChecked());
            DatosIdentidad.put("Ultimoscuatrodigitos", txt4DigitosTarjeta.getText());
            DatosIdentidad.put("Creditohipotecario", swtCreditoHipotecario.isChecked());
            DatosIdentidad.put("Creditoautomotriz", swtCreditoAutomotriz.isChecked());
            DatosIdentidad.put("UltimaAct", catidentidadcliente.getUltimaAct());
            //Campos de los controles ocultos debemos considerar que tienen valor para el momento en que
            //la app lo posicione sobre el bloque actual carge los datos que guardo con anterioridad

            DatosEntidad.put("DatosPersonales", DatosPersonales);
            DatosEntidad.put("DatosIdentidad", DatosIdentidad);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }
        return DatosEntidad.toString();
    }
    //GUARDAR

    //Region Existe Info del BLOQUE
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
            if (catdatospersonal.getUltimaAct() != 0) {
                SetInfoBloque();
            }
            btnFocoInicialB2.requestFocus();
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
    private void GetInfoBloque() {
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatDatosIdentidad";
        String METHOD_NAME = "GetCatDatosIdentidad";
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
                    SoapObject Datos = (SoapObject) respuesta.getProperty("Datos");
                    SoapObject DatosPersonales = (SoapObject) Datos.getProperty("DatosPersonales");
                    SoapObject DatosIdentidad = (SoapObject)  Datos.getProperty("DatosIdentidad");
                    SoapObject DatosCodigoPostal = (SoapObject) Datos.getProperty("CP");
                    SoapObject DatosColonias = (SoapObject) Datos.getProperty("Colonias");
                    indexColonias = Datos.getProperty("IndexColonia").toString().toUpperCase().trim();

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

                    if(Integer.parseInt(DatosIdentidad.getProperty("UltimaAct").toString()) != 0) {
                        catidentidadcliente.setIdentidadclienteid(DatosIdentidad.getProperty("Identidadclienteid").toString());
                        catidentidadcliente.setClienteid(DatosIdentidad.getProperty("Clienteid").toString());
                        catidentidadcliente.setTarjetacredito(Boolean.parseBoolean(DatosIdentidad.getProperty("Tarjetacredito").toString()));
                        catidentidadcliente.setUltimoscuatrodigitos(DatosIdentidad.getProperty("Ultimoscuatrodigitos").toString());
                        catidentidadcliente.setCreditohipotecario(Boolean.parseBoolean(DatosIdentidad.getProperty("Creditohipotecario").toString()));
                        catidentidadcliente.setCreditoautomotriz(Boolean.parseBoolean(DatosIdentidad.getProperty("Creditoautomotriz").toString()));
                        catidentidadcliente.setUltimaAct(Integer.parseInt(DatosIdentidad.getProperty("UltimaAct").toString()));
                    }

                    lstCatColonia = new ArrayList<Catcolonia>();
                    for (int i = 0; i < DatosColonias.getPropertyCount(); i++) {
                        SoapObject item = (SoapObject) DatosColonias.getProperty(i);
                        Catcolonia entidad = new Catcolonia();
                        entidad.setCiudadid(item.getProperty("Ciudadid").toString());
                        entidad.setColoniaid(item.getProperty("Coloniaid").toString());
                        entidad.setColonia(item.getProperty("Colonia").toString());
                        lstCatColonia.add(entidad);
                    }

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



                } else {
                    //Toast.makeText(getApplicationContext(),"Error: "+respuesta.getProperty("Mensaje").toString(),Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    private void SetInfoBloque() {
        txtFechaNacimiento.setText(new SimpleDateFormat("dd/MM/yyyy").format(catdatospersonal.getFechanacimiento()));
        txtDependientes.setText(String.valueOf(catdatospersonal.getDependientes()));
        spnGenero.setSelection((catdatospersonal.getGenero().toString() == "M" ? 1 : 2));
        spnEstadoCivil.setSelection(catdatospersonal.getEstadocivilid());
        txtCodigoPostal.setText(catdatospersonal.getCodigopostal());
        //Preguntamos si el usuario ya ingreso los datos del codigo postal para mostrarlos en pantalla
        if(catdatospersonal.getCodigopostal().trim().length()>0){
            hnEstadoMunicipioTexto.setText(datosCP.getMunicipio() + "/" + datosCP.getEstado());
            spnColonia.setEnabled(true);
            //ColoniasAdapter = new AdapterColonias(lstCatColonia);
            ArrayAdapter<String> adapterColonia = adapterSpinner(arrayCatColonia);
            spnColonia.setAdapter(adapterColonia);
        }
        //Preguntamos si el usuario ya ingreso o selecciono una colonia
        if(catdatospersonal.getColonia().trim().length()>0){
            if(indexColonias.trim().toUpperCase().equals("0")){
                lyNuevaColonia.setVisibility(View.VISIBLE);
                spnColonia.setSelection(0);
                spnColonia.setEnabled(false);
                NuevaColonia = true;
                txtNuevaColonia.setText(catdatospersonal.getColonia().trim());
            }else{
                spnColonia.setSelection(getIndexColonia(indexColonias));
            }
        }
        txtCalle.setText(catdatospersonal.getCalle());
        txtNumeroExt.setText(catdatospersonal.getNumeroext());
        txtNumeroInt.setText(catdatospersonal.getNumeroint());
        txtTelefonoCelular.setText(catdatospersonal.getTelefono());
        spnMarcaCelular.setSelection(catdatospersonal.getMarcaid());

        //Llenamos los datos de identidad
        swtTarjetaCredito.setChecked(catidentidadcliente.getTarjetacredito());
        if(catidentidadcliente.getTarjetacredito()){
            txt4DigitosTarjeta.setText(catidentidadcliente.getUltimoscuatrodigitos());
            txt4DigitosTarjeta.setEnabled(catidentidadcliente.getTarjetacredito());
        }
        swtCreditoHipotecario.setChecked(catidentidadcliente.getCreditohipotecario());
        swtCreditoAutomotriz.setChecked(catidentidadcliente.getCreditoautomotriz());
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
    //Endregion


    //Region Estatus Solicitud y Bloqueos
    CatBloqueoCliente Bloqueos = new CatBloqueoCliente();
    DatosSolicitud Solicitud = new DatosSolicitud();
    private class AsyncEstatusSolicitud extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            while (true) {
                try {
                    GetEstatusSolicitud();
                    if (Bloqueos.getBloqueoid() != 0)
                        break;
                    int Segundos = 0;
                    while (Segundos <= 30) {
                        if (isCancelled()) {
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
                    new AlertDialog.Builder(Act_B2_Personal.this)
                            .setTitle(R.string.msgRefTitulo)
                            .setMessage(R.string.msgRefNoContesto)
                            .setCancelable(false)
                            .setPositiveButton(R.string.msgRefOk, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Sesion.setBloqueoReferencia(true);
                                    Sesion.setBloqueoCliente(Bloqueos);
                                    Sesion.setSolicitud(Solicitud);
                                    Sesion.setBloqueActual(2);
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



    //Para el Date
    @Override
    protected Dialog onCreateDialog(int id) {
        /*
        if (id == DILOG_ID)
            return new DatePickerDialog(this, dpickerListner, year_x, month_x, day_x);
            */
        if (id == DILOG_ID) {
            try {
                String fecha;
                Date MaxDate = null;
                Date MinDate = null;
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                format.setTimeZone(TimeZone.getTimeZone("UTC"));

                fecha =  "31/12/" + (year_x - 18);
                MaxDate = format.parse(fecha);

                fecha =  "01/01/" + (year_x - 55);
                MinDate = format.parse(fecha);

                DatePickerDialog dp = new DatePickerDialog(this, dpickerListner, (year_x - 18), month_x, day_x);
                dp.getDatePicker().setMaxDate(MaxDate.getTime());
                dp.getDatePicker().setMinDate(MinDate.getTime());
                return dp;
            }
            catch (Exception ex)
            {
                return  null;
            }
        }

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
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            ParsePosition pp = new ParsePosition(0);
            Date d = format.parse(sfecha, pp);
            txtFechaNacimiento.setText(format.format(d).toString());
        }
    };

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

    private void AplicaFormato(int idT, int idR) {
        TextView t =(TextView) findViewById(idT);
        String s = getResources().getString(idR);
        CharSequence cs = Html.fromHtml(s);
        t.setText(cs);
    }

    public ArrayAdapter<String> adapterSpinner(ArrayList<String> arrayList ) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Act_B2_Personal.this, android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return  adapter;
    }
}
