package com.pem.project.pem_app;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class BluetoothServerActivity extends Activity {

    private BluetoothAdapter bAdapter;
    private ListView clientList;
    private ClientListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        clientList = (ListView) findViewById(R.id.clientsList);
        listAdapter = new ClientListAdapter(this);
        clientList.setAdapter(listAdapter);

        bluetoothSetup();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bluetooth, menu);
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

    public void bluetoothSetup() {

        bAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bAdapter != null) {
            bAdapter.setName("Game Server");

            // Make the device discoverable
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);

            AcceptThread acceptThread = new AcceptThread();
            acceptThread.start();

        }
    }


    public void sendDataToPairedDevice(BluetoothSocket socket, String message){
        byte[] toSend = message.getBytes();
        try {
            //BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(Constants.uuid);
            OutputStream mmOutStream = socket.getOutputStream();
            mmOutStream.write(toSend);
            // Your Data is sent to  BT connected paired device ENJOY.
        } catch (IOException e) {
            Log.e("BluetoothSend", "Exception during write", e);
        }
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            Log.d("Bluetooth", "Started Thread");
            // Create a new listening server socket
            try {
                tmp = bAdapter.listenUsingRfcommWithServiceRecord("PEM", Constants.uuid);
            } catch (IOException e) {
                Log.e("Bluetooth", "listen() failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            setName("AcceptThread");

            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (ServerData.getNumOfClients() < 4) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                    manageConnections(socket);
                } catch (IOException e) {
                    Log.e("Bluetooth", "accept() failed", e);
                    break;
                }
            }

            cancel();

        }

        public void manageConnections(final BluetoothSocket socket) {
            Log.d("BluetoothServer", "Client ist da! " + socket.getRemoteDevice());
            ServerData.addToClients(socket);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //refresh ListView
                    listAdapter.add(socket);
                    listAdapter.notifyDataSetChanged();
                    sendDataToPairedDevice(socket, "Hello, welcome to the game!");
                }
            });
        }

        public void cancel() {
            Log.d("Bluetooth", "cancel()" + this);
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e("Bluetooth", "close() of server failed", e);
            }
        }
    }
}
