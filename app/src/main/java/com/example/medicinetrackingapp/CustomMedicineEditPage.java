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

public class CustomMedicineEditPage extends Fragment {

    private int id;

    //TODO implement barcode stuff and make it work

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.remember_medicine_input_fragment, container, false);
        setHasOptionsMenu(false);
        Bundle b = getArguments();
        id = (int) b.get("displayRememberMedicineId");
        getActivity().setTitle("Edit Custom Medicine");
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //almost same click listener as CustomMedicineInputPage, except changed to edit data instead of add new entry
        view.findViewById(R.id.remember_input_savebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String name = ((TextView) v.getRootView().findViewById(R.id.remember_input_name)).getText().toString();
                    int dose = Integer.parseInt(((TextView) v.getRootView().findViewById(R.id.remember_input_dose)).getText().toString());
                    String reason = ((TextView) v.getRootView().findViewById(R.id.remember_input_reason)).getText().toString();
                    String use = ((TextView) v.getRootView().findViewById(R.id.remember_input_use)).getText().toString();
                    int quantity = Integer.parseInt(((TextView) v.getRootView().findViewById(R.id.remember_input_quantity)).getText().toString());
                    int medicineLeft = Integer.parseInt(((TextView) v.getRootView().findViewById(R.id.remember_input_medsleft)).getText().toString());
                    String barcodes = ((TextView) v.getRootView().findViewById(R.id.remember_input_barcodes)).getText().toString();

                    MainActivity.medicineDatabase.customMedicineDao().update(name, quantity, reason, dose, id, use, barcodes,medicineLeft);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Missing data!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }

            }
        });
        CustomMedicineEntity entry = MainActivity.medicineDatabase.customMedicineDao().findById(id);

        //populate already known data into input page, but treat it as an edit page
        ((TextView) view.findViewById(R.id.remember_input_name)).setText(entry.name);
        ((TextView) view.findViewById(R.id.remember_input_dose)).setText(Integer.toString(entry.dose));
        ((TextView) view.findViewById(R.id.remember_input_reason)).setText(entry.reason);
        ((TextView) view.findViewById(R.id.remember_input_use)).setText(entry.use);
        ((TextView) view.findViewById(R.id.remember_input_quantity)).setText(Integer.toString(entry.quantity));
        ((TextView) view.findViewById(R.id.remember_input_medsleft)).setText(Integer.toString(entry.medicineLeft));
        ((TextView) view.findViewById(R.id.remember_input_barcodes)).setText(entry.barcodes);

        super.onViewCreated(view, savedInstanceState);
    }
}
