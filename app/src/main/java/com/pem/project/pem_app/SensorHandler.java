package com.pem.project.pem_app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.lang.reflect.Array;

/**
 * Created by Matthias on 07.12.2014.
 */
public class SensorHandler implements SensorEventListener{
    public interface SensorCallback{
        public void dataSensed(double[] data);
    }

    SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorCallback callback;

    public SensorHandler(Fragment f, Context c){
        sensorManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, sensorManager.SENSOR_DELAY_GAME);
        callback = (SensorCallback) f;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.

        final double alpha = 0.8;
        double[] gravity = new double[3];
        double[] linear_acceleration = new double[3];

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];
        double d = calcDistance(linear_acceleration);
        if(d > 12){
            Log.d("Distance", "weit genug " + d);
            sensorManager.unregisterListener(this);
            double[] data = new double[1];
            data[0] = d;
            dataSensed(data);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public double calcDistance(double[] data){

        double x = data[0];
        double y = data[1];
        double z = data[2];
        
        double distance = 0;
        distance =  Math.sqrt(x*x + y*y + z*z);

        return distance-7.0;
    }

    public void dataSensed(double[] data){
        callback.dataSensed(data);
    }
}
