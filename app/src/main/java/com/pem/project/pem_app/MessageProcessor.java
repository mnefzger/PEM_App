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

        //check what kind of message we got [START, INFO, GAMEDATA]
        if(messageArt.equals("START")){
            //default
            if(miniGame.equals("null"))  return "START";

            if(miniGame.equals("Rescue")){
                if(ServerData.isServer()){
                    //check if socket belongs to server's team
                    if(ServerData.getTeam(socket) == 1){
                        return extra;
                    } else {
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getOtherTeamMember(socket), "START_Rescue_ropeWait_");
                    }
                } else {
                    return extra;
                }
            }
            if(miniGame.equals("Math")){
                if(ServerData.isServer()){
                    //check if socket belongs to server's team
                    if(ServerData.getTeam(socket) == 1){
                        return extra;
                    } else {
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getOtherTeamMember(socket), "START_Math_Player2_");
                    }
                } else {
                    return extra;
                }
            }
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
                    Log.d("Processing", "...");
                    return extra;
                }
            }

            if(miniGame.equals("Math")){
                if(ServerData.isServer()){
                    //check if socket belongs to server's team
                    if(ServerData.getTeam(socket) == 1){
                        return extra;
                    } else {
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getOtherTeamMember(socket), "GAMEDATA_Math_" + extra + "_");
                    }
                } else {
                    Log.d("Processing", "...");
                    return extra;
                }
            }

        }



        return "null";

    }

}
