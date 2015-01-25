package com.pem.project.pem_app;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Matthias on 23.11.2014.
 *
 * Contains data that is stored on the server, such as client sockets, game variables etc.
 */
public class ServerData {
    private static ArrayList<BluetoothSocket> clients = new ArrayList<BluetoothSocket>();
    private static ArrayList<BluetoothSocket> team1 = new ArrayList<BluetoothSocket>();
    private static ArrayList<BluetoothSocket> team2 = new ArrayList<BluetoothSocket>();
    private static ArrayList<String> team1_keys = new ArrayList<String>();
    private static ArrayList<String> team2_keys = new ArrayList<String>();
    private static String team1_coin = "coinleft";
    private static String team2_coin = "coinright";
    private static boolean isServer = false;
    private static BluetoothSocket server;
    private static int myTeam;

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

    public static void setMyTeam(int team){
        myTeam = team;
    }

    public static int getMyTeam(){
        return myTeam;
    }


    public static ArrayList<BluetoothSocket> getTeamMembers(int team){
        if(team == 1) return team1;
        else return team2;
    }

    public static BluetoothSocket getOtherTeamMember(BluetoothSocket s){
        int team = getTeam(s);
        if(team == 1){
            return team1.get(0);
        }else{
            BluetoothSocket member = (team2.get(0) == s)? team2.get(1) : team2.get(0);
            return member;
        }
    }


    public static void addKey(String team, String key){
        Log.d("ADD", team+", "+key);

        if(team.equals("team1")){
            if(!team1_keys.contains(key))
                team1_keys.add(key);
        }else{ // team2
            if(!team2_keys.contains(key))
                team2_keys.add(key);
        }
    }

    public static void removeKey(String team, String key){
        Log.d("REMOVE", team+", "+key);

        if(team.equals("team1")){
            if(team1_keys.contains(key))
                team1_keys.remove(key);
        }else{
            if(team2_keys.contains(key))
                team2_keys.remove(key);
        }
    }

    public static void setCoin(String team, String coin){
        Log.d("COIN", coin);
        if(team.equals("team1")){
            team1_coin = coin;
        } else {
            team2_coin = coin;
        }
    }

    // remove random key after LUCK minigame failure
    public static String removeRandomKey(String team){
        String removedKey = "";
        if(team.equals("team1")){
            if(!team1_keys.isEmpty()){
                int s = team1_keys.size();
                Random random = new Random();
                int r = random.nextInt(s);
                removedKey = team1_keys.get(r);
                team1_keys.remove(r);
            }
        }else{ // team2
            if(!team2_keys.isEmpty()){
                int s = team2_keys.size();
                Random random = new Random();
                int r = random.nextInt(s);
                removedKey = team2_keys.get(r);
                team2_keys.remove(r);
            }
        }
        return removedKey;
    }

     // for updating the view
    public static ArrayList<String> getTeam1_keys(){
        return team1_keys;
    }
    public static ArrayList<String> getTeam2_keys(){
        return team2_keys;
    }
    public static String getTeam1_coin(){
        return team1_coin;
    }
    public static String getTeam2_coin(){
        return team2_coin;
    }



    //only the server calls this method
    public static void markLocalAsServer(){
        isServer = true;
    }
    public static boolean isServer(){
        return isServer;
    }

    //only the clients call this method
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
