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

        btnfrgLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Frg_Registro newFragment = new Frg_Registro();
                transaction.replace(R.id.flContenedor, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
