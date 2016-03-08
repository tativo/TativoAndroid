package com.tativo.app.tativo.Bloques.Actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tativo.app.tativo.R;

public class Act_B4_Laboral extends AppCompatActivity {

    Button btnLaboral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_b4_laboral);

        btnLaboral = (Button) findViewById(R.id.btnLaboral);

        btnLaboral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Act_B5_General.class);
                startActivity(i);
                finish();
            }
        });
    }
}
