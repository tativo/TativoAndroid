package com.tativo.app.tativo.Bloques.Actividades;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.tativo.app.tativo.Bloques.Clases.Catformasdepago;
import com.tativo.app.tativo.Bloques.Clases.Catperiodosdepago;
import com.tativo.app.tativo.Bloques.Clases.Catrelacionespersonal;
import com.tativo.app.tativo.Bloques.Clases.DatosSolicitud;
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

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class Act_B3_InfDeposito extends AppCompatActivity {

    int year_x, month_x, day_x;
    private static final int MY_SCAN_REQUEST_CODE = 2;
    Globals Sesion;
    ProgressDialog progressDialog;

    Button btnDatosDeposito, btnCardIO,btnFocoInicialB3,btnFechaProximoPago;
    Switch swtTarjetaDebito,swtRecibesNomina;
    Spinner spnBanco,spnFrecuenciaPago,spnMedioPago;
    EditText txtNumeroTarjetaCLABE,txtFechaProximoPago;

    ArrayList<Catbanco> ListaCatbanco = new ArrayList<Catbanco>();
    ArrayList<Catperiodosdepago> ListaCatperiodosdepago = new ArrayList<Catperiodosdepago>();
    ArrayList<Catformasdepago> ListaCatformasdepago = new ArrayList<Catformasdepago>();

    AdapterCatbanco spnBancoAdapter;
    AdapterCatformasdepago spnMedioPagoAdapter;
    AdapterCatperiodosdepago spnFrecuenciaPagoAdapter;

    CatBloqueoCliente Bloqueos = new CatBloqueoCliente();
    DatosSolicitud Solicitud = new DatosSolicitud();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_b3_infdeposito);
        LoadFormControls();
        FocusManager();
        EventManager();
        new AsyncLoadData().execute();
        Sesion = (Globals) getApplicationContext();
        btnFocoInicialB3.requestFocus();
        new AsyncEstatusSolicitud().execute();
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
        FocusNextControl(R.id.spnFrecuenciaPago, "S", R.id.spnMedioPago, "S");
        FocusNextControl(R.id.spnMedioPago, "S", R.id.txtFechaProximoPago, "T");
    }
    private void LoadFormControls(){
        //Progress Bar
        progressDialog = new ProgressDialog(Act_B3_InfDeposito.this);

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
        swtTarjetaDebito =(Switch) findViewById(R.id.swtTarjetaDebito);
        //swtTarjetaDebito.setFocusable(true);
        //swtTarjetaDebito.setFocusableInTouchMode(true);

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
        btnDatosDeposito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Act_B3_ConfirmarPIN.class);
                startActivity(i);
                finish();
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
            spnBancoAdapter = new AdapterCatbanco(ListaCatbanco);
            spnFrecuenciaPagoAdapter = new AdapterCatperiodosdepago(ListaCatperiodosdepago);
            spnMedioPagoAdapter = new AdapterCatformasdepago(ListaCatformasdepago);
            spnBanco.setAdapter(spnBancoAdapter);
            spnMedioPago.setAdapter(spnMedioPagoAdapter);
            spnFrecuenciaPago.setAdapter(spnFrecuenciaPagoAdapter);
            /*
            new AsyncInfoBloque().execute();
            */
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
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);

                    Catbanco entidad = new Catbanco();
                    entidad.setBancoid(item.getProperty("Bancoid").toString());
                    entidad.setCodigo(Integer.parseInt(item.getProperty("Codigo").toString()));
                    entidad.setNombre(item.getProperty("Nombre").toString().trim());
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
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);

                    Catformasdepago entidad = new Catformasdepago();
                    entidad.setFormadepagoid(Integer.parseInt(item.getProperty("Formadepagoid").toString()));
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
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
                SoapObject listaElementos = (SoapObject) respuesta.getProperty(0);
                for (int i = 0; i < listaElementos.getPropertyCount(); i++) {
                    SoapObject item = (SoapObject) listaElementos.getProperty(i);

                    Catperiodosdepago entidad = new Catperiodosdepago();
                    entidad.setPeriododepagoid(Integer.parseInt(item.getProperty("Periododepagoid").toString()));
                    entidad.setDescripcion(item.getProperty("Descripcion").toString());
                    ListaCatperiodosdepago.add(entidad);

                }
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
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
    //Endregion


    //Region CARD IO
    public void onScanPress(View v) {
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
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
                txtNumeroTarjetaCLABE.setText(scanResult.getRedactedCardNumber());
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

    //Region Estatus Solicitud y Bloqueos
    private class AsyncEstatusSolicitud extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            while (true) {
                try {
                    GetEstatusSolicitud();
                    if(Bloqueos.getBloqueoid()!=0)
                        break;
                    Thread.sleep(30000);
                } catch (InterruptedException ex) {
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
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
}
