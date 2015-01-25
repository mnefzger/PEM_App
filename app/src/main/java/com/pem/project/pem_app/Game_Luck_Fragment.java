package com.pem.project.pem_app;

import android.app.Fragment;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Random;

import android.view.LayoutInflater;
import android.view.ViewGroup;

public class Game_Luck_Fragment extends Fragment implements View.OnClickListener{
    private LinearLayout luckInfoText;
    private TableRow startLuck;
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

        luckInfoText = (LinearLayout)v.findViewById(R.id.luckInfoText);
        startLuck = (TableRow) v.findViewById(R.id.startLuck);

        luckText = (TextView)v.findViewById(R.id.luckText);
        path1 = (ImageButton)v.findViewById(R.id.luckPath1);
        path2 = (ImageButton)v.findViewById(R.id.luckPath2);
        path3 = (ImageButton)v.findViewById(R.id.luckPath3);
        done = (Button)v.findViewById(R.id.luckDone);

        startLuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                luckInfoText.setVisibility(View.GONE);
            }
        });


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

                if (!ServerData.isServer()){
                    //send to server
                    if(outcome.equals("success")) BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "UPDATE_won_keyYellow_");
                    if(outcome.equals("fail")) BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "UPDATE_lost_keyYellow_");
                } else {
                    if(outcome.equals("success")){
                        for(BluetoothSocket client : ServerData.getClients()){
                            BluetoothHelper.sendDataToPairedDevice(client, "UPDATE_won_team1_keyYellow_");
                        }
                        ServerData.addKey("team1", "keyYellow");
                    } else if(outcome.equals("fail")){
                        for(BluetoothSocket client : ServerData.getClients()){
                            BluetoothHelper.sendDataToPairedDevice(client, "UPDATE_lost_team1_keyYellow_");
                        }
                        ServerData.removeKey("team1", "keyYellow");
                    }
                }

                ((GameActivity)getActivity()).changeFragment(Game_Main_Fragment.newInstance(), "MAIN");


            }
        });

        return v;

    }

    public void playLucky(){
        path1.setVisibility(View.GONE);
        path2.setVisibility(View.GONE);
        path3.setVisibility(View.GONE);

        //randomly generate result
        r = new Random();
        chance = r.nextInt(10) + 1;
        if(chance<=4){
            //40% lucky
            luckText.setText("\nBy the wayside of the path you chose, you found a key!");
            outcome = "success";
            path2.setImageResource(R.drawable.key_yellow_yes);
            path2.setEnabled(false);
        }else if(chance<=8){
            //40% neutral - no pain & no gain
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