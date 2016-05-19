package com.tativo.app.tativo.LogIn.Actividades;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tativo.app.tativo.Bloques.Clases.Cattasasfinanciamiento;
import com.tativo.app.tativo.LogIn.Clases.CatFechasPago;
import com.tativo.app.tativo.LogIn.Fragmentos.Frg_Requisitos;

import com.tativo.app.tativo.R;

import com.tativo.app.tativo.Utilidades.Globals;
import com.tativo.app.tativo.Utilidades.ServiciosSoap;
import com.tativo.app.tativo.Utilidades.Utilerias;

import org.ksoap2.serialization.SoapObject;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

public class Act_Cotizador_r extends AppCompatActivity {

    LinearLayout divPantalla,divPantallaTitulo,divPantallaImagen1,divPantallaImagen2,divPantallaCotizacion;
    TextView lblTitulo1,lblTitulo2,lblTitulo3,lblQuiero,lblPago,lblCotizacion1,lblCotizacion2,lblCotizacion3,lblCotizacion4,lblCotizacion5,lblCotizacion6;
    WheelView spnImporte,spnFechaPago;
    Button btnListo;
    ImageButton btnCalendario;

    List<CatFechasPago> FechasPago = new ArrayList<CatFechasPago>();
    List<Double> Importes = new ArrayList<Double>();

    ProgressDialog progressDialog;
    Globals Sesion;
    Cattasasfinanciamiento TasaFinanciamiento = new Cattasasfinanciamiento();
    boolean touchImporte=false;

    int year_x, month_x, day_x;
    //static final int DILOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cotizador_r);

