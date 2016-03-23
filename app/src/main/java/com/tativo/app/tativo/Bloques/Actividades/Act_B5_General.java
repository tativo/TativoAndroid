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
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tativo.app.tativo.Bloques.Clases.Catestado;
import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.Globals;
import com.tativo.app.tativo.Utilidades.ServiciosSoap;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Act_B5_General extends AppCompatActivity {

    MaterialSpinner spnEdoNacimiento, spnTipoVivienda, spnConQuienVives, spnViviendoAhi, spnNivelEstudio, spnCurso, spnIngles, spnActividad, spnIngresoExtra, spnRedesSociales1, spnRedesSociales2, spnRedesSociales3, spnDondeInternet1, spnDondeInternet2, spnDondeInternet3;
    AutoCompleteTextView txtTelefonoFijo, txtNombreEscuela, txtEspecificaActividad, txtActivdadIngresoExtra, txtParaQue;
    Switch swtTienesAutomovil, swtEsPropio;
    CheckBox ckTieneCelular, ckTieneTablet, ckTieneComputadora;
    Button btnInfGeneral;

    Globals Sesion;
    ProgressDialog progressDialog;

    AdapterCatEstados CatEstadosAdapter;
    ArrayList<Catestado> lstCatEstado = new ArrayList<Catestado>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_b5_general);
        LoadFormControls();
        FocusManager();
        EventManager();
        new AsyncLoadData().execute();
    }

    private void LoadFormControls()
    {
        Sesion = new Globals();
        Sesion.setCliendeID("83F0E461-3887-4185-94FB-D992D9AC7E26");

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


        ckTieneCelular = (CheckBox) findViewById(R.id.ckTieneCelular);
        ckTieneTablet = (CheckBox) findViewById(R.id.ckTieneTablet);
        ckTieneComputadora = (CheckBox) findViewById(R.id.ckTieneComputadora);


        btnInfGeneral = (Button) findViewById(R.id.btnInfGeneral);
    }

    public void FocusManager()
    {
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
        FocusNextControl(R.id.spnRedesSociales3, "S", R.id.spnDondeInternet1, "S");
        FocusNextControl(R.id.spnDondeInternet1, "S", R.id.spnDondeInternet2, "S");
        FocusNextControl(R.id.spnDondeInternet2, "S", R.id.spnDondeInternet3, "S");
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

    private void EventManager()
    {
        btnInfGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Act_Documentos.class);
                startActivity(i);
                finish();
            }
        });
    }

    //LLENA SPINNER
    private class AsyncLoadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetCatEstados();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            CatEstadosAdapter = new AdapterCatEstados(lstCatEstado);
            spnEdoNacimiento.setAdapter(CatEstadosAdapter);
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

    private void GetCatEstados(){
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatEstados";
        String METHOD_NAME = "GetCatEstados";
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
                    Catestado entidad = new Catestado();
                    entidad.setEstadoid(item.getProperty("Estadoid").toString());
                    entidad.setEstado(item.getProperty("Estado").toString());
                    lstCatEstado.add(entidad);
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class AdapterCatEstados extends BaseAdapter implements SpinnerAdapter {
        private final List<Catestado> data;

        public AdapterCatEstados(List<Catestado> data){
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
            text.setText(data.get(position).getEstado());
            return text;
        }
    }
    //LLENA SPINNER

}
