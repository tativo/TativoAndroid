package com.tativo.app.tativo.Operaciones.Fragmentos;

import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tativo.app.tativo.Bloques.Clases.Cattasasfinanciamiento;
import com.tativo.app.tativo.LogIn.Clases.CatFechasPago;
import com.tativo.app.tativo.LogIn.Fragmentos.Frg_LogIn;
import com.tativo.app.tativo.Operaciones.Clases.DatosPerfilCliente;
import com.tativo.app.tativo.Operaciones.Clases.HistorialOperacion;
import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.Globals;
import com.tativo.app.tativo.Utilidades.ServiciosSoap;
import com.tativo.app.tativo.Utilidades.Utilerias;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by SISTEMAS1 on 09/04/2016.
 */
public class Frg_Cotizador extends Fragment {

    public static final String ARG_SECTION_TITLE = "section_number";

    public static Frg_Cotizador newInstance(String sectionTitle) {
        Frg_Cotizador fragment = new Frg_Cotizador();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_TITLE, sectionTitle);
        fragment.setArguments(args);
        return fragment;
    }

    public Frg_Cotizador() {
    }

    View v;

    EditText txtRecotizarImporte, txtRecotizarFecha;
    TextView lblRecotizarImporte, lblRecotizarComision, lblRecotizarTotal, lblRecotizarFechaCompromiso, lblMultiplos;
    Button btnRecotizarSolicita, btnRecotizarPerfil;

    LinearLayout lyRecotizar, lyOperacionActiva;

    ProgressDialog progressDialog;
    Cattasasfinanciamiento TasaFinanciamiento = new Cattasasfinanciamiento();
    static List<CatFechasPago> FechasPago = new ArrayList<CatFechasPago>();
    List<Double> Importes = new ArrayList<Double>();

    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    static Integer DiasUso = 1;

    static Globals Sesion;

    DatosPerfilCliente datosPerfilCliente = new DatosPerfilCliente();
    HistorialOperacion datosOperacionActual = new HistorialOperacion();

    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.frg_cotizador,container,false);
        Sesion = (Globals) getActivity().getApplicationContext();
        LoadFormControls();
        EventManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new AsyncGetPerfilHistorialCliente().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new AsyncGetPerfilHistorialCliente().execute();
        }

        return v;
    }

    private void LoadFormControls() {
        nf.setMaximumFractionDigits(2);

        final Calendar cal = Calendar.getInstance();

        lyRecotizar = (LinearLayout) v.findViewById(R.id.lyRecotizar);
        lyOperacionActiva = (LinearLayout) v.findViewById(R.id.lyOperacionActiva);

        txtRecotizarImporte = (EditText) v.findViewById(R.id.txtRecotizarImporte);
        txtRecotizarFecha = (EditText) v.findViewById(R.id.txtRecotizarFecha);

        lblRecotizarImporte = (TextView) v.findViewById(R.id.lblRecotizarImporte);
        lblRecotizarComision = (TextView) v.findViewById(R.id.lblRecotizarComision);
        lblRecotizarTotal = (TextView) v.findViewById(R.id.lblRecotizarTotal);
        lblRecotizarFechaCompromiso = (TextView) v.findViewById(R.id.lblRecotizarFechaCompromiso);
        lblMultiplos = (TextView) v.findViewById(R.id.lblMultiplos);

        btnRecotizarSolicita = (Button) v.findViewById(R.id.btnRecotizarSolicita);
        btnRecotizarPerfil = (Button) v.findViewById(R.id.btnRecotizarPerfil);

        progressDialog = new ProgressDialog(getActivity());
    }

    private void EventManager() {
        txtRecotizarFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment picker = new DatePickerFragment();
                picker.show(getActivity().getFragmentManager(),"datePicker");
            }
        });


        txtRecotizarFecha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CalculaInteres(txtRecotizarImporte.getText().toString());
            }
        });

        txtRecotizarImporte.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                CalculaInteres(s.toString());
                //txtRecotizarImporte.setText(nf.format(s.toString()));
            }
        });

        btnRecotizarSolicita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new AsyncGuardaSolicitud().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    new AsyncGuardaSolicitud().execute();
                }
            }
        });

        btnRecotizarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                Fragment fragment = null;
                args.putString(Frg_Perfil.ARG_SECTION_TITLE, "Perfil");
                fragment = Frg_Perfil.newInstance("Perfil");

                fragment.setArguments(args);
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.navconten, fragment)
                        .commit();
            }
        });
    }

    private  void CalculaInteres(String s) {

        Double importe;
        if(s.toString() ==  null || s.toString().equals(""))
            importe = 0.0;
        else
            importe = Double.parseDouble(s.toString());

        if (importe > 0 && importe <= 2000)
        {
            Double resto = importe % 100;
            if (resto != 0)
            {
                btnRecotizarSolicita.setEnabled(false);
                btnRecotizarSolicita.setBackgroundColor(getResources().getColor(R.color.colorGris));
                lblMultiplos.setTextColor(Color.RED);
            }
            else
            {
                btnRecotizarSolicita.setEnabled(true);
                btnRecotizarSolicita.setBackgroundColor(getResources().getColor(R.color.colorVerde));
                lblMultiplos.setTextColor(Color.BLACK);
            }
            Sesion.setImporteSolicitado(importe);

            Double CostoDiario = (importe * TasaFinanciamiento.getFactorinteresordinario());
            Double Comision = CostoDiario * DiasUso;
            Double IVA = (Comision * 1.16) - Comision;

            lblRecotizarImporte.setText(nf.format(importe));
            lblRecotizarComision.setText(nf.format((Comision+IVA)));
            lblRecotizarTotal.setText(nf.format((importe+Comision+IVA)));
        }
        else
        {
            btnRecotizarSolicita.setEnabled(false);
            btnRecotizarSolicita.setBackgroundColor(getResources().getColor(R.color.colorGris));
            lblMultiplos.setTextColor(Color.RED);
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            String fecha;
            Date MaxDate = null;
            Date MinDate = null;

            try
            {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                //format.setTimeZone(TimeZone.getTimeZone("UTC"));

                fecha =  (day + 30)  + "/" + (month + 1) + "/" + year;
                MaxDate = format.parse(fecha);

                fecha =  (day + 1)  + "/" + (month + 1) + "/" + year;
                MinDate = format.parse(fecha);

                DatePickerDialog dp = new DatePickerDialog(getActivity(), this, year, month, (day+1));
                dp.getDatePicker().setMaxDate(MaxDate.getTime());
                dp.getDatePicker().setMinDate(MinDate.getTime());
                //dp.setCancelable(true);
                //dp.getDatePicker().setCalendarViewShown(false);
                //dp.getDatePicker().setSpinnersShown(false);

                // Create a new instance of DatePickerDialog and return it
                return  dp;
            }
            catch (Exception ex)
            {
                return  null;
            }
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            int DiaSemana = c.get(c.DAY_OF_WEEK);
            Sesion.setFechaPago(c.getTime());

            long MaxDias = (view.getMaxDate() / 1000 / 60 / 60 / 24);
            long MinDias = (view.getMinDate() / 1000 / 60 / 60 / 24);
            long Dias = (c.getTimeInMillis() / 1000 / 60 / 60 / 24);

            TextView txtRF = (TextView) getActivity().findViewById(R.id.txtRecotizarFecha);
            TextView txtDU = (TextView) getActivity().findViewById(R.id.lblRecotizarFechaCompromiso);

            //SI ESTA FUERA DE RANGO
            if (Dias < MinDias || Dias > MaxDias)
            {
                Toast.makeText(getActivity().getApplicationContext(),"Día fuera de rango",Toast.LENGTH_LONG).show();
            }
            else {
                if(DiaSemana != 1)
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
                    //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String formattedDate = sdf.format(c.getTime());

                    int index = -1;
                    for (int i = 0; i < FechasPago.size(); i++) {
                        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        //s.setTimeZone(TimeZone.getTimeZone("UTC"));
                        String sd = s.format(FechasPago.get(i).getFechaPago());
                        if ( sd.equals(formattedDate)) {
                            index = i;
                        }
                    }

                    if (index >= 0)
                    {
                        DiasUso = FechasPago.get(index).getDiasDeUso();
                        SimpleDateFormat fc = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                        //fc.setTimeZone(TimeZone.getTimeZone("UTC"));
                        String sfc = fc.format(c.getTime());

                        txtDU.setText("A pagar el " + sfc + " (" + DiasUso + " días)" );
                        txtRF.setText(formattedDate);
                    }
                    else
                        Toast.makeText(getActivity().getApplicationContext(),"Día inhábil para pago",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(),"Día inhábil para pago",Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    //Consultar GetPerfilHistorialCliente
    private class AsyncGetPerfilHistorialCliente extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetPerfilHistorialCliente();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            SetPerfilHitorialCliente();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    private void GetPerfilHistorialCliente(){
        String SOAP_ACTION = "http://tempuri.org/IService1/GetPerfilHistorialCliente";
        String METHOD_NAME = "GetPerfilHistorialCliente";
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
                    SoapObject datosPefil = (SoapObject) Datos.getProperty("PerfilCliente");
                    SoapObject sOA = (SoapObject) Datos.getProperty("OperacionActual");

                    datosPerfilCliente.setSolicitudActiva(Boolean.parseBoolean(datosPefil.getProperty("SolicitudActiva").toString()));

                    SoapObject sOperacionActual = null;
                    if(sOA.getPropertyCount() > 0)
                    {
                        sOperacionActual = (SoapObject) sOA.getProperty(0);
                        datosOperacionActual.setSolicitudid(sOperacionActual.getProperty("Solicitudid").toString());
                    }


                }else{
                    //Toast.makeText(getApplicationContext(),"Error: "+respuesta.getProperty("Mensaje").toString(),Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    private void SetPerfilHitorialCliente(){

        if(datosPerfilCliente.isSolicitudActiva())
        {
            lyRecotizar.setVisibility(View.GONE);
            lyOperacionActiva.setVisibility(View.VISIBLE);
        }
        else
        {
            if(datosOperacionActual.getSolicitudid() != null) {
                lyRecotizar.setVisibility(View.GONE);
                lyOperacionActiva.setVisibility(View.VISIBLE);
            }
            else {
                lyRecotizar.setVisibility(View.VISIBLE);
                lyOperacionActiva.setVisibility(View.GONE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new AsyncLoadData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    new AsyncLoadData().execute();
                }
            }
        }
    }
    //Consultar GetPerfilHistorialCliente

    //CargaDatos
    private class AsyncLoadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetDatosSpinner();
            GetTasasFinanciamiento();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            /*
            spnFechaPago.setViewAdapter(new FechaPagoAdapter(getApplicationContext(), FechasPago));
            spnImporte.setViewAdapter(new ImporteAdapter(getApplicationContext(), Importes));
            spnImporte.setCurrentItem(Importes.indexOf(2000.00));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                imageHandler.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                imageHandler.execute();
            }*/
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
    private void GetDatosSpinner(){
        String SOAP_ACTION = "http://tempuri.org/IService1/GetDatosCotizador";
        String METHOD_NAME = "GetDatosCotizador";
        String NAMESPACE = "http://tempuri.org/";

        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION,METHOD_NAME,NAMESPACE,null);
        if (respuesta != null) {
            try {
                if (Boolean.parseBoolean(respuesta.getProperty("EsValido").toString())) {
                    SoapObject Datos = (SoapObject) respuesta.getProperty("Datos");
                    SoapObject DatosFechasPago = (SoapObject) Datos.getProperty("FechasPago");
                    SoapObject DatosImportes = (SoapObject) Datos.getProperty("Importes");
                    for (int i = 0; i < DatosFechasPago.getPropertyCount(); i++) {

                        SoapObject itemFechaPago = (SoapObject) DatosFechasPago.getProperty(i);
                        CatFechasPago fechaPago= new CatFechasPago();
                        fechaPago.setFechaPago(Utilerias.stringToDate(itemFechaPago.getProperty("FechaPago").toString().substring(0, 10)));
                        fechaPago.setDiasDeUso(Integer.parseInt(itemFechaPago.getProperty("DiasDeUso").toString()));
                        FechasPago.add(fechaPago);
                    }
                    for (int i = 0; i < DatosImportes.getPropertyCount(); i++) {
                        Double importe = Double.parseDouble(DatosImportes.getProperty(i).toString());
                        Importes.add(importe);
                    }
                }
            }catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    private void GetTasasFinanciamiento(){
        String SOAP_ACTION = "http://tempuri.org/IService1/GetTasasFinanciamiento";
        String METHOD_NAME = "GetTasasFinanciamiento";
        String NAMESPACE = "http://tempuri.org/";

        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION,METHOD_NAME,NAMESPACE,null);
        if (respuesta != null) {
            try {
                if (Boolean.parseBoolean(respuesta.getProperty("EsValido").toString())) {
                    SoapObject Datos = (SoapObject) respuesta.getProperty("Datos");
                    TasaFinanciamiento.setTasafinanciamientoid(Datos.getProperty("Tasafinanciamientoid").toString());
                    TasaFinanciamiento.setFactorinteresordinario(Double.parseDouble(Datos.getProperty("Factorinteresordinario").toString()));
                    TasaFinanciamiento.setFactormoratorio(Double.parseDouble(Datos.getProperty("Factormoratorio").toString()));
                    TasaFinanciamiento.setIVA(Double.parseDouble(Datos.getProperty("IVA").toString()));
                }
            }catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    //CargaDatos


    //Region Guardar
    private class AsyncGuardaSolicitud extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GuardarSolicitud();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();

            FragmentManager fragmento = getActivity().getFragmentManager();
            Frg_ResumenOperacion ofra = new Frg_ResumenOperacion();
            ofra.setCancelable(false);
            ofra.show(fragmento,"frmResumenOperacion");
            //new Frg_ResumenOperacion().show(fragmento,"frmResumenOperacion");

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
    private void GuardarSolicitud(){
        String SOAP_ACTION = "http://tempuri.org/IService1/RegistrarSolicitudEfectivo";
        String METHOD_NAME = "RegistrarSolicitudEfectivo";
        String NAMESPACE = "http://tempuri.org/";

        ArrayList<PropertyInfo> valores =  new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("ClienteID");
        pi1.setValue(Sesion.getCliendeID());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        pi1 = new PropertyInfo();
        pi1.setName("FechaVencimiento");
        pi1.setValue(android.text.format.DateFormat.format("yyyy-MM-dd",Sesion.getFechaPago()).toString());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        pi1 = new PropertyInfo();
        pi1.setName("Importe");
        pi1.setValue(String.valueOf(Sesion.getImporteSolicitado()));
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION, METHOD_NAME, NAMESPACE,valores);
        if(respuesta != null) {
            try {
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
                if (ev)
                {
                    SoapObject item = (SoapObject) respuesta.getProperty(0);
                    Sesion.setSolicitudID(item.getProperty("Solicitudid").toString());
                }
            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    //Region Guardar

}
