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


public class GameActivity extends Activity implements BluetoothListener.IListenCallback,
        Game_Main_Fragment.OnFragmentInteractionListener,
        Game_Rescue_Fragment.OnFragmentInteractionListener,
        Game_Math_Fragment.OnFragmentInteractionListener{
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        changeFragment(new Game_Main_Fragment());

        if(!ServerData.isServer()){
            BluetoothListener listener = new BluetoothListener(this);
            listener.listen(ServerData.getServer());
            BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "Hallo Server, Client checking in!_");
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
        Log.d("Received Message", "From " + s.getRemoteDevice().getName() + ": " + m);
    }

    public void changeFragment(Fragment f){
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        fragmentTransaction.replace(R.id.fragment_container, f);
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
