package com.pem.project.pem_app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.pem.project.pem_app.Game_MathRunes_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.pem.project.pem_app.Game_MathRunes_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Game_MathRunes_Fragment extends Fragment implements OnClickListener {
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
    private TextView countTextField;
    private int input_my;
    private int input_partner;
    private int correctFinalResult_my = -9999;
    private int correctFinalResult_partner = -9999;
    private int result1_my;
    private int result2_my;
    private int result1_partner;
    private int result2_partner;
    private CountDownTimer countdown;
    private boolean won;

    ServerData serverData;
    private boolean field_one_pressed;
    private boolean field_two_pressed;
    private EditText editText1;
    private boolean wait_partner = false;
   // private int player;
    private GridView listView1;
    private GridView listView2;
    private GridView listView3;
    private GridView listView4;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MathGameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Game_MathRunes_Fragment newInstance(String param1, String param2) {
        Game_MathRunes_Fragment fragment = new Game_MathRunes_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public Game_MathRunes_Fragment() {
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

    /**    if (mParam1.equals("Player2")){
            player = 2;
            rootView = inflater.inflate(R.layout.fragment_game_mathrunes, container, false);
        } else {
            player = 1;
            rootView = inflater.inflate(R.layout.fragment_game_mathrunes, container, false);

        }**/


        rootView = inflater.inflate(R.layout.fragment_game_mathrunes, container, false);


        operation1 = new Game_Math_Helper();
        operation2 = new Game_Math_Helper();

        createOperation();

        countTextField = (TextView) rootView.findViewById(R.id.countTextField);

        countdown = new CountDownTimer(40000, 1000) {

            public void onTick(long millisUntilFinished) {
                countTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                if (!won) {

                    gameEnds();

                    gameLost();
                }
            }
        }.start();


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
                    try {
                        InputMethodManager input = (InputMethodManager) getActivity()
                                .getSystemService(Activity.INPUT_METHOD_SERVICE);
                        input.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }catch(Exception e) {
                        e.printStackTrace();
                    }

                    String input = editText1.getText().toString();

                    if (input != null && !input.isEmpty()) {
                        input_my = Integer.parseInt(editText1.getText().toString());

                        won = checkIfGameWon();
                    }

                    if (!won) {
                        if (!ServerData.isServer()) {
                            //send to server
                            BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_MathRunes_MR:waitIfGameWon:" + input_my + ":" + (correct1 && correct2) + "_");

                        } else {
                            // send to partner of server
                            BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_MathRunes_MR:waitIfGameWon:" + input_my + ":" + (correct1 && correct2) + "_");
                        }

                    }
                }
                return false;
            }
        });

        operation1.createOperation(18);
        operation2.createOperation(18);

        correctFinalResult_partner = operation1.getRightValue() + operation2.getRightValue();

        listView1 = (GridView) rootView.findViewById(R.id.listView1);
        listView2 = (GridView) rootView.findViewById(R.id.listView2);
        listView3 = (GridView) rootView.findViewById(R.id.listView3);
        listView4 = (GridView) rootView.findViewById(R.id.listView4);

        final RowAdapter rowAdapter1 = new RowAdapter(getActivity().getApplicationContext(), operation1.getRunesArray(1));
        final RowAdapter rowAdapter2 = new RowAdapter(getActivity().getApplicationContext(), operation1.getRunesArray(2));
        final RowAdapter rowAdapter3 = new RowAdapter(getActivity().getApplicationContext(), operation2.getRunesArray(1));
        final RowAdapter rowAdapter4 = new RowAdapter(getActivity().getApplicationContext(), operation2.getRunesArray(2));

        listView1.setAdapter(rowAdapter1);
        listView2.setAdapter(rowAdapter2);
        listView3.setAdapter(rowAdapter3);
        listView4.setAdapter(rowAdapter4);

        TextView textView_operation1 = (TextView) rootView.findViewById((R.id.operation1));
        TextView textView_operation2 = (TextView) rootView.findViewById((R.id.operation2));

        textView_operation1.setText(operation1.getOperationString(operation1.getOperation()));
        textView_operation2.setText(operation2.getOperationString(operation2.getOperation()));

        System.out.println("Operation1: " + operation1.getOperationText());
        System.out.println("Operation2: " + operation2.getOperationText());


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

        }

        public void buttonPressed(int field, View view){

            Button butPressed = (Button) rootView.findViewById(view.getId());

            if (field == 0){
                field_one_pressed = true;

                buttonOption1.setBackgroundColor(0x95000000);
                buttonOption2.setBackgroundColor(0x95000000);
                buttonOption3.setBackgroundColor(0x95000000);

                butPressed.setBackgroundColor(0xcc480000);

                if (!ServerData.isServer()) {
                    //send to server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_MathRunes_MR:result1:" + butPressed.getText() + "_");
                } else {
                    // send to partner of server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_MathRunes_MR:result1:" + butPressed.getText() + "_");
                }

                result1_my = Integer.parseInt(butPressed.getText().toString());


            } else if (field == 1) {

                field_two_pressed = true;

                buttonOption4.setBackgroundColor(0x95000000);
                buttonOption5.setBackgroundColor(0x95000000);
                buttonOption6.setBackgroundColor(0x95000000);

                butPressed.setBackgroundColor(0xcc480000);

                if (!ServerData.isServer()) {
                    //send to server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_MathRunes_MR:result2:" + butPressed.getText() + "_");
                } else {
                    // send to partner of server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_MathRunes_MR:result2:" + butPressed.getText() + "_");
                }

                result2_my = Integer.parseInt(butPressed.getText().toString());

            }

            if (field_one_pressed && field_two_pressed){

                int correctResult = operation1.getRightValue() + operation2.getRightValue();

                if (!ServerData.isServer()) {
                    //send to server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_MathRunes_MR:correctResult:" + correctResult + "_");
                } else {
                    // send to partner of server
                    BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_MathRunes_MR:correctResult:" + correctResult + "_");
                }
            }
        }

        public void setResult(final String processed){

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView textView1;
                    TextView textView2;

                    textView1 = (TextView) rootView.findViewById(R.id.result1);
                    textView2 = (TextView) rootView.findViewById(R.id.result2);

                    if (processed.startsWith("MR:result1")) {
                        textView1.setText(processed.substring(11));
                        result1_partner = Integer.parseInt(processed.substring(11));
                    } else if (processed.startsWith("MR:result2")){
                        textView2.setText(processed.substring(11));
                        result2_partner = Integer.parseInt(processed.substring(11));
                    }

                }
            });
        }

    public void setCorrectResult(String correctResult){
        this.correctFinalResult_my = Integer.parseInt(correctResult.substring(17));
        System.out.println("Correct Result: " + correctResult.substring(17));
    }

    private boolean checkIfGameWon() {

        System.out.println(wait_partner + "; " + correctFinalResult_my + "; " + input_my + "; " + correct_partner);

        if (wait_partner == true &&
            correctFinalResult_my != -9999 &&
            correctFinalResult_my == input_my && // Check own
            (correct1 && correct2) && // Check own
            correct_partner ){ // Check Partner

            gameEnds();

            gameWon();



            return true;
        } else if (wait_partner == false){
            if (!ServerData.isServer()) {
                //send to server
                BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "GAMEDATA_MathRunes_MR:WaitForPartner_");
            } else {
                // send to partner of server
                BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "GAMEDATA_MathRunes_MR:WaitForPartner_");
            }
        } else {

            gameEnds();

            gameLost();
        }
    return false;
    }




    public void setWaitIfGameWon() {
        wait_partner = true;
    }

    public void checkCorrectPartner(String processed) {
        if (processed.equals("true") && input_partner == correctFinalResult_partner){
            correct_partner = true;

        }
        System.out.println("correct_partner: " + correct_partner + "; correctFinalResult_partner: " + correctFinalResult_partner + "; input_partner: " + input_partner);
    }

    public void setInputPartner(String inputPartner) {
        this.input_partner = Integer.parseInt(inputPartner);
        System.out.println("Set input_partner: " + this.input_partner);
    }

    public void getData(String processed) {
        String[] messageParameters = processed.split(":");
        String input_partner = messageParameters[2];
        String correct_partner = messageParameters[3];

        setInputPartner(input_partner);
        checkCorrectPartner(correct_partner);
    }

    public void gameEnds(){
        countdown.cancel();

      /**  getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditText textView1;


                textView1 = (EditText) rootView.findViewById(R.id.result);


                textView1.clearFocus();


            }
        });**/

        //clear Focus of Keyboard
        try {
            InputMethodManager input = (InputMethodManager) getActivity()
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            input.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    private void gameLost() {

        Log.d("MathRunes", "lost!!");
        if (!ServerData.isServer()) {
            //send to server
            BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "LOST_MathRunes_keyBlue_");
        } else {
            // send to partner of server
            BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "LOST_MathRunes_keyBlue_");
        }

        ((GameActivity) getActivity()).changeFragment(Game_Lost_Fragment.newInstance("keyBlue",1), "LOST");
    }

    public void gameWon() {
        won = true;

        Log.d("MathRunes", "won!!");

        if (!ServerData.isServer()) {
            //send to server
            BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "WON_MathRunes_keyBlue_");
        } else {
            // send to partner of server
            BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "WON_MathRunes_keyBlue_");
        }

        ((GameActivity)getActivity()).changeFragment(Game_Won_Fragment.newInstance("keyBlue",1), "MAIN");
    }

}

class RowAdapter extends ArrayAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private int[] runesArray;

    public RowAdapter(Context context, int[] runesArray) {
        super(context, 0);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.runesArray = runesArray;

    }



    public View getView(int position, View view, ViewGroup viewGroup){
        if(view==null)
            view = layoutInflater.inflate(R.layout.fragment_game_mathrunes_listitem, null);

        ImageView imageView1 = (ImageView) view.findViewById(R.id.rune);


        if (runesArray[position] == 5) {
            imageView1.setImageResource(R.drawable.five_white);
        } else {
            imageView1.setImageResource(R.drawable.one_white);
        }

        return view;
    }


    public int getCount(){
        int x = 0;
        int counter = 0;
        while(x<runesArray.length){
            if(runesArray[x] != 0){
                counter++;
            }
            x++;
        }

        return counter;
    }


}