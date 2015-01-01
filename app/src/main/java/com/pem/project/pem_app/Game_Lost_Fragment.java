package com.pem.project.pem_app;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class Game_Lost_Fragment extends Fragment {


    public Game_Lost_Fragment() {
        // Required empty public constructor
    }

    public static Game_Lost_Fragment newInstance() {
        Game_Lost_Fragment fragment = new Game_Lost_Fragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game__lost_, container, false);
        Button proceed = (Button) v.findViewById(R.id.proceed_button);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GameActivity)getActivity()).changeFragment(Game_Main_Fragment.newInstance(), "MAIN");
            }
        });

        return v;
    }


}