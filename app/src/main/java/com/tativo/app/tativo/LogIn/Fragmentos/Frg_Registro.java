package com.tativo.app.tativo.LogIn.Fragmentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tativo.app.tativo.R;

/**
 * Created by SISTEMAS1 on 29/02/2016.
 */
public class Frg_Registro extends Fragment {

    public Frg_Registro()
    {

    }

    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.frg_registro,container,false);
        return v;
    }
}
