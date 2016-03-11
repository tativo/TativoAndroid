package com.tativo.app.tativo.Bloques.Actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tativo.app.tativo.R;

public class Act_B3_InfDeposito extends AppCompatActivity {

    Button btnDatosDeposito, btnCardIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_b3_infdeposito);

        btnDatosDeposito = (Button) findViewById(R.id.btnDatosDeposito);
        btnCardIO = (Button) findViewById(R.id.btnCardIO);

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
                Toast.makeText(getApplicationContext(),"Escanear tarjeta",Toast.LENGTH_LONG).show();
            }
        });

    }
}
