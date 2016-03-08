package com.tativo.app.tativo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tativo.app.tativo.LogIn.Actividades.Act_Cotizador;
import com.tativo.app.tativo.LogIn.Actividades.Act_LogIn;

/**
 * Created by SISTEMAS1 on 01/03/2016.
 */
public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(Splash.this,Act_LogIn.class);
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