        LoadFormControls();
        EventManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new AsyncLoadData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new AsyncLoadData().execute();
        }
    }

    //Region Carga Controles Pantalla y manejador de eventos
    private void LoadFormControls()
    {
        progressDialog = new ProgressDialog(this);

        divPantalla = (LinearLayout) findViewById(R.id.divPantalla);
        divPantallaTitulo = (LinearLayout) findViewById(R.id.divPantallaTitulo);
        divPantallaImagen1= (LinearLayout) findViewById(R.id.divPantallaImagen1);
        divPantallaImagen2= (LinearLayout) findViewById(R.id.divPantallaImagen2);
        divPantallaCotizacion = (LinearLayout) findViewById(R.id.divPantallaCotizacion);
        divPantallaTitulo.setVisibility(View.GONE);
        divPantallaImagen2.setVisibility(View.GONE);
        divPantallaCotizacion.setVisibility(View.GONE);

        lblTitulo1 = (TextView) findViewById(R.id.lblTitulo1);
        lblTitulo1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/SFDigitalReadout-Heavy.ttf"));
        lblTitulo1.setText("¡CUMPLE");

        lblTitulo2 = (TextView) findViewById(R.id.lblTitulo2);
        lblTitulo2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/SFDigitalReadout-Heavy.ttf"));
        lblTitulo2.setText("TUS DESEOS");

        lblTitulo3 = (TextView) findViewById(R.id.lblTitulo3);
        lblTitulo3.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/SFDigitalReadout-Heavy.ttf"));
        lblTitulo3.setText("AQUI!");


        lblCotizacion1 = (TextView) findViewById(R.id.lblCotizacion1);
        lblCotizacion1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/SFDigitalReadout-Heavy.ttf"));

        lblCotizacion2 = (TextView) findViewById(R.id.lblCotizacion2);
        lblCotizacion2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/SFDigitalReadout-Heavy.ttf"));

        lblCotizacion3 = (TextView) findViewById(R.id.lblCotizacion3);
        lblCotizacion3.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/SFDigitalReadout-Heavy.ttf"));

        lblCotizacion4 = (TextView) findViewById(R.id.lblCotizacion4);
        lblCotizacion4.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/SFDigitalReadout-Heavy.ttf"));

        lblCotizacion5 = (TextView) findViewById(R.id.lblCotizacion5);
        lblCotizacion5.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/SFDigitalReadout-Heavy.ttf"));

        lblCotizacion6 = (TextView) findViewById(R.id.lblCotizacion6);
        lblCotizacion6.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/SFDigitalReadout-Heavy.ttf"));

        lblQuiero = (TextView) findViewById(R.id.lblQuiero);
        lblQuiero.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/HindVadodara-SemiBold.ttf"));

        lblPago = (TextView) findViewById(R.id.lblPago);
        lblPago.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/HindVadodara-SemiBold.ttf"));

        //Spinner
        spnFechaPago = (WheelView) findViewById(R.id.spnFechaPago);
        spnFechaPago.setDrawShadows(false);
        spnFechaPago.setViewAdapter(new FechaPagoAdapter(getApplicationContext(), FechasPago));

        spnImporte = (WheelView) findViewById(R.id.spnImporte);
        spnImporte.setDrawShadows(false);
        spnImporte.setViewAdapter(new ImporteAdapter(getApplicationContext(), Importes));

        btnListo = (Button) findViewById(R.id.btnListo);
        btnCalendario = (ImageButton) findViewById(R.id.btnCalendario);

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

    }
    private void EventManager()
    {

        spnImporte.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheelView, int i, int i1) {
                if(imageHandler!=null && imageHandler.getStatus() == AsyncTask.Status.RUNNING) {
                    imageHandler.cancel(true);
                }
                if(touchImporte)
                    CalcularInteres(Importes.get(spnImporte.getCurrentItem()),FechasPago.get(spnFechaPago.getCurrentItem()).getFechaPago(),FechasPago.get(spnFechaPago.getCurrentItem()).getDiasDeUso());
            }
        });
        spnImporte.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchImporte=true;
                return false;
            }
        });
        spnFechaPago.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheelView, int i, int i1) {
                if(imageHandler!=null && imageHandler.getStatus() == AsyncTask.Status.RUNNING) {
                    imageHandler.cancel(true);
                }
                CalcularInteres(Importes.get(spnImporte.getCurrentItem()),FechasPago.get(spnFechaPago.getCurrentItem()).getFechaPago(),FechasPago.get(spnFechaPago.getCurrentItem()).getDiasDeUso());
            }
        });

        btnListo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals g = (Globals) getApplicationContext();
                g.setImporteSolicitado(Importes.get(spnImporte.getCurrentItem()));
                g.setFechaPago(FechasPago.get(spnFechaPago.getCurrentItem()).getFechaPago());
                FragmentManager fragmento = getFragmentManager();
                new Frg_Requisitos().show(fragmento,"frmRequisitos");
            }
        });

        btnCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showDialog(DILOG_ID);
                showDatePicker();
            }
        });

    }
    //End Region

    //Region Manejador de imagenes de la pantalla principal
    int i = 0;
    AsyncImageHandler imageHandler = null;
    private class AsyncImageHandler extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            int i=1;
            while (i<=3) {
                try {

                    if(i==2){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                divPantallaTitulo.setVisibility(View.GONE);
                                divPantallaImagen1.setVisibility(View.GONE);
                                divPantallaCotizacion.setVisibility(View.GONE);
                                divPantallaImagen2.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                    if(i==3){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                divPantallaTitulo.setVisibility(View.VISIBLE);
                                divPantallaCotizacion.setVisibility(View.GONE);
                                divPantallaImagen1.setVisibility(View.GONE);
                                divPantallaImagen2.setVisibility(View.GONE);
                            }
                        });
                    }
                    if(i==1){
                        Thread.sleep(2000);
                    }else{
                        Thread.sleep(1500);
                    }
                    i++;
                } catch (InterruptedException ex) {
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
        }
        @Override
        protected void onCancelled() {
            divPantallaTitulo.setVisibility(View.GONE);
            divPantallaImagen1.setVisibility(View.GONE);
            divPantallaImagen2.setVisibility(View.GONE);
        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    //End Region

    //Region Manejador de Spinner de Importes y Fechas de Pago

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
            spnFechaPago.setViewAdapter(new FechaPagoAdapter(getApplicationContext(), FechasPago));
            spnImporte.setViewAdapter(new ImporteAdapter(getApplicationContext(), Importes));
            spnImporte.setCurrentItem(Importes.indexOf(2000.00));
            imageHandler =   new AsyncImageHandler();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                imageHandler.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                imageHandler.execute();
            }
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
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    private List<Double> getImportes(Double importeMinimo,Double importeMaximo){
        List<Double> datosImporte = new ArrayList<Double>();
        while (importeMinimo<=importeMaximo){
            datosImporte.add(importeMinimo);
            importeMinimo+=100;
        }
        return datosImporte;
    }
    private List<Date> getNextNumberOfDays(Date originalDate, int days){
        List<Date> dates = new ArrayList<>();
        long offset;
        for(int i= 0; i<= days; i++){
            offset = 86400 * 1000L * i;
            Date date = new Date( originalDate.getTime()+offset);
            dates.add(date);
        }
        return dates;
    }

    private class FechaPagoAdapter extends AbstractWheelTextAdapter {

        private final List<CatFechasPago> mDateList;

        public FechaPagoAdapter(Context context, List<CatFechasPago> dateList) {
            super(context, R.layout.wheel_item);
            this.mDateList = dateList;
        }

        @Override
        public View getItem(int index, View convertView, ViewGroup parent) {
            View view = super.getItem(index, convertView, parent);
            TextView item = (TextView) view.findViewById(R.id.itemwheel);
            item.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/HindVadodara-SemiBold.ttf"));
            //Format the date (Name of the day / number of the day)
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMM", Locale.getDefault());
            //Assign the text
            item.setText( Utilerias.LetraCapital(dateFormat.format(mDateList.get(index).getFechaPago())));

            item.setTextColor(Color.BLACK);

            return view;
        }

        @Override
        protected CharSequence getItemText(int i) {
            return "";
        }

        @Override
        public int getItemsCount() {
            if(mDateList != null) {
                return mDateList.size();
            }
            return 0;
        }
    }
    private class ImporteAdapter extends AbstractWheelTextAdapter {

        private final List<Double> ListaDatos;

        public ImporteAdapter(Context context, List<Double> datos) {
            super(context, R.layout.wheel_item);
            this.ListaDatos = datos;
        }

        @Override
        public View getItem(int index, View convertView, ViewGroup parent) {
            View view = super.getItem(index, convertView, parent);
            TextView item = (TextView) view.findViewById(R.id.itemwheel);
            item.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/HindVadodara-SemiBold.ttf"));

            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            nf.setMaximumFractionDigits(2);
            item.setText(nf.format(ListaDatos.get(index)));
            item.setTextColor(Color.BLACK);
            return view;
        }

        @Override
        protected CharSequence getItemText(int i) {
            return "";
        }

        @Override
        public int getItemsCount() {
            if(ListaDatos != null) {
                return ListaDatos.size();
            }
            return 0;
        }
    }
    //End Region

    //Region Calculo de Intereses Diarios
    private void CalcularInteres(Double Importe,Date FechaPago,long DiasDeUso) {
        double costoDiario = Importe * TasaFinanciamiento.getFactorinteresordinario();
        double subtotal = costoDiario * DiasDeUso;
        double iva = (TasaFinanciamiento.getIVA() / 100)+1;
        double total = Utilerias.round(subtotal * iva,2);

        divPantalla.setGravity(Gravity.LEFT);
        divPantallaCotizacion.setVisibility(View.VISIBLE);
        divPantallaTitulo.setVisibility(View.GONE);
        divPantallaImagen1.setVisibility(View.GONE);
        divPantallaImagen2.setVisibility(View.GONE);

        lblCotizacion2.setText("$" + String.valueOf(Importe) + " DE PRÉSTAMO");
        lblCotizacion3.setText("A "+String.valueOf(DiasDeUso)+" DIAS SON");
        lblCotizacion4.setText("$" +String.valueOf(total)+" DE COMISIÓN");
        lblCotizacion6.setText("REGRESARÍAS $"+String.valueOf( Utilerias.round(Importe+total,2)));


    }
    //End Region


