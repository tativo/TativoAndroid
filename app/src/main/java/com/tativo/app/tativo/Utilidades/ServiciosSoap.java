package com.tativo.app.tativo.Utilidades;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.util.ArrayList;

/**
 * Created by AlfonsoM on 08/03/2016.
 */
public class ServiciosSoap {
    public SoapObject RespuestaServicios(String SOAP_ACTION,String METHOD_NAME,String NAMESPACE,ArrayList<PropertyInfo> listaValores)
    {
        String URL = "http://tativo.com.mx/WcfTativo/Service1.svc?wsdl";
        SoapObject respuesta;

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            if(listaValores != null)
            {
                for (int i = 0;i<listaValores.size();i++) {
                    Request.addProperty(listaValores.get(i));
                }
            }
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);
            respuesta = (SoapObject) soapEnvelope.getResponse();

        } catch (Exception ex) {
            respuesta = null;
        }
        return respuesta;
    }
}
