package com.pem.project.pem_app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


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
    private Button startMiniGameButton4;


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

        View view =  inflater.inflate(R.layout.fragment_game_main, container, false);


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


        ImageView team1_coin = (ImageView) view.findViewById(R.id.team1_coin);
        ImageView team2_coin = (ImageView) view.findViewById(R.id.team2_coin);

        ImageView team1_keyyellow = (ImageView) view.findViewById(R.id.team1_keyyellow);
        ImageView team1_keyred = (ImageView) view.findViewById(R.id.team1_keyred);
        ImageView team1_keygreen = (ImageView) view.findViewById(R.id.team1_keygreen);
        ImageView team1_keyblue = (ImageView) view.findViewById(R.id.team1_keyblue);

        ImageView team2_keyyellow = (ImageView) view.findViewById(R.id.team2_keyyellow);
        ImageView team2_keyred = (ImageView) view.findViewById(R.id.team2_keyred);
        ImageView team2_keygreen = (ImageView) view.findViewById(R.id.team2_keygreen);
        ImageView team2_keyblue = (ImageView) view.findViewById(R.id.team2_keyblue);

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

        startMiniGameButton4 = (Button)view.findViewById(R.id.startMiniGameButton4);
        startMiniGameButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GameActivity)getActivity()).changeFragment(Game_Scream_Fragment.newInstance("Player1S",""), "SCREAM");

                if (!ServerData.isServer()){
                    //send to server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "START_Scream_Player2S_");
                } else {
                    // send to partner of server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "START_Scream_Player2S_");
                }
            }
        });

        return view;
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
