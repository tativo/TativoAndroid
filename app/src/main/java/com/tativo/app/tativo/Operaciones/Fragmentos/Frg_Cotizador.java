package com.tativo.app.tativo.Operaciones.Fragmentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tativo.app.tativo.R;

/**
 * Created by SISTEMAS1 on 09/04/2016.
 */
public class Frg_Cotizador extends Fragment {

    public static final String ARG_SECTION_TITLE = "section_number";

    public static Frg_Cotizador newInstance(String sectionTitle) {
        Frg_Cotizador fragment = new Frg_Cotizador();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_TITLE, sectionTitle);
        fragment.setArguments(args);
        return fragment;
    }

    public Frg_Cotizador() {
    }

    View v;

    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.frg_cotizador,container,false);
        //String title = getArguments().getString(ARG_SECTION_TITLE);
        //TextView titulo = (TextView) v.findViewById(R.id.title);
        //titulo.setText(title);
        //titulo.setTextColor(getResources().getColor(R.color.colorBlanco));
        return v;
    }
}
