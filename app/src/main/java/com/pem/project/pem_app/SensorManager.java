package com.pem.project.pem_app;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.ESSensorManagerInterface;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

/**
 * Created by Matthias on 01.12.2014.
 */
public class SensorManager implements ESSensorManagerInterface {
    ESSensorManager sensorManager;
    SensorData data;

    public SensorManager(Context c){
        try{
            sensorManager = ESSensorManager.getSensorManager(c);
        } catch (ESException e){
            Log.d("ESException", e.toString());
        }
    }

    @Override
    public int subscribeToSensorData(int i, SensorDataListener sensorDataListener) throws ESException {
        return 0;
    }

    @Override
    public void unsubscribeFromSensorData(int i) throws ESException {

    }

    @Override
    public SensorData getDataFromSensor(int i) throws ESException {
        return sensorManager.getDataFromSensor(i);
    }

    @Override
    public void pauseSubscription(int i) throws ESException {

    }

    @Override
    public void unPauseSubscription(int i) throws ESException {

    }

    @Override
    public void setSensorConfig(int i, String s, Object o) throws ESException {

    }

    @Override
    public Object getSensorConfigValue(int i, String s) throws ESException {
        return null;
    }

    @Override
    public void setGlobalConfig(String s, Object o) throws ESException {

    }

    @Override
    public Object getGlobalConfig(String s) throws ESException {
        return null;
    }

}
