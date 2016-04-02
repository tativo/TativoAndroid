package com.tativo.app.tativo.Bloques.Actividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.tativo.app.tativo.R;


public class Act_Mensajes extends AppCompatActivity {

    TextView lblMSJTitulo, lblMSJTexto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mensajes);
        LoadFormControls();
        Bundle bundle = getIntent().getExtras();
        lblMSJTitulo.setText(bundle.getString("Titulo"));
        lblMSJTexto.setText(bundle.getString("Texto"));
    }

    private void LoadFormControls() {
        lblMSJTitulo = (TextView) findViewById(R.id.lblMSJTitulo);
        lblMSJTexto = (TextView) findViewById(R.id.lblMSJTexto);
    }
}
