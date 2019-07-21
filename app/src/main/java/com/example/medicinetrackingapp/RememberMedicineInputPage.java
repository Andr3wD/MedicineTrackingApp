package com.example.medicinetrackingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RememberMedicineInputPage extends Fragment {
    String barcode;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.remember_medicine_input_fragment, container, false);
        setHasOptionsMenu(false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.remember_input_savebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndividualCustomMedicine entry = new IndividualCustomMedicine();
                try {
                    entry.name = ((TextView) v.getRootView().findViewById(R.id.remember_input_name)).getText().toString();
                    entry.dose = Integer.parseInt(((TextView) v.getRootView().findViewById(R.id.remember_input_dose)).getText().toString());
                    entry.reason = ((TextView) v.getRootView().findViewById(R.id.remember_input_reason)).getText().toString();
                    entry.use = ((TextView) v.getRootView().findViewById(R.id.remember_input_use)).getText().toString();

                    //dealing with barcode list
                    entry.quantity = Integer.parseInt(((TextView) v.getRootView().findViewById(R.id.remember_input_quantity)).getText().toString());
                    String[] s = ((TextView) v.getRootView().findViewById(R.id.remember_input_barcodes)).getText().toString().split(" ");
                    for (int i = 0; i < s.length; i++) {
                        entry.barcodes.add(s[i]);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(),"Missing data!", Toast.LENGTH_SHORT).show();
                    return;
                }
                MainActivity.customMedicineManager.add(entry);
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }

            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}
