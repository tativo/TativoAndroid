package com.tativo.app.tativo.Bloques.Actividades;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.Globals;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Act_B2_Personal extends AppCompatActivity {

    Button btnInfPersonal, btnFechaNacimiento;
    AutoCompleteTextView txtFechaNacimiento, txtDependientes, txtCodigoPostal, txtCalle, txtNumeroExt, txtNumeroInt, txtTelefonoCelular, txt4DigitosTarjeta;
    MaterialSpinner spnGenero, spnEstadoCivil, spnColonia, spnMarcaCelular;
    Switch swtTarjetaCredito, swtCreditoHipotecario, swtCreditoAutomotriz;
    CheckBox ckTerminosCondiciones1, ckTerminosCondiciones2, ckTerminosCondiciones3, ckTerminosCondiciones4;
    TextView hnEstadoMunicipioTexto;
    ProgressDialog progressDialog;
    Globals g;

    int year_x, month_x, day_x;
    static final int DILOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_b2_personal);
        g = (Globals) getApplicationContext();
        progressDialog = new ProgressDialog(this);
        LoadFormControls();
        FocusManager();
        EventManager();

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        txtFechaNacimiento.requestFocus();
    }

    private void LoadFormControls()
    {
        btnInfPersonal = (Button) findViewById(R.id.btnInfPersonal);
        txtFechaNacimiento = (AutoCompleteTextView) findViewById(R.id.txtFechaNacimiento);
        btnFechaNacimiento = (Button) findViewById(R.id.btnFechaNacimiento);
        txtDependientes = (AutoCompleteTextView) findViewById(R.id.txtDependientes);
        txtCodigoPostal = (AutoCompleteTextView) findViewById(R.id.txtCodigoPostal);
        txtCalle = (AutoCompleteTextView) findViewById(R.id.txtCalle);
        txtNumeroExt = (AutoCompleteTextView) findViewById(R.id.txtNumeroExt);
        txtNumeroInt = (AutoCompleteTextView) findViewById(R.id.txtNumeroInt);
        txtTelefonoCelular = (AutoCompleteTextView) findViewById(R.id.txtTelefonoCelular);
        txt4DigitosTarjeta = (AutoCompleteTextView) findViewById(R.id.txt4DigitosTarjeta);

        spnGenero = (MaterialSpinner) findViewById(R.id.spnGenero);
        spnEstadoCivil = (MaterialSpinner) findViewById(R.id.spnEstadoCivil);
        spnColonia = (MaterialSpinner) findViewById(R.id.spnColonia);
        spnMarcaCelular = (MaterialSpinner) findViewById(R.id.spnMarcaCelular);

        swtTarjetaCredito = (Switch) findViewById(R.id.swtTarjetaCredito);
        swtCreditoHipotecario = (Switch) findViewById(R.id.swtCreditoHipotecario);
        swtCreditoAutomotriz = (Switch) findViewById(R.id.swtCreditoAutomotriz);

        ckTerminosCondiciones1 = (CheckBox) findViewById(R.id.ckTerminosCondiciones1);
        ckTerminosCondiciones2 = (CheckBox) findViewById(R.id.ckTerminosCondiciones2);
        ckTerminosCondiciones3 = (CheckBox) findViewById(R.id.ckTerminosCondiciones3);
        ckTerminosCondiciones4 = (CheckBox) findViewById(R.id.ckTerminosCondiciones4);

        hnEstadoMunicipioTexto = (TextView) findViewById(R.id.hnEstadoMunicipioTexto);


    }

    public void FocusManager()
    {
        FocusNextControl(R.id.txtFechaNacimiento, "T", R.id.txtDependientes, "T");
        FocusNextControl(R.id.txtDependientes, "T", R.id.spnGenero, "S");
        FocusNextControl(R.id.spnGenero, "S", R.id.spnEstadoCivil, "S");
        FocusNextControl(R.id.spnEstadoCivil, "S", R.id.txtCodigoPostal, "T");
        FocusNextControl(R.id.txtCodigoPostal, "T", R.id.spnColonia, "S");
        FocusNextControl(R.id.spnColonia, "S", R.id.txtCalle, "T");
        FocusNextControl(R.id.txtNumeroExt, "T", R.id.txtNumeroInt, "T");
        FocusNextControl(R.id.txtNumeroInt, "T", R.id.txtTelefonoCelular, "T");
        FocusNextControl(R.id.txtTelefonoCelular, "T", R.id.spnEstadoCivil, "S");
    }

    private void EventManager()
    {
        btnFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DILOG_ID);
            }
        });

        swtTarjetaCredito.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txt4DigitosTarjeta.setEnabled(true);
                    txt4DigitosTarjeta.requestFocus();
                } else {
                    txt4DigitosTarjeta.setEnabled(false);
                    txtTelefonoCelular.requestFocus();
                }
            }
        });

        btnInfPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Act_B3_InfDeposito.class);
                startActivity(i);
                finish();
            }
        });


    }

    public void FocusNextControl(int o,String ot, int d,String dt)
    {
        final EditText destino = (dt.toUpperCase()=="T"?(EditText) findViewById(d):null);
        final Spinner destinoS = (dt.toUpperCase()=="S"?(Spinner) findViewById(d):null);

        if(ot.toUpperCase()=="T"){
            EditText origen = (EditText) findViewById(o);
            origen.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE ||
                            actionId == EditorInfo.IME_ACTION_NEXT) {
                        if (destino != null)
                            destino.requestFocus();
                        else {
                            destinoS.requestFocus();
                            destinoS.performClick();
                        }
                        return false;
                    }
                    return false;
                }
            });
        }
        if(ot.toUpperCase()=="S"){
            Spinner origen = (Spinner) findViewById(o);
            origen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != -1) {
                        if (destino != null)
                            destino.requestFocus();
                        else {
                            destinoS.requestFocus();
                            destinoS.performClick();
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        if (id == DILOG_ID)
            return new DatePickerDialog(this, dpickerListner, year_x, month_x, day_x);

        return  null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            year_x = year;
            month_x = monthOfYear+1;
            day_x = dayOfMonth;

            String sfecha = day_x+"/"+month_x+"/"+year_x;
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            ParsePosition pp = new ParsePosition(0);
            Date d = format.parse(sfecha, pp);
            txtFechaNacimiento.setText(format.format(d).toString());
        }
    };
}
