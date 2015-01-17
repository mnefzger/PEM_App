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
            if(miniGame.equals("null")){
                return "START";
            } else if(miniGame.equals("Scream")){
                if (ServerData.isServer()) {
                    //check if socket belongs to server's team
                    if (ServerData.getTeam(socket) == 1) {
                        //this message is meant for the enemy
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(2).get(0), "START_" + miniGame + "_" + extra + "_");
                    } else {
                        //this message is meant for the server
                        return "START_" + miniGame + "_" + extra + "_";
                    }
                } else {
                    return "START_" + miniGame + "_" + extra + "_";
                }
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
                        if(miniGame.equals("Scream")){
                            BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(2).get(0), "START_" + miniGame + "_" + extra + "_");
                        } else return extra;
                    } else {
                        if(miniGame.equals("Scream")){
                            return extra;
                        } else {
                            BluetoothHelper.sendDataToPairedDevice(ServerData.getOtherTeamMember(socket), "GAMEDATA_" + miniGame + "_" + extra + "_");
                        }

                    }

                } else {
                    return extra;
                }


        } else if(messageArt.equals("UPDATE")){
            if(ServerData.isServer()){
                int team = ServerData.getTeam(socket);
                String t = "team"+team;

                if(extra.equals("pitSuccess")){
                    for(BluetoothSocket client : ServerData.getClients()){
                        BluetoothHelper.sendDataToPairedDevice(client, "UPDATE_Rescue_"+t+"_keyYellow_");
                    }
                    return "UPDATE_"+t+"_keyYellow_";
                }
                if(extra.equals("mathRunesSuccess")){
                    for(BluetoothSocket client : ServerData.getClients()){
                        BluetoothHelper.sendDataToPairedDevice(client, "UPDATE_MathRunes_"+t+"_keyGreen_");
                    }
                    return "UPDATE_"+t+"_keyGreen_";
                }
                if(extra.equals("luckSuccess")){
                    for(BluetoothSocket client : ServerData.getClients()){
                        BluetoothHelper.sendDataToPairedDevice(client, "UPDATE_Luck_"+t+"_keyBlue_");
                    }

                    return "UPDATE_"+t+"_keyBlue_";
                }
                if(extra.equals("fightSuccess")){
                    // to do
                    for(BluetoothSocket client : ServerData.getClients()){
                        //BluetoothHelper.sendDataToPairedDevice(client, "UPDATE_Fight_"+t+"_keyBlue_");
                    }
                }


            } else {
                // e.g "UPDATE_team1_keyBlue"
                return messageArt+"_"+extra+"_"+messageParameters[3]+"_";
            }

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
