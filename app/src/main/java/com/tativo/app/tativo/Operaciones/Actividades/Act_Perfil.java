package com.tativo.app.tativo.Operaciones.Actividades;

import android.graphics.Color;
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

import com.tativo.app.tativo.Operaciones.Fragmentos.Frg_Cotizador;
import com.tativo.app.tativo.Operaciones.Fragmentos.Frg_Nav;
import com.tativo.app.tativo.Operaciones.Fragmentos.Frg_Perfil;
import com.tativo.app.tativo.R;

public class Act_Perfil extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private String drawerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_perfil);

        setToolbar(); // Setear Toolbar como action bar

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        drawerTitle = "Perfil";
        if (savedInstanceState == null) {
            selectItem(drawerTitle);
        }
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

    private void setupDrawerContent(NavigationView navigationView) {
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
}
