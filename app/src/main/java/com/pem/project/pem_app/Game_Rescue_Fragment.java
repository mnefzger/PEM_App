package com.pem.project.pem_app;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.hardware.*;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Game_Rescue_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Game_Rescue_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Game_Rescue_Fragment extends Fragment implements SensorHandler.SensorCallback{
        private OnFragmentInteractionListener mListener;
        private SensorManager sm;
        TextView distanceText;
        private FrameLayout info;
        private FrameLayout info2;
        private FrameLayout rescue;
        private FrameLayout rescue2;

        //differentiate players' parts
        static final String param1 = "param1";
        String mode;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     */
    // TODO: Rename and change types and number of parameters
    public static Game_Rescue_Fragment newInstance(String m) {
        Game_Rescue_Fragment fragment = new Game_Rescue_Fragment();
        Bundle args = new Bundle();
        args.putString(param1, m);
        fragment.setArguments(args);
        return fragment;
    }

    public Game_Rescue_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getString(param1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_game_rescue, container, false);
        info = (FrameLayout)v.findViewById(R.id.theRescueIntroText);
        info2 = (FrameLayout)v.findViewById(R.id.theRescueIntroText2);
        rescue = (FrameLayout)v.findViewById(R.id.theRescueLayout);
        rescue2 = (FrameLayout)v.findViewById(R.id.theRescueLayout2);


        if(!mode.equals("rope")){
            info.setVisibility(View.INVISIBLE);
            rescue.setVisibility(View.INVISIBLE);
        } else{
            info2.setVisibility(View.INVISIBLE);
        }

        Button startTheRescue = (Button)v.findViewById(R.id.startTheRescue);
        startTheRescue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setVisibility(View.GONE);
                rescue.setVisibility(View.VISIBLE);
                startSensing();
            }
        });

        distanceText = (TextView)v.findViewById(R.id.distanceText);

        return v;
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

    public void startSensing(){
        new SensorHandler(this, getActivity().getApplicationContext());
    }

    @Override
    public void dataSensed(double[] data) {
        distanceText.setText("You have thrown the rope\n" + data[0] + " meters!\n\n" +
                "Your partner can now begin to climb out..." );
        if (!ServerData.isServer()){
            //send to server
            BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_Rescue_ropeThrown_");
        } else {
            // send to partner of server
            BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_Rescue_ropeThrown_");
        }
    }

    public void ropeIsThrown(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                info2.setVisibility(View.INVISIBLE);
                rescue2.setVisibility(View.VISIBLE);
            }
        });
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
