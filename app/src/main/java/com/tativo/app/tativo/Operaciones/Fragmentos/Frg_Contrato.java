package com.tativo.app.tativo.Operaciones.Fragmentos;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tativo.app.tativo.Bloques.Clases.DatosDocumentoCaratula;
import com.tativo.app.tativo.Bloques.Clases.DatosDocumentoPagare;
import com.tativo.app.tativo.Bloques.Clases.DatosDocumentosContrato;
import com.tativo.app.tativo.R;
import com.tativo.app.tativo.Utilidades.Globals;
import com.tativo.app.tativo.Utilidades.ServiciosSoap;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by SISTEMAS1 on 09/04/2016.
 */
public class Frg_Contrato extends DialogFragment  {

    private static final String TAG = Frg_Contrato .class.getSimpleName();

    public Frg_Contrato() {
    }

    View v;
    LinearLayout lyContratoCaratula, lyContratoOperacion, lyContratoPagare;
    TextView lblCaratulaNombreComercial, lblCaratulaRFC, lblCaratulaDireccion, lblCaratulaTelefono, lblCaratulaCorreo, lblCaratulaNombreCompleto, lblCaratulaDireccionAcreditado, lblCaratulaTelefonoAcreditado, lblCaratulaCorreoAcreditado, lblBancoDeposito, lblCLABEnoTarjeta, lblInteresOrdinaria, lblInteresMoratoria, lblMontoSolicitado, lblCaratulaInteres, lblCaratulaIVA, lblCaratulaTotalPagar, lblCaratulaFechaInicio, lblCaratulaPlazo, lblCaratulaFechaLimite;
    TextView lblDomiciliacionEmisor, lblDomiciliacionRFC, lblDomiciliacionDomicilio, lblDomiciliacionNombre, lblDomiciliacionReferencia, lblDomiciliacionTitularCuenta, lblDomiciliacionCLABE, lblDomiciliacionBanco, lblDomiciliacionTarjetaDebido;
    CheckBox ckTerminosCaratula, ckTerminosContrato, ckTerminosPagare;
    Button btnAceptaCaratula, btnAceptaContrato, btnAceptaPagare;

    Globals Sesion;
    ProgressDialog progressDialog;

    DatosDocumentoCaratula datosCaratula;
    DatosDocumentosContrato datosContrato;
    DatosDocumentoPagare datosPagare;

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
        v = inflater.inflate(R.layout.frg_contrato, null);

        Sesion = (Globals) getActivity().getApplicationContext();

