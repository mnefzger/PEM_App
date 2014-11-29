package com.pem.project.pem_app;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Matthias on 23.11.2014.
 */
public class ClientListAdapter extends ArrayAdapter<BluetoothSocket>{
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<BluetoothSocket> data;

    public ClientListAdapter(Context context){
        super(context,0);
        this.inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.data = ServerData.getClients();
        Log.d("List Adapter", data + "");
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup){
        View v = view;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.clients_list_row, null);
        }

        BluetoothSocket clientSocket = data.get(position);

        if(clientSocket != null){
            TextView client = (TextView) v.findViewById(R.id.clientText);
            TextView team = (TextView) v.findViewById(R.id.teamText);

            client.setText(clientSocket.getRemoteDevice().getName());
            team.setText("Team " + ServerData.getTeam(clientSocket));
        }

        return v;
    }


    public void refreshArrayData(){
        this.data = ServerData.getClients();
        Log.d("List Adapter", data + "");
    }


}
