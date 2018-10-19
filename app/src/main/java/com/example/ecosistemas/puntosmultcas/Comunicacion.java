package com.example.ecosistemas.puntosmultcas;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

public class Comunicacion extends Thread {

    private boolean conectado;
    MulticastSocket socket;
    InetAddress IPgrupo;
    int puerto = 5000;
    public String msj;
    int ID;

    Observer observer;

    public Comunicacion(Observer observer) {
        this.observer = observer;
        this.conectado = false;
        ID = -1;
    }

    public void identificar() {
       try {
            socket.setSoTimeout(600);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        enviarDatos("Identify");

        while (this.conectado == false) {
            try {

                recibirDatos();

            } catch (IOException e) {
                e.printStackTrace();
                this.conectado = true;

                if (ID == -1) {
                    ID = 0;

                }

                enviarDatos("Recibido: " + ID);

                try {
                    socket.setSoTimeout(0);
                } catch (SocketException e1) {
                    e1.printStackTrace();
                }
            }
        }


    }

    @Override
    public void run() {

        try {

            if (!conectado) {
                IPgrupo = InetAddress.getByName("228.0.0.19");
                socket = new MulticastSocket(puerto);
                socket.joinGroup(IPgrupo);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        identificar();


        while (true) {
            try {
                recibirDatos();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void recibirDatos() throws IOException {

        byte[] buff = new byte[1000];
        DatagramPacket datagrama = new DatagramPacket(buff, buff.length, IPgrupo, puerto);
        socket.receive(datagrama);
        msj = new String(datagrama.getData()).trim();

        ejecutar(msj);

        observer.recibido(msj);
    }

    public void enviarDatos(final String dato) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    DatagramPacket datagrama = new DatagramPacket(dato.getBytes(), dato.length(), IPgrupo, puerto);
                    socket.send(datagrama);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    public void ejecutar(final String msj) {

        if (msj.equals("Identify")) {
            if (conectado) {
                enviarDatos("Myid:" + this.ID);
            }
        }

        if (conectado == false) {
            if (msj.contains("Myid:")) {
                String[] mensaje = separar(msj, ":");
                int temId = toInt(mensaje[1]);
                if (temId >= this.ID) {
                    this.ID = temId + 1;
                }

            }
        }
    }

    public int toInt(String msj) {
        return Integer.parseInt(msj);
    }

    public float toFloat(String msj) {
        return Float.parseFloat(msj);
    }

    public String[] separar(String msj, String caracter) {
        String[] separado = msj.split(caracter);
        return separado;
    }

    interface Observer {
        public void recibido(String recibido);
    }
}
