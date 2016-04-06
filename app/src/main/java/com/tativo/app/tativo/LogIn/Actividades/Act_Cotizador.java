package com.tativo.app.tativo.LogIn.Actividades;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tativo.app.tativo.LogIn.Fragmentos.Frg_Requisitos;
import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.Globals;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Act_Cotizador extends AppCompatActivity {

       
    Button btnListo;
    TextView lblTitulo1,lblTitulo2,lblTitulo3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cotizador);

        lblTitulo1 = (TextView) findViewById(R.id.lblTitulo1);
        lblTitulo1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/SFDigitalReadout-Light.ttf"));
        lblTitulo1.setText("¡CUMPLE");

        lblTitulo2 = (TextView) findViewById(R.id.lblTitulo2);
        lblTitulo2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/SFDigitalReadout-Light.ttf"));
        lblTitulo2.setText("TUS DESEOS");

        lblTitulo3 = (TextView) findViewById(R.id.lblTitulo3);
        lblTitulo3.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/SFDigitalReadout-Light.ttf"));
        lblTitulo3.setText("AQUI!");

        btnListo = (Button) findViewById(R.id.btnListo);
        btnListo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fecha ="01/05/2016";
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date newDate = null;
                try {
                    newDate = format.parse(fecha);
                }
                catch (Exception ex)
                {}

                Globals g = (Globals)getApplicationContext();
                g.setImporteSolicitado(2000);
                g.setFechaPago(newDate);

                FragmentManager fragmentManager = getFragmentManager();
                new Frg_Requisitos().show(fragmentManager,"frmRequisitos");

                //Intent i = new Intent(getApplicationContext(), Act_LogIn.class);
                //startActivity(i);
                //finish();
            }
        });
    }
}
