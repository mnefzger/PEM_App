package com.pem.project.pem_app;

import android.app.Activity;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.os.SystemClock.sleep;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Game_Scream_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Game_Scream_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Game_Scream_Fragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View rootView;
    private MediaRecorder mRecorder;
    private TextView screamTextView;
    private TextView otherScreamTextView;
    private TextView myScreamTextView;
    private RecorderScreamGame recorder;
    private Button buttonStart1;
    private TableRow startScream;
    private Thread thread;
    private int player;
    private double myVol;
    private double otherVol;
    private FrameLayout screamGame;
    private LinearLayout info;
    private int myRoundsPlayed;
    private int roundsPlayedOtherTeam;
    private boolean isPlaying;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Game_Scream_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Game_Scream_Fragment newInstance(String param1, String param2) {
        Game_Scream_Fragment fragment = new Game_Scream_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Game_Scream_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (mParam1.equals("Player2S")){
            player = 2;
            rootView = inflater.inflate(R.layout.fragment_game__scream_2, container, false);
        } else {
            player = 1;
            rootView = inflater.inflate(R.layout.fragment_game__scream_, container, false);
        }

        info = (LinearLayout)rootView.findViewById(R.id.screamInfoText);
        startScream = (TableRow)rootView.findViewById(R.id.startScream);
        screamGame = (FrameLayout)rootView.findViewById(R.id.screamGame);
        myRoundsPlayed=0;
        roundsPlayedOtherTeam=0;

        if(player==1){
            isPlaying=true;
        }

        startScream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            info.setVisibility(View.GONE);
            screamGame.setVisibility(View.VISIBLE);
            createMediaRecorder();
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void createMediaRecorder() {
        isPlaying=false;
        buttonStart1 = (Button) rootView.findViewById(R.id.screamButton1);
        screamTextView = (TextView) rootView.findViewById(R.id.screamField1);
        myScreamTextView= (TextView) rootView.findViewById(R.id.myScreamTxtView);
        otherScreamTextView = (TextView) rootView.findViewById(R.id.otherScreamTxtView);

        buttonStart1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                myRoundsPlayed= myRoundsPlayed+1;
                recorder = new RecorderScreamGame();
                recorder.start();

/*
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(30);
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                double ampl= recorder.getAmplitudeEMA();
                screamTextView.setText(ampl +"");
            }
        }, 0, 1, TimeUnit.SECONDS);
*/

/*
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textview1;
                TextView textview2;

                textview1 = (TextView) rootView.findViewById(R.id.result1);
                textview2 = (TextView) rootView.findViewById(R.id.result2);

                if (processed.startsWith("result1")) {
                    textview1.setText(processed.substring(8));
                    result1_partner = Integer.parseInt(processed.substring(8));
                } else if (processed.startsWith("result2")){
                    textview2.setText(processed.substring(8));
                    result2_partner = Integer.parseInt(processed.substring(8));
                }


            }
        });


*/

                for (int i=0; i<10; i++){
                    double ampl= recorder.getAmplitudeEMA();
                    screamTextView.setText(ampl +"");
                    myVol=ampl;
                }

               // buttonStart1.setEnabled(true);

                recorder.stop();

                //send Volume to other Device
                if (!ServerData.isServer()) {
                    //send to server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_Scream_myVolume:" + myVol + "_\n");
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_Scream_myRounds:" + myRoundsPlayed + "_\n");
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_Scream_isPlaying:" + isPlaying + "_\n");
                } else {
                    // send to partner of server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_Scream_myVolume:" + myVol + "_\n");
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_Scream_myRounds:" + myRoundsPlayed + "_\n");
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_Scream_isPlaying:" + isPlaying + "_\n");

                }

            }
        });
    }

    private boolean checkIfGameWon() {
        if (myVol>otherVol){
            Log.d("Scream", "won!!");
            if (!ServerData.isServer()) {
                //send to server
                BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "WON_null_null_");
             //   BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_Scream_end:" + "Iwon" + "_\n");
            } else {
                // send to partner of server
                BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "WON_null_null_");
           //     BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_Scream_end:" +"Iwon" + "_\n");
            }
            ((GameActivity) getActivity()).changeFragment(Game_Lost_Fragment.newInstance(), "WON");
            return true;
        }  else {
            Log.d("Scream", "lost!!");
            if (!ServerData.isServer()) {
                //send to server
                BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "LOST_null_null_");
       //         BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_Scream_end:" + "Ilost" + "_\n");
            } else {
                // send to partner of server
                BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "LOST_null_null_");
  //              BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_Scream_end:" +"Ilost" + "_\n");
            }
            ((GameActivity) getActivity()).changeFragment(Game_Lost_Fragment.newInstance(), "LOST");
        }
        return false;
    }

    public void setOtherVol(final String volPartner) {

        /*

        otherScreamTextView.setText(otherVol+"");
        myScreamTextView.setText(myVol+"");
        */
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                otherScreamTextView.setText(volPartner.substring(9));
            }
        });
    }

    public void getPlayedRounds(final String roundPlayed) {
        roundsPlayedOtherTeam = Integer.parseInt(roundPlayed.substring(9));
        Log.d(roundsPlayedOtherTeam + " otherTeam", "Saron");
        Log.d(myRoundsPlayed + " Ich", "Saron");

        if (myRoundsPlayed >= 2 && roundsPlayedOtherTeam >= 2)
            checkIfGameWon();

    }
    public void getWhosTurn(final String whosPlaying) {
  //      if(whosPlaying.substring(10)=="true"){
   //     buttonStart1.setEnabled(false);

    }

    public void endGame(final String processed) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (processed.substring(4) == "Ilost") {
                    Log.d("Scream", "won!!");
                    if (!ServerData.isServer()) {
                        //send to server
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "WON_null_null");
                    } else {
                        // send to partner of server
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "WON_null_null");
                    }
                    ((GameActivity) getActivity()).changeFragment(Game_Lost_Fragment.newInstance(), "WON");
                } else if (processed.substring(4) == "Iwon") {
                    Log.d("Scream", "lost!!");
                    if (!ServerData.isServer()) {
                        //send to server
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "LOST_null_null_");
                    } else {
                        // send to partner of server
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "LOST_null_null_");
                    }
                    ((GameActivity) getActivity()).changeFragment(Game_Lost_Fragment.newInstance(), "LOST");
                }
            }

        });
    }
}
