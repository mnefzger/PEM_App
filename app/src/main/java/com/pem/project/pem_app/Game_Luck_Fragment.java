package com.pem.project.pem_app;

import android.app.Fragment;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import android.view.LayoutInflater;
import android.view.ViewGroup;

public class Game_Luck_Fragment extends Fragment implements View.OnClickListener{
    private TextView luckText;
    private ImageButton path1, path2, path3;
    private Button done;
    private Random r;
    private int chance = 0;
    private String outcome = "";


    public Game_Luck_Fragment(){
    }

    public static Game_Luck_Fragment newInstance(){
        Game_Luck_Fragment fragment = new Game_Luck_Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game_luck, container, false);

        luckText = (TextView)v.findViewById(R.id.luckText);
        path1 = (ImageButton)v.findViewById(R.id.luckPath1);
        path2 = (ImageButton)v.findViewById(R.id.luckPath2);
        path3 = (ImageButton)v.findViewById(R.id.luckPath3);
        done = (Button)v.findViewById(R.id.luckDone);


        View.OnClickListener askFate = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playLucky();
            }
        };

        path1.setOnClickListener(askFate);
        path2.setOnClickListener(askFate);
        path3.setOnClickListener(askFate);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(!ServerData.isServer()) {
                    if (outcome.equals("success"))
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "UPDATE_Luck_luckSuccess");
                    else if (outcome.equals("fail"))
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "UPDATE_Luck_luckFail");
                    else
                        ((GameActivity) getActivity()).changeFragment(Game_Main_Fragment.newInstance(), "MAIN");
                }*/

                if (!ServerData.isServer()){
                    //send to server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "UPDATE_Luck_luckSuccess_");
                } else {
                    for(BluetoothSocket client : ServerData.getClients()){
                        BluetoothHelper.sendDataToPairedDevice(client, "UPDATE_Luck_team1_keyBlue_");
                    }
                    ServerData.toggleKey("team1", "keyBlue");
                    ((GameActivity)getActivity()).changeFragment(Game_Main_Fragment.newInstance(), "MAIN");
                }
            }
        });

        return v;

    }

    public void playLucky(){
        path1.setVisibility(View.INVISIBLE);
        path2.setVisibility(View.INVISIBLE);
        path3.setVisibility(View.INVISIBLE);

        //randomly generate result
        r = new Random();
        chance = r.nextInt(10) + 1;
        if(chance<=5){
            //50% lucky
            luckText.setText("\nBy the wayside of the path you chose, you found a key!");
            outcome = "success";
            path2.setImageResource(R.drawable.key_yellow_yes);
            path2.setEnabled(false);
        }else if(chance<=8){
            //30% neutral - no pain & no gain
            luckText.setText("\nYou continue your way along the chosen path.");
        }else{
            //20% bad luck
            luckText.setText("\nJust around the corner, a raging rhino appears and starts to charge after you with heavy stomps! \n\n You manage to escape up a tree just in time, but unfortunately you lost a key on the way.");
            outcome = "fail";
        }
        chance = 0;
        // open result fragment with accept button to return to game screen
        done.setEnabled(true);
        done.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
    }

}