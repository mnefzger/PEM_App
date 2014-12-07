package com.pem.project.pem_app;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

/**
 * Created by Matthias on 04.12.2014.
 */
public class MessageProcessor {

    public String processMessage(String message, BluetoothSocket socket){
        String[] messageParameters = message.split("_");
        String messageArt = messageParameters[0];
        String miniGame = messageParameters[1];
        String extra = messageParameters[2];

        if(messageArt.equals("START")){
            return "START";
        } else if(messageArt.equals("INFO")){
            return extra;
        } else if(messageArt.equals("GAMEDATA")){

            if(miniGame.equals("Rescue")){
                if(ServerData.isServer()){
                    //check if socket belongs to server's team
                    if(ServerData.getTeam(socket) == 1){
                        return extra;
                    } else {
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getOtherTeamMember(socket), "GAMEDATA_Rescue_ropeThrown_");
                    }
                } else {
                    Log.d("Processiong", "...");
                    return extra;
                }
            }

        }



        return "null";

    }

}
