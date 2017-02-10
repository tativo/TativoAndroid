package com.tativo.app.tativo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tativo.app.tativo.Bloques.Actividades.Act_Documentos;
import com.tativo.app.tativo.LogIn.Actividades.Act_Cotizador;
import com.tativo.app.tativo.LogIn.Actividades.Act_Cotizador_r;
import com.tativo.app.tativo.LogIn.Actividades.Act_LogIn;
import com.tativo.app.tativo.LogIn.Actividades.Act_tatibot;

/**
 * Created by SISTEMAS1 on 01/03/2016.
 */
public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(4000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(Splash.this, Act_Cotizador_r.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}
