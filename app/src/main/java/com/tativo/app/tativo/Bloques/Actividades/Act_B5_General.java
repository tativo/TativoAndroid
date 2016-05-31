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
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tativo.app.tativo.Bloques.Clases.CatBloqueoCliente;
import com.tativo.app.tativo.Bloques.Clases.Catanio;
import com.tativo.app.tativo.Bloques.Clases.Catactividadentretenimiento;
import com.tativo.app.tativo.Bloques.Clases.Catcurso;
import com.tativo.app.tativo.Bloques.Clases.Catdatosgeneral;
import com.tativo.app.tativo.Bloques.Clases.Catdondeinternet;
import com.tativo.app.tativo.Bloques.Clases.Catestado;
import com.tativo.app.tativo.Bloques.Clases.Catingresoextra;
import com.tativo.app.tativo.Bloques.Clases.Catnivelestudio;
import com.tativo.app.tativo.Bloques.Clases.Catnivelingles;
import com.tativo.app.tativo.Bloques.Clases.Catredessociales;
import com.tativo.app.tativo.Bloques.Clases.Cattipovivienda;
import com.tativo.app.tativo.Bloques.Clases.Catvivecon;
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

public class Act_B5_General extends AppCompatActivity {

    MaterialSpinner spnEdoNacimiento, spnTipoVivienda, spnConQuienVives, spnViviendoAhi, spnNivelEstudio, spnCurso, spnIngles, spnActividad, spnIngresoExtra, spnRedesSociales1, spnRedesSociales2, spnRedesSociales3, spnDondeInternet1, spnDondeInternet2, spnDondeInternet3;
    AutoCompleteTextView txtTelefonoFijo, txtNombreEscuela, txtEspecificaActividad, txtActivdadIngresoExtra, txtParaQue;
    Switch swtTienesAutomovil, swtEsPropio;
    CheckBox ckTieneCelular, ckTieneTablet, ckTieneComputadora;
    Button btnInfGeneral, btnFocoInicialB5;
    LinearLayout lyEspecificaActividad, lyIngresoExtra, lyEsPropio;

    Globals Sesion;
    ProgressDialog progressDialog;

    ArrayAdapter<String> estadosAdapter;
    ArrayList<String> s_lstEstados= new ArrayList<String>();
    ArrayList<Catestado> lstEstados = new ArrayList<Catestado>();

    ArrayAdapter<String> tipoViviendaAdapter;
    ArrayList<String> s_lstTipoVivienda= new ArrayList<String>();
    ArrayList<Cattipovivienda> lstTipoVivienda = new ArrayList<Cattipovivienda>();

    ArrayAdapter<String> viveConAdapter;
    ArrayList<String> s_lstViveCon= new ArrayList<String>();
    ArrayList<Catvivecon> lstViveCon = new ArrayList<Catvivecon>();

    ArrayAdapter<String> spnCatanioAdapter;
    ArrayList<String> s_listaCatanio= new ArrayList<String>();
    ArrayList<Catanio> listaCatanio = new ArrayList<Catanio>();

    ArrayAdapter<String> nivelEstudioAdapter;
    ArrayList<String> s_lstNivelEstudio= new ArrayList<String>();
    ArrayList<Catnivelestudio> lstNivelEstudio = new ArrayList<Catnivelestudio>();

    ArrayAdapter<String> cursoAdapter;
    ArrayList<String> s_lstCurso= new ArrayList<String>();
    ArrayList<Catcurso> lstCurso = new ArrayList<Catcurso>();

    ArrayAdapter<String> nivelInglesAdapter;
    ArrayList<String> s_lstNivelIngles= new ArrayList<String>();
    ArrayList<Catnivelingles> lstNivelIngles = new ArrayList<Catnivelingles>();

    ArrayAdapter<String> actividadEntretenimientoAdapter;
    ArrayList<String> s_lstactividadentretenimiento= new ArrayList<String>();
    ArrayList<Catactividadentretenimiento> lstactividadentretenimiento = new ArrayList<Catactividadentretenimiento>();

    ArrayAdapter<String> ingresoExtraAdapter;
    ArrayList<String> s_lstIngresoExtra= new ArrayList<String>();
    ArrayList<Catingresoextra> lstIngresoExtra = new ArrayList<Catingresoextra>();

    ArrayAdapter<String> redesSocialesAdapter;
    ArrayList<String> s_lstRedesSociales= new ArrayList<String>();
    ArrayList<Catredessociales> lstRedesSociales = new ArrayList<Catredessociales>();

    ArrayAdapter<String> dondeInternetAdapter;
    ArrayList<String> s_lstDondeInternet= new ArrayList<String>();
    ArrayList<Catdondeinternet> lstDondeInternet = new ArrayList<Catdondeinternet>();

    Catdatosgeneral catdatosgeneral;

