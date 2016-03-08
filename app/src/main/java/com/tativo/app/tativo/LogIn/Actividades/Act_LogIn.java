package com.tativo.app.tativo.LogIn.Actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tativo.app.tativo.Bloques.Actividades.Act_B1_Referencias;
import com.tativo.app.tativo.R;

public class Act_LogIn extends AppCompatActivity {

    Button btnRegistro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        btnRegistro = (Button) findViewById(R.id.btnRegistro);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Act_B1_Referencias.class);
                startActivity(i);
                finish();
            }
        });
    }
}
