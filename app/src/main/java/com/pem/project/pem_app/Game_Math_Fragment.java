package com.pem.project.pem_app;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Game_Math_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Game_Math_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Game_Math_Fragment extends Fragment implements OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private View rootView;
    private Game_Math_Helper operation1;
    private Game_Math_Helper operation2;
    private boolean correct1 = false;
    private boolean correct2 = false;
    private Button buttonOption1;
    private Button buttonOption2;
    private Button buttonOption3;
    private Button buttonOption4;
    private Button buttonOption5;
    private Button buttonOption6;
    private int result1;
    private int result2;
    private int player = 2;

    ServerData serverData;




    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MathGameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Game_Math_Fragment newInstance(String param1, String param2) {
        Game_Math_Fragment fragment = new Game_Math_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public Game_Math_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        serverData = new ServerData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (player == 1) {
            rootView = inflater.inflate(R.layout.fragment_game_math, container, false);
        } else {
            rootView = inflater.inflate(R.layout.fragment_game_math2, container, false);
        }


        operation1 = new Game_Math_Helper();
        operation2 = new Game_Math_Helper();

        createOperation();


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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void createOperation(){

        buttonOption1 = (Button) rootView.findViewById(R.id.buttonMath1);
        buttonOption2 = (Button) rootView.findViewById(R.id.buttonMath2);
        buttonOption3 = (Button) rootView.findViewById(R.id.buttonMath3);
        buttonOption4 = (Button) rootView.findViewById(R.id.buttonMath4);
        buttonOption5 = (Button) rootView.findViewById(R.id.buttonMath5);
        buttonOption6 = (Button) rootView.findViewById(R.id.buttonMath6);

        buttonOption1.setOnClickListener(this);
        buttonOption2.setOnClickListener(this);
        buttonOption3.setOnClickListener(this);
        buttonOption4.setOnClickListener(this);
        buttonOption5.setOnClickListener(this);
        buttonOption6.setOnClickListener(this);

        operation1.createOperation();
        operation2.createOperation();


        TextView textViewOperation1 = (TextView) rootView.findViewById(R.id.textViewOperation1);
        textViewOperation1.setText(operation1.getOperationText());

        TextView textViewOperation2 = (TextView) rootView.findViewById(R.id.textViewOperation2);
        textViewOperation2.setText(operation2.getOperationText());

        buttonOption1 = (Button) rootView.findViewById(R.id.buttonMath1);
        buttonOption2 = (Button) rootView.findViewById(R.id.buttonMath2);
        buttonOption3 = (Button) rootView.findViewById(R.id.buttonMath3);
        buttonOption4 = (Button) rootView.findViewById(R.id.buttonMath4);
        buttonOption5 = (Button) rootView.findViewById(R.id.buttonMath5);
        buttonOption6 = (Button) rootView.findViewById(R.id.buttonMath6);


        switch (operation1.getRightBox()) {
            case 0:
                buttonOption1.setText(String.valueOf(operation1.getRightValue()));
                buttonOption2.setText(String.valueOf(operation1.getRandWrongValue1()));
                buttonOption3.setText(String.valueOf(operation1.getRandWrongValue2()));
                break;
            case 1:
                buttonOption2.setText(String.valueOf(operation1.getRightValue()));
                buttonOption1.setText(String.valueOf(operation1.getRandWrongValue1()));
                buttonOption3.setText(String.valueOf(operation1.getRandWrongValue2()));
                break;
            case 2:
                buttonOption3.setText(String.valueOf(operation1.getRightValue()));
                buttonOption1.setText(String.valueOf(operation1.getRandWrongValue1()));
                buttonOption2.setText(String.valueOf(operation1.getRandWrongValue2()));
                break;
        }

        switch (operation2.getRightBox()) {
            case 0:
                buttonOption4.setText(String.valueOf(operation2.getRightValue()));
                buttonOption5.setText(String.valueOf(operation2.getRandWrongValue1()));
                buttonOption6.setText(String.valueOf(operation2.getRandWrongValue2()));
                break;
            case 1:
                buttonOption5.setText(String.valueOf(operation2.getRightValue()));
                buttonOption4.setText(String.valueOf(operation2.getRandWrongValue1()));
                buttonOption6.setText(String.valueOf(operation2.getRandWrongValue2()));
                break;
            case 2:
                buttonOption6.setText(String.valueOf(operation2.getRightValue()));
                buttonOption4.setText(String.valueOf(operation2.getRandWrongValue1()));
                buttonOption5.setText(String.valueOf(operation2.getRandWrongValue2()));
                break;
        }
    }


        @Override
        public void onClick(View view) {

            //boolean play = true;


            switch(view.getId()) {
                case R.id.buttonMath1:
                    if (operation1.getRightBox() == 0) {
                        correct1 = true;
                    }
                    buttonPressed(0, view);
                    break;
                case R.id.buttonMath2:
                    if (operation1.getRightBox() == 1) {
                        correct1 = true;
                    }
                    buttonPressed(0, view);
                    break;
                case R.id.buttonMath3:
                    if (operation1.getRightBox() == 2) {
                        correct1 = true;
                    }
                    buttonPressed(0, view);
                    break;

                case R.id.buttonMath4:
                    if (operation2.getRightBox() == 0) {
                        correct2 = true;
                    }
                    buttonPressed(1, view);
                    break;
                case R.id.buttonMath5:
                    if (operation2.getRightBox() == 1) {
                        correct2 = true;
                    }
                    buttonPressed(1, view);
                    break;
                case R.id.buttonMath6:
                    if (operation2.getRightBox() == 2) {
                        correct2 = true;
                    }
                    buttonPressed(1, view);
                    break;
            }


            if (correct1) {
                System.out.println("1 correct");
            }
            if (correct2) {
                System.out.println("2 correct");
            }
            if(correct1 && correct2) {
                createOperation();
                correct1 = false;
                correct2 = false;
            }
        }

        public void buttonPressed(int field, View view){

            Button butPressed = (Button) rootView.findViewById(view.getId());

            TextView result1;
            TextView result2;

            if (player == 1) {
                result1 = (TextView) rootView.findViewById(R.id.result1player2);
                result2 = (TextView) rootView.findViewById(R.id.result2player2);
            } else {
                result1 = (TextView) rootView.findViewById(R.id.result1player1);
                result2 = (TextView) rootView.findViewById(R.id.result2player1);
            }

            if (field == 0){
                if (player == 1) butPressed.setBackgroundColor(0xFFFF0000);
                if (player == 2) butPressed.setBackgroundColor(0xFF0000FF);
                result1.setText(butPressed.getText());
            } else if (field == 1) {
                if (player == 1) butPressed.setBackgroundColor(0xFFFFFF00);
                if (player == 2) butPressed.setBackgroundColor(0xFF00FF00);
                result2.setText(butPressed.getText());
            }





        }




}
