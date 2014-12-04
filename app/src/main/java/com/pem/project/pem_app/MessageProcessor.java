package com.pem.project.pem_app;

import android.bluetooth.BluetoothSocket;

/**
 * Created by Matthias on 04.12.2014.
 */
public class MessageProcessor {

    public void processMessage(String message, BluetoothSocket socket){
        String[] messageParameters = message.split("_");
        String messageArt = messageParameters[0];
        String miniGame = messageParameters[1];
        String extra = messageParameters[2];

        if(ServerData.isServer()){

        } else {

        }

    }

}
