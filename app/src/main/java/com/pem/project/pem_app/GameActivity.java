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
        Game_Math_Fragment.OnFragmentInteractionListener
        {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private MessageProcessor messageProcessor;
            private Button qrcode, fight;
            private ImageView team1_coin, team2_coin,
                    team1_keyyellow, team1_keyred, team1_keygreen, team1_keyblue,
                    team2_keyyellow, team2_keyred, team2_keygreen, team2_keyblue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        messageProcessor = new MessageProcessor();
        fragmentManager = getFragmentManager();

        changeFragment(new Game_Main_Fragment(), "MAIN");





        if(!ServerData.isServer()){
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
        Log.d("Message", m);
        String processed = messageProcessor.processMessage(m, s);
        Log.d("Received Processed Message", processed);
        if(processed.equals("ropeWait")){
            this.changeFragment(Game_Rescue_Fragment.newInstance("pit"), "RESCUE");
        } else if(processed.equals("ropeThrown")){
            Game_Rescue_Fragment fragment = (Game_Rescue_Fragment)fragmentManager.findFragmentByTag("RESCUE");
            fragment.ropeIsThrown();
        }
        if(processed.equals("Player2")){
            this.changeFragment(Game_Math_Fragment.newInstance("Player2", ""), "MATH");
        } else if(processed.startsWith("result")) {
            Game_Math_Fragment fragment = (Game_Math_Fragment) fragmentManager.findFragmentByTag("MATH");
            fragment.setResult(processed);
        } else if(processed.startsWith("correctResult")){
            Game_Math_Fragment fragment = (Game_Math_Fragment)fragmentManager.findFragmentByTag("MATH");
            fragment.setCorrectResult(processed);
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
}
