package com.pem.project.pem_app;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Matthias on 24.11.2014.
 */
public class BluetoothListener {
    public interface IListenCallback {
        void processReceivedMessage(String message, BluetoothSocket socket);
    }

    private IListenCallback callback;
    Thread messageListener;
    private boolean listening = true;

    public BluetoothListener(Activity activity){
        callback = (IListenCallback) activity;
    }

    public void listen(BluetoothSocket socket){
        BluetoothSocketListener bsl = new BluetoothSocketListener(socket);
        messageListener = new Thread(bsl);
        messageListener.start();
    }

    private class BluetoothSocketListener implements Runnable {

        private BluetoothSocket socket;
        private TextView textView;
        private Handler handler;

        public BluetoothSocketListener(BluetoothSocket socket) {
            Log.d("BluetoothClient", "started listening");
            this.socket = socket;
        }

        public void run() {
           while(listening) {
               int bufferSize = 1024;
               byte[] buffer = new byte[bufferSize];
               try {
                   InputStream instream = socket.getInputStream();
                   int bytesRead = -1;
                   String message = "";
                   while (true) {
                       message = "";
                       bytesRead = instream.read(buffer);
                       if (bytesRead != -1) {
                           while ((bytesRead == bufferSize) && (buffer[bufferSize - 1] != 0)) {
                               message = message + new String(buffer, 0, bytesRead);
                               bytesRead = instream.read(buffer);
                           }
                           message = message + new String(buffer, 0, bytesRead - 1);
                           processMessage(message, socket);
                           socket.getInputStream();
                       }
                   }
               } catch (IOException e) {
                   Log.d("BLUETOOTH_COMMS", e.getMessage());
               }
           }
        }
    }

    public void destroy(){
        listening = false;
        try{
            messageListener.join();
        } catch (Exception e){
            Log.d("Destroy", e.toString());
        }
    }

    public void processMessage(String m, BluetoothSocket socket){
           Log.d("Received", m);
           callback.processReceivedMessage(m, socket);
    }
}
