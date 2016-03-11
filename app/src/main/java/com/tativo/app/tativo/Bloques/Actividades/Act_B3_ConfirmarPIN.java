package com.tativo.app.tativo.Bloques.Actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tativo.app.tativo.R;

public class Act_B3_ConfirmarPIN extends AppCompatActivity {

    Button btnConfirmarPIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_b3_confirmarpin);

        btnConfirmarPIN = (Button) findViewById(R.id.btnConfirmarPIN);

        btnConfirmarPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Act_B4_Laboral.class);
                startActivity(i);
                finish();
            }
        });

    }
}
