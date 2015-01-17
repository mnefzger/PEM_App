package com.pem.project.pem_app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothSocket;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;


public class GameActivity extends Activity implements BluetoothListener.IListenCallback,
        Game_Main_Fragment.OnFragmentInteractionListener,
        Game_Rescue_Fragment.OnFragmentInteractionListener,
        Game_Math_Fragment.OnFragmentInteractionListener,
        Game_Scream_Fragment.OnFragmentInteractionListener,
        Game_MathRunes_Fragment.OnFragmentInteractionListener
        {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private MessageProcessor messageProcessor;
            private ImageView team1_coin, team2_coin,
                    team1_keyyellow, team1_keyred, team1_keygreen, team1_keyblue,
                    team2_keyyellow, team2_keyred, team2_keygreen, team2_keyblue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        messageProcessor = new MessageProcessor();
        fragmentManager = getFragmentManager();

        /*Intent intent = getIntent();
        String miniGame = intent.getStringExtra("miniGame");

        if(miniGame.equals("Run")) {
            changeFragment(Game_Run_Fragment.newInstance(), "RUN");
        } else if(miniGame.equals("Rescue")) {
            changeFragment(Game_Rescue_Fragment.newInstance("rope"), "RESCUE");
        } else if(miniGame.equals("Math")) {
            changeFragment(Game_Math_Fragment.newInstance("Player1", ""), "MATH");
        } else if(miniGame.equals("Luck")) {
            //changeFragment(Game_Luck_Fragment().newInstance(), "LUCK");
        } else {
            changeFragment(Game_Main_Fragment.newInstance(), "MAIN");
        }*/

        changeFragment(Game_Main_Fragment.newInstance(), "MAIN");

        if (!ServerData.isServer()) {
            BluetoothListener listener = new BluetoothListener(this);
            listener.listen(ServerData.getServer());
            BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "INFO_null_Hallo Server, Client checking in!_");
        } else {
            BluetoothListener listener = new BluetoothListener(this);
            listener.listen(ServerData.getClientAt(0));

            /*BluetoothListener listener2 = new BluetoothListener(this);
            listener2.listen(ServerData.getClientAt(1));*/

            /*BluetoothListener listener3 = new BluetoothListener(this);
            listener3.listen(ServerData.getClientAt(2)); */
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void processReceivedMessage(String m, BluetoothSocket s){
        String processed = messageProcessor.processMessage(m, s);
        Log.d("Received Processed Message", processed);

        // START
        if(processed.startsWith("START")){
            if(processed.contains("Rescue")) {
                this.changeFragment(Game_Rescue_Fragment.newInstance("pit"), "RESCUE");
            } else if(processed.contains("MathRunes")){
                this.changeFragment(Game_MathRunes_Fragment.newInstance("Player2", ""), "MATHRUNES");
            } else if(processed.contains("Math")){
                this.changeFragment(Game_Math_Fragment.newInstance("Player2", ""), "MATH");
            } else if(processed.contains("Run")) {
                this.changeFragment(Game_Run_Fragment.newInstance(), "RUN");
            } else if(processed.contains("Luck")) {
                this.changeFragment(Game_Luck_Fragment.newInstance(), "LUCK");
            }else if(processed.contains("Scream")) {
                this.changeFragment(Game_Scream_Fragment.newInstance("Player2S", ""), "SCREAM");
            }
        }

        // MINIGAME LOST
        if(processed.equals("LOST")){
            this.changeFragment(Game_Lost_Fragment.newInstance(), "LOST");
        }


        // RESCUE
        if(processed.equals("ropeThrown")){
            Game_Rescue_Fragment fragment = (Game_Rescue_Fragment)fragmentManager.findFragmentByTag("RESCUE");
            fragment.ropeIsThrown();
        } else if(processed.equals("ropeClimb")){
            Game_Rescue_Fragment fragment = (Game_Rescue_Fragment)fragmentManager.findFragmentByTag("RESCUE");
            fragment.pullRope();
        } else if(processed.equals("pitSuccess")){
            this.changeFragment(Game_Main_Fragment.newInstance(), "MAIN");
        }

        // MATH
        if(processed.startsWith("result")) {
            Game_Math_Fragment fragment = (Game_Math_Fragment) fragmentManager.findFragmentByTag("MATH");
            fragment.setResult(processed);
        } else if(processed.startsWith("correctResult")){
            Game_Math_Fragment fragment = (Game_Math_Fragment)fragmentManager.findFragmentByTag("MATH");
            fragment.setCorrectResult(processed);
        } else if(processed.startsWith("waitIfGameWon")){
            Game_Math_Fragment fragment = (Game_Math_Fragment)fragmentManager.findFragmentByTag("MATH");
            fragment.setWaitIfGameWon();
            fragment.getData(processed);
        } else if(processed.equals("mathSuccess")){
            Game_Math_Fragment fragment = (Game_Math_Fragment)fragmentManager.findFragmentByTag("MATH");
            fragment.setWon();
            this.changeFragment(Game_Main_Fragment.newInstance(), "MAIN");
        }

        // MATHRUNES
        if(processed.startsWith("MR:result")) {
            Game_MathRunes_Fragment fragment = (Game_MathRunes_Fragment) fragmentManager.findFragmentByTag("MATHRUNES");
            fragment.setResult(processed);
        } else if(processed.startsWith("MR:correctResult")){
            Game_MathRunes_Fragment fragment = (Game_MathRunes_Fragment)fragmentManager.findFragmentByTag("MATHRUNES");
            fragment.setCorrectResult(processed);
        } else if(processed.startsWith("MR:waitIfGameWon")){
            Game_MathRunes_Fragment fragment = (Game_MathRunes_Fragment)fragmentManager.findFragmentByTag("MATHRUNES");
            fragment.setWaitIfGameWon();
            fragment.getData(processed);
        } else if(processed.equals("MR:mathSuccess")){
            Game_MathRunes_Fragment fragment = (Game_MathRunes_Fragment)fragmentManager.findFragmentByTag("MATHRUNES");
            fragment.setWon();
            this.changeFragment(Game_Main_Fragment.newInstance(), "MAIN");
        }

        //RUN
       if(processed.equals("startRunning")){
            Game_Run_Fragment fragment = (Game_Run_Fragment)fragmentManager.findFragmentByTag("RUN");
            fragment.startRunning();
        }

        // Scream
        if(processed.equals("Player2S")) {
            this.changeFragment(Game_Scream_Fragment.newInstance("Player2S", ""), "SCREAM");
        } else if(processed.startsWith("myVolume")) {
            Game_Scream_Fragment fragment = (Game_Scream_Fragment) fragmentManager.findFragmentByTag("SCREAM");
            fragment.setOtherTeamVol(processed);
        } else if(processed.startsWith("myPlayedRounds")) {
            Game_Scream_Fragment fragment = (Game_Scream_Fragment) fragmentManager.findFragmentByTag("SCREAM");
            fragment.getPlayedRounds(processed);
        }
    }

    public void changeFragment(Fragment f, String tag){

        fragmentTransaction = fragmentManager.beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        fragmentTransaction.replace(R.id.fragment_container, f, tag);
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }

    public void onFragmentInteraction(){

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
    }
}