    AsyncEstatusSolicitud EstatusSolicitud = new AsyncEstatusSolicitud();
    boolean CancelaEstatusSolicitud = false ;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_b5_general);
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

    private void LoadFormControls() {
        progressDialog = new ProgressDialog(this);

        catdatosgeneral = new Catdatosgeneral();
        catdatosgeneral.setDatosgeneralesid("");
        catdatosgeneral.setUltimaAct(0);

        spnEdoNacimiento = (MaterialSpinner) findViewById(R.id.spnEdoNacimiento);
        spnTipoVivienda = (MaterialSpinner) findViewById(R.id.spnTipoVivienda);
        spnConQuienVives = (MaterialSpinner) findViewById(R.id.spnConQuienVives);
        spnViviendoAhi = (MaterialSpinner) findViewById(R.id.spnViviendoAhi);
        spnNivelEstudio = (MaterialSpinner) findViewById(R.id.spnNivelEstudio);
        spnCurso = (MaterialSpinner) findViewById(R.id.spnCurso);
        spnIngles = (MaterialSpinner) findViewById(R.id.spnIngles);
        spnActividad = (MaterialSpinner) findViewById(R.id.spnActividad);
        spnIngresoExtra = (MaterialSpinner) findViewById(R.id.spnIngresoExtra);
        spnRedesSociales1 = (MaterialSpinner) findViewById(R.id.spnRedesSociales1);
        spnRedesSociales2 = (MaterialSpinner) findViewById(R.id.spnRedesSociales2);
        spnRedesSociales3 = (MaterialSpinner) findViewById(R.id.spnRedesSociales3);
        spnDondeInternet1 = (MaterialSpinner) findViewById(R.id.spnDondeInternet1);
        spnDondeInternet2 = (MaterialSpinner) findViewById(R.id.spnDondeInternet2);
        spnDondeInternet3 = (MaterialSpinner) findViewById(R.id.spnDondeInternet3);

        txtTelefonoFijo = (AutoCompleteTextView) findViewById(R.id.txtTelefonoFijo);
        txtNombreEscuela = (AutoCompleteTextView) findViewById(R.id.txtNombreEscuela);
        txtEspecificaActividad = (AutoCompleteTextView) findViewById(R.id.txtEspecificaActividad);
        txtActivdadIngresoExtra = (AutoCompleteTextView) findViewById(R.id.txtActivdadIngresoExtra);
        txtParaQue = (AutoCompleteTextView) findViewById(R.id.txtParaQue);

        swtTienesAutomovil = (Switch) findViewById(R.id.swtTienesAutomovil);
        swtEsPropio = (Switch) findViewById(R.id.swtEsPropio);
        swtEsPropio.setEnabled(false);

        ckTieneCelular = (CheckBox) findViewById(R.id.ckTieneCelular);
        ckTieneTablet = (CheckBox) findViewById(R.id.ckTieneTablet);
        ckTieneComputadora = (CheckBox) findViewById(R.id.ckTieneComputadora);

        lyEspecificaActividad = (LinearLayout) findViewById(R.id.lyEspecificaActividad);
        lyIngresoExtra = (LinearLayout) findViewById(R.id.lyIngresoExtra);
        lyEsPropio = (LinearLayout) findViewById(R.id.lyEsPropio);

        btnFocoInicialB5 = (Button) findViewById(R.id.btnFocoInicialB5);
        btnInfGeneral = (Button) findViewById(R.id.btnInfGeneral);
    }

    public void FocusManager() {
        FocusNextControl(R.id.spnEdoNacimiento, "S", R.id.spnTipoVivienda, "S");
        FocusNextControl(R.id.spnTipoVivienda, "S", R.id.spnConQuienVives, "S");
        FocusNextControl(R.id.spnConQuienVives, "S", R.id.spnViviendoAhi, "S");
        FocusNextControl(R.id.spnViviendoAhi, "S", R.id.txtTelefonoFijo, "T");
        FocusNextControl(R.id.txtTelefonoFijo, "T", R.id.spnNivelEstudio, "S");
        FocusNextControl(R.id.spnNivelEstudio, "S", R.id.txtNombreEscuela, "T");
        FocusNextControl(R.id.txtNombreEscuela, "T", R.id.spnCurso, "S");
        FocusNextControl(R.id.spnCurso, "S", R.id.spnIngles, "S");
        FocusNextControl(R.id.spnIngles, "S", R.id.spnActividad, "S");

        //FocusNextControl(R.id.spnActividad, "S", R.id.txtEspecificaActividad, "T");
        FocusNextControl(R.id.txtEspecificaActividad, "T", R.id.spnIngresoExtra, "S");
        //FocusNextControl(R.id.spnIngresoExtra, "S", R.id.txtActivdadIngresoExtra, "T");
        FocusNextControl(R.id.txtActivdadIngresoExtra, "T", R.id.txtParaQue, "T");

        FocusNextControl(R.id.txtParaQue, "T", R.id.spnRedesSociales1, "S");
        FocusNextControl(R.id.spnRedesSociales1, "S", R.id.spnRedesSociales2, "S");
        FocusNextControl(R.id.spnRedesSociales2, "S", R.id.spnRedesSociales3, "S");
        //FocusNextControl(R.id.spnRedesSociales3, "S", R.id.spnDondeInternet1, "S");
        FocusNextControl(R.id.spnDondeInternet1, "S", R.id.spnDondeInternet2, "S");
        FocusNextControl(R.id.spnDondeInternet2, "S", R.id.spnDondeInternet3, "S");
    }

    public void FocusNextControl(int o, String ot, int d, String dt) {
        final EditText destino = (dt.toUpperCase() == "T" ? (EditText) findViewById(d) : null);
        final Spinner destinoS = (dt.toUpperCase() == "S" ? (Spinner) findViewById(d) : null);

        if (ot.toUpperCase() == "T") {
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
        if (ot.toUpperCase() == "S") {
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

    private void EventManager() {
        spnEdoNacimiento.setOnTouchListener(new spOcultaTeclado());
        spnTipoVivienda.setOnTouchListener(new spOcultaTeclado());
        spnConQuienVives.setOnTouchListener(new spOcultaTeclado());
        spnViviendoAhi.setOnTouchListener(new spOcultaTeclado());
        spnNivelEstudio.setOnTouchListener(new spOcultaTeclado());
        spnCurso.setOnTouchListener(new spOcultaTeclado());
        spnIngles.setOnTouchListener(new spOcultaTeclado());
        spnActividad.setOnTouchListener(new spOcultaTeclado());
        spnIngresoExtra.setOnTouchListener(new spOcultaTeclado());
        spnRedesSociales1.setOnTouchListener(new spOcultaTeclado());
        spnRedesSociales2.setOnTouchListener(new spOcultaTeclado());
        spnRedesSociales3.setOnTouchListener(new spOcultaTeclado());
        spnDondeInternet1.setOnTouchListener(new spOcultaTeclado());
        spnDondeInternet2.setOnTouchListener(new spOcultaTeclado());
        spnDondeInternet3.setOnTouchListener(new spOcultaTeclado());

        txtTelefonoFijo.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        swtTienesAutomovil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    swtEsPropio.setEnabled(true);
                    lyEsPropio.setBackgroundResource(R.drawable.border);
                }

                else {
                    swtEsPropio.setChecked(false);
                    swtEsPropio.setEnabled(false);
                    lyEsPropio.setBackgroundResource(R.drawable.border_disable);
                }
            }
        });

        spnActividad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            protected Adapter initializedAdapter = null;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (initializedAdapter != parent.getAdapter()) {
                    initializedAdapter = parent.getAdapter();
                    return;
                }
                //Catactividadentretenimiento cat = (Catactividadentretenimiento) parent.getItemAtPosition(position);
                Catactividadentretenimiento cat = lstactividadentretenimiento.get(position);
                if (cat.getDescripcion().toString().toLowerCase().equals("otro")) {
                    lyEspecificaActividad.setVisibility(View.VISIBLE);
                    txtEspecificaActividad.requestFocus();
                } else {
                    lyEspecificaActividad.setVisibility(View.GONE);
                    spnIngresoExtra.requestFocus();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnIngresoExtra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            protected Adapter initializedAdapter = null;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (initializedAdapter != parent.getAdapter()) {
                    initializedAdapter = parent.getAdapter();
                    return;
                }
                Catingresoextra cat = lstIngresoExtra.get(position);
                if (cat.getDescripcion().toString().equals("$0")) {
                    lyIngresoExtra.setVisibility(View.GONE);
                    txtParaQue.requestFocus();
                } else {
                    lyIngresoExtra.setVisibility(View.VISIBLE);
                    txtActivdadIngresoExtra.requestFocus();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnInfGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidaGuardar())
                {
                    EstatusSolicitud.cancel(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new AsyncGuardar().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }else{
                        new AsyncGuardar().execute();
                    }
                }
            }
        });
    }

    //LLENA SPINNER
    private class AsyncLoadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetCatEstados();
            GetCatTipoVivienda();
            GetCatViveCon();
            GetCatanio();
            GetCatNivelEstudio();
            GetCatCurso();
            GetCatNivelIngles();
            GetCatactividadEntretenimiento();
            GetCatIngresoExtra();
            GetCatRedesSolciales();
            GetCatDondeInternet();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            estadosAdapter = adapterSpinner(s_lstEstados);
            spnEdoNacimiento.setAdapter(estadosAdapter);

            tipoViviendaAdapter = adapterSpinner(s_lstTipoVivienda);
            spnTipoVivienda.setAdapter(tipoViviendaAdapter);

            viveConAdapter = adapterSpinner(s_lstViveCon);
            spnConQuienVives.setAdapter(viveConAdapter);

            spnCatanioAdapter = adapterSpinner(s_listaCatanio);
            spnViviendoAhi.setAdapter(spnCatanioAdapter);

            nivelEstudioAdapter = adapterSpinner(s_lstNivelEstudio);
            spnNivelEstudio.setAdapter(nivelEstudioAdapter);

            cursoAdapter = adapterSpinner(s_lstCurso);
            spnCurso.setAdapter(cursoAdapter);

            nivelInglesAdapter = adapterSpinner(s_lstNivelIngles);
            spnIngles.setAdapter(nivelInglesAdapter);

            actividadEntretenimientoAdapter = adapterSpinner(s_lstactividadentretenimiento);
            spnActividad.setAdapter(actividadEntretenimientoAdapter);

            ingresoExtraAdapter = adapterSpinner(s_lstIngresoExtra);
            spnIngresoExtra.setAdapter(ingresoExtraAdapter);

            redesSocialesAdapter = adapterSpinner(s_lstRedesSociales);
            spnRedesSociales1.setAdapter(redesSocialesAdapter);
            spnRedesSociales2.setAdapter(redesSocialesAdapter);
            spnRedesSociales3.setAdapter(redesSocialesAdapter);

            dondeInternetAdapter = adapterSpinner(s_lstDondeInternet);
            spnDondeInternet1.setAdapter(dondeInternetAdapter);
            spnDondeInternet2.setAdapter(dondeInternetAdapter);
            spnDondeInternet3.setAdapter(dondeInternetAdapter);

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

    private void GetCatEstados() {
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatEstados";
        String METHOD_NAME = "GetCatEstados";
        String NAMESPACE = "http://tempuri.org/";
        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE, null);
        if (respuesta != null) {
            try {
                String[] listaRespuesta;
                listaRespuesta = new String[respuesta.getPropertyCount()];
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);
                    Catestado entidad = new Catestado();
                    entidad.setEstadoid(item.getProperty("Estadoid").toString());
                    entidad.setEstado(item.getProperty("Estado").toString());
                    lstEstados.add(entidad);
                    s_lstEstados.add(item.getProperty("Estado").toString());
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private class AdapterEstados extends BaseAdapter implements SpinnerAdapter {
        private final List<Catestado> data;

        public AdapterEstados(List<Catestado> data) {
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
            if (recycle != null) {
                text = (TextView) recycle;
            } else {
                text = (TextView) getLayoutInflater().inflate(
                        android.R.layout.simple_dropdown_item_1line, parent, false
                );
            }
            text.setTextColor(Color.BLACK);
            text.setText(data.get(position).getEstado());
            return text;
        }
    }

    private void GetCatTipoVivienda() {
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatTipoVivienda";
        String METHOD_NAME = "GetCatTipoVivienda";
        String NAMESPACE = "http://tempuri.org/";
        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE, null);
        if (respuesta != null) {
            try {
                String[] listaRespuesta;
                listaRespuesta = new String[respuesta.getPropertyCount()];
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);
                    Cattipovivienda entidad = new Cattipovivienda();
                    entidad.setTipoviviendaid(Integer.parseInt(item.getProperty("Tipoviviendaid").toString()));
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    lstTipoVivienda.add(entidad);
                    s_lstTipoVivienda.add(item.getProperty("Descripcion").toString());
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private class AdapterTipoVivienda extends BaseAdapter implements SpinnerAdapter {
        private final List<Cattipovivienda> data;

        public AdapterTipoVivienda(List<Cattipovivienda> data) {
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
            if (recycle != null) {
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

    private void GetCatViveCon() {
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatvivecon";
        String METHOD_NAME = "GetCatvivecon";
        String NAMESPACE = "http://tempuri.org/";
        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE, null);
        if (respuesta != null) {
            try {
                String[] listaRespuesta;
                listaRespuesta = new String[respuesta.getPropertyCount()];
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);
                    Catvivecon entidad = new Catvivecon();
                    entidad.setViveconid(item.getProperty("Viveconid").toString());
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    lstViveCon.add(entidad);
                    s_lstViveCon.add(item.getProperty("Descripcion").toString());
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private class AdapterViveCon extends BaseAdapter implements SpinnerAdapter {
        private final List<Catvivecon> data;

        public AdapterViveCon(List<Catvivecon> data) {
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
            if (recycle != null) {
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

    private void GetCatanio() {
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatanio";
        String METHOD_NAME = "GetCatanio";
        String NAMESPACE = "http://tempuri.org/";
        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE, null);
        if (respuesta != null) {
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
                    s_listaCatanio.add(item.getProperty("Descripcion").toString());
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private class AdapterAnio extends BaseAdapter implements SpinnerAdapter {
        private final List<Catanio> data;

        public AdapterAnio(List<Catanio> data) {
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
            if (recycle != null) {
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

    private void GetCatNivelEstudio() {
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatNivelEstudio";
        String METHOD_NAME = "GetCatNivelEstudio";
        String NAMESPACE = "http://tempuri.org/";
        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE, null);
        if (respuesta != null) {
            try {
                String[] listaRespuesta;
                listaRespuesta = new String[respuesta.getPropertyCount()];
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);
                    Catnivelestudio entidad = new Catnivelestudio();
                    entidad.setNivelestudioid(Integer.parseInt(item.getProperty("Nivelestudiosid").toString()));
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    lstNivelEstudio.add(entidad);
                    s_lstNivelEstudio.add(item.getProperty("Descripcion").toString());
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private class AdapterNivelEstudio extends BaseAdapter implements SpinnerAdapter {
        private final List<Catnivelestudio> data;

        public AdapterNivelEstudio(List<Catnivelestudio> data) {
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
            if (recycle != null) {
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

    private void GetCatCurso() {
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatCursos";
        String METHOD_NAME = "GetCatCursos";
        String NAMESPACE = "http://tempuri.org/";
        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE, null);
        if (respuesta != null) {
            try {
                String[] listaRespuesta;
                listaRespuesta = new String[respuesta.getPropertyCount()];
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);
                    Catcurso entidad = new Catcurso();
                    entidad.setCursoid(Integer.parseInt(item.getProperty("Cursoid").toString()));
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    lstCurso.add(entidad);
                    s_lstCurso.add(item.getProperty("Descripcion").toString());
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private class AdapterCurso extends BaseAdapter implements SpinnerAdapter {
        private final List<Catcurso> data;

        public AdapterCurso(List<Catcurso> data) {
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
            if (recycle != null) {
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

    private void GetCatNivelIngles() {
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatNivelIngles";
        String METHOD_NAME = "GetCatNivelIngles";
        String NAMESPACE = "http://tempuri.org/";
        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE, null);
        if (respuesta != null) {
            try {
                String[] listaRespuesta;
                listaRespuesta = new String[respuesta.getPropertyCount()];
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);
                    Catnivelingles entidad = new Catnivelingles();
                    entidad.setNivelinglesid(item.getProperty("Nivelinglesid").toString());
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    lstNivelIngles.add(entidad);
                    s_lstNivelIngles.add(item.getProperty("Descripcion").toString());
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private class AdapterNivelIngles extends BaseAdapter implements SpinnerAdapter {
        private final List<Catnivelingles> data;

        public AdapterNivelIngles(List<Catnivelingles> data) {
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
            if (recycle != null) {
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

    private void GetCatactividadEntretenimiento() {
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatActividadEntretenimiento";
        String METHOD_NAME = "GetCatActividadEntretenimiento";
        String NAMESPACE = "http://tempuri.org/";
        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE, null);
        if (respuesta != null) {
            try {
                String[] listaRespuesta;
                listaRespuesta = new String[respuesta.getPropertyCount()];
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);
                    Catactividadentretenimiento entidad = new Catactividadentretenimiento();
                    entidad.setActividadentretenimientoid(Integer.parseInt(item.getProperty("Actividadentretenimientoid").toString()));
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    lstactividadentretenimiento.add(entidad);
                    s_lstactividadentretenimiento.add(item.getProperty("Descripcion").toString());
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private class AdapterActividadEntretenimiento extends BaseAdapter implements SpinnerAdapter {
        private final List<Catactividadentretenimiento> data;

        public AdapterActividadEntretenimiento(List<Catactividadentretenimiento> data) {
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
            if (recycle != null) {
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

    private void GetCatIngresoExtra() {
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatIngresoExtra";
        String METHOD_NAME = "GetCatIngresoExtra";
        String NAMESPACE = "http://tempuri.org/";
        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE, null);
        if (respuesta != null) {
            try {
                String[] listaRespuesta;
                listaRespuesta = new String[respuesta.getPropertyCount()];
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);
                    Catingresoextra entidad = new Catingresoextra();
                    entidad.setIngresoextraid(Integer.parseInt(item.getProperty("Ingresoextraid").toString()));
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    lstIngresoExtra.add(entidad);
                    s_lstIngresoExtra.add(item.getProperty("Descripcion").toString());
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private class AdapterIngresoExtra extends BaseAdapter implements SpinnerAdapter {
        private final List<Catingresoextra> data;

        public AdapterIngresoExtra(List<Catingresoextra> data) {
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
            if (recycle != null) {
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

    private void GetCatRedesSolciales() {
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatRedesSociales";
        String METHOD_NAME = "GetCatRedesSociales";
        String NAMESPACE = "http://tempuri.org/";
        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE, null);
        if (respuesta != null) {
            try {
                String[] listaRespuesta;
                listaRespuesta = new String[respuesta.getPropertyCount()];
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);
                    Catredessociales entidad = new Catredessociales();
                    entidad.setRedessocialesid(Integer.parseInt(item.getProperty("Redessocialesid").toString()));
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    lstRedesSociales.add(entidad);
                    s_lstRedesSociales.add(item.getProperty("Descripcion").toString());
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private class AdapterRedesSociales extends BaseAdapter implements SpinnerAdapter {
        private final List<Catredessociales> data;

        public AdapterRedesSociales(List<Catredessociales> data) {
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
            if (recycle != null) {
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

    private void GetCatDondeInternet() {
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatDondeInternet";
        String METHOD_NAME = "GetCatDondeInternet";
        String NAMESPACE = "http://tempuri.org/";
        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE, null);
        if (respuesta != null) {
            try {
                String[] listaRespuesta;
                listaRespuesta = new String[respuesta.getPropertyCount()];
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);
                    Catdondeinternet entidad = new Catdondeinternet();
                    entidad.setDondeinternetid(Integer.parseInt(item.getProperty("Dondeinternetid").toString()));
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    lstDondeInternet.add(entidad);
                    s_lstDondeInternet.add(item.getProperty("Descripcion").toString());
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private class AdapterDondeInternet extends BaseAdapter implements SpinnerAdapter {
        private final List<Catdondeinternet> data;

        public AdapterDondeInternet(List<Catdondeinternet> data) {
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
            if (recycle != null) {
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

    //Adapter generico para los spiner
    private ArrayAdapter<String> adapterSpinner(ArrayList<String> arrayList )
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Act_B5_General.this, android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return  adapter;
    }

    //LLENA SPINNER


    //GUARDAR
    private boolean ValidaGuardar() {
        ArrayList<Object> Objetos = new ArrayList<Object>();
        Objetos.add(spnEdoNacimiento);
        //Objetos.add(spnTipoVivienda);
        //Objetos.add(spnConQuienVives);
        //Objetos.add(spnViviendoAhi);
        //Objetos.add(txtTelefonoFijo);
        //Objetos.add(spnNivelEstudio);
        //Objetos.add(txtNombreEscuela);
        //Objetos.add(spnCurso);
        //Objetos.add(spnIngles);
        //Objetos.add(spnActividad);
        //if (lyEspecificaActividad.getVisibility() != View.GONE)
            //Objetos.add(txtEspecificaActividad);
        //Objetos.add(spnIngresoExtra);
        //if (lyIngresoExtra.getVisibility() != View.GONE)
            //Objetos.add(txtActivdadIngresoExtra);
        Objetos.add(txtParaQue);
        //Objetos.add(spnRedesSociales1);
        //Objetos.add(spnRedesSociales2);
        //Objetos.add(spnRedesSociales3);
        //Objetos.add(spnDondeInternet1);
        //Objetos.add(spnDondeInternet2);
        //Objetos.add(spnDondeInternet3);
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
            Intent i = new Intent(getApplicationContext(), Act_Mensajes.class);
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
        String SOAP_ACTION = "http://tempuri.org/IService1/SetDatosGeneralesTelefono";
        String METHOD_NAME = "SetDatosGeneralesTelefono";
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
            DatosEntidad.put("Datosgeneralesid", catdatosgeneral.getDatosgeneralesid());
            DatosEntidad.put("Clienteid", Sesion.getCliendeID());
            DatosEntidad.put("Estadonacimientoid", lstEstados.get(spnEdoNacimiento.getSelectedItemPosition()-1).getEstadoid());

            if (spnTipoVivienda.getSelectedItemPosition() > 0)
                DatosEntidad.put("Tipoviviendaid", lstTipoVivienda.get(spnTipoVivienda.getSelectedItemPosition()-1).getTipoviviendaid());

            if (spnConQuienVives.getSelectedItemPosition() > 0)
                DatosEntidad.put("Viveconid", lstViveCon.get(spnConQuienVives.getSelectedItemPosition()-1).getViveconid());

            if (spnViviendoAhi.getSelectedItemPosition() > 0)
                DatosEntidad.put("Añoid", listaCatanio.get(spnViviendoAhi.getSelectedItemPosition()-1).getAnioid());

            DatosEntidad.put("Telefonofijo", txtTelefonoFijo.getText().toString());
            DatosEntidad.put("Automovil", swtTienesAutomovil.isChecked());
            DatosEntidad.put("Espropio", swtEsPropio.isChecked());

            if (spnNivelEstudio.getSelectedItemPosition() > 0)
                DatosEntidad.put("Nivelestudiosid", lstNivelEstudio.get(spnNivelEstudio.getSelectedItemPosition()-1).getNivelestudioid());

            DatosEntidad.put("Nombreinstitucioneducativa", txtNombreEscuela.getText().toString());

            if (spnCurso.getSelectedItemPosition() > 0)
                DatosEntidad.put("Cursoid", lstCurso.get(spnCurso.getSelectedItemPosition()-1).getCursoid());

            if (spnIngles.getSelectedItemPosition() > 0)
                DatosEntidad.put("Nivelinglesid", lstNivelIngles.get(spnIngles.getSelectedItemPosition()-1).getNivelinglesid());

            if (spnActividad.getSelectedItemPosition() > 0)
                DatosEntidad.put("Actividadentretenimientoid", lstactividadentretenimiento.get(spnActividad.getSelectedItemPosition()-1).getActividadentretenimientoid());

            if (lyEspecificaActividad.getVisibility() != View.GONE)
                DatosEntidad.put("Especificacionactividad", txtEspecificaActividad.getText().toString().trim());
            else
                DatosEntidad.put("Especificacionactividad", "");

            if (spnConQuienVives.getSelectedItemPosition() > 0)
                DatosEntidad.put("Ingresoextraid", lstIngresoExtra.get(spnConQuienVives.getSelectedItemPosition()-1).getIngresoextraid());

            if (lyIngresoExtra.getVisibility() != View.GONE)
                DatosEntidad.put("Actividadingresoextra", txtActivdadIngresoExtra.getText().toString().trim());
            else
                DatosEntidad.put("Actividadingresoextra", "");

            DatosEntidad.put("Tienecelularinteligente", ckTieneCelular.isChecked());
            DatosEntidad.put("Tienetablet", ckTieneTablet.isChecked());
            DatosEntidad.put("Tienecomputadora", ckTieneComputadora.isChecked());
            DatosEntidad.put("Paraqueocupaselservicio", txtParaQue.getText().toString());
            if (spnRedesSociales1.getSelectedItemPosition() > 0)
                DatosEntidad.put("Redessocialesid1", lstRedesSociales.get(spnRedesSociales1.getSelectedItemPosition()-1).getRedessocialesid());
            if (spnRedesSociales2.getSelectedItemPosition() > 0)
                DatosEntidad.put("Redessocialesid2", lstRedesSociales.get(spnRedesSociales2.getSelectedItemPosition()-1).getRedessocialesid());
            if (spnRedesSociales3.getSelectedItemPosition() > 0)
                DatosEntidad.put("Redessocialesid3", lstRedesSociales.get(spnRedesSociales3.getSelectedItemPosition()-1).getRedessocialesid());
            if (spnDondeInternet1.getSelectedItemPosition() > 0)
                DatosEntidad.put("Dondeinternetid1", lstDondeInternet.get(spnDondeInternet1.getSelectedItemPosition()-1).getDondeinternetid());
            if (spnDondeInternet2.getSelectedItemPosition() > 0)
                DatosEntidad.put("Dondeinternetid2", lstDondeInternet.get(spnDondeInternet2.getSelectedItemPosition()-1).getDondeinternetid());
            if (spnDondeInternet3.getSelectedItemPosition() > 0)
                DatosEntidad.put("Dondeinternetid3", lstDondeInternet.get(spnDondeInternet3.getSelectedItemPosition()-1).getDondeinternetid());
            DatosEntidad.put("UltimaAct", catdatosgeneral.getUltimaAct());
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }
        return DatosEntidad.toString();
    }
    //GUARDAR


    //Info del BLOQUE
    private class AsyncInfoBloque extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetInfoBloque();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            if (catdatosgeneral.getUltimaAct() != 0) {
                SetInfoBloque();
            }
            btnFocoInicialB5.requestFocus();
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
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatDatosGenerales";
        String METHOD_NAME = "GetCatDatosGenerales";
        String NAMESPACE = "http://tempuri.org/";

        ArrayList<PropertyInfo> valores =  new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("Clienteid");
        pi1.setValue(Sesion.getCliendeID());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION,METHOD_NAME,NAMESPACE,valores);
        if(respuesta != null) {
            try
            {
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
                if (ev)
                {
                    String[] listaRespuesta;
                    listaRespuesta = new String[respuesta.getPropertyCount()];
                    SoapObject item = (SoapObject) respuesta.getProperty(0);
                    if(Integer.parseInt(item.getProperty("UltimaAct").toString())!=0){
                        catdatosgeneral.setDatosgeneralesid(item.getProperty("Datosgeneralesid").toString());
                        catdatosgeneral.setClienteid(item.getProperty("Clienteid").toString());
                        catdatosgeneral.setEstadonacimientoid(item.getProperty("Estadonacimientoid").toString());
                        catdatosgeneral.setTipoviviendaid(Integer.parseInt(item.getProperty("Tipoviviendaid").toString()));
                        catdatosgeneral.setViveconid(Integer.parseInt(item.getProperty("Viveconid").toString()));
                        catdatosgeneral.setAñoid(Integer.parseInt(item.getProperty("Añoid").toString()));
                        catdatosgeneral.setTelefonofijo(item.getProperty("Telefonofijo").toString());
                        catdatosgeneral.setAutomovil(Boolean.parseBoolean(item.getProperty("Automovil").toString()));
                        catdatosgeneral.setEspropio(Boolean.parseBoolean(item.getProperty("Espropio").toString()));
                        catdatosgeneral.setNivelestudiosid(Integer.parseInt(item.getProperty("Nivelestudiosid").toString()));
                        catdatosgeneral.setNombreinstitucioneducativa(item.getProperty("Nombreinstitucioneducativa").toString());
                        catdatosgeneral.setCursoid(Integer.parseInt(item.getProperty("Cursoid").toString()));
                        catdatosgeneral.setNivelinglesid(Integer.parseInt(item.getProperty("Nivelinglesid").toString()));
                        catdatosgeneral.setActividadentretenimientoid(Integer.parseInt(item.getProperty("Actividadentretenimientoid").toString()));
                        catdatosgeneral.setEspecificacionactividad(item.getProperty("Especificacionactividad").toString().trim());
                        catdatosgeneral.setIngresoextraid(Integer.parseInt(item.getProperty("Ingresoextraid").toString()));
                        catdatosgeneral.setActividadingresoextra(item.getProperty("Actividadingresoextra").toString());
                        catdatosgeneral.setTienecelularinteligente(Boolean.parseBoolean(item.getProperty("Tienecelularinteligente").toString()));
                        catdatosgeneral.setTienetablet(Boolean.parseBoolean(item.getProperty("Tienetablet").toString()));
                        catdatosgeneral.setTienecomputadora(Boolean.parseBoolean(item.getProperty("Tienecomputadora").toString()));
                        catdatosgeneral.setParaqueocupaselservicio(item.getProperty("Paraqueocupaselservicio").toString());
                        catdatosgeneral.setRedessocialesid1(Integer.parseInt(item.getProperty("Redessocialesid1").toString()));
                        catdatosgeneral.setRedessocialesid2(Integer.parseInt(item.getProperty("Redessocialesid2").toString()));
                        catdatosgeneral.setRedessocialesid3(Integer.parseInt(item.getProperty("Redessocialesid3").toString()));
                        catdatosgeneral.setDondeinternetid1(Integer.parseInt(item.getProperty("Dondeinternetid1").toString()));
                        catdatosgeneral.setDondeinternetid2(Integer.parseInt(item.getProperty("Dondeinternetid2").toString()));
                        catdatosgeneral.setDondeinternetid3(Integer.parseInt(item.getProperty("Dondeinternetid3").toString()));
                        catdatosgeneral.setUltimaAct(Integer.parseInt(item.getProperty("UltimaAct").toString()));
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    private void SetInfoBloque() {
        spnEdoNacimiento.setSelection(getIndexEstado(catdatosgeneral.getEstadonacimientoid()));
        spnTipoVivienda.setSelection(catdatosgeneral.getTipoviviendaid());
        spnConQuienVives.setSelection(catdatosgeneral.getViveconid());
        spnViviendoAhi.setSelection(catdatosgeneral.getAñoid());
        txtTelefonoFijo.setText(catdatosgeneral.getTelefonofijo());
        swtTienesAutomovil.setChecked(catdatosgeneral.getAutomovil());
        swtEsPropio.setChecked(catdatosgeneral.getEspropio());
        spnNivelEstudio.setSelection(catdatosgeneral.getNivelestudiosid());
        txtNombreEscuela.setText(catdatosgeneral.getNombreinstitucioneducativa());
        spnCurso.setSelection(catdatosgeneral.getCursoid());
        spnIngles.setSelection(catdatosgeneral.getNivelinglesid());
        spnActividad.setSelection(catdatosgeneral.getActividadentretenimientoid());
        txtEspecificaActividad.setText(catdatosgeneral.getEspecificacionactividad());
        spnIngresoExtra.setSelection(catdatosgeneral.getIngresoextraid());
        txtActivdadIngresoExtra.setText(catdatosgeneral.getActividadingresoextra());
        ckTieneCelular.setChecked(catdatosgeneral.getTienecelularinteligente());
        ckTieneTablet.setChecked(catdatosgeneral.getTienetablet());
        ckTieneComputadora.setChecked(catdatosgeneral.getTienecomputadora());
        txtParaQue.setText(catdatosgeneral.getParaqueocupaselservicio());
        spnRedesSociales1.setSelection(catdatosgeneral.getRedessocialesid1());
        spnRedesSociales2.setSelection(catdatosgeneral.getRedessocialesid2());
        spnRedesSociales3.setSelection(catdatosgeneral.getRedessocialesid3());
        spnDondeInternet1.setSelection(catdatosgeneral.getDondeinternetid1());
        spnDondeInternet2.setSelection(catdatosgeneral.getDondeinternetid2());
        spnDondeInternet3.setSelection(catdatosgeneral.getDondeinternetid3());
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
                    new AlertDialog.Builder(Act_B5_General.this)
                            .setTitle(R.string.msgRefTitulo)
                            .setMessage(R.string.msgRefNoContesto)
                            .setCancelable(false)
                            .setPositiveButton(R.string.msgRefOk, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Sesion.setBloqueoReferencia(true);
                                    Sesion.setBloqueoCliente(Bloqueos);
                                    Sesion.setSolicitud(Solicitud);
                                    Sesion.setBloqueActual(5);
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

    private int getIndexEstado(String myString){
        int index = 0;
        for (int i=0;i< lstEstados.size();i++){
            if (lstEstados.get(i).getEstadoid().equals(myString))
            {
                index = i+1;
            }
        }
        return index;
    }

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
