package com.tativo.app.tativo.Operaciones.Fragmentos;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tativo.app.tativo.R;

/**
 * Created by SISTEMAS1 on 09/04/2016.
 */
public class Frg_Contrato extends DialogFragment  {

    private static final String TAG = Frg_Contrato .class.getSimpleName();

    public Frg_Contrato() {
    }

    View v;
    LinearLayout lyContratoCaratula, lyContratoOperacion, lyContratoPagare;
    TextView lblCaratulaNombreComercial, lblCaratulaRFC, lblCaratulaDireccion, lblCaratulaTelefono, lblCaratulaCorreo, lblCaratulaNombreCompleto, lblCaratulaDireccionAcreditado, lblCaratulaTelefonoAcreditado, lblCaratulaCorreoAcreditado, lblBancoDeposito, lblCLABEnoTarjeta, lblbInteresOrdinaria, lblInteresMoratoria, lblMontoSolicitado, lblCaratulaInteres, lblCaratulaIVA, lblCaratulaTotalPagar, lblCaratulaFechaInicio, lblbCaratulaPlazo, lblCaratulaFechaLimite;
    CheckBox ckTerminosCaratula, ckTerminosContrato, ckTerminosPagare;
    Button btnAceptaCaratula, btnAceptaContrato, btnAceptaPagare;

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

        CargaDocumentos();
        LoadFormControls();
        EventManager();

