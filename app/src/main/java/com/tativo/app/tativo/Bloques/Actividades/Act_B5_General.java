package com.tativo.app.tativo.Bloques.Actividades;

import android.accounts.AccountAuthenticatorResponse;
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
import android.widget.Adapter;
import android.widget.AdapterView;
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

import fr.ganfra.materialspinner.MaterialSpinner;

public class Act_B5_General extends AppCompatActivity {

    MaterialSpinner spnEdoNacimiento, spnTipoVivienda, spnConQuienVives, spnViviendoAhi, spnNivelEstudio, spnCurso, spnIngles, spnActividad, spnIngresoExtra, spnRedesSociales1, spnRedesSociales2, spnRedesSociales3, spnDondeInternet1, spnDondeInternet2, spnDondeInternet3;
    AutoCompleteTextView txtTelefonoFijo, txtNombreEscuela, txtEspecificaActividad, txtActivdadIngresoExtra, txtParaQue;
    Switch swtTienesAutomovil, swtEsPropio;
    CheckBox ckTieneCelular, ckTieneTablet, ckTieneComputadora;
    Button btnInfGeneral;
    LinearLayout lyEspecificaActividad, lyIngresoExtra;

    Globals Sesion;
    ProgressDialog progressDialog;

    AdapterEstados estadosAdapter;
    ArrayList<Catestado> lstEstados = new ArrayList<Catestado>();

    AdapterTipoVivienda tipoViviendaAdapter;
    ArrayList<Cattipovivienda> lstTipoVivienda = new ArrayList<Cattipovivienda>();

    AdapterViveCon viveConAdapter;
    ArrayList<Catvivecon> lstViveCon = new ArrayList<Catvivecon>();

    AdapterAnio spnCatanioAdapter;
    ArrayList<Catanio> listaCatanio = new ArrayList<Catanio>();

    AdapterNivelEstudio nivelEstudioAdapter;
    ArrayList<Catnivelestudio> lstNivelEstudio = new ArrayList<Catnivelestudio>();

    AdapterCurso cursoAdapter;
    ArrayList<Catcurso> lstCurso = new ArrayList<Catcurso>();

    AdapterNivelIngles nivelInglesAdapter;
    ArrayList<Catnivelingles> lstNivelIngles = new ArrayList<Catnivelingles>();

    AdapterActividadEntretenimiento actividadEntretenimientoAdapter;
    ArrayList<Catactividadentretenimiento> lstactividadentretenimiento = new ArrayList<Catactividadentretenimiento>();

    AdapterIngresoExtra ingresoExtraAdapter;
    ArrayList<Catingresoextra> lstIngresoExtra = new ArrayList<Catingresoextra>();

    AdapterRedesSociales redesSocialesAdapter;
    ArrayList<Catredessociales> lstRedesSociales = new ArrayList<Catredessociales>();

    AdapterDondeInternet dondeInternetAdapter;
    ArrayList<Catdondeinternet> lstDondeInternet = new ArrayList<Catdondeinternet>();

