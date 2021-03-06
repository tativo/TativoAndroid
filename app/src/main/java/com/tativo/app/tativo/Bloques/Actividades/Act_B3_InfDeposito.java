package com.tativo.app.tativo.Bloques.Actividades;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tativo.app.tativo.Bloques.Clases.CatBloqueoCliente;
import com.tativo.app.tativo.Bloques.Clases.Catbanco;
import com.tativo.app.tativo.Bloques.Clases.Catdatosdeposito;
import com.tativo.app.tativo.Bloques.Clases.Catformasdepago;
import com.tativo.app.tativo.Bloques.Clases.Catperiodosdepago;
import com.tativo.app.tativo.Bloques.Clases.DatosSolicitud;
import com.tativo.app.tativo.Bloques.Fragmentos.Frg_NoTarjetaDebito;
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

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class Act_B3_InfDeposito extends AppCompatActivity {

    int year_x, month_x, day_x;
    boolean validaCadena = false;
    private static final int MY_SCAN_REQUEST_CODE = 2;
    Globals Sesion;
    ProgressDialog progressDialog;

    Button btnDatosDeposito, btnCardIO,btnFocoInicialB3,btnFechaProximoPago;
    Switch swtRecibesNomina;
    Spinner spnBanco,spnFrecuenciaPago,spnMedioPago;
    EditText txtNumeroTarjetaCLABE,txtFechaProximoPago;
    TextView lblTarjetaDebito, lblErrorNumeroTarjeta;


    ArrayList<Catbanco> ListaCatbanco = new ArrayList<Catbanco>();
    ArrayList<Catperiodosdepago> ListaCatperiodosdepago = new ArrayList<Catperiodosdepago>();
    ArrayList<Catformasdepago> ListaCatformasdepago = new ArrayList<Catformasdepago>();

    //AdapterCatbanco spnBancoAdapter;
    //AdapterCatformasdepago spnMedioPagoAdapter;
    //AdapterCatperiodosdepago spnFrecuenciaPagoAdapter;

    Catdatosdeposito EntityCatdatosdeposito = new Catdatosdeposito();

    AsyncEstatusSolicitud EstatusSolicitud = new AsyncEstatusSolicitud();
    boolean CancelaEstatusSolicitud = false ;

    ArrayList<String> arrayCatBanco, arrayCatFormaPago, arrayCatPeriodoPago;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_b3_infdeposito);
        LoadFormControls();
        FocusManager();
        EventManager();
        new AsyncLoadData().execute();
        Sesion = (Globals) getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            EstatusSolicitud.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            EstatusSolicitud.execute();
        }
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
    public void FocusManager(){
        FocusNextControl(R.id.spnBanco, "S", R.id.txtNumeroTarjetaCLABE, "T");
        FocusNextControl(R.id.txtNumeroTarjetaCLABE, "T", R.id.spnFrecuenciaPago, "S");
        FocusNextControl(R.id.spnFrecuenciaPago, "S", R.id.spnMedioPago, "S");
        FocusNextControl(R.id.spnMedioPago, "S", R.id.txtFechaProximoPago, "T");
    }
    private void LoadFormControls(){
        //Progress Bar
        progressDialog = new ProgressDialog(Act_B3_InfDeposito.this);


        lblTarjetaDebito = (TextView) findViewById(R.id.lblTarjetaDebito);
        lblErrorNumeroTarjeta = (TextView) findViewById(R.id.lblErrorNumeroTarjeta);

        //Combos
        spnBanco = (Spinner) findViewById(R.id.spnBanco);
        spnBanco.setFocusable(true);
        spnBanco.setFocusableInTouchMode(true);

        spnFrecuenciaPago = (Spinner) findViewById(R.id.spnFrecuenciaPago);
        spnFrecuenciaPago.setFocusable(true);
        spnFrecuenciaPago.setFocusableInTouchMode(true);

        spnMedioPago = (Spinner) findViewById(R.id.spnMedioPago);
        spnMedioPago.setFocusable(true);
        spnMedioPago.setFocusableInTouchMode(true);

        //Cajas de texto
        txtNumeroTarjetaCLABE = (EditText) findViewById(R.id.txtNumeroTarjetaCLABE);
        txtFechaProximoPago = (EditText) findViewById(R.id.txtFechaProximoPago);
        //txtFechaProximoPago.addTextChangedListener(tw);

        //Switch
        swtRecibesNomina =(Switch) findViewById(R.id.swtRecibesNomina);
        //swtRecibesNomina.setFocusable(true);
        //swtRecibesNomina.setFocusableInTouchMode(true);

        //Botones
        btnDatosDeposito = (Button) findViewById(R.id.btnDatosDeposito);
        //btnDatosDeposito.setFocusable(true);
        //btnDatosDeposito.setFocusableInTouchMode(true);

        btnCardIO = (Button) findViewById(R.id.btnCardIO);
        //btnCardIO.setFocusable(true);
        //btnCardIO.setFocusableInTouchMode(true);

        btnFocoInicialB3 = (Button) findViewById(R.id.btnFocoInicialB3);
        btnFocoInicialB3.setFocusable(true);
        btnFocoInicialB3.setFocusableInTouchMode(true);

        btnFechaProximoPago = (Button) findViewById(R.id.btnFechaProximoPago);

        //Constantes
        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

    }
    private void EventManager() {

        lblTarjetaDebito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                new Frg_NoTarjetaDebito().show(fragmentManager,"frmNoTarjetaDebito");
            }
        });

        spnBanco.setOnTouchListener(new spOcultaTeclado());
        spnFrecuenciaPago.setOnTouchListener(new spOcultaTeclado());
        spnMedioPago.setOnTouchListener(new spOcultaTeclado());

        btnDatosDeposito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidaGuardar()) {
                    EstatusSolicitud.cancel(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new AsyncSaveData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        new AsyncSaveData().execute();
                    }
                }
            }
        });

        btnCardIO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScanPress(v);
            }
        });

        btnFechaProximoPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog(R.id.txtFechaProximoPago);
            }
        });

        txtFechaProximoPago.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DateDialog(R.id.txtFechaProximoPago);
            }
        });

        txtNumeroTarjetaCLABE.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(validaCadena)
                {
                    if (txtNumeroTarjetaCLABE.getText().length() > 0 && txtNumeroTarjetaCLABE.getText().length() < 16) {
                        lblErrorNumeroTarjeta.setText("Son menos de 16 números ¿la información es correcta?");
                    }
                    else if(txtNumeroTarjetaCLABE.getText().length() == 17) {
                        lblErrorNumeroTarjeta.setText("Son 17 números no parece un número CLABE");
                    }
                    else if(txtNumeroTarjetaCLABE.getText().length() == 0)
                    {
                        lblErrorNumeroTarjeta.setText("Necesitamos este dato");
                    }
                    else
                    {
                        lblErrorNumeroTarjeta.setText("");
                    }
                }
            }
        });

        txtNumeroTarjetaCLABE.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (txtNumeroTarjetaCLABE.getText().length() > 0 && txtNumeroTarjetaCLABE.getText().length() < 16) {
                    lblErrorNumeroTarjeta.setText("Son menos de 16 números ¿la información es correcta?");
                    validaCadena = true;
                }
                else if(txtNumeroTarjetaCLABE.getText().length() == 17) {
                    lblErrorNumeroTarjeta.setText("Son 17 números no parece un número CLABE");
                    validaCadena = true;
                }
                else
                {
                    lblErrorNumeroTarjeta.setText("");
                }
            }
        });
    }

    //Region Llenar datos del formulario
    private class AsyncLoadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetCatBanco();
            GetCatFormasPago();
            GetCatPeriodosDePago();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();

            //spnBancoAdapter = new AdapterCatbanco(ListaCatbanco);
            ArrayAdapter<String> adapterBanco = adapterSpinner(arrayCatBanco);
            spnBanco.setAdapter(adapterBanco);

            //spnMedioPagoAdapter = new AdapterCatformasdepago(ListaCatformasdepago);
            ArrayAdapter<String> adapterMedioPago = adapterSpinner(arrayCatFormaPago);
            spnMedioPago.setAdapter(adapterMedioPago);

            //spnFrecuenciaPagoAdapter = new AdapterCatperiodosdepago(ListaCatperiodosdepago);
            ArrayAdapter<String> adapterFrecuenciaPago = adapterSpinner(arrayCatPeriodoPago);
            spnFrecuenciaPago.setAdapter(adapterFrecuenciaPago);

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
    private void GetCatBanco(){
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatBanco";
        String METHOD_NAME = "GetCatBanco";
        String NAMESPACE = "http://tempuri.org/";
        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION,METHOD_NAME,NAMESPACE,null);
        if(respuesta != null) {
            try {
                String[] listaRespuesta;
                listaRespuesta = new String[respuesta.getPropertyCount()];
                arrayCatBanco = new ArrayList<String>();
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);

                    Catbanco entidad = new Catbanco();
                    entidad.setBancoid(item.getProperty("Bancoid").toString());
                    entidad.setCodigo(Integer.parseInt(item.getProperty("Codigo").toString()));
                    entidad.setNombre(item.getProperty("Nombre").toString().trim());
                    arrayCatBanco.add(item.getProperty("Nombre").toString().trim());
                    ListaCatbanco.add(entidad);

                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private void GetCatFormasPago(){
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatFormasPago";
        String METHOD_NAME = "GetCatFormasPago";
        String NAMESPACE = "http://tempuri.org/";
        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION,METHOD_NAME,NAMESPACE,null);
        if(respuesta != null) {
            try {
                String[] listaRespuesta;
                listaRespuesta = new String[respuesta.getPropertyCount()];
                arrayCatFormaPago = new ArrayList<String>();
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);

                    Catformasdepago entidad = new Catformasdepago();
                    entidad.setFormadepagoid(Integer.parseInt(item.getProperty("Formadepagoid").toString()));
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    arrayCatFormaPago.add(item.getProperty("Descripcion").toString());
                    ListaCatformasdepago.add(entidad);
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private void GetCatPeriodosDePago(){
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatPeriodosDePago";
        String METHOD_NAME = "GetCatPeriodosDePago";
        String NAMESPACE = "http://tempuri.org/";
        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION,METHOD_NAME,NAMESPACE,null);
        if(respuesta != null) {
            try {
                String[] listaRespuesta;
                listaRespuesta = new String[respuesta.getPropertyCount()];
                arrayCatPeriodoPago = new ArrayList<String>();
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);

                    Catperiodosdepago entidad = new Catperiodosdepago();
                    entidad.setPeriododepagoid(Integer.parseInt(item.getProperty("Periododepagoid").toString()));
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    arrayCatPeriodoPago.add(item.getProperty("Descripcion").toString());
                    ListaCatperiodosdepago.add(entidad);
                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    /*
    private class AdapterCatbanco extends BaseAdapter implements SpinnerAdapter {
        private final List<Catbanco> data;

        public AdapterCatbanco(List<Catbanco> data){
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
            text.setText(data.get(position).getNombre());
            return text;
        }
    }
    private class AdapterCatformasdepago extends BaseAdapter implements SpinnerAdapter {
        private final List<Catformasdepago> data;

        public AdapterCatformasdepago(List<Catformasdepago> data){
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
    private class AdapterCatperiodosdepago extends BaseAdapter implements SpinnerAdapter {
        private final List<Catperiodosdepago> data;

        public AdapterCatperiodosdepago(List<Catperiodosdepago> data){
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

    //Region CARD IO
    public void onScanPress(View v) {
        Intent scanIntent = new Intent(this, CardIOActivity.class);
        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_SCAN_REQUEST_CODE) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                //resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
                txtNumeroTarjetaCLABE.setText(scanResult.cardNumber);
                txtNumeroTarjetaCLABE.setSelection(txtNumeroTarjetaCLABE.getText().length());
                btnFocoInicialB3.requestFocus();

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
                    //resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    //resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                }

                if (scanResult.postalCode != null) {
                    //resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }
            }
            else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultDisplayStr);
        }
        // else handle other activity results
    }
    //Endregion

    //Region DateDialog
    public void DateDialog(int d){
        final EditText txtDate = (EditText) findViewById(d);
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
            {
                year_x = year;
                month_x = monthOfYear+1;
                day_x = dayOfMonth;

                String sfecha = day_x+"/"+month_x+"/"+year_x;
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                ParsePosition pp = new ParsePosition(0);
                Date d = format.parse(sfecha, pp);
                txtDate.setText(format.format(d).toString());
            }};
        DatePickerDialog dpDialog=new DatePickerDialog(this, listener, year_x, month_x, day_x);
        dpDialog.show();
    }
    //Endregion

    //Region Guardar
    private boolean ValidaGuardar() {
        ArrayList<Object> Objetos = new ArrayList<Object>();
        Objetos.add(spnBanco);
        Objetos.add(txtNumeroTarjetaCLABE);
        Objetos.add(spnFrecuenciaPago);
        Objetos.add(spnMedioPago);
        Objetos.add(txtFechaProximoPago);
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
            Intent i = new Intent(getApplicationContext(), Act_B3_ConfirmarPIN.class);
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
        String SOAP_ACTION = "http://tempuri.org/IService1/SetCatdatosdepositoTelefono";
        String METHOD_NAME = "SetCatdatosdepositoTelefono";
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
            DatosEntidad.put("Datodepositoid", EntityCatdatosdeposito.getDatodepositoid());
            DatosEntidad.put("Clienteid",Sesion.getCliendeID());
            //DatosEntidad.put("Bancoid",  ((Catbanco) spnBanco.getSelectedItem()).getBancoid());
            DatosEntidad.put("Bancoid", ListaCatbanco.get(spnBanco.getSelectedItemPosition() - 1).getBancoid());
            DatosEntidad.put("NumeroDeDeposito", txtNumeroTarjetaCLABE.getText().toString());
            DatosEntidad.put("Recibenomina", swtRecibesNomina.isChecked());
            DatosEntidad.put("Periododepagoid", ListaCatperiodosdepago.get(spnFrecuenciaPago.getSelectedItemPosition() - 1).getPeriododepagoid());
            DatosEntidad.put("Formadepagoid", ListaCatformasdepago.get(spnMedioPago.getSelectedItemPosition() - 1).getFormadepagoid());
            DatosEntidad.put("Fechaproxpago", Utilerias.getDate(txtFechaProximoPago.getText().toString()));
            DatosEntidad.put("UltimaAct", EntityCatdatosdeposito.getUltimaAct());

        } catch (Exception ex) {

        }
        return DatosEntidad.toString();
    }
    //EndGuardar


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
                    new AlertDialog.Builder(Act_B3_InfDeposito.this)
                            .setTitle(R.string.msgRefTitulo)
                            .setMessage(R.string.msgRefNoContesto)
                            .setCancelable(false)
                            .setPositiveButton(R.string.msgRefOk, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Sesion.setBloqueoReferencia(true);
                                    Sesion.setBloqueoCliente(Bloqueos);
                                    Sesion.setSolicitud(Solicitud);
                                    Sesion.setBloqueActual(3);
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
            if (EntityCatdatosdeposito.getUltimaAct() != 0) {
                SetInfoBloque();
            }
            btnFocoInicialB3.requestFocus();
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
        String SOAP_ACTION = "http://tempuri.org/IService1/GetCatdatosdeposito";
        String METHOD_NAME = "GetCatdatosdeposito";
        String NAMESPACE = "http://tempuri.org/";

        ArrayList<PropertyInfo> valores = new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("Clienteid");
        pi1.setValue(Sesion.getCliendeID());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE, valores);

        if (respuesta != null) {
            try {
                if (Boolean.parseBoolean(respuesta.getProperty("EsValido").toString())) {
                    SoapObject Datos = (SoapObject) respuesta.getProperty("Datos");
                    if(Integer.parseInt(Datos.getProperty("UltimaAct").toString())!=0){
                        EntityCatdatosdeposito.setDatodepositoid(Datos.getProperty("Datodepositoid").toString());
                        EntityCatdatosdeposito.setClienteid(Datos.getProperty("Clienteid").toString());
                        EntityCatdatosdeposito.setBancoid(Datos.getProperty("Bancoid").toString());
                        EntityCatdatosdeposito.setNumeroDeDeposito(Datos.getProperty("NumeroDeDeposito").toString());
                        EntityCatdatosdeposito.setRecibenomina(Boolean.parseBoolean(Datos.getProperty("Recibenomina").toString()));
                        EntityCatdatosdeposito.setPeriododepagoid(Integer.parseInt(Datos.getProperty("Periododepagoid").toString()));
                        EntityCatdatosdeposito.setFormadepagoid(Integer.parseInt(Datos.getProperty("Formadepagoid").toString()));
                        EntityCatdatosdeposito.setFechaproxpago(new SimpleDateFormat("yyyy-MM-dd").parse(Datos.getProperty("Fechaproxpago").toString().substring(0, 10)));
                        EntityCatdatosdeposito.setUltimaAct(Integer.parseInt(Datos.getProperty("UltimaAct").toString()));
                    }
                } else {
                    //Toast.makeText(getApplicationContext(),"Error: "+respuesta.getProperty("Mensaje").toString(),Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    private void SetInfoBloque() {
        spnBanco.setSelection(getIndexBancos(EntityCatdatosdeposito.getBancoid()));
        txtNumeroTarjetaCLABE.setText(EntityCatdatosdeposito.getNumeroDeDeposito());
        swtRecibesNomina.setChecked(EntityCatdatosdeposito.isRecibenomina());
        spnFrecuenciaPago.setSelection(EntityCatdatosdeposito.getPeriododepagoid());
        spnMedioPago.setSelection(EntityCatdatosdeposito.getFormadepagoid());
        txtFechaProximoPago.setText(new SimpleDateFormat("dd/MM/yyyy").format(EntityCatdatosdeposito.getFechaproxpago()));
    }
    private int getIndexBancos(String myString) {
        int index = 0;
        for (int i = 0; i < ListaCatbanco.size(); i++) {
            if (ListaCatbanco.get(i).getBancoid().equals(myString)) {
                index = i + 1;
            }
        }
        return index;
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

    public ArrayAdapter<String> adapterSpinner(ArrayList<String> arrayList ) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Act_B3_InfDeposito.this, android.R.layout.simple_spinner_item, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return  adapter;
    }
}
