package com.tativo.app.tativo.Operaciones.Fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tativo.app.tativo.R;

/**
 * Created by SISTEMAS1 on 08/04/2016.
 */
public class Frg_Nav extends Fragment {
    public static final String ARG_SECTION_TITLE = "section_number";

    public static Frg_Nav newInstance(String sectionTitle) {
        Frg_Nav fragment = new Frg_Nav();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_TITLE, sectionTitle);
        fragment.setArguments(args);
        return fragment;
    }

    public Frg_Nav() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_fragment, container, false);

        // Ubicar argumento en el text view de section_fragment.xml
        String title = getArguments().getString(ARG_SECTION_TITLE);
        TextView titulo = (TextView) view.findViewById(R.id.title);
        titulo.setText(title);
        return view;
    }
}
