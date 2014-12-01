package com.pem.project.pem_app;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.AbstractMotionData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Game_Rescue_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Game_Rescue_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Game_Rescue_Fragment extends Fragment {
        private OnFragmentInteractionListener mListener;
        private SensorManager sm;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     */
    // TODO: Rename and change types and number of parameters
    public static Game_Rescue_Fragment newInstance() {
        Game_Rescue_Fragment fragment = new Game_Rescue_Fragment();
        return fragment;
    }

    public Game_Rescue_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startSensing();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_rescue, container, false);
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

    public void startSensing(){
       sm = new SensorManager(getActivity().getApplicationContext());
       new SensingTask().execute();
    }

    private class SensingTask extends AsyncTask<SensorManager, SensorData, SensorData> {

        @Override
        protected SensorData doInBackground(SensorManager... s) {
            SensorData data  = null;
            try{
                data = sm.getDataFromSensor(5001);
                int sensorType = data.getSensorType();
                Log.d("DATA", "Received from: " + SensorUtils.getSensorName(sensorType));

                //get values out of Data object
                AbstractMotionData abstract_data = (AbstractMotionData) data;
                ArrayList<float[]> readings = abstract_data.getSensorReadings();
                ArrayList<Float> xs = new ArrayList<Float>();
                ArrayList<Float> ys = new ArrayList<Float>();
                ArrayList<Float> zs = new ArrayList<Float>();

                for (int i = 0; i < readings.size(); i++)
                {
                    float[] sample = readings.get(i);
                    xs.add(sample[0]);
                    ys.add(sample[1]);
                    zs.add(sample[2]);
                }

                Log.d("Distance" , calcDistance(xs,ys,zs) + " Meter");
            } catch(ESException e){
                //fail
            }
            return data;
        }

        protected void onPostExecute(Long result) {

        }
    }

    public float getMax(ArrayList<Float> al){
        float max = 0;
        for(int i = 0; i< al.size(); i++){
            max = (al.get(i) > max)? al.get(i) : max;
        }
        return max;
    }

    public double calcDistance(ArrayList<Float> x, ArrayList<Float> y, ArrayList<Float> z){
        Log.d("Max X",x + "");
        Log.d("Max Y",y + "");
        Log.d("Max Z",z + "");
        float distance = 0;
        float dt = x.size()/x.size();
        float dx=0.0f;
        float vx=0.0f;
        for (int i=1; i<x.size(); i++)
        {
            vx+=(x.get(i-1) + x.get(i))/2.0f*dt;
            dx+=vx*dt;
        }
        float dy=0.0f;
        float vy=0.0f;
        for (int i=1; i<y.size(); i++)
        {
            vy+=(y.get(i-1) + y.get(i))/2.0f*dt;
            dy+=vy*dt;
        }
        float dz=0.0f;
        float vz=0.0f;
        for (int i=1; i<z.size(); i++)
        {
            vz+=(z.get(i-1) + z.get(i))/2.0f*dt;
            dz+=vz*dt;
        }
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction();
    }

}
