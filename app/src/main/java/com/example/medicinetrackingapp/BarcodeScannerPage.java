package com.example.medicinetrackingapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarcodeScannerPage extends Fragment {

    OnScanListener callback;
    CameraSource source;
    BarcodeDetector bd;
    //TODO switch this to return scanned stuff, and not do anything else

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.barcode_scanner_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        return v;
    }

    @Override
    public void onStop() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        super.onStop();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SurfaceView sv = view.findViewById(R.id.camerapreviewview);
        bd = new BarcodeDetector.Builder(getActivity().getApplicationContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        source = new CameraSource.Builder(getContext(), bd).setAutoFocusEnabled(true).build(); //Default size: 1024x768


        //followed tutorial https://www.youtube.com/watch?v=ej51mAYXbKs and looked at a few others
        sv.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) { //making sure that camera is allowed
                    return;
                }
                try {
                    source.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                source.stop();
            }
        });

        bd.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            boolean done = false;

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> codes = detections.getDetectedItems();//list of detected codes
                if (codes.size() != 0 && !done) {//something is detected
                    callback.onScan(codes.valueAt(0).displayValue);
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public void setScanListener(OnScanListener callback) {
        this.callback = callback;
    }

    public interface OnScanListener {
        void onScan(String code);
    }
}
