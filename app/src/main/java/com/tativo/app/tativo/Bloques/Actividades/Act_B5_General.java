package com.tativo.app.tativo.Bloques.Actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tativo.app.tativo.R;

public class Act_B5_General extends AppCompatActivity {

    Button btnInfGeneral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_b5_general);

        btnInfGeneral = (Button) findViewById(R.id.btnInfGeneral);

        btnInfGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Act_Documentos.class);
                startActivity(i);
                finish();
            }
        });
    }
}
