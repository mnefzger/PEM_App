package com.pem.project.pem_app;


import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Game_Won_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Game_Won_Fragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String wonKey;
    private int player;

    private ImageView image;


    public static Game_Won_Fragment newInstance(String param1, int param2) {
        Game_Won_Fragment fragment = new Game_Won_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Game_Won_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wonKey = getArguments().getString(ARG_PARAM1);
            player = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game__won_, container, false);
        image = (ImageView) v.findViewById(R.id.trophy);
        setTrophy(wonKey);
        TextView message = (TextView)v.findViewById(R.id.wonMessage);
        if(message != null) message.setText("Congratulations!\nOn the wayside, you found the " + convertKey(wonKey) + "!");

        Button proceed = (Button) v.findViewById(R.id.proceed_button);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ServerData.isServer()){
                    //send to server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "UPDATE_won_"+wonKey+"_");
                } else {
                    for(BluetoothSocket client : ServerData.getClients()){
                        BluetoothHelper.sendDataToPairedDevice(client, "UPDATE_won_team1_"+wonKey+"_");
                    }
                    if(!wonKey.contains("coin")) ServerData.addKey("team1", wonKey);
                    else{
                        ServerData.setCoin("team1", "coinBoth");
                        ServerData.setCoin("team2", "coinNo");
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

    public void setTrophy(String key){
        if(key.equals("keyBlue")) image.setImageResource(R.drawable.key_blue_yes);
        if(key.equals("keyYellow")) image.setImageResource(R.drawable.key_yellow_yes);
        if(key.equals("keyGreen")) image.setImageResource(R.drawable.key_green_yes);
        if(key.equals("keyRed")) image.setImageResource(R.drawable.key_red_yes);
        if(key.contains("coin")) image.setImageResource(R.drawable.coinboth);
    }


}
