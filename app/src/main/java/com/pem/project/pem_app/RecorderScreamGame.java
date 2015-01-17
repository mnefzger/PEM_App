package com.pem.project.pem_app;
import android.media.MediaRecorder;
import android.util.Log;
import java.io.IOException;


public class RecorderScreamGame {
    static final private double EMA_FILTER = 0.6;

    private MediaRecorder mRecorder = null;
    private double mEMA = 0.0;
    private String mFileName;

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
            Log.d("Scream Fight", "started to record");
            mEMA = 0.0;
        }
    }

    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
            Log.d("Scream Fight", "stopped to record");
        }
    }

    public double getAmplitude() {
        if (mRecorder != null) {
            double ampl=mRecorder.getMaxAmplitude();
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

    public double getDecibel() {
        double amp = getAmplitude();
        double dec= (20 * Math.log10(amp / 2700));
        return dec;
    }

}
