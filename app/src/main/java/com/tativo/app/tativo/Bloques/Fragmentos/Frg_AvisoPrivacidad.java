package com.tativo.app.tativo.Bloques.Fragmentos;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tativo.app.tativo.R;

/**
 * Created by SISTEMAS1 on 06/04/2016.
 */
public class Frg_AvisoPrivacidad extends DialogFragment {
    private static final String TAG = Frg_AvisoPrivacidad.class.getSimpleName();

    public Frg_AvisoPrivacidad() {
    }


    View v;
    Button cerrar;




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
        v = inflater.inflate(R.layout.aviso_privacidad, null);

        builder.setView(v);

        LoadFormControls();
        EventManager();
        CargaAviso();

        return builder.create();
    }

    public void LoadFormControls() {
        cerrar = (Button) v.findViewById(R.id.btnCerrarRequisitos);
    };

    public void EventManager() {
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNegativeButtonClick();
                dismiss();
            }
        });
    };

    public void CargaAviso() {

        AplicaFormato(R.id.lbladpTitulo, R.string.adpTitulo, null);
        AplicaFormato(R.id.lbladpResponsable, R.string.adpResponsable, null);
        AplicaFormato(R.id.lbladpDomicilio, R.string.adpDomicilio, null);
        AplicaFormato(R.id.lbladpSitioWEB, R.string.adpSitioWEB, null);
        AplicaFormato(R.id.lbladpParrafo1, R.string.adpParrafo1, null);
        AplicaFormato(R.id.lbladpParrafo2, R.string.adpParrafo2, null);
        AplicaFormato(R.id.lbladpParrafo3, R.string.adpParrafo3, null);
        AplicaFormato(R.id.lbladpParrafo4, R.string.adpParrafo4, null);
        AplicaFormato(R.id.lbladpParrafo5, R.string.adpParrafo5, null);
        AplicaFormato(R.id.lbladpParrafo6, R.string.adpParrafo6, null);
        AplicaFormato(R.id.lbladpParrafo7, R.string.adpParrafo7, null);
        AplicaFormato(R.id.lbladpParrafo8, R.string.adpParrafo8, null);
        AplicaFormato(R.id.lbladpParrafo9, R.string.adpParrafo9, null);
        AplicaFormato(R.id.lbladpParrafo10, R.string.adpParrafo10, null);
        AplicaFormato(R.id.lbladpParrafo11, R.string.adpParrafo11, null);


    };

    private void AplicaFormato(int idT, int idR, @Nullable String[] lstP) {
        //String par = TextUtils.htmlEncode("prueba");
        TextView t =(TextView) v.findViewById(idT);
        String s = getResources().getString(idR);
        String sf = s;
        if (lstP != null)
            sf = String.format(s, lstP);
        CharSequence cs = Html.fromHtml(sf);
        t.setText(cs);
    }

    public interface DialogResponseRequisitos {
        void onPossitiveButtonClick();
        void onNegativeButtonClick();
    }

    Frg_AvisoPrivacidad.DialogResponseRequisitos listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (Frg_AvisoPrivacidad.DialogResponseRequisitos) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() +
                            " no implementó DialogResponseContrato");

        }
    }
}
