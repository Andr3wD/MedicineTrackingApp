package com.example.medicinetrackingapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

public class MedicineHistoryDetailPage extends Fragment {
    private IndividualMedicine displayMed;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.history_specific_medicine_fragment, container, false);
        setHasOptionsMenu(true);
        Bundle b = getArguments();
        displayMed = MainActivity.medicineManager.medicineHistoryList.get((int) b.get("displayMedInt"));
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((TextView) view.findViewById(R.id.doseview)).setText(Integer.toString(displayMed.dose));
        ((TextView) view.findViewById(R.id.nameview)).setText(displayMed.name);
        ((TextView) view.findViewById(R.id.quantityview)).setText(displayMed.quantity.toString());
        ((TextView) view.findViewById(R.id.timetakenview)).setText(displayMed.takenDateTime.get(Calendar.HOUR_OF_DAY) + displayMed.takenDateTime.get(Calendar.MINUTE) + " on " + displayMed.takenDateTime.get(Calendar.MONTH) + "/" + displayMed.takenDateTime.get(Calendar.DAY_OF_MONTH) + "/" + displayMed.takenDateTime.get(Calendar.YEAR)); //TODO fix formatting
        ((TextView) view.findViewById(R.id.timeinputtedview)).setText(displayMed.inputTimeDate.get(Calendar.HOUR_OF_DAY) + displayMed.inputTimeDate.get(Calendar.MINUTE) + " on " + displayMed.inputTimeDate.get(Calendar.MONTH) + "/" + displayMed.inputTimeDate.get(Calendar.DAY_OF_MONTH) + "/" + displayMed.inputTimeDate.get(Calendar.YEAR)); //TODO fix formatting


        super.onViewCreated(view, savedInstanceState);
    }
}
