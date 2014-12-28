package com.pem.project.pem_app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.hardware.*;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Game_Rescue_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Game_Rescue_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Game_Rescue_Fragment extends Fragment implements SensorHandler.SensorCallback, View.OnClickListener{
        private OnFragmentInteractionListener mListener;
        private Display display;
        private Point size;
        private TextView distanceText;
        private TextView pitText;
        private Button startTheRescue;
        private Button startTheRescue2;
        private Button backToMain;
        private RelativeLayout info;
        private RelativeLayout info2;
        private FrameLayout rescue;
        private FrameLayout rescue2;
        private RelativeLayout end;
        private int thrownRocks = 0;
        private String side = "";
        private boolean alive = false;
        private GestureDetector gestureDetector;
        View.OnTouchListener gestureListener;


        //differentiate players' parts
        static final String param1 = "param1";
        String mode;

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
        display = getActivity().getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        info = (RelativeLayout)v.findViewById(R.id.theRescueIntroText);
        info2 = (RelativeLayout)v.findViewById(R.id.theRescueIntroText2);
        rescue = (FrameLayout)v.findViewById(R.id.theRescueLayout);
        rescue2 = (FrameLayout)v.findViewById(R.id.theRescueLayout2);
        end = (RelativeLayout)v.findViewById(R.id.endScreen);
        pitText = (TextView)v.findViewById(R.id.pitText);
        distanceText = (TextView)v.findViewById(R.id.distanceText);

        gestureDetector = new GestureDetector(getActivity().getApplicationContext(), new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        v.setOnClickListener(this);
        v.setOnTouchListener(gestureListener);


        if(!mode.equals("rope")){
            info.setVisibility(View.INVISIBLE);
            rescue.setVisibility(View.INVISIBLE);
        } else{
            info2.setVisibility(View.INVISIBLE);
        }

        startTheRescue = (Button)v.findViewById(R.id.startTheRescue);
        startTheRescue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setVisibility(View.GONE);
                rescue.setVisibility(View.VISIBLE);
                startSensing();
            }
        });


        startTheRescue2 = (Button)v.findViewById(R.id.startTheRescue2);
        startTheRescue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rescue2.setVisibility(View.VISIBLE);
                info2.setVisibility(View.GONE);
                throwRocks();
            }
        });

        backToMain = (Button)v.findViewById(R.id.backToMain);
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GameActivity)getActivity()).changeFragment(Game_Main_Fragment.newInstance(), "MAIN");
                if (!ServerData.isServer()){
                    //send to server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_Rescue_pitSuccess_");
                } else {
                    // send to partner of server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_Rescue_pitSuccess_");
                }
            }
        });

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
                pitText.setText("Your partner managed to throw the rope down your pit!\nYou begin climbing out...\n\nBut as you climb, rocks are getting loose and are falling down on you!\nSwing on the rope to avoid being hit!");
                startTheRescue2.setVisibility(View.VISIBLE);
            }
        });
    }


    public void throwRocks(){
            // assume the worst ...
            alive = false;

           // Instantiate an ImageView and define its properties
           ImageView i = new ImageView(getActivity().getApplicationContext());
           int screen_width = size.x;
           int rock_size =  screen_width/5;
           i.setImageResource(R.drawable.rock);
           i.setLayoutParams(new ViewGroup.LayoutParams(rock_size, rock_size));
           i.setAdjustViewBounds(true); // set the ImageView bounds to match the Drawable's dimensions

           // Spawn rock at random x position
           int x = (Math.random() > 0.5) ? 200 : screen_width-200-rock_size;
           side = (x == 200) ? "right" : "left";
           TranslateAnimation anim = new TranslateAnimation(x, x, 0, size.y+rock_size);
           anim.setDuration(1500);
           anim.setFillAfter(true);
           anim.setAnimationListener(new Animation.AnimationListener() {
               @Override
               public void onAnimationStart(Animation animation) {}

               @Override
               public void onAnimationEnd(Animation animation) {
                    if(alive == false){
                        Log.d("FAIL","verloren!!");
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "LOST_null_null_");
                        ((GameActivity)getActivity()).changeFragment(Game_Lost_Fragment.newInstance(), "LOST");
                    }
                    if(thrownRocks == 5 && alive == true){
                        Log.d("SUCCESS", "you win");
                        rescue2.setVisibility(View.GONE);
                        end.setVisibility(View.VISIBLE);
                    }
                    side = "";
               }

               @Override
               public void onAnimationRepeat(Animation animation) {}
           });
           i.startAnimation(anim);

           thrownRocks += 1;

           // Add the ImageView to the layout and set the layout as the content view
           rescue2.addView(i);

           Runnable r = new Runnable() {
               @Override
               public void run() {
                  if(thrownRocks < 5 && alive) throwRocks();
               }};

           Handler mHandler = new Handler();
           mHandler.postDelayed(r, 3000);
    }

    @Override
    public void onClick(View view) {

    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > 250)
                    return false;
                if(e1.getX() - e2.getX() > 120 && Math.abs(velocityX) > 100) {
                    if(side.equals("left")){
                        Log.d("SWIPE", "correct left");
                        alive = true;
                    }
                }  else if (e2.getX() - e1.getX() > 120 && Math.abs(velocityX) > 100) {
                    if(side.equals("right")){
                        Log.d("SWIPE", "correct right");
                        alive = true;
                    }
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
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
