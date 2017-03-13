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
public class Frg_RequisitosHome extends DialogFragment {
    private static final String TAG = Frg_RequisitosHome.class.getSimpleName();

    public Frg_RequisitosHome() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createRequisitosHome();
    }

    /**
     * Crea un diálogo con personalizado para comportarse
     * como formulario de login
     *
     * @return Diálogo
     */
    public AlertDialog createRequisitosHome() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.requisitos_home, null);

        builder.setView(v);

        Button cerrar = (Button) v.findViewById(R.id.btnCerrarRequisitosHome);

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

    Frg_RequisitosHome.DialogResponseRequisitos listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (Frg_RequisitosHome.DialogResponseRequisitos) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() +
                            " no implementó DialogResponseContrato");

        }
    }
}
