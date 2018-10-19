package com.example.ecosistemas.puntosmultcas;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, Comunicacion.Observer {

    DrawView lienzo;
    Comunicacion com;
    int ID;
    boolean conectado;
    String mensaje;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* com =new Comunicacion();
        com.start();*/

        //HABILITAR MULTICAST
        WifiManager wm= (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiManager.MulticastLock multicastLock=wm.createMulticastLock( "mydebuginfo");
        multicastLock.acquire();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels; // ancho absoluto en pixels
        int height = metrics.heightPixels; // alto absoluto en pixels

        lienzo = findViewById(R.id.lienzo);
        lienzo.setOnTouchListener(this);

        com = new Comunicacion(this);
        com.start();


    }

    public void press(View v, MotionEvent event){
        switch (v.getId()){
            case R.id.lienzo:
                if(conectado == false) {
                    ID = com.ID;
                    lienzo.addPoint(event.getX(), event.getY());
                    lienzo.identificar(ID);
                    conectado = true;
                    Toast.makeText(MainActivity.this, "Mi id es: " + ID, Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
    public void dragged(View v, MotionEvent event){
        switch (v.getId()){
            case R.id.lienzo:
                lienzo.actualizarPos(ID, event.getX(), event.getY());
                com.enviarDatos("Iam:" + ID + ":" +event.getX() + ":" + event.getY());

                break;
        }
    }

    public void released(View v, MotionEvent event){
        switch (v.getId()){
            case R.id.lienzo:

                break;
        }

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            press(v, event);
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            dragged(v, event);
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            released(v, event);
        }

        return true;
    }

    @Override
    public void recibido(String recibido) {

        mensaje = recibido;

        if(recibido.contains("Iam:")){
            new ActualizarPuntos().execute();
        }
    }

    public class ActualizarPuntos extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            String[] m = com.separar(mensaje, ":");
            int temID = com.toInt(m[1]);
            int x = (int) com.toFloat(m[2]);
            int y = (int) com.toFloat(m[3]);

            if(temID > lienzo.getPunticos().size()-1){
                lienzo.addPoint(x, y);
            }

                lienzo.actualizarPos(temID, x, y);


        }
    }
}
