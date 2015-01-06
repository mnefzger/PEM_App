package com.pem.project.pem_app;


import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



/**
 * A simple {@link Fragment} subclass.
 */
public class Game_Run_Fragment extends Fragment implements SensorHandler.runCallback{
    private SensorHandler sensorHandler;
    private TextView runSpeed;
    private RelativeLayout info;
    private FrameLayout runGame;
    private ImageView runIndicator;
    private ImageView leftFoot;
    private ImageView rightFoot;
    private boolean animated = false;
    private int fails = 0;
    private Animation anim1;
    private Animation anim2;
    private Animation anim;

    public Game_Run_Fragment() {
        // Required empty public constructor
    }


    public static Game_Run_Fragment newInstance() {
        Game_Run_Fragment fragment = new Game_Run_Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game__run_, container, false);
        info = (RelativeLayout)v.findViewById(R.id.runInfoText);
        runGame = (FrameLayout)v.findViewById(R.id.runGame);
        runSpeed = (TextView)v.findViewById(R.id.runSpeed);
        runIndicator = (ImageView)v.findViewById(R.id.runIndicator);
        leftFoot  =(ImageView)v.findViewById(R.id.left_foot);
        rightFoot  =(ImageView)v.findViewById(R.id.right_foot);
        anim1 = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_run_1);
        anim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_run_2);
        anim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_run);
        anim1.setAnimationListener(l);
        anim2.setAnimationListener(l);


        Button startRun = (Button)v.findViewById(R.id.startRun);
        startRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ServerData.isServer()){
                    //send to server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_Run_startRunning_");
                } else {
                    // send to partner of server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_Run_startRunning_");
                }
                startRunning();
            }
        });

        return v;
    }

    public void startRunning(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                info.setVisibility(View.GONE);
                runGame.setVisibility(View.VISIBLE);
                runSpeed.setText("3");


                Handler one = new Handler();
                one.postDelayed(new Runnable() {
                                     @Override
                                     public void run() {
                                         runSpeed.setText("2");
                                     }},
                        1000
                );
                Handler two = new Handler();
                two.postDelayed(new Runnable() {
                                     @Override
                                     public void run() {
                                         runSpeed.setText("1");
                                     }},
                        2000
                );

                Handler three = new Handler();
                three.postDelayed(new Runnable() {
                                     @Override
                                     public void run() {
                                         sensorHandler = new SensorHandler(getFragmentManager().findFragmentByTag("RUN"), getActivity().getApplicationContext(), "shake");
                                         runSpeed.setText("Run!");
                                     }},
                        3000
                );


                Handler wait = new Handler();
                wait.postDelayed(new Runnable() {
                                     @Override
                                     public void run() {
                                         sensorHandler.stopSensing();
                                         runSpeed.setText("geschafft");
                                     }},
                        13000
                );
            }
        });

    }


    @Override
    public void runSensed(double[] data) {
        // too slow
        if(Math.abs(data[0]) < 8){

                if(animated == false) {
                    if (fails == 0) {
                        runIndicator.startAnimation(anim1);
                    }
                    if (fails == 1) {
                        runIndicator.startAnimation(anim2);
                    }
                }

        }
        // player failed two times -> lost
        if(fails == 2){
            sensorHandler.stopSensing();
            if(!ServerData.isServer()) {
                BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "LOST_null_null_");
            } else {
                BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "LOST_null_null_");
            }
            ((GameActivity) getActivity()).changeFragment(Game_Lost_Fragment.newInstance(), "LOST");
        }
    }


    Animation.AnimationListener l = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            animated = true;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            animated  = false;
            fails++;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
}
