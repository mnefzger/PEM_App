package com.pem.project.pem_app;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
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
    private boolean correct_partner = false;
    private Button buttonOption1;
    private Button buttonOption2;
    private Button buttonOption3;
    private Button buttonOption4;
    private Button buttonOption5;
    private Button buttonOption6;
    private int input_my;
    private int input_partner;
    private int correctFinalResult_my = -9999;
    private int correctFinalResult_partner = -9999;
    private int result1_my;
    private int result2_my;
    private int result1_partner;
    private int result2_partner;

    ServerData serverData;
    private boolean field_one_pressed;
    private boolean field_two_pressed;
    private EditText editText1;
    private boolean wait_partner = false;
    private int player;


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

        if (mParam1.equals("Player2")){
            player = 2;
            rootView = inflater.inflate(R.layout.fragment_game_math2, container, false);
        } else {
            player = 1;
            rootView = inflater.inflate(R.layout.fragment_game_math, container, false);

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
        editText1 = (EditText) rootView.findViewById(R.id.result);
       // editText1.setImeActionLabel("Calc", KeyEvent.KEYCODE_ENTER);

        buttonOption1.setOnClickListener(this);
        buttonOption2.setOnClickListener(this);
        buttonOption3.setOnClickListener(this);
        buttonOption4.setOnClickListener(this);
        buttonOption5.setOnClickListener(this);
        buttonOption6.setOnClickListener(this);

        editText1.setRawInputType(Configuration.KEYBOARD_12KEY);


        editText1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    //Clear focus here from edittext
                    editText1.clearFocus();

                    input_my = Integer.parseInt(editText1.getText().toString());

                        if (!ServerData.isServer()) {
                            //send to server
                            BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_Math_checkIfGameWon_\n");
                            BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_Math_inputMy:" + input_my + "_\n");
                            BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_Math_correctMy:" + (correct1 && correct2) + "_\n");

                        } else {
                            // send to partner of server
                            BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_Math_checkIfGameWon_\n");
                            BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_Math_inputMy:" + input_my + "_\n");
                            BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_Math_correctMy:" + (correct1 && correct2) + "_\n");
                        }

                    checkIfGameWon();
                }
                return false;
            }
        });
     /**   editText1.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    input_my = Integer.parseInt(editText1.getText().toString());

                    if (!ServerData.isServer()) {
                        //send to server
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_Math_checkIfGameWon_");
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_Math_inputMy:" + input_my + "_");
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_Math_correctMy:" + (correct1 && correct2) + "_");

                    } else {
                        // send to partner of server
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_Math_checkIfGameWon_");
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_Math_inputMy:" + input_my + "_");
                        BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_Math_correctMy:" + (correct1 && correct2) + "_");
                    }
                    checkIfGameWon();

                   // editText1.clearFocus();


                    return true;
                }
                return false;
            }
        });
**/
        operation1.createOperation();
        operation2.createOperation();

        correctFinalResult_partner = operation1.getRightValue() + operation2.getRightValue();


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
            /**if(correct1 && correct2) {
                createOperation();
                correct1 = false;
                correct2 = false;
            }**/
        }

        public void buttonPressed(int field, View view){

            Button butPressed = (Button) rootView.findViewById(view.getId());

            if (field == 0){
                field_one_pressed = true;

                buttonOption1.setBackgroundColor(0xFFFFFFFF);
                buttonOption2.setBackgroundColor(0xFFFFFFFF);
                buttonOption3.setBackgroundColor(0xFFFFFFFF);

                butPressed.setBackgroundColor(0xFFD3D3D3);

                if (!ServerData.isServer()) {
                    //send to server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_Math_result1:" + butPressed.getText() + "_");
                } else {
                    // send to partner of server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_Math_result1:" + butPressed.getText() + "_");
                }

                result1_my = Integer.parseInt(butPressed.getText().toString());


            } else if (field == 1) {

                field_two_pressed = true;

                buttonOption4.setBackgroundColor(0xFFFFFFFF);
                buttonOption5.setBackgroundColor(0xFFFFFFFF);
                buttonOption6.setBackgroundColor(0xFFFFFFFF);

                butPressed.setBackgroundColor(0xFFD3D3D3);

                if (!ServerData.isServer()) {
                    //send to server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_Math_result2:" + butPressed.getText() + "_");
                } else {
                    // send to partner of server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_Math_result2:" + butPressed.getText() + "_");
                }

                result2_my = Integer.parseInt(butPressed.getText().toString());

            }

            if (field_one_pressed && field_two_pressed){

                int correctResult = operation1.getRightValue() + operation2.getRightValue();

                if (!ServerData.isServer()) {
                    //send to server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_Math_correctResult:" + correctResult + "_");
                } else {
                    // send to partner of server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_Math_correctResult:" + correctResult + "_");
                }

            }

        }


        public void setResult(final String processed){

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



        }

    public void setCorrectResult(String correctResult){
        this.correctFinalResult_my = Integer.parseInt(correctResult.substring(14));
        System.out.println("Correct Result: " + correctResult.substring(14));
    }

    private boolean checkIfGameWon() {

        System.out.println(wait_partner + "; " + correctFinalResult_my + "; " + input_my + "; " + correct_partner);

        if (wait_partner == true &&
            correctFinalResult_my != -9999 &&
            correctFinalResult_my == input_my && // Check own
            (correct1 && correct2) && // Check own
            correct_partner ){ // Check Partner
            Log.d("Math", "won!!");
            if (!ServerData.isServer()) {
                //send to server
                BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "WON_null_null");
            } else {
                // send to partner of server
                BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "WON_null_null");
            }
            ((GameActivity) getActivity()).changeFragment(Game_Won_Fragment.newInstance(), "WON");
            return true;
        } else if (wait_partner == false){
            if (!ServerData.isServer()) {
                //send to server
                BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_Math_WaitForPartner_");
            } else {
                // send to partner of server
                BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_Math_WaitForPartner_");
            }
        } else {
            Log.d("Math", "lost!!");
            if (!ServerData.isServer()) {
                //send to server
                BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "LOST_null_null_");
            } else {
                // send to partner of server
                BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "LOST_null_null_");
            }
            ((GameActivity) getActivity()).changeFragment(Game_Lost_Fragment.newInstance(), "LOST");
        }
    return false;
    }


    public void setWaitIfGameWon() {
        
        wait_partner = true;
    }

    public void checkCorrectPartner(String processed) {

        if (processed.substring(10).equals("true") && input_partner == correctFinalResult_partner)
        correct_partner = true;

    }

    public void setInputPartner(String inputPartner) {
        this.input_partner = Integer.parseInt(inputPartner.substring(8));
    }
}
