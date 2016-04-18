package com.tativo.app.tativo.Operaciones.Fragmentos;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tativo.app.tativo.R;

/**
 * Created by SISTEMAS1 on 09/04/2016.
 */
public class Frg_Contrato extends DialogFragment  {

    private static final String TAG = Frg_Contrato .class.getSimpleName();

    public Frg_Contrato() {
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
        View v = inflater.inflate(R.layout.frg_contrato_caratula, null);

        String[] p = {"prueba","de que","funciona"};
        AplicaFormato(v,R.id.lblContratoOperacionP1, R.string.lblAntecedentesP1, p);

        builder.setView(v);

        /*
        Button sig = (Button) v.findViewById(R.id.btnCaratulaContrato);
        sig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });
        */
        return builder.create();
    }

    private void AplicaFormato(View v, int idT, int idR, String[] lstP)
    {
        //String par = TextUtils.htmlEncode("prueba");
        TextView t =(TextView) v.findViewById(idT);
        String s = getResources().getString(idR);
        String sf = s;
        sf = String.format(s, lstP);
        CharSequence cs = Html.fromHtml(sf);
        t.setText(cs);
    }

}
