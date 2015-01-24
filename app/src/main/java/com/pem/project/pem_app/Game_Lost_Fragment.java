package com.pem.project.pem_app;


import android.app.Fragment;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Game_Lost_Fragment extends Fragment {
    private String lostKey;
    private int player;

    static final String param1 = "param1";
    static final String param2 = "param2";

    public Game_Lost_Fragment() {
        // Required empty public constructor
    }

    public static Game_Lost_Fragment newInstance(String key, int player) {
        Game_Lost_Fragment fragment = new Game_Lost_Fragment();
        Bundle args = new Bundle();
        args.putString(param1, key);
        args.putInt(param2, player);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lostKey = getArguments().getString(param1);
            player = getArguments().getInt(param2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game__lost_, container, false);
        TextView message = (TextView)v.findViewById(R.id.lostMessage);

        if(checkKey("team1", lostKey)){
            message.setText("Oh no! \nYou had the " + convertKey(lostKey) + " \nbut lost it again!");
        }else{
            message.setText("You didn't get the " + convertKey(lostKey) + "!");
        }

        Button proceed = (Button) v.findViewById(R.id.proceed_button_lost);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ServerData.isServer()){
                    //send to server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "UPDATE_lost_"+lostKey+"_");
                } else {
                    for(BluetoothSocket client : ServerData.getClients()){
                        Log.d("CLIENTS", client.getRemoteDevice().getName());
                        BluetoothHelper.sendDataToPairedDevice(client, "UPDATE_lost_team1_"+lostKey+"_");
                    }
                    if(!lostKey.contains("coin")) ServerData.removeKey("team1", lostKey);
                    else{
                        ServerData.setCoin("team1", "coinno");
                        ServerData.setCoin("team2", "coinboth");
                    }
                }
                ((GameActivity)getActivity()).changeFragment(Game_Main_Fragment.newInstance(), "MAIN");
            }
        });

        //if(player == 2) proceed.setVisibility(View.INVISIBLE);

        return v;
    }

    public String convertKey(String key){
        if(key.equals("keyBlue")) return "blue key";
        if(key.equals("keyYellow")) return "yellow key";
        if(key.equals("keyGreen")) return "green key";
        if(key.equals("keyRed")) return "red key";
        if(key.contains("coin")) return "amulet";

        return "";
    }

    public boolean checkKey (String team, String key) {
        if (team == "team1") {
            return ServerData.getTeam1_keys().contains(key);
        } else
            return ServerData.getTeam2_keys().contains(key);
    }

}
