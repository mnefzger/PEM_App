package com.pem.project.pem_app;

import android.bluetooth.BluetoothSocket;

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
            if(miniGame.equals("null")){
                return "START";
            } else {
                if (ServerData.isServer()) {
                    //check if socket belongs to server's team
                    if (ServerData.getTeam(socket) == 1) {
                        //this message is meant for the server
                        return "START_" + miniGame + "_" + extra + "_";
                    } else {
                        //this message is meant for a player of the opposite team
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getOtherTeamMember(socket), "START_" + miniGame + "_" + extra + "_");
                    }
                } else {
                    return "START_" + miniGame + "_" + extra + "_";
                }
            }
        } else if(messageArt.equals("INFO")){

            return extra;

        } else if(messageArt.equals("GAMEDATA")){

                if(ServerData.isServer()){
                    //check if socket belongs to server's team
                    if(ServerData.getTeam(socket) == 1){
                        return extra;
                    } else {
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getOtherTeamMember(socket), "GAMEDATA_"+ miniGame + "_" + extra + "_");
                    }

                    if(extra.equals("pitSuccess")){
                        // update ServerData and broadcast new data
                        // to do
                        for(BluetoothSocket client : ServerData.getClients()){
                            BluetoothHelper.sendDataToPairedDevice(client, "UPDATE_null_team1_keyYellow");
                        }
                    }
                    if(extra.equals("mathSuccess")){
                        // update ServerData and broadcast new data
                        // to do
                        for(BluetoothSocket client : ServerData.getClients()){
                            BluetoothHelper.sendDataToPairedDevice(client, "UPDATE_null_team1_keyBlue");
                        }
                    }

                } else {
                    return extra;
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
        } else if(messageArt.equals("WON")){
            if(ServerData.isServer()){
                //check if socket belongs to server's team
                if(ServerData.getTeam(socket) == 1){
                    return messageArt;
                } else {
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getOtherTeamMember(socket), "WON_null_null_");
                }
            } else {
                return messageArt;
            }
        }



        return "null";

    }

}
