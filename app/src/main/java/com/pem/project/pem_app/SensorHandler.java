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
    public interface ropeCallback{
        public void ropeSensed(double[] data);
    }

    public interface pullCallback{
        public void pullSensed(double[] data);
    }

    SensorManager sensorManager;
    private Sensor sensor;
    private ropeCallback callback;
    private pullCallback callback2;
    private String mode;

    public SensorHandler(Fragment f, Context c, String mode){
        sensorManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
        this.mode = mode;

        if(mode.equals("accelerometer")) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            callback = (ropeCallback) f;
        } else if(mode.equals("gyroscope")){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            callback2 = (pullCallback) f;
        }

        sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(this.mode.equals("accelerometer")) {
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
            if (d > 15) {
                sensorManager.unregisterListener(this);
                double[] data = new double[1];
                data[0] = d;
                ropeSensed(data);
            }
        } else if(this.mode.equals("gyroscope")) {
            if(event.values[0] > 10){
                sensorManager.unregisterListener(this);
                double[] data = new double[1];
                pullSensed(data);
            }
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

    public void stopSensing(){
        sensorManager.unregisterListener(this);
    }

    public void ropeSensed(double[] data){
        callback.ropeSensed(data);
    }

    public void pullSensed(double[] data){
        callback2.pullSensed(data);
    }
}
