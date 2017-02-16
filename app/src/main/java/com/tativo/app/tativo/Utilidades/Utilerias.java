package com.tativo.app.tativo.Utilidades;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static String LetraCapital(String cadena) {
        char[] caracteres = cadena.toCharArray();
        caracteres[0] = Character.toUpperCase(caracteres[0]);
        for (int i = 0; i < cadena.length() - 2; i++) {
            if (caracteres[i] == ' ' || caracteres[i] == '.' || caracteres[i] == ',')
                caracteres[i + 1] = Character.toUpperCase(caracteres[i + 1]);
        }
        return new String(caracteres);
    }

    public static Date stringToDate(String cadena){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date=null;
        try {
            date = dateFormat.parse(cadena);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static long Days(Date fecha1,Date fecha2){
        // Crear 2 instancias de Calendar
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        int year1 = Integer.parseInt(df.format(fecha1));

        df = new SimpleDateFormat("MM");
        int month1 = Integer.parseInt(df.format(fecha1));

        df = new SimpleDateFormat("dd");
        int day1 = Integer.parseInt(df.format(fecha1));

        df = new SimpleDateFormat("yyyy");
        int year2 = Integer.parseInt(df.format(fecha2));

        df = new SimpleDateFormat("MM");
        int month2 = Integer.parseInt(df.format(fecha2));

        df = new SimpleDateFormat("dd");
        int day2 = Integer.parseInt(df.format(fecha2));






        // Establecer las fechas
        cal1.set(year1, month1, day1);
        cal2.set(year2, month2, day2);

        // conseguir la representacion de la fecha en milisegundos
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();

        // calcular la diferencia en milisengundos
        long diff = milis2 - milis1;

        // calcular la diferencia en segundos
        long diffSeconds = diff / 1000;

        // calcular la diferencia en minutos
        long diffMinutes = diff / (60 * 1000);

        // calcular la diferencia en horas
        long diffHours = diff / (60 * 60 * 1000);

        // calcular la diferencia en dias
        long diffDays = diff / (24 * 60 * 60 * 1000);

      return  diffDays;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    private boolean isNumber(String text){
        if(text != null || !text.equals("")) {
            char[] characters = text.toCharArray();
            for (int i = 0; i < text.length(); i++) {
                if (characters[i] < 48 || characters[i] > 57)
                    return false;
            }
        }
        return true;
    }
}
