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

        //check what kind of message we got [START, INFO, GAMEDATA, UPDATE, LOST]
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
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getOtherTeamMember(socket), "GAMEDATA_Rescue_" + extra + "_");
                    }

                    if(extra.equals("pitSuccess")){
                        // update ServerData and broadcast new data
                        // to do
                        for(BluetoothSocket client : ServerData.getClients()){
                            BluetoothHelper.sendDataToPairedDevice(client, "UPDATE_Null_team1_keyYellow");
                        }
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
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getOtherTeamMember(socket), "GAMEDATA_Math_" + extra + "_");
                    }
                } else {
                    return extra;
                }
            }

        } else if(messageArt.equals("UPDATE")){
           // to do
           return extra+messageParameters[3];

        } else if(messageArt.equals("LOST")){
            if(ServerData.isServer()){
                //check if socket belongs to server's team
                if(ServerData.getTeam(socket) == 1){
                    return messageArt;
                } else {
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getOtherTeamMember(socket), "LOST_null_null_");
                }
            } else {
                return messageArt;
            }
        }



        return "null";

    }

}