//PARA EL CALENDARIO
    boolean isOkayClicked = true;
    private void showDatePicker() {
        // TODO Auto-generated method stub
        final Calendar c = Calendar.getInstance();

        year_x = c.get(Calendar.YEAR);
        month_x = c.get(Calendar.MONTH);
        day_x = c.get(Calendar.DAY_OF_MONTH);

        String fecha;
        Date MaxDate = null;
        Date MinDate = null;

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            //format.setTimeZone(TimeZone.getTimeZone("UTC"));

            fecha =  (day_x + 30)  + "/" + (month_x + 1) + "/" + year_x;
            MaxDate = format.parse(fecha);

            fecha =  (day_x +1) + "/" + (month_x + 1) + "/" + year_x;
            MinDate = format.parse(fecha);
        }
        catch (Exception ex)
        {
        }

        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                /*
                if (isOkayClicked)
                {
                    year_x = selectedYear;
                    month_x = (selectedMonth + 1);
                    day_x = selectedDay;

                    String sfecha = day_x+"/"+month_x+"/"+year_x;
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    ParsePosition pp = new ParsePosition(0);
                    Date d = format.parse(sfecha, pp);

                    int i = getIndexFecha(d);
                    spnFechaPago.setCurrentItem(i);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Dia no valido",Toast.LENGTH_LONG).show();
                }
                */
                isOkayClicked = false;
            }
        };

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, datePickerListener, year_x, month_x, (day_x+1));
        //final DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog, datePickerListener, year_x, month_x, (day_x+1));

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
            "Cancelar",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_NEGATIVE) {
                        dialog.cancel();
                        isOkayClicked = false;
                    }
                }
            });

        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        Calendar n = Calendar.getInstance();
                        Calendar c = Calendar.getInstance();
                        day_x = datePickerDialog.getDatePicker().getDayOfMonth();
                        month_x = datePickerDialog.getDatePicker().getMonth();
                        year_x = datePickerDialog.getDatePicker().getYear();

                        n.set(year_x, month_x , day_x,0,0,0);
                        if(c.getTime().getDate() != n.getTime().getDate())
                        {
                            int dow = n.get(n.DAY_OF_WEEK);
                            if (dow != 1)
                            {
                                isOkayClicked = true;
                                String sfecha = day_x+"/"+(month_x+1)+"/"+year_x;
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                ParsePosition pp = new ParsePosition(0);
                                Date d = format.parse(sfecha, pp);

                                int i = getIndexFecha(d);
                                if (i >= 0)
                                    spnFechaPago.setCurrentItem(i);
                                else
                                    Toast.makeText(getApplicationContext(),"Día inhábil para pago",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                isOkayClicked = false;
                                Toast.makeText(getApplicationContext(),"Día no valido",Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            isOkayClicked = false;
                            Toast.makeText(getApplicationContext(),"Mismo día",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });


        datePickerDialog.setCancelable(true);
        datePickerDialog.getDatePicker().setMaxDate(MaxDate.getTime());
        datePickerDialog.getDatePicker().setMinDate(MinDate.getTime());
        datePickerDialog.getDatePicker().setCalendarViewShown(true);
        datePickerDialog.getDatePicker().setSpinnersShown(false);
        datePickerDialog.show();
    }

    private int getIndexFecha(Date myString) {
        int index = -1;
        for (int i = 0; i < FechasPago.size(); i++) {
            if (FechasPago.get(i).getFechaPago().getDate() == myString.getDate() ) {
                index = i;
            }
        }
        return index;
    }

//PARA EL CALENDARIO

}
