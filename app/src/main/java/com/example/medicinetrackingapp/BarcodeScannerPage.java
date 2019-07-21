package com.example.medicinetrackingapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.Calendar;

public class BarcodeScannerPage extends Fragment {

    CameraSource source;
    BarcodeDetector bd;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.barcode_scanner_fragment, container, false);
        setHasOptionsMenu(false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SurfaceView sv = view.findViewById(R.id.camerapreviewview);
        final TextView tv = view.findViewById(R.id.outputtextview);
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
                    tv.setText(codes.valueAt(0).displayValue);
                    IndividualCustomMedicine data = MainActivity.customMedicineManager.exists(codes.valueAt(0).displayValue);
                    if (data != null) { //Barcode has already been recorded into remembered medicine
                        done = true;
                        IndividualMedicine entry = new IndividualMedicine();
                        entry.name = data.name;
                        entry.quantity = data.quantity;
                        entry.reason = data.reason;
                        entry.dose = data.dose;
                        entry.takenDateTime = Calendar.getInstance();
                        //TODO add use to entry edit page and other places
                        MainActivity.medicineManager.add(entry);
                        getFragmentManager().popBackStack();
                    } else { //barcode is brand new, and isn't on record.
                        //open up a new RememberMedicineInputPage page with barcode populated already
                        Toast.makeText(getContext(), "Code not recognized!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
