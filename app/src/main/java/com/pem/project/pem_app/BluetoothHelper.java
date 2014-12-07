package com.pem.project.pem_app;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Matthias on 24.11.2014.
 */
public class BluetoothHelper {

    public static void sendDataToPairedDevice(BluetoothSocket socket, String message){
        byte[] toSend = message.getBytes();
        try {
            //BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(Constants.uuid);
            OutputStream mmOutStream = socket.getOutputStream();
            mmOutStream.write(toSend);
            // Your Data is sent to  BT connected paired device ENJOY.
        } catch (IOException e) {
            Log.e("BluetoothSend", "Exception during write", e);
        }

        Log.d("Sent to: ", socket.getRemoteDevice().getName() + ", " + message);
    }
}
