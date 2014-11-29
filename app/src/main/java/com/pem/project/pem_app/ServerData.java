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
    private static ArrayList<BluetoothSocket> team1 = new ArrayList<BluetoothSocket>();
    private static ArrayList<BluetoothSocket> team2 = new ArrayList<BluetoothSocket>();
    private static boolean isServer = false;
    private static BluetoothSocket server;

    public static void addToClients(BluetoothSocket socket){
        Log.d("Bluetooth ServerData", "Added Client " + socket);
        clients.add(socket);
    }

    public static void addToTeam(BluetoothSocket socket, int team){
        if(team == 1) team1.add(socket);
        else team2.add(socket);
    }

    public static void removeFromTeam(BluetoothSocket socket){
        if(team1.contains(socket)) team1.remove(socket);
        else team2.remove(socket);
    }

    public static int getTeam(BluetoothSocket socket){
        if(team1.contains(socket)) return 1;
        else if(team2.contains(socket)) return 2;
        else return 0;
    }

    //only the server calls this method
    public static void markLocalAsServer(){
        isServer = true;
    }
    public static boolean isServer(){
        return isServer;
    }

    public static void markRemoteAsServer(BluetoothSocket socket){
        server = socket;
    }
    public static BluetoothSocket getServer(){
        return server;
    }

    public static ArrayList<BluetoothSocket> getClients(){
        return clients;
    }

    public static BluetoothSocket getClientAt(int pos){
        return clients.get(pos);
    }

    public static int getNumOfClients(){
        return clients.size();
    }

}
