package com.tativo.app.tativo.Operaciones.Fragmentos;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tativo.app.tativo.R;

import org.w3c.dom.Text;

/**
 * Created by SISTEMAS1 on 18/04/2016.
 */
public class Frg_Contrato_Caratula extends Fragment {

    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.frg_contrato_caratula,container,false);

        return v;
    }
}
