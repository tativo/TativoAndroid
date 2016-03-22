package com.tativo.app.tativo.Utilidades;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by AlfonsoM on 08/03/2016.
 */
public class Utilerias extends Activity {

    public static String getDate(String fecha)
    {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try
        {
            Date newDate = format.parse(fecha);
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            return format.format(newDate);
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
