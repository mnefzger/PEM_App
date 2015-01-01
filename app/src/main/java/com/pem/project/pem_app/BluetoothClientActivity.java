package com.pem.project.pem_app;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class BluetoothClientActivity extends Activity implements BluetoothListener.IListenCallback {
    private BluetoothAdapter bAdapter;
    private BroadcastReceiver mReceiver;
    private TextView text;
    Activity clientActivity;
    private BluetoothListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_client);
        text = (TextView)findViewById(R.id.status);
        clientActivity = this;

        bAdapter = BluetoothAdapter.getDefaultAdapter();
        //check if bluetooth is on
        if (!bAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,1);
        } else {
            connectToServer();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bluetooth_client, menu);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    connectToServer();
                }else{
                    // User did not enable Bluetooth or an error occurred
                    Log.d("BT FAIL", "BT not enabled");
                    Toast.makeText(this, "Could not enabled Bluetooth.",
                            Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void connectToServer(){
        // Create a BroadcastReceiver for ACTION_FOUND
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    Log.d("PEM_Bluetooth", "Discovered: " + device.getName() + "\n" + device.getAddress());
                    Log.d("PEM_Bluetooth", device.getUuids() + "");
                    if(device.getName().equals("Game Server")) {
                        ConnectThread connectThread = new ConnectThread(device);
                        connectThread.start();
                    }
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        bAdapter.startDiscovery();

    }

    public void processReceivedMessage(String m, BluetoothSocket s){
        final BluetoothSocket socket = s;
        Log.d("BLABLAB", "");
        final String processed = new MessageProcessor().processMessage(m, socket);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText("From " + socket.getRemoteDevice().getName() + ": " + processed);
            }
        });

        if(processed.equals("START")){
            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
            startActivity(intent);
            listener.destroy();
        }
    }


    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            Log.d("PEM_Bluetooth", "started ConnectClient");
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(Constants.uuid);
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            bAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
                unregisterReceiver(mReceiver);
                ServerData.markRemoteAsServer(mmSocket);
                listener = new BluetoothListener(clientActivity);
                listener.listen(mmSocket);
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    @Override
    public void onStop(){
        super.onStop();
    }




}
