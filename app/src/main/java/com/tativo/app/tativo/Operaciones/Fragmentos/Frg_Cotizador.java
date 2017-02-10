package com.tativo.app.tativo.Operaciones.Fragmentos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.tativo.app.tativo.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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


    EditText txtRecotizarImporte, txtRecotizarFecha;
    TextView lblRecotizarImporte, lblRecotizarComision, lblRecotizarTotal, lblRecotizarFechaCompromiso;
    Button btnRecotizarSolicita;

    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.frg_cotizador,container,false);
        //String title = getArguments().getString(ARG_SECTION_TITLE);
        //TextView titulo = (TextView) v.findViewById(R.id.title);
        //titulo.setText(title);
        //titulo.setTextColor(getResources().getColor(R.color.colorBlanco));
        LoadFormControls();
        EventManager();


        return v;
    }

    private void LoadFormControls() {

        final Calendar cal = Calendar.getInstance();

        txtRecotizarImporte = (EditText) v.findViewById(R.id.txtRecotizarImporte);
        txtRecotizarFecha = (EditText) v.findViewById(R.id.txtRecotizarFecha);

        lblRecotizarImporte = (TextView) v.findViewById(R.id.lblRecotizarImporte);
        lblRecotizarComision = (TextView) v.findViewById(R.id.lblRecotizarComision);
        lblRecotizarTotal = (TextView) v.findViewById(R.id.lblRecotizarTotal);
        lblRecotizarFechaCompromiso = (TextView) v.findViewById(R.id.lblRecotizarFechaCompromiso);

        btnRecotizarSolicita = (Button) v.findViewById(R.id.btnRecotizarSolicita);
    }

    private void EventManager() {
        txtRecotizarFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DialogFragment picker = new DatePickerFragment();
                //picker.show(getFragmentManager(),"datePicker");
            }
        });

        txtRecotizarImporte.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(c.getTime());
        }
    }
}
