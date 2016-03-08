package com.tativo.app.tativo.Bloques.Actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tativo.app.tativo.R;

public class Act_B1_Referencias extends AppCompatActivity {

    Button btnReferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_b1_referencias);

        btnReferencias = (Button) findViewById(R.id.btnReferencias);

        btnReferencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Act_B2_Personal.class);
                startActivity(i);
                finish();
            }
        });
    }
}
