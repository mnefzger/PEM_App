package com.pem.project.pem_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;


public class RecorderScreamGame {
    static final private double EMA_FILTER = 0.6;

    private MediaRecorder mRecorder = null;
    private double mEMA = 0.0;

    public void start() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorder.start();
            mEMA = 0.0;
        }
    }

    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double getAmplitude() {
        if (mRecorder != null) {
            double ampl=(mRecorder.getMaxAmplitude() / 2700.0);
            return ampl;
        }
        else
            return 0;

    }

    public double getAmplitudeEMA() {
        double amp = getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }

/*
    private MediaRecorder mRecorder;

    public void start() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile("/dev/null");
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRecorder.start();
Log.d("Juhuu","Start");
    }

    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
            Log.d("Juhuu","Stop");
        }
    }

    public long getAmplitude() {
        if (mRecorder != null) {
            long ampl=mRecorder.getMaxAmplitude();
            Log.d("Juhuu", "Amplitude: "+ ampl*100);
            return ampl;
        }
        else
            return 0;

    }
*/




    /*
    Handler mHandler = new Handler(){
    public void run() {
        int i = 0;
        while (i == 0) {
            Message msg = mHandler.obtainMessage();
            Bundle b = new Bundle();
            try {
                sleep(250);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (mRecorder != null) {
                amplitude = mRecorder.getMaxAmplitude();
                b.putLong("currentTime", amplitude);
                Log.i("AMPLITUDE", amplitude+"");
            } else {
                b.putLong("currentTime", 0);
            }
            msg.setData(b);
            mHandler.sendMessage(msg);
        }
    }
*/
}