    Catdatosgeneral catdatosgeneral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_b5_general);
        LoadFormControls();
        FocusManager();
        EventManager();
        new AsyncLoadData().execute();
    }

    private void LoadFormControls() {
        progressDialog = new ProgressDialog(this);
        Sesion = new Globals();
        Sesion.setCliendeID("83F0E461-3887-4185-94FB-D992D9AC7E26");

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
        FocusNextControl(R.id.spnActividad, "S", R.id.txtEspecificaActividad, "T");
        FocusNextControl(R.id.txtEspecificaActividad, "T", R.id.spnIngresoExtra, "S");
        FocusNextControl(R.id.spnIngresoExtra, "S", R.id.txtActivdadIngresoExtra, "T");
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
        swtTienesAutomovil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    swtEsPropio.setEnabled(true);
                else {
                    swtEsPropio.setChecked(false);
                    swtEsPropio.setEnabled(false);
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
                Catactividadentretenimiento cat = (Catactividadentretenimiento) parent.getItemAtPosition(position);
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
                Catingresoextra cat = (Catingresoextra) parent.getItemAtPosition(position);
                if (cat.getDescripcion().toString().equals("$0")) {
                    lyIngresoExtra.setVisibility(View.GONE);
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
                    new AsyncGuardar().execute();
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
            estadosAdapter = new AdapterEstados(lstEstados);
            spnEdoNacimiento.setAdapter(estadosAdapter);

            tipoViviendaAdapter = new AdapterTipoVivienda(lstTipoVivienda);
            spnTipoVivienda.setAdapter(tipoViviendaAdapter);

            viveConAdapter = new AdapterViveCon(lstViveCon);
            spnConQuienVives.setAdapter(viveConAdapter);

            spnCatanioAdapter = new AdapterAnio(listaCatanio);
            spnViviendoAhi.setAdapter(spnCatanioAdapter);

            nivelEstudioAdapter = new AdapterNivelEstudio(lstNivelEstudio);
            spnNivelEstudio.setAdapter(nivelEstudioAdapter);

            cursoAdapter = new AdapterCurso(lstCurso);
            spnCurso.setAdapter(cursoAdapter);

            nivelInglesAdapter = new AdapterNivelIngles(lstNivelIngles);
            spnIngles.setAdapter(nivelInglesAdapter);

            actividadEntretenimientoAdapter = new AdapterActividadEntretenimiento(lstactividadentretenimiento);
            spnActividad.setAdapter(actividadEntretenimientoAdapter);

            ingresoExtraAdapter = new AdapterIngresoExtra(lstIngresoExtra);
            spnIngresoExtra.setAdapter(ingresoExtraAdapter);

            redesSocialesAdapter = new AdapterRedesSociales(lstRedesSociales);
            spnRedesSociales1.setAdapter(redesSocialesAdapter);
            spnRedesSociales2.setAdapter(redesSocialesAdapter);
            spnRedesSociales3.setAdapter(redesSocialesAdapter);

            dondeInternetAdapter = new AdapterDondeInternet(lstDondeInternet);
            spnDondeInternet1.setAdapter(dondeInternetAdapter);
            spnDondeInternet2.setAdapter(dondeInternetAdapter);
            spnDondeInternet3.setAdapter(dondeInternetAdapter);
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
    //LLENA SPINNER


    //GUARDAR
    private boolean ValidaGuardar() {
        ArrayList<Object> Objetos = new ArrayList<Object>();
        Objetos.add(spnEdoNacimiento);
        Objetos.add(spnTipoVivienda);
        Objetos.add(spnConQuienVives);
        Objetos.add(spnViviendoAhi);
        Objetos.add(txtTelefonoFijo);
        Objetos.add(spnNivelEstudio);
        Objetos.add(txtNombreEscuela);
        Objetos.add(spnCurso);
        Objetos.add(spnIngles);
        Objetos.add(spnActividad);
        if (lyEspecificaActividad.getVisibility() != View.GONE)
            Objetos.add(txtEspecificaActividad);
        Objetos.add(spnIngresoExtra);
        if (lyIngresoExtra.getVisibility() != View.GONE)
            Objetos.add(txtActivdadIngresoExtra);
        Objetos.add(txtParaQue);
        Objetos.add(spnRedesSociales1);
        Objetos.add(spnRedesSociales2);
        Objetos.add(spnRedesSociales3);
        Objetos.add(spnDondeInternet1);
        Objetos.add(spnDondeInternet2);
        Objetos.add(spnDondeInternet3);
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
            Intent i = new Intent(getApplicationContext(), Act_Documentos.class);
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
            DatosEntidad.put("Estadonacimientoid", ((Catestado) spnEdoNacimiento.getSelectedItem()).getEstadoid());
            DatosEntidad.put("Tipoviviendaid", ((Cattipovivienda) spnTipoVivienda.getSelectedItem()).getTipoviviendaid());
            DatosEntidad.put("Viveconid", ((Catvivecon) spnConQuienVives.getSelectedItem()).getViveconid());
            DatosEntidad.put("Añoid", ((Catanio) spnViviendoAhi.getSelectedItem()).getAnioid());
            DatosEntidad.put("Telefonofijo", txtTelefonoFijo.getText().toString());
            DatosEntidad.put("Automovil", swtTienesAutomovil.isChecked());
            DatosEntidad.put("Espropio", swtEsPropio.isChecked());
            DatosEntidad.put("Nivelestudiosid", ((Catnivelestudio) spnNivelEstudio.getSelectedItem()).getNivelestudioid());
            DatosEntidad.put("Nombreinstitucioneducativa", txtNombreEscuela.getText().toString());
            DatosEntidad.put("Cursoid", ((Catcurso) spnCurso.getSelectedItem()).getCursoid());
            DatosEntidad.put("Nivelinglesid", ((Catnivelingles) spnIngles.getSelectedItem()).getNivelinglesid());
            DatosEntidad.put("Actividadentretenimientoid", ((Catactividadentretenimiento) spnActividad.getSelectedItem()).getActividadentretenimientoid());
            DatosEntidad.put("Especificacionactividad", txtEspecificaActividad.getText().toString());
            DatosEntidad.put("Ingresoextraid", ((Catingresoextra) spnIngresoExtra.getSelectedItem()).getIngresoextraid());
            DatosEntidad.put("Actividadingresoextra", txtEspecificaActividad.getText().toString());
            DatosEntidad.put("Tienecelularinteligente", ckTieneCelular.isChecked());
            DatosEntidad.put("Tienetablet", ckTieneTablet.isChecked());
            DatosEntidad.put("Tienecomputadora", ckTieneComputadora.isChecked());
            DatosEntidad.put("Paraqueocupaselservicio", txtParaQue.getText().toString());
            DatosEntidad.put("Redessocialesid1", ((Catredessociales) spnRedesSociales1.getSelectedItem()).getRedessocialesid());
            DatosEntidad.put("Redessocialesid3", ((Catredessociales) spnRedesSociales2.getSelectedItem()).getRedessocialesid());
            DatosEntidad.put("Redessocialesid3", ((Catredessociales) spnRedesSociales3.getSelectedItem()).getRedessocialesid());
            DatosEntidad.put("Dondeinternetid1", ((Catdondeinternet) spnDondeInternet1.getSelectedItem()).getDondeinternetid());
            DatosEntidad.put("Dondeinternetid2", ((Catdondeinternet) spnDondeInternet2.getSelectedItem()).getDondeinternetid());
            DatosEntidad.put("Dondeinternetid3", ((Catdondeinternet) spnDondeInternet3.getSelectedItem()).getDondeinternetid());
            DatosEntidad.put("UltimaAct", catdatosgeneral.getUltimaAct());
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }
        return DatosEntidad.toString();
    }
    //GUARDAR

}