        builder.setView(v);
        return builder.create();
    }

    public void LoadFormControls()
    {
        lyContratoCaratula = (LinearLayout) v.findViewById(R.id.lyContratoCaratula);
        lyContratoOperacion = (LinearLayout) v.findViewById(R.id.lyContratoOperacion);
        lyContratoPagare = (LinearLayout) v.findViewById(R.id.lyContratoPagare);

        lblCaratulaNombreComercial = (TextView) v.findViewById(R.id.lblCaratulaNombreComercial);
        lblCaratulaRFC = (TextView) v.findViewById(R.id.lblCaratulaRFC);
        lblCaratulaDireccion = (TextView) v.findViewById(R.id.lblCaratulaDireccion);
        lblCaratulaTelefono = (TextView) v.findViewById(R.id.lblCaratulaTelefono);
        lblCaratulaCorreo = (TextView) v.findViewById(R.id.lblCaratulaCorreo);
        lblCaratulaDireccionAcreditado = (TextView) v.findViewById(R.id.lblCaratulaDireccionAcreditado);
        lblCaratulaTelefonoAcreditado = (TextView) v.findViewById(R.id.lblCaratulaTelefonoAcreditado);
        lblCaratulaCorreoAcreditado = (TextView) v.findViewById(R.id.lblCaratulaCorreoAcreditado);
        lblBancoDeposito = (TextView) v.findViewById(R.id.lblBancoDeposito);
        lblCLABEnoTarjeta = (TextView) v.findViewById(R.id.lblCLABEnoTarjeta);
        lblbInteresOrdinaria = (TextView) v.findViewById(R.id.lblbInteresOrdinaria);
        lblInteresMoratoria = (TextView) v.findViewById(R.id.lblInteresMoratoria);
        lblMontoSolicitado = (TextView) v.findViewById(R.id.lblMontoSolicitado);
        lblCaratulaInteres = (TextView) v.findViewById(R.id.lblCaratulaInteres);
        lblCaratulaIVA = (TextView) v.findViewById(R.id.lblCaratulaIVA);
        lblCaratulaTotalPagar = (TextView) v.findViewById(R.id.lblCaratulaTotalPagar);
        lblCaratulaFechaInicio = (TextView) v.findViewById(R.id.lblCaratulaFechaInicio);
        lblbCaratulaPlazo = (TextView) v.findViewById(R.id.lblbCaratulaPlazo);
        lblCaratulaFechaLimite = (TextView) v.findViewById(R.id.lblCaratulaFechaLimite);

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

    public void CargaDocumentos()
    {
        //CONTRATO
        String[] p = {"PARAMETRO_1","PARAMETRO_2","PARAMETRO_3","PARAMETRO_4","PARAMETRO_5","PARAMETRO_6","PARAMETRO_7","PARAMETRO_8","PARAMETRO_9","PARAMETRO_10"};

        AplicaFormato(R.id.lblContratoOperacionP1, R.string.lblContratoOperacionP1, p);

        AplicaFormato(R.id.lblAntecedentes, R.string.lblAntecedentes, null);
        AplicaFormato(R.id.lblAntecedentesP1, R.string.lblAntecedentesP1, p);

        AplicaFormato(R.id.lblDeclaraciones, R.string.lblDeclaraciones, null);
        AplicaFormato(R.id.lblDeclaracionesP1, R.string.lblDeclaracionesP1, p);
        AplicaFormato(R.id.lblDeclaracionesP1a, R.string.lblDeclaracionesP1a, p);
        AplicaFormato(R.id.lblDeclaracionesP1b, R.string.lblDeclaracionesP1b, null);
        AplicaFormato(R.id.lblDeclaracionesP1c, R.string.lblDeclaracionesP1c, p);
        AplicaFormato(R.id.lblDeclaracionesP1d, R.string.lblDeclaracionesP1d, p);
        AplicaFormato(R.id.lblDeclaracionesP1e, R.string.lblDeclaracionesP1e, p);
        AplicaFormato(R.id.lblDeclaracionesP2, R.string.lblDeclaracionesP2, null);
        AplicaFormato(R.id.lblDeclaracionesP2a, R.string.lblDeclaracionesP2a, p);
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
        AplicaFormato(R.id.lblClausulasPrimeraP2, R.string.lblClausulasPrimeraP2, p);
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
        AplicaFormato(R.id.lblClausulasDecimaQuintaIII, R.string.lblClausulasDecimaQuintaIII, p);
        AplicaFormato(R.id.lblClausulasDecimaQuintaIIIP2, R.string.lblClausulasDecimaQuintaIIIP2, null);

        AplicaFormato(R.id.lblClausulasDecimaSexta, R.string.lblClausulasDecimaSexta, p);
        AplicaFormato(R.id.lblClausulasDecimaSextaP2, R.string.lblClausulasDecimaSextaP2, null);
        AplicaFormato(R.id.lblClausulasDecimaSextaP3, R.string.lblClausulasDecimaSextaP3, null);

        AplicaFormato(R.id.lblClausulasDecimaSeptima, R.string.lblClausulasDecimaSeptima, p);
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

        //PAGARE
        AplicaFormato(R.id.lblNoCliente, R.string.lblNoCliente, p);
        AplicaFormato(R.id.lblBuenoPor, R.string.lblBuenoPor, p);
        AplicaFormato(R.id.lblPagareP1, R.string.lblPagareP1, p);
        AplicaFormato(R.id.lblPagareP2, R.string.lblPagareP2, p);
        AplicaFormato(R.id.lblPagareP3, R.string.lblPagareP3, null);
        AplicaFormato(R.id.lblPagareP4, R.string.lblPagareP4, null);
        AplicaFormato(R.id.lblPagareP5, R.string.lblPagareP5, null);
        AplicaFormato(R.id.lblPagareP6, R.string.lblPagareP6, p);
        AplicaFormato(R.id.lblPagareP7, R.string.lblPagareP7, null);
        AplicaFormato(R.id.lblPagareP8, R.string.lblPagareP8, p);
        AplicaFormato(R.id.lblPagareP9, R.string.lblPagareP9, null);
        AplicaFormato(R.id.lblPagareP10, R.string.lblPagareP10, p);
        AplicaFormato(R.id.lblPagareP11, R.string.lblPagareP11, p);
        AplicaFormato(R.id.lblPagareP12, R.string.lblPagareP12, p);
        AplicaFormato(R.id.lblPagareP13, R.string.lblPagareP13, p);
        AplicaFormato(R.id.lblPagareP14, R.string.lblPagareP14, p);
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
}
