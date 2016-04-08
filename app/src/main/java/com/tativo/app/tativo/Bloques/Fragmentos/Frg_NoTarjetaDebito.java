package com.tativo.app.tativo.Bloques.Fragmentos;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.tativo.app.tativo.LogIn.Actividades.Act_LogIn;
import com.tativo.app.tativo.R;

/**
 * Created by SISTEMAS1 on 06/04/2016.
 */
public class Frg_NoTarjetaDebito extends DialogFragment {
    private static final String TAG = Frg_NoTarjetaDebito.class.getSimpleName();

    public Frg_NoTarjetaDebito() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createRequisitos();
    }

    /**
     * Crea un diálogo con personalizado para comportarse
     * como formulario de login
     *
     * @return Diálogo
     */
    public AlertDialog createRequisitos() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.frg_notarjetadebito, null);

        builder.setView(v);

        Button ir = (Button) v.findViewById(R.id.btnNTDCerrar);

        ir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        return builder.create();
    }
}
