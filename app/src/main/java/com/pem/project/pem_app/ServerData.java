package com.pem.project.pem_app;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Matthias on 23.11.2014.
 *
 * Contains data that is stored on the server, such as client sockets, game variables etc.
 */
public class ServerData {
    private static ArrayList<BluetoothSocket> clients = new ArrayList<BluetoothSocket>();

    public static void addToClients(BluetoothSocket socket){
        Log.d("ThreadPass", "Found: " + socket);
        clients.add(socket);
    }

    public static ArrayList<BluetoothSocket> getClients(){
        return clients;
    }

    public static int getNumOfClients(){
        return clients.size();
    }

}
