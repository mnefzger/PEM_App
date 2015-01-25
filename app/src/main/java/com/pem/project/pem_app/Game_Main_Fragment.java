package com.pem.project.pem_app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Game_Main_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Game_Main_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Game_Main_Fragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private Button startMiniGameButton;
    private Button startMiniGameButton2;
    private Button startMiniGameButton3;
    private Button startMiniGameButton5;
    private Button startMiniGameButton6;

    TextView team1;
    TextView team2;
    ImageView team1_coin;
    ImageView team2_coin;
    ImageView team1_keyYellow;
    ImageView team1_keyRed;
    ImageView team1_keyGreen;
    ImageView team1_keyBlue;
    ImageView team2_keyYellow;
    ImageView team2_keyRed;
    ImageView team2_keyGreen;
    ImageView team2_keyBlue;

    private View view;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     */

    public static Game_Main_Fragment newInstance() {
        Game_Main_Fragment fragment = new Game_Main_Fragment();
        return fragment;
    }

    public Game_Main_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_game_main, container, false);


        Button qrcode = (Button) view.findViewById(R.id.scanQRButton);
        Button fight = (Button) view.findViewById(R.id.fightButton);


        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Context context = getActivity().getApplicationContext();
                Intent intent= new Intent();
                intent.setClassName(context.getPackageName(), context.getPackageName()+".QRScanActivity");
                startActivity(intent);*/
                ((GameActivity)getActivity()).changeFragment(QR_Fragment.newInstance(), "QR");
            }
        });

        fight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GameActivity)getActivity()).changeFragment(Game_Scream_Fragment.newInstance("Player1S",""), "SCREAM");

                if (!ServerData.isServer()){
                    //send to server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "START_Scream_Player2S_");
                } else {
                    // send to enemy of server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(2).get(0), "START_Scream_Player2S_");
                }
            }
        });

        team1 = (TextView)view.findViewById(R.id.team1);
        team2 = (TextView)view.findViewById(R.id.team2);

        team1_coin = (ImageView) view.findViewById(R.id.team1_coin);
        team2_coin = (ImageView) view.findViewById(R.id.team2_coin);

        team1_keyYellow = (ImageView) view.findViewById(R.id.team1_keyYellow);
        team1_keyRed = (ImageView) view.findViewById(R.id.team1_keyRed);
        team1_keyGreen = (ImageView) view.findViewById(R.id.team1_keyGreen);
        team1_keyBlue = (ImageView) view.findViewById(R.id.team1_keyBlue);

        team2_keyYellow = (ImageView) view.findViewById(R.id.team2_keyYellow);
        team2_keyRed = (ImageView) view.findViewById(R.id.team2_keyRed);
        team2_keyGreen = (ImageView) view.findViewById(R.id.team2_keyGreen);
        team2_keyBlue = (ImageView) view.findViewById(R.id.team2_keyBlue);




        // testing buttons

        startMiniGameButton = (Button)view.findViewById(R.id.startMiniGameButton);
        startMiniGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GameActivity)getActivity()).changeFragment(Game_Rescue_Fragment.newInstance("rope"), "RESCUE");

                if (!ServerData.isServer()){
                    //send to server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "START_Rescue_ropeWait_");
                } else {
                    // send to partner of server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "START_Rescue_ropeWait_");
                }
            }
        });

        startMiniGameButton2 = (Button)view.findViewById(R.id.startMiniGameButton2);
        startMiniGameButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GameActivity)getActivity()).changeFragment(Game_Math_Fragment.newInstance("Player1",""), "MATH");

                if (!ServerData.isServer()){
                    //send to server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "START_Math_Player2_");
                } else {
                    // send to partner of server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "START_Math_Player2_");
                }
            }
        });

        startMiniGameButton6 = (Button)view.findViewById(R.id.startMiniGameButton6);
        startMiniGameButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GameActivity)getActivity()).changeFragment(Game_MathRunes_Fragment.newInstance("Player1",""), "MATHRUNES");

                if (!ServerData.isServer()){
                    //send to server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "START_MathRunes_Player2_");
                } else {
                    // send to partner of server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "START_MathRunes_Player2_");
                }
            }
        });

        startMiniGameButton3 = (Button)view.findViewById(R.id.startMiniGameButton3);
        startMiniGameButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GameActivity)getActivity()).changeFragment(Game_Run_Fragment.newInstance(), "RUN");

                if (!ServerData.isServer()){
                    //send to server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "START_Run_runInfo_");
                } else {
                    // send to partner of server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "START_Run_runInfo_");
                }

            }
        });
        startMiniGameButton5 = (Button)view.findViewById(R.id.startMiniGameButton5);
        startMiniGameButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GameActivity)getActivity()).changeFragment(Game_Luck_Fragment.newInstance(), "LUCK");
            /*
                if (!ServerData.isServer()){
                    //send to server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "START_Luck_Player2_");
                } else {
                    // send to partner of server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "START_Luck_Player2_");
                }
                */
            }
        });
        updateGUI();


        return view;
    }


    public void updateGUI(){
        // retrieve data
        String coin1 = ServerData.getTeam1_coin();
        String coin2 = ServerData.getTeam2_coin();
        ArrayList<String> keys1 = ServerData.getTeam1_keys();
        ArrayList<String> keys2 = ServerData.getTeam2_keys();

        int team = ServerData.getMyTeam();

        if(team == 1) team1.setText("Team Blue (You)");
        else team2.setText("Team Red (You)");

        // update game state view
        // -> coins
        if(coin1.equals("coinleft")){
            team1_coin.setImageResource(R.drawable.coinleft);
        }else if(coin1.equals("coinright")){
            team1_coin.setImageResource(R.drawable.coinright);
        }else if(coin1.equals("coinboth")){
            team1_coin.setImageResource(R.drawable.coinboth);
        }else {
            team1_coin.setImageResource(R.drawable.coinno);
        }
        if(coin2.equals("coinleft")){
            team2_coin.setImageResource(R.drawable.coinleft);
        }else if(coin2.equals("coinright")){
            team2_coin.setImageResource(R.drawable.coinright);
        }else if(coin2.equals("coinboth")){
            team2_coin.setImageResource(R.drawable.coinboth);
        }else {
            team2_coin.setImageResource(R.drawable.coinno);
        }

        // -> keys
        if(keys1.contains("keyYellow")){
            team1_keyYellow.setImageResource(R.drawable.key_yellow_yes);
        }else{
            team1_keyYellow.setImageResource(R.drawable.key_yellow_no);
        }
        if(keys1.contains("keyRed")){
            team1_keyRed.setImageResource(R.drawable.key_red_yes);
        }else{
            team1_keyRed.setImageResource(R.drawable.key_red_no);
        }
        if(keys1.contains("keyGreen")){
            team1_keyGreen.setImageResource(R.drawable.key_green_yes);
        }else{
            team1_keyGreen.setImageResource(R.drawable.key_green_no);
        }
        if(keys1.contains("keyBlue")){
            team1_keyBlue.setImageResource(R.drawable.key_blue_yes);
        }else{
            team1_keyBlue.setImageResource(R.drawable.key_blue_no);
        }

        if(keys2.contains("keyYellow")){
            team2_keyYellow.setImageResource(R.drawable.key_yellow_yes);
        }else{
            team2_keyYellow.setImageResource(R.drawable.key_yellow_no);
        }
        if(keys2.contains("keyRed")){
            team2_keyRed.setImageResource(R.drawable.key_red_yes);
        }else{
            team2_keyRed.setImageResource(R.drawable.key_red_no);
        }
        if(keys2.contains("keyGreen")){
            team2_keyGreen.setImageResource(R.drawable.key_green_yes);
        }else{
            team2_keyGreen.setImageResource(R.drawable.key_green_no);
        }
        if(keys2.contains("keyBlue")){
            team2_keyBlue.setImageResource(R.drawable.key_blue_yes);
        }else{
            team2_keyBlue.setImageResource(R.drawable.key_blue_no);
        }

        view.invalidate();

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction();
    }

}
