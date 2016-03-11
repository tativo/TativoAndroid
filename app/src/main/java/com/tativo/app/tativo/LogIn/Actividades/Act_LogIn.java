package com.tativo.app.tativo.LogIn.Actividades;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.tativo.app.tativo.LogIn.Fragmentos.Frg_LogIn;
import com.tativo.app.tativo.LogIn.Fragmentos.Frg_Registro;
import com.tativo.app.tativo.R;

public class Act_LogIn extends FragmentActivity {

    Button btnfrgLogIn, btnfrgRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        btnfrgLogIn = (Button) findViewById(R.id.frgIniciarSesion);
        btnfrgRegistrar = (Button) findViewById(R.id.frgRegistrarse);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Frg_LogIn newFragment = new Frg_LogIn();
        transaction.replace(R.id.flContenedor, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        btnfrgLogIn.setBackgroundColor(getResources().getColor(R.color.colorAzulSeleccion));
        btnfrgRegistrar.setBackgroundColor(getResources().getColor(R.color.colorAzulOscuro));

        btnfrgLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnfrgLogIn.setBackgroundColor(getResources().getColor(R.color.colorAzulSeleccion));
                btnfrgRegistrar.setBackgroundColor(getResources().getColor(R.color.colorAzulOscuro));

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Frg_LogIn newFragment = new Frg_LogIn();
                transaction.replace(R.id.flContenedor, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        btnfrgRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnfrgLogIn.setBackgroundColor(getResources().getColor(R.color.colorAzulOscuro));
                btnfrgRegistrar.setBackgroundColor(getResources().getColor(R.color.colorAzulSeleccion));

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Frg_Registro newFragment = new Frg_Registro();
                transaction.replace(R.id.flContenedor, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
