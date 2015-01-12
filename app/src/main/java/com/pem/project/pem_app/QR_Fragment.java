package com.pem.project.pem_app;


import android.app.Fragment;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;


/**
 * A simple {@link Fragment} subclass.
 */
public class QR_Fragment extends Fragment {
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    TextView scanText;
    Button scanButton;

    ImageScanner scanner;

    private boolean barcodeScanned = false;
    private boolean previewing = true;

    static {
        System.loadLibrary("iconv");
    }

    public static QR_Fragment newInstance() {
        QR_Fragment fragment = new QR_Fragment();
        return fragment;
    }

    public QR_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_qr_, container, false);
        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(getActivity(), mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout)v.findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        scanText = (TextView)v.findViewById(R.id.scanText);

        scanButton = (Button)v.findViewById(R.id.ScanButton);

        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (barcodeScanned) {
                    barcodeScanned = false;
                    scanText.setText("Scanning...");
                    mCamera.setPreviewCallback(previewCb);
                    mCamera.startPreview();
                    previewing = true;
                    mCamera.autoFocus(autoFocusCB);
                    scanButton.setVisibility(View.INVISIBLE);
                }
            }
        });
        return v;
    }

    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    PreviewCallback previewCb = new PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                scanButton.setVisibility(View.VISIBLE);
                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    scanText.setText("Barcode Result: " + sym.getData());
                    processScan(sym.getData());
                    barcodeScanned = true;
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    private void processScan(String scan){
        Fragment f = null;
        String minigame = "";
        Double d = Math.random();
        if(scan.equals("Category:Speed")) {
            f = Game_Run_Fragment.newInstance();
            minigame = "Run";
            //minigame = d>0.5 ? "Run" : "Items";
        }else if(scan.equals("Category:Puzzle")) {
            f = Game_Math_Fragment.newInstance("Player1", "");
            //minigame = "Math";
            minigame = d>0.5 ? "MathRunes" : "Math";
        } else if(scan.equals("Category:Skill")) {
            f = Game_Rescue_Fragment.newInstance("rope");
            minigame = "Rescue";
        } else if(scan.equals("Category:Luck")) {
            f = Game_Luck_Fragment.newInstance();
            minigame = "Luck";
        } else {
            scanText.setText("Unable to detect Minigame.");
        }

        if(minigame != "") {
            if (!ServerData.isServer()) {
                BluetoothHelper.sendDataToPairedDevice(ServerData.getServer(), "START_" + minigame + "_null_");
            } else{
                BluetoothHelper.sendDataToPairedDevice(ServerData.getTeamMembers(1).get(0), "START_" + minigame + "_null_");
            }

            ((GameActivity) getActivity()).changeFragment(f, minigame.toUpperCase());
        }
    }


}
