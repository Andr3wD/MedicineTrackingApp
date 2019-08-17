package com.example.medicinetrackingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import datastuff.CustomMedicineEntity;

public class CustomMedicineInputPage extends Fragment implements BarcodeScannerPage.OnScanListener {

    String barcode = "";
    CustomMedicineInputPage me;
    TextView testView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.remember_medicine_input_fragment, container, false);
        getActivity().setTitle("Add Custom Medicine");
        me = this;
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        testView = view.findViewById(R.id.remember_input_barcodes);

        view.findViewById(R.id.remember_input_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //attach listener to BarcodeScannerPage so I can get the barcode scanned here
                BarcodeScannerPage b = new BarcodeScannerPage();
                b.setScanListener(me);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, b).addToBackStack(null).commit();
            }
        });


        view.findViewById(R.id.remember_input_savebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomMedicineEntity entry = new CustomMedicineEntity();

                try {//grab all the stuff inputted by user or barcode inputted by BarcodeScannerPage and save it into a new IndividualCustomMedicine
                    entry.name = ((TextView) v.getRootView().findViewById(R.id.remember_input_name)).getText().toString();
                    entry.dose = Integer.parseInt(((TextView) v.getRootView().findViewById(R.id.remember_input_dose)).getText().toString());
                    entry.reason = ((TextView) v.getRootView().findViewById(R.id.remember_input_reason)).getText().toString();
                    entry.use = ((TextView) v.getRootView().findViewById(R.id.remember_input_use)).getText().toString();
                    entry.medicineLeft = Integer.parseInt(((TextView) v.getRootView().findViewById(R.id.remember_input_medsleft)).getText().toString());
                    entry.quantity = Integer.parseInt(((TextView) v.getRootView().findViewById(R.id.remember_input_quantity)).getText().toString());
                    entry.barcodes = ((TextView) v.getRootView().findViewById(R.id.remember_input_barcodes)).getText().toString();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Missing data!", Toast.LENGTH_SHORT).show();
                    return;
                }
                MainActivity.medicineDatabase.customMedicineDao().insertAll(entry);
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }

            }
        });

        ((TextView) view.findViewById(R.id.remember_input_medsleft)).setText("-1"); //this will be default, and will stand for 'not tracking'
        //TODO find better way to do this

        super.onViewCreated(view, savedInstanceState);
    }

    //when fragment is resumed after BarcodeScannerPage is popped off in onScan
    @Override
    public void onResume() {
        testView.setText(barcode);
        super.onResume();
    }

    @Override
    public void onScan(String code) {
        barcode = barcode + " " + code;
        getFragmentManager().popBackStack();
    }
}