        LoadFormControls();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new AsyncInfoBloque().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new AsyncInfoBloque().execute();
        }
        EventManager();

        builder.setView(v);
        return builder.create();
    }

    public void LoadFormControls()
    {
        progressDialog = new ProgressDialog(getActivity());

        datosPagare = new DatosDocumentoPagare();
        datosCaratula = new DatosDocumentoCaratula();
        datosContrato = new DatosDocumentosContrato();

        lyContratoCaratula = (LinearLayout) v.findViewById(R.id.lyContratoCaratula);
        lyContratoOperacion = (LinearLayout) v.findViewById(R.id.lyContratoOperacion);
        lyContratoPagare = (LinearLayout) v.findViewById(R.id.lyContratoPagare);

        lblCaratulaNombreComercial = (TextView) v.findViewById(R.id.lblCaratulaNombreComercial);
        lblCaratulaRFC = (TextView) v.findViewById(R.id.lblCaratulaRFC);
        lblCaratulaDireccion = (TextView) v.findViewById(R.id.lblCaratulaDireccion);
        lblCaratulaTelefono = (TextView) v.findViewById(R.id.lblCaratulaTelefono);
        lblCaratulaCorreo = (TextView) v.findViewById(R.id.lblCaratulaCorreo);
        lblCaratulaNombreCompleto = (TextView) v.findViewById(R.id.lblCaratulaNombreCompleto);
        lblCaratulaDireccionAcreditado = (TextView) v.findViewById(R.id.lblCaratulaDireccionAcreditado);
        lblCaratulaTelefonoAcreditado = (TextView) v.findViewById(R.id.lblCaratulaTelefonoAcreditado);
        lblCaratulaCorreoAcreditado = (TextView) v.findViewById(R.id.lblCaratulaCorreoAcreditado);
        lblBancoDeposito = (TextView) v.findViewById(R.id.lblBancoDeposito);
        lblCLABEnoTarjeta = (TextView) v.findViewById(R.id.lblCLABEnoTarjeta);
        lblInteresOrdinaria = (TextView) v.findViewById(R.id.lblInteresOrdinaria);
        lblInteresMoratoria = (TextView) v.findViewById(R.id.lblInteresMoratoria);
        lblMontoSolicitado = (TextView) v.findViewById(R.id.lblMontoSolicitado);
        lblCaratulaInteres = (TextView) v.findViewById(R.id.lblCaratulaInteres);
        lblCaratulaIVA = (TextView) v.findViewById(R.id.lblCaratulaIVA);
        lblCaratulaTotalPagar = (TextView) v.findViewById(R.id.lblCaratulaTotalPagar);
        lblCaratulaFechaInicio = (TextView) v.findViewById(R.id.lblCaratulaFechaInicio);
        lblCaratulaPlazo = (TextView) v.findViewById(R.id.lblCaratulaPlazo);
        lblCaratulaFechaLimite = (TextView) v.findViewById(R.id.lblCaratulaFechaLimite);

        lblDomiciliacionEmisor = (TextView) v.findViewById(R.id.lblDomiciliacionEmisor);
        lblDomiciliacionRFC = (TextView) v.findViewById(R.id.lblDomiciliacionRFC);
        lblDomiciliacionDomicilio = (TextView) v.findViewById(R.id.lblDomiciliacionDomicilio);
        lblDomiciliacionNombre = (TextView) v.findViewById(R.id.lblDomiciliacionNombre);
        lblDomiciliacionReferencia = (TextView) v.findViewById(R.id.lblDomiciliacionReferencia);
        lblDomiciliacionTitularCuenta = (TextView) v.findViewById(R.id.lblDomiciliacionTitularCuenta);
        lblDomiciliacionCLABE = (TextView) v.findViewById(R.id.lblDomiciliacionCLABE);
        lblDomiciliacionBanco = (TextView) v.findViewById(R.id.lblDomiciliacionBanco);
        lblDomiciliacionTarjetaDebido = (TextView) v.findViewById(R.id.lblDomiciliacionTarjetaDebido);




        ckTerminosCaratula = (CheckBox) v.findViewById(R.id.ckTerminosCaratula);
        ckTerminosContrato = (CheckBox) v.findViewById(R.id.ckTerminosContrato);
        ckTerminosPagare = (CheckBox) v.findViewById(R.id.ckTerminosPagare);

        btnAceptaCaratula = (Button) v.findViewById(R.id.btnAceptaCaratula);
        btnAceptaContrato = (Button) v.findViewById(R.id.btnAceptaContrato);
        btnAceptaPagare = (Button) v.findViewById(R.id.btnAceptaPagare);

        btnAceptaCaratula.setEnabled(false);
        btnAceptaCaratula.setBackgroundColor(getResources().getColor(R.color.colorAzulSeleccion));

        btnAceptaContrato.setEnabled(false);
        btnAceptaContrato.setBackgroundColor(getResources().getColor(R.color.colorAzulSeleccion));

        btnAceptaPagare.setEnabled(false);
        btnAceptaPagare.setBackgroundColor(getResources().getColor(R.color.colorAzulSeleccion));
    }

    public void EventManager()
    {
        ckTerminosCaratula.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnAceptaCaratula.setEnabled(true);
                    btnAceptaCaratula.setBackgroundColor(getResources().getColor(R.color.colorAzulOscuro));
                }
                else {
                    btnAceptaCaratula.setEnabled(false);
                    btnAceptaCaratula.setBackgroundColor(getResources().getColor(R.color.colorAzulSeleccion));
                }
            }
        });

        btnAceptaCaratula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyContratoCaratula.setVisibility(View.GONE);
                lyContratoOperacion.setVisibility(View.VISIBLE);
            }
        });


        ckTerminosContrato.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnAceptaContrato.setEnabled(true);
                    btnAceptaContrato.setBackgroundColor(getResources().getColor(R.color.colorAzulOscuro));
                }
                else {
                    btnAceptaContrato.setEnabled(false);
                    btnAceptaContrato.setBackgroundColor(getResources().getColor(R.color.colorAzulSeleccion));
                }
            }
        });

        btnAceptaContrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyContratoOperacion.setVisibility(View.GONE);
                lyContratoPagare.setVisibility(View.VISIBLE);
            }
        });


        ckTerminosPagare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnAceptaPagare.setEnabled(true);
                    btnAceptaPagare.setBackgroundColor(getResources().getColor(R.color.colorAzulOscuro));
                }
                else {
                    btnAceptaPagare.setEnabled(false);
                    btnAceptaPagare.setBackgroundColor(getResources().getColor(R.color.colorAzulSeleccion));
                }
            }
        });

        btnAceptaPagare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyContratoPagare.setVisibility(View.GONE);
                lyContratoCaratula.setVisibility(View.VISIBLE);
                listener.onPossitiveButtonClick();
                dismiss();
            }
        });
    }

    public void CargaDocumentosContrato()
    {
        //CONTRATO
        String[] c = {"xXxXxXxXxXx"};
        AplicaFormato(R.id.lblContratoOperacionP1, R.string.lblContratoOperacionP1, c);
        AplicaFormato(R.id.lblAntecedentes, R.string.lblAntecedentes, null);

        c = new String[]{"www.tativo.com"};
        AplicaFormato(R.id.lblAntecedentesP1, R.string.lblAntecedentesP1, c);
        AplicaFormato(R.id.lblDeclaraciones, R.string.lblDeclaraciones, null);
        AplicaFormato(R.id.lblDeclaracionesP1, R.string.lblDeclaracionesP1, null);

        c = new String[]{"01","20 de Abril de 2016","Nombre Licenciado","001"};
        AplicaFormato(R.id.lblDeclaracionesP1a, R.string.lblDeclaracionesP1a, c);
        AplicaFormato(R.id.lblDeclaracionesP1b, R.string.lblDeclaracionesP1b, null);

        c = new String[]{"xXxXxXxXxXx"};
        AplicaFormato(R.id.lblDeclaracionesP1c, R.string.lblDeclaracionesP1c, c);

        c = new String[]{"xXxXxXxXxXx"};
        AplicaFormato(R.id.lblDeclaracionesP1d, R.string.lblDeclaracionesP1d, c);

        c = new String[]{"xXxXxXxXxXx"};
        AplicaFormato(R.id.lblDeclaracionesP1e, R.string.lblDeclaracionesP1e, c);
        AplicaFormato(R.id.lblDeclaracionesP2, R.string.lblDeclaracionesP2, null);

        c = new String[]{String.format(datosContrato.getNacionalidad().toString())};
        AplicaFormato(R.id.lblDeclaracionesP2a, R.string.lblDeclaracionesP2a, c);
        AplicaFormato(R.id.lblDeclaracionesP2b, R.string.lblDeclaracionesP2b, null);
        AplicaFormato(R.id.lblDeclaracionesP2c, R.string.lblDeclaracionesP2c, null);
        AplicaFormato(R.id.lblDeclaracionesP2d, R.string.lblDeclaracionesP2d, null);
        AplicaFormato(R.id.lblDeclaracionesP2e, R.string.lblDeclaracionesP2e, null);
        AplicaFormato(R.id.lblDeclaracionesP2f, R.string.lblDeclaracionesP2f, null);
        AplicaFormato(R.id.lblDeclaracionesP2g, R.string.lblDeclaracionesP2g, null);
        AplicaFormato(R.id.lblDeclaracionesP3, R.string.lblDeclaracionesP3, null);
        AplicaFormato(R.id.lblDeclaracionesP3a, R.string.lblDeclaracionesP3a, null);
        AplicaFormato(R.id.lblDeclaracionesP3b, R.string.lblDeclaracionesP3b, null);
        AplicaFormato(R.id.lblDeclaracionesP3c, R.string.lblDeclaracionesP3c, null);
        AplicaFormato(R.id.lblDeclaracionesP3d, R.string.lblDeclaracionesP3d, null);
        AplicaFormato(R.id.lblDeclaracionesP3dP2, R.string.lblDeclaracionesP3dP2, null);

        AplicaFormato(R.id.lblClausulas, R.string.lblClausulas, null);
        AplicaFormato(R.id.lblClausulasPrimera, R.string.lblClausulasPrimera, null);

        c = new String[]{datosContrato.getNumeroDeDeposito()};
        AplicaFormato(R.id.lblClausulasPrimeraP2, R.string.lblClausulasPrimeraP2, c);
        AplicaFormato(R.id.lblClausulasSegunda, R.string.lblClausulasSegunda, null);
        AplicaFormato(R.id.lblClausulasTercera, R.string.lblClausulasTercera, null);
        AplicaFormato(R.id.lblClausulasCuarta, R.string.lblClausulasCuarta, null);
        AplicaFormato(R.id.lblClausulasQuinta, R.string.lblClausulasQuinta, null);
        AplicaFormato(R.id.lblClausulasQuintaP2, R.string.lblClausulasQuintaP2, null);
        AplicaFormato(R.id.lblClausulasQuintaP3, R.string.lblClausulasQuintaP3, null);
        AplicaFormato(R.id.lblClausulasComisiones, R.string.lblClausulasComisiones, null);
        AplicaFormato(R.id.lblClausulasProrroga, R.string.lblClausulasProrroga, null);
        AplicaFormato(R.id.lblClausulasProrrogaP2, R.string.lblClausulasProrrogaP2, null);
        AplicaFormato(R.id.lblClausulasProrrogaP3, R.string.lblClausulasProrrogaP3, null);
        AplicaFormato(R.id.lblClausulasProrrogaP4, R.string.lblClausulasProrrogaP4, null);
        AplicaFormato(R.id.lblClausulasSexta, R.string.lblClausulasSexta, null);
        AplicaFormato(R.id.lblClausulasSextaP2, R.string.lblClausulasSextaP2, null);
        AplicaFormato(R.id.lblClausulasSextaP3, R.string.lblClausulasSextaP3, null);


        lblDomiciliacionNombre.setText(datosContrato.getNombreCompleto());
        lblDomiciliacionBanco.setText(datosContrato.getBanco());
        if (datosContrato.getNumeroDeDeposito().toString().length() == 16)
        {
            lblDomiciliacionTarjetaDebido.setText(datosContrato.getNumeroDeDeposito());
            lblDomiciliacionCLABE.setText("-");
        }
        else
        {
            lblDomiciliacionCLABE.setText(datosContrato.getNumeroDeDeposito());
            lblDomiciliacionTarjetaDebido.setText("-");
        }



        AplicaFormato(R.id.lblClausulasSeptima, R.string.lblClausulasSeptima, null);
        AplicaFormato(R.id.lblClausulasSeptimaP2, R.string.lblClausulasSeptimaP2, null);
        AplicaFormato(R.id.lblClausulasOctava, R.string.lblClausulasOctava, null);
        AplicaFormato(R.id.lblClausulasNovena, R.string.lblClausulasNovena, null);
        AplicaFormato(R.id.lblClausulasNovenaI, R.string.lblClausulasNovenaI, null);
        AplicaFormato(R.id.lblClausulasNovenaII, R.string.lblClausulasNovenaII, null);
        AplicaFormato(R.id.lblClausulasNovenaIII, R.string.lblClausulasNovenaIII, null);
        AplicaFormato(R.id.lblClausulasDecima, R.string.lblClausulasDecima, null);
        AplicaFormato(R.id.lblClausulasDecimaI, R.string.lblClausulasDecimaI, null);
        AplicaFormato(R.id.lblClausulasDecimaII, R.string.lblClausulasDecimaII, null);
        AplicaFormato(R.id.lblClausulasDecimaIII, R.string.lblClausulasDecimaIII, null);
        AplicaFormato(R.id.lblClausulasDecimaPrimera, R.string.lblClausulasDecimaPrimera, null);
        AplicaFormato(R.id.lblClausulasDecimaSegunda, R.string.lblClausulasDecimaSegunda, null);
        AplicaFormato(R.id.lblClausulasDecimaSegundaP2, R.string.lblClausulasDecimaSegundaP2, null);
        AplicaFormato(R.id.lblClausulasDecimaSegundaI, R.string.lblClausulasDecimaSegundaI, null);
        AplicaFormato(R.id.lblClausulasDecimaSegundaII, R.string.lblClausulasDecimaSegundaII, null);
        AplicaFormato(R.id.lblClausulasDecimaTercera, R.string.lblClausulasDecimaTercera, null);
        AplicaFormato(R.id.lblClausulasDecimaCuarta, R.string.lblClausulasDecimaCuarta, null);
        AplicaFormato(R.id.lblClausulasDecimaQuinta, R.string.lblClausulasDecimaQuinta, null);
        AplicaFormato(R.id.lblClausulasDecimaQuintaI, R.string.lblClausulasDecimaQuintaI, null);
        AplicaFormato(R.id.lblClausulasDecimaQuintaII, R.string.lblClausulasDecimaQuintaII, null);

        c = new String[]{"www.tativo.com","www.tativo.com"};
        AplicaFormato(R.id.lblClausulasDecimaQuintaIII, R.string.lblClausulasDecimaQuintaIII, c);
        AplicaFormato(R.id.lblClausulasDecimaQuintaIIIP2, R.string.lblClausulasDecimaQuintaIIIP2, null);

        c = new String[]{"www.tativo.com"};
        AplicaFormato(R.id.lblClausulasDecimaSexta, R.string.lblClausulasDecimaSexta, c);
        AplicaFormato(R.id.lblClausulasDecimaSextaP2, R.string.lblClausulasDecimaSextaP2, null);
        AplicaFormato(R.id.lblClausulasDecimaSextaP3, R.string.lblClausulasDecimaSextaP3, null);

        c = new String[]{"www.tativo.com","3","(667) 716-7418",datosContrato.getCorreo()};
        AplicaFormato(R.id.lblClausulasDecimaSeptima, R.string.lblClausulasDecimaSeptima, c);
        AplicaFormato(R.id.lblClausulasDecimaSeptimaP2, R.string.lblClausulasDecimaSeptimaP2, null);
        AplicaFormato(R.id.lblClausulasDecimaSeptimaP3, R.string.lblClausulasDecimaSeptimaP3, null);
        AplicaFormato(R.id.lblClausulasDecimaSeptimaP4, R.string.lblClausulasDecimaSeptimaP4, null);

        AplicaFormato(R.id.lblClausulasDecimaOctava, R.string.lblClausulasDecimaOctava, null);
        AplicaFormato(R.id.lblClausulasDecimaNovena, R.string.lblClausulasDecimaNovena, null);
        AplicaFormato(R.id.lblClausulasDecimaNovenaP2, R.string.lblClausulasDecimaNovenaP2, null);

        AplicaFormato(R.id.lblClausulasVigesima, R.string.lblClausulasVigesima, null);
        AplicaFormato(R.id.lblClausulasVigesimaPrimera, R.string.lblClausulasVigesimaPrimera, null);
        AplicaFormato(R.id.lblClausulasVigesimaSegunda, R.string.lblClausulasVigesimaSegunda, null);
        AplicaFormato(R.id.lblClausulasVigesimaSegundaP2, R.string.lblClausulasVigesimaSegundaP2, null);
        //CONTRATO
    }

    public void  CargaDocumentosPagare()
    {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        nf.setMaximumFractionDigits(2);

        String[] p = {"PARAMETRO_1"};

        //PAGARE
        p = new String[]{String.valueOf(datosPagare.getCodigo())};
        AplicaFormato(R.id.lblNoCliente, R.string.lblNoCliente, p);

        p = new String[]{nf.format(datosPagare.getFinanciamiento())};
        AplicaFormato(R.id.lblBuenoPor, R.string.lblBuenoPor, p);

        p = new String[]{new SimpleDateFormat("dd 'de' MMMM 'de' yyyy").format(datosPagare.getFechaVence()), nf.format(datosPagare.getFinanciamiento()),datosPagare.getFinanciamientoLetra()};
        AplicaFormato(R.id.lblPagareP1, R.string.lblPagareP1, p);

        p = new String[]{datosPagare.getFolio(),new SimpleDateFormat("dd 'de' MMMM 'de' yyyy").format(datosPagare.getFechaSolicitud())};
        AplicaFormato(R.id.lblPagareP2, R.string.lblPagareP2, p);

        p = new String[]{datosPagare.getTasa().toString() + " %", datosPagare.getTasaLetra(), datosPagare.getTasaMoratoria().toString() + " %", datosPagare.getTasaMoratoriaLetra()};
        AplicaFormato(R.id.lblPagareP3, R.string.lblPagareP3, p);
        AplicaFormato(R.id.lblPagareP4, R.string.lblPagareP4, null);
        AplicaFormato(R.id.lblPagareP5, R.string.lblPagareP5, null);

        p = new String[]{datosPagare.getDomicilio()};
        AplicaFormato(R.id.lblPagareP6, R.string.lblPagareP6, p);
        AplicaFormato(R.id.lblPagareP7, R.string.lblPagareP7, null);

        //p = new String[]{datosPagare.getFechaSolicitud().toString()};
        p = new String[]{datosPagare.getCiudad() + ", " + datosPagare.getEstado(),new SimpleDateFormat("dd 'de' MMMM 'de' yyyy").format(datosPagare.getFechaSolicitud())};
        AplicaFormato(R.id.lblPagareP8, R.string.lblPagareP8, p);
        AplicaFormato(R.id.lblPagareP9, R.string.lblPagareP9, null);

        p = new String[]{datosPagare.getNombreCompleto()};
        AplicaFormato(R.id.lblPagareP10, R.string.lblPagareP10, p);

        //p = new String[]{""};
        //AplicaFormato(R.id.lblPagareP11, R.string.lblPagareP11, p);

        //p = new String[]{""};
        //AplicaFormato(R.id.lblPagareP12, R.string.lblPagareP12, p);

        p = new String[]{datosPagare.getClienteID()};
        AplicaFormato(R.id.lblPagareP13, R.string.lblPagareP13, p);

        //p = new String[]{""};
        //AplicaFormato(R.id.lblPagareP14, R.string.lblPagareP14, p);
        //PAGARE
    }

    private void AplicaFormato(int idT, int idR, @Nullable String[] lstP)
    {
        //String par = TextUtils.htmlEncode("prueba");
        TextView t =(TextView) v.findViewById(idT);
        String s = getResources().getString(idR);
        String sf = s;
        if (lstP != null)
            sf = String.format(s, lstP);
        CharSequence cs = Html.fromHtml(sf);
        t.setText(cs);
    }

    public interface DialogResponseContrato {
        void onPossitiveButtonClick();
        void onNegativeButtonClick();
    }

    DialogResponseContrato listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (DialogResponseContrato) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() +
                            " no implementó DialogResponseContrato");

        }
    }




    //Info del BLOQUE
    private class AsyncInfoBloque extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            GetInfoBloque();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            CargaDocumentosContrato();
            CargaDocumentosPagare();
            SetInfoBloque();
        }
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Cargando...");
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    private void GetInfoBloque(){
        String SOAP_ACTION = "http://tempuri.org/IService1/GetDatosDocumentos";
        String METHOD_NAME = "GetDatosDocumentos";
        String NAMESPACE = "http://tempuri.org/";

        ArrayList<PropertyInfo> valores =  new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("SolicitudID");
        pi1.setValue(Sesion.getSolicitudID());
        pi1.setType(PropertyInfo.STRING_CLASS);
        valores.add(pi1);

        ServiciosSoap oServiciosSoap = new ServiciosSoap();
        SoapObject respuesta = oServiciosSoap.RespuestaServicios(SOAP_ACTION,METHOD_NAME,NAMESPACE,valores);
        if(respuesta != null) {
            try
            {
                SoapPrimitive esValido = (SoapPrimitive) respuesta.getProperty(1);
                Boolean ev = Boolean.parseBoolean(esValido.toString());
                if (ev)
                {
                    String[] listaRespuesta;
                    listaRespuesta = new String[respuesta.getPropertyCount()];
                    SoapObject iOperacion = (SoapObject) respuesta.getProperty(0);
                    SoapObject iPagare = (SoapObject) iOperacion.getProperty("DatosPagare");
                    SoapObject iCaratula = (SoapObject) iOperacion.getProperty("DatosCaratula");
                    SoapObject iContrato = (SoapObject) iOperacion.getProperty("DatosContrato");

                    if(iPagare.getProperty("ClienteID") != null)
                    {
                        datosPagare.setFolio(iPagare.getProperty("Folio").toString());
                        datosPagare.setCodigo(Integer.parseInt(iPagare.getProperty("Codigo").toString()));
                        datosPagare.setCodigoLargo(iPagare.getProperty("CodigoLargo").toString());
                        datosPagare.setClienteID(iPagare.getProperty("ClienteID").toString());
                        datosPagare.setNombreCompleto(iPagare.getProperty("NombreCompleto").toString());
                        datosPagare.setFechaSolicitud(new SimpleDateFormat("yyyy-MM-dd").parse(iPagare.getProperty("FechaSolicitud").toString().substring(0, 10)));
                        datosPagare.setFinanciamiento(Double.parseDouble(iPagare.getProperty("Financiamiento").toString()));
                        datosPagare.setFechaDocu(iPagare.getProperty("FechaDocu").toString());
                        datosPagare.setFechaVence(new SimpleDateFormat("yyyy-MM-dd").parse(iPagare.getProperty("FechaVence").toString().substring(0, 10)));
                        datosPagare.setTasa(Double.parseDouble(iPagare.getProperty("tasa").toString()));
                        datosPagare.setTasaMoratoria(Double.parseDouble(iPagare.getProperty("TasaMoratoria").toString()));
                        datosPagare.setDomicilio(iPagare.getProperty("Domicilio").toString());

                        datosPagare.setPlazo(Integer.parseInt(iPagare.getProperty("Plazo").toString()));
                        datosPagare.setFinanciamientoLetra(iPagare.getProperty("FinanciamientoLetra").toString());
                        datosPagare.setTasaLetra(iPagare.getProperty("tasaLetra").toString());
                        datosPagare.setTasaMoratoriaLetra(iPagare.getProperty("TasaMoratoriaLetra").toString());
                        datosPagare.setFechaDocuLetra(iPagare.getProperty("FechaDocuLetra").toString());
                        datosPagare.setFechaVenceLetra(iPagare.getProperty("FechaVenceLetra").toString());
                        datosPagare.setCalle(iPagare.getProperty("Calle").toString());
                        datosPagare.setNumeroExt(iPagare.getProperty("NumeroExt").toString());
                        datosPagare.setNumeroInt(iPagare.getProperty("NumeroInt").toString());
                        datosPagare.setColonia(iPagare.getProperty("Colonia").toString());
                        datosPagare.setCodigoPostal(iPagare.getProperty("CodigoPostal").toString());
                        datosPagare.setCiudad(iPagare.getProperty("Ciudad").toString());
                        datosPagare.setMunicipio(iPagare.getProperty("Municipio").toString());
                        datosPagare.setEstado(iPagare.getProperty("Estado").toString());
                    }

                    if(iCaratula.getProperty("ClienteID") != null)
                    {
                        datosCaratula.setClienteID(iCaratula.getProperty("ClienteID").toString());
                        datosCaratula.setNombreCompleto(iCaratula.getProperty("NombreCompleto").toString());
                        datosCaratula.setDomicilio(iCaratula.getProperty("Domicilio").toString());
                        datosCaratula.setTelefono(iCaratula.getProperty("Telefono").toString());
                        datosCaratula.setCorreo(iCaratula.getProperty("Correo").toString());
                        datosCaratula.setBanco(iCaratula.getProperty("Banco").toString());
                        datosCaratula.setNumeroDeTarjeta(iCaratula.getProperty("NumeroDeTarjeta").toString());
                        datosCaratula.setClabe(iCaratula.getProperty("Clabe").toString());
                        datosCaratula.setCapital(Double.parseDouble(iCaratula.getProperty("Capital").toString()));
                        datosCaratula.setInteres(Double.parseDouble(iCaratula.getProperty("Interes").toString()));
                        datosCaratula.setIVA(Double.parseDouble(iCaratula.getProperty("IVA").toString()));
                        datosCaratula.setTotalPagar(Double.parseDouble(iCaratula.getProperty("TotalPagar").toString()));
                        datosCaratula.setFechaSolicitud(new SimpleDateFormat("yyyy-MM-dd").parse(iCaratula.getProperty("FechaSolicitud").toString().substring(0, 10)));
                        datosCaratula.setFechaVence(new SimpleDateFormat("yyyy-MM-dd").parse(iCaratula.getProperty("FechaVence").toString().substring(0, 10)));
                        datosCaratula.setNumeroContrato(iCaratula.getProperty("NumeroContrato").toString());
                        datosCaratula.setFechaContrato(iCaratula.getProperty("FechaContrato").toString());
                        datosCaratula.setPlazo(Integer.parseInt(iCaratula.getProperty("Plazo").toString()));
                    }

                    if(iContrato.getProperty("Clienteid") != null)
                    {
                        datosContrato.setClienteid(iContrato.getProperty("Clienteid").toString());
                        datosContrato.setNombreCompleto(iContrato.getProperty("NombreCompleto").toString());
                        datosContrato.setCorreo(iContrato.getProperty("Correo").toString());
                        datosContrato.setBanco(iContrato.getProperty("Banco").toString());
                        datosContrato.setNumeroDeDeposito(iContrato.getProperty("NumeroDeDeposito").toString());
                        datosContrato.setRFC(iContrato.getPrimitivePropertyAsString("RFC").toString());
                        datosContrato.setNacionalidad(iContrato.getPrimitivePropertyAsString("Nacionalidad").toString());
                        //datosContrato.setNacionalidad(String.valueOf(iContrato.getProperty("Nacionalidad")));
                    }
                }
            } catch (Exception e) {
                Toast.makeText(v.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private void SetInfoBloque() {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        nf.setMaximumFractionDigits(2);

        if (datosCaratula.getClienteID() != null)
        {
            //TextView lblCaratulaNombreComercial, lblCaratulaRFC, lblCaratulaDireccion, lblCaratulaTelefono, lblCaratulaCorreo, , ,

            lblCaratulaNombreCompleto.setText(datosCaratula.getNombreCompleto());
            lblCaratulaDireccionAcreditado.setText(datosCaratula.getDomicilio());
            lblCaratulaTelefonoAcreditado.setText(datosCaratula.getTelefono());
            lblCaratulaCorreoAcreditado.setText(datosCaratula.getCorreo());

            lblBancoDeposito.setText(datosCaratula.getBanco());
            lblCLABEnoTarjeta.setText(datosCaratula.getNumeroDeTarjeta());

            lblMontoSolicitado.setText(nf.format(datosCaratula.getCapital()));
            lblCaratulaInteres.setText(nf.format(datosCaratula.getInteres()));
            lblCaratulaIVA.setText(nf.format(datosCaratula.getIVA()));
            lblCaratulaTotalPagar.setText(nf.format(datosCaratula.getTotalPagar()));

            lblCaratulaFechaInicio.setText(new SimpleDateFormat("dd/MM/yyyy").format(datosCaratula.getFechaSolicitud()));
            lblCaratulaPlazo.setText(String.valueOf(datosCaratula.getPlazo()));
            lblCaratulaFechaLimite.setText(new SimpleDateFormat("dd/MM/yyyy").format(datosCaratula.getFechaVence()));
        }
    }
    //Info del BLOQUE
}
