package com.tativo.app.tativo.LogIn.Actividades;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.Globals;
import com.tativo.app.tativo.Utilidades.Utilerias;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

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

                createLoginDialogo();

                //Intent i = new Intent(getApplicationContext(), Act_LogIn.class);
                //startActivity(i);
                //finish();
            }
        });
    }

    public AlertDialog createLoginDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        View v = inflater.inflate(R.layout.requisitos, null);

        builder.setView(v);

        Button ir = (Button) v.findViewById(R.id.btnIrARegistro);

        ir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Act_LogIn.class);
                startActivity(i);
                finish();
            }
        });

        return builder.create();
    }
}
