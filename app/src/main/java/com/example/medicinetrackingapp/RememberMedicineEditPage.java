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

import java.util.ArrayList;

public class RememberMedicineEditPage extends Fragment {

    private int position;

    //TODO implement barcode stuff and make it work

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.remember_medicine_input_fragment, container, false);
        setHasOptionsMenu(false);
        Bundle b = getArguments();
        position = (int) b.get("displayRememberMedicinePosition");
        getActivity().setTitle("Edit Custom Medicine");
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //almost same click listener as RememberMedicineInputPage, except changed to edit data instead of add new entry
        view.findViewById(R.id.remember_input_savebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndividualCustomMedicine entry = MainActivity.customMedicineManager.rememberedMedicineList.get(position);
                try {
                    entry.name = ((TextView) v.getRootView().findViewById(R.id.remember_input_name)).getText().toString();
                    entry.dose = Integer.parseInt(((TextView) v.getRootView().findViewById(R.id.remember_input_dose)).getText().toString());
                    entry.reason = ((TextView) v.getRootView().findViewById(R.id.remember_input_reason)).getText().toString();
                    entry.use = ((TextView) v.getRootView().findViewById(R.id.remember_input_use)).getText().toString();
                    entry.quantity = Integer.parseInt(((TextView) v.getRootView().findViewById(R.id.remember_input_quantity)).getText().toString());
                    entry.medsLeft = Integer.parseInt(((TextView) v.getRootView().findViewById(R.id.remember_input_medsleft)).getText().toString());


                    //dealing with barcode list
                    String[] s = ((TextView) v.getRootView().findViewById(R.id.remember_input_barcodes)).getText().toString().split(" ");
                    ArrayList a = new ArrayList();
                    for (int i = 0; i < s.length; i++) {
                        entry.barcodes.add(s[i]);
                    }
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

        //populate already known data into input page, but treat it as an edit page
        ((TextView) view.findViewById(R.id.remember_input_name)).setText(MainActivity.customMedicineManager.rememberedMedicineList.get(position).name);
        ((TextView) view.findViewById(R.id.remember_input_dose)).setText(Integer.toString(MainActivity.customMedicineManager.rememberedMedicineList.get(position).dose));
        ((TextView) view.findViewById(R.id.remember_input_reason)).setText(MainActivity.customMedicineManager.rememberedMedicineList.get(position).reason);
        ((TextView) view.findViewById(R.id.remember_input_use)).setText(MainActivity.customMedicineManager.rememberedMedicineList.get(position).use);
        ((TextView) view.findViewById(R.id.remember_input_quantity)).setText(Integer.toString(MainActivity.customMedicineManager.rememberedMedicineList.get(position).quantity));
        ((TextView) view.findViewById(R.id.remember_input_medsleft)).setText(Integer.toString(MainActivity.customMedicineManager.rememberedMedicineList.get(position).medsLeft));

        //dealing with setting the barcodes, which there could be multiple, so it's an array, so I need to deal with turing it into a string
        String s = "";
        ArrayList<String> arr = MainActivity.customMedicineManager.rememberedMedicineList.get(position).barcodes;
        for (int i = 0; i < arr.size(); i++) {
            s = s + arr.get(i) + " ";
        }
        ((TextView) view.findViewById(R.id.remember_input_barcodes)).setText(s);

        super.onViewCreated(view, savedInstanceState);
    }
}
