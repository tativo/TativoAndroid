package com.tativo.app.tativo.LogIn.Fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tativo.app.tativo.Bloques.Actividades.Act_B1_Referencias;
import com.tativo.app.tativo.R;

/**
 * Created by SISTEMAS1 on 29/02/2016.
 */
public class Frg_Registro extends Fragment {

    public Frg_Registro()
    {

    }

    Button btnRegistro;

    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.frg_registro,container,false);

        btnRegistro = (Button) v.findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Act_B1_Referencias.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        return v;
    }
}
