package com.tativo.app.tativo.LogIn.Fragmentos;


import android.app.Activity;
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
import com.tativo.app.tativo.Operaciones.Fragmentos.Frg_Contrato;
import com.tativo.app.tativo.R;

/**
 * Created by SISTEMAS1 on 06/04/2016.
 */
public class Frg_Requisitos extends DialogFragment {
    private static final String TAG = Frg_Requisitos.class.getSimpleName();

    public Frg_Requisitos() {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.requisitos, null);

        builder.setView(v);

        Button ir = (Button) v.findViewById(R.id.btnIrARegistro);
        Button cerrar = (Button) v.findViewById(R.id.btnCerrarRequisitos);

        ir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(getActivity(), Act_LogIn.class);
                //startActivity(i);
                //getActivity().finish();
                listener.onPossitiveButtonClick();
                dismiss();
            }
        });

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNegativeButtonClick();
                dismiss();
            }
        });


        return builder.create();
    }

    public interface DialogResponseRequisitos {
        void onPossitiveButtonClick();
        void onNegativeButtonClick();
    }

    Frg_Requisitos.DialogResponseRequisitos listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (Frg_Requisitos.DialogResponseRequisitos) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() +
                            " no implementó DialogResponseContrato");

        }
    }
}
