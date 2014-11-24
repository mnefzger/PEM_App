package com.pem.project.pem_app;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import java.io.IOException;



public class BluetoothServerActivity extends Activity {

    private BluetoothAdapter bAdapter;
    private ListView clientList;
    private ClientListAdapter listAdapter;
    private Button startGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        clientList = (ListView) findViewById(R.id.clientsList);
        listAdapter = new ClientListAdapter(this);
        clientList.setAdapter(listAdapter);
        clientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BluetoothSocket s = listAdapter.getItem(i);
                int team = (ServerData.getTeam(s) == 1) ? 2 : 1;
                ServerData.removeFromTeam(s);
                ServerData.addToTeam(listAdapter.getItem(i), team);
                listAdapter.notifyDataSetChanged();
            }
        });

        startGame = (Button) findViewById(R.id.startGameButton);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tell Clients to start the game
                for(BluetoothSocket client : ServerData.getClients()) BluetoothHelper.sendDataToPairedDevice(client, "START_");

                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });

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
        //first, mark this device as server
        ServerData.markLocalAsServer();

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
            while (ServerData.getNumOfClients() < 3) {
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
            int team = (ServerData.getNumOfClients() < 2)?  1 : 2;
            ServerData.addToTeam(socket, team);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //refresh ListView
                    listAdapter.add(socket);
                    listAdapter.notifyDataSetChanged();
                    BluetoothHelper.sendDataToPairedDevice(socket, "Hello, welcome to the game!_");

                    if (ServerData.getNumOfClients() == 1) startGame.setEnabled(true);
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
