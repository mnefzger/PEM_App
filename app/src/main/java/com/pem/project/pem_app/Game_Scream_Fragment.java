package com.pem.project.pem_app;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.text.DecimalFormat;



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
    private double ampl;
    private boolean won;
    private long end;
    private long start;
    private DecimalFormat df;


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
        rootView = inflater.inflate(R.layout.fragment_game__scream_, container, false);

        info = (LinearLayout)rootView.findViewById(R.id.screamInfoText);
        startScream = (TableRow)rootView.findViewById(R.id.startScream);
        screamGame = (FrameLayout)rootView.findViewById(R.id.screamGame);
        buttonStart1 = (Button) rootView.findViewById(R.id.screamButton1);
        screamTextView = (TextView) rootView.findViewById(R.id.screamField1);
        myScreamTextView= (TextView) rootView.findViewById(R.id.myScreamTxtView);
        otherScreamTextView = (TextView) rootView.findViewById(R.id.otherScreamTxtView);
        myVol=0;
        otherVol=0;
        myRoundsPlayed=0;
        roundsPlayedOtherTeam=0;
        won = false;

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

        df = new DecimalFormat("0.00");

        //Player starts
        if (mParam1.equals("Player2S")) {
            buttonStart1.setEnabled(false);
        }

        buttonStart1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //disable button after click
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonStart1.setEnabled(false);
                    }
                });

                recorder = new RecorderScreamGame();
                recorder.start();
                start = System.currentTimeMillis();
                end = start + 3 * 1000;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (System.currentTimeMillis() < end) {
                            ampl = recorder.getAmplitudeEMA();
                            Log.d("Scream Ampltude", ampl+"");
                            updateView();

                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } recorder.stop();

                        myRoundsPlayed++;
                        if (myRoundsPlayed >= 2 && roundsPlayedOtherTeam >= 2) {
                            checkIfGameWon();
                        }
                        //send to other Device
                        if (!ServerData.isServer()) {
                            //send to server
                            BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_Scream_myVolume:" + df.format(myVol) + "_\n");
                            BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_Scream_myPlayedRounds:" + myRoundsPlayed + "_\n");
                        } else {
                            // send to partner of server
                            BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(2).get(0), "GAMEDATA_Scream_myVolume:" + df.format(myVol) + "_\n");
                            BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(2).get(0), "GAMEDATA_Scream_myPlayedRounds:" + myRoundsPlayed + "_\n");
                        }
                    }
                }).start();
            }
        });
    }

    private void updateView() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (ampl > myVol) {
                    myVol = ampl;
                    screamTextView.setText(df.format(ampl));
                    myScreamTextView.setText("My loudest Scream: " + df.format(myVol));
                }else
                    screamTextView.setText(df.format(ampl));
            }

        });
    }
    public void setOtherTeamVol(final String volPartner) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                otherVol= Double.parseDouble(volPartner);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //otherScreamTextView is invisible
                        otherScreamTextView.setText("Other Team: " + volPartner.substring(9));
                    }
                });
            }

        });

    }

    private boolean checkIfGameWon() {
        if (myVol>otherVol){
            Log.d("Scream", "won!!");
            if (!ServerData.isServer()) {
                //send to server
                BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "WON_null_null_");
            } else {
                BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(2).get(0), "WON_null_null_");
            }
            ((GameActivity) getActivity()).changeFragment(Game_Lost_Fragment.newInstance("coin",1), "LOST");
            won = true;
            return won;
        }  else {
            Log.d("Scream", "lost!!");
            if (!ServerData.isServer()) {
                //send to server
                BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "LOST_null_null_");
            } else {
                BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(2).get(0), "LOST_null_null_");
            }
            ((GameActivity) getActivity()).changeFragment(Game_Lost_Fragment.newInstance("coin",1), "LOST");
        }
        won = false;
        return won;
    }

    public void getPlayedRounds(final String roundPlayed) {
        roundsPlayedOtherTeam = Integer.parseInt(roundPlayed.substring(15));

        if (myRoundsPlayed == 2 && roundsPlayedOtherTeam == 2) {
            checkIfGameWon();
        }
        //enable button to start scream
         getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buttonStart1.setEnabled(true);
            }
        });

    }

}
