package com.tativo.app.tativo.Operaciones.Actividades;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tativo.app.tativo.Bloques.Clases.DatosSolicitud;
import com.tativo.app.tativo.Operaciones.Clases.DatosPerfilCliente;
import com.tativo.app.tativo.Operaciones.Clases.HistorialOperacion;
import com.tativo.app.tativo.Operaciones.Fragmentos.Frg_Cotizador;
import com.tativo.app.tativo.Operaciones.Fragmentos.Frg_Nav;
import com.tativo.app.tativo.Operaciones.Fragmentos.Frg_Perfil;
import com.tativo.app.tativo.Operaciones.Fragmentos.Frg_ResumenOperacion;
import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.Globals;
import com.tativo.app.tativo.Utilidades.ServiciosSoap;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Act_Perfil extends AppCompatActivity implements Frg_ResumenOperacion.DialogResponseResumenOperacion {

    private DrawerLayout drawerLayout;
    private String drawerTitle;
    Globals Sesion;
    //DatosSolicitud Solicitud = new DatosSolicitud();
    DatosPerfilCliente datosPerfilCliente = new DatosPerfilCliente();
    HistorialOperacion datosOperacionActual = new HistorialOperacion();
    //AsyncEstatusSolicitud EstatusSolicitud = new AsyncEstatusSolicitud();

    Bundle sIntanceState;

    @Override
    public void onBackPressed() {

        //super.onBackPressed();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_perfil);

        sIntanceState = savedInstanceState;
        Sesion = (Globals) getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new AsyncGetPerfilHistorialCliente().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new AsyncGetPerfilHistorialCliente().execute();
        }

        setToolbar(); // Setear Toolbar como action bar

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private void setToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);

        }
    }
    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Marcar item presionado
                        menuItem.setChecked(true);
                        // Crear nuevo fragmento
                        String title = menuItem.getTitle().toString();
                        selectItem(title);
                        return true;
                    }
                }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //AGREGA EL MENU SUPERIOR-DERECHA
            //getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectItem(String title) {
        // Enviar título como arguemento del fragmento

        Bundle args = new Bundle();
        Fragment fragment = null;
        if(title.equals("Perfil"))
        {
            args.putString(Frg_Perfil.ARG_SECTION_TITLE, title);
            fragment = Frg_Perfil.newInstance(title);
        }
        else if(title.equals("Cotizador"))
        {
            args.putString(Frg_Cotizador.ARG_SECTION_TITLE, title);
            fragment = Frg_Cotizador.newInstance(title);
        }
        else
        {
            args.putString(Frg_Nav.ARG_SECTION_TITLE, title);
            fragment = Frg_Nav.newInstance(title);
        }

        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.navconten, fragment)
                .commit();

        drawerLayout.closeDrawers(); // Cerrar drawer
        setTitle(title); // Setear título actual

    }


/*
    //Region Estatus Solicitud
    private class AsyncEstatusSolicitud extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetEstatusSolicitud();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
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

                    //Llenamos los datos de la solicitud
                    Solicitud.setEstatusCliente(datosSolicitud.getProperty("EstatusCliente").toString());
                    Solicitud.setPagareEnviado(Boolean.parseBoolean(datosSolicitud.getProperty("PagareEnviado").toString()));
                    Solicitud.setPagareAceptado(Boolean.parseBoolean(datosSolicitud.getProperty("PagareAceptado").toString()));
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
                    Solicitud.setPinEnviado(Boolean.parseBoolean(datosSolicitud.getProperty("PinEnviado").toString()));
                    Solicitud.setPin(datosSolicitud.getProperty("Pin").toString());
                }else{
                    //Toast.makeText(getApplicationContext(),"Error: "+respuesta.getProperty("Mensaje").toString(),Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    //Region Estatus Solicitud
*/


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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        if(datosPerfilCliente.isSolicitudActiva())
        {
                drawerTitle = "Perfil";
                navigationView.setCheckedItem(R.id.nav_Perfil);
        }
        else
        {
            if(datosOperacionActual.getSolicitudid() != null) {
                drawerTitle = "Perfil";
                navigationView.setCheckedItem(R.id.nav_Perfil);
            }
            else {
                drawerTitle = "Cotizador";
                navigationView.setCheckedItem(R.id.nav_Cotizador);
            }
        }

        if (sIntanceState == null) {
            selectItem(drawerTitle);
        }
    }
    //Consultar GetPerfilHistorialCliente

    @Override
    public void onPossitiveButtonClick() {
        Bundle args = new Bundle();
        Fragment fragment = null;
        args.putString(Frg_Perfil.ARG_SECTION_TITLE, "Perfil");
        fragment = Frg_Perfil.newInstance("Perfil");

        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.navconten, fragment)
                .commit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new AsyncGetPerfilHistorialCliente().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new AsyncGetPerfilHistorialCliente().execute();
        }

    }

    @Override
    public void onNegativeButtonClick() {

        Bundle args = new Bundle();
        Fragment fragment = null;
        args.putString(Frg_Perfil.ARG_SECTION_TITLE, "Perfil");
        fragment = Frg_Perfil.newInstance("Perfil");

        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.navconten, fragment)
                .commit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new AsyncGetPerfilHistorialCliente().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new AsyncGetPerfilHistorialCliente().execute();
        }

        Toast.makeText(
                this,
                "No se aceptaron los terminos",
                Toast.LENGTH_LONG)
                .show();
    }

}
