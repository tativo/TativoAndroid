package com.tativo.app.tativo.Utilidades;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.tativo.app.tativo.Bloques.Actividades.Act_B3_ConfirmarPIN;

/**
 * Created by AlfonsoM on 28/03/2016.
 */
public class SmsReceiver extends BroadcastReceiver {
    private String TAG = SmsReceiver.class.getSimpleName();

    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the data (SMS data) bound to intent
        try {
            Bundle bundle = intent.getExtras();

            SmsMessage[] msgs = null;

            String str = "";

            if (bundle != null) {
                // Retrieve the SMS Messages received
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];

                // For every SMS message received
                for (int i = 0; i < msgs.length; i++) {
                    // Convert Object array
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    // Sender's phone number
                    //str += "SMS from " + msgs[i].getOriginatingAddress() + " : ";
                    // Fetch the text message
                    str += msgs[i].getMessageBody().toString();
                    // Newline <img src="http://codetheory.in/wp-includes/images/smilies/icon_smile.gif" alt=":-)" class="wp-smiley">
                    str += "\n";
                }

                // Display the entire SMS Message
                String[] codigo = str.split(" ");
                if (codigo[0].toString().toUpperCase().trim().equals("TATIVO"))
                    Act_B3_ConfirmarPIN.getInstance().verificarCodigoSms(codigo[4].toString().trim());
            }
        }
        catch (Exception e)
        {

        }
    }
}