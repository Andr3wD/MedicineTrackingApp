package com.example.medicinetrackingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MedicineHistoryDetailPage extends Fragment {
    private IndividualMedicine displayMed;
    private int position;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.history_specific_medicine_fragment, container, false);
        setHasOptionsMenu(true);
        Bundle b = getArguments();
        position = (int) b.get("displayMedInt");
        displayMed = MainActivity.medicineManager.medicineHistoryList.get(position);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //display all the info in the specific medicine history fragment
        ((TextView) view.findViewById(R.id.doseview)).setText(Integer.toString(displayMed.dose));
        ((TextView) view.findViewById(R.id.nameview)).setText(displayMed.name);
        ((TextView) view.findViewById(R.id.quantityview)).setText(Integer.toString(displayMed.quantity));
        SimpleDateFormat d = new SimpleDateFormat("h:mm aa", Locale.getDefault());
        ((TextView) view.findViewById(R.id.timetakenview)).setText(String.format(Locale.getDefault(), "%s on %d/%d/%d", d.format(displayMed.takenDateTime.getTime()), displayMed.takenDateTime.get(Calendar.MONTH), displayMed.takenDateTime.get(Calendar.DAY_OF_MONTH), displayMed.takenDateTime.get(Calendar.YEAR)));
        ((TextView) view.findViewById(R.id.timeinputtedview)).setText(String.format(Locale.getDefault(), "%s on %d/%d/%d", d.format(displayMed.inputTimeDate.getTime()), displayMed.inputTimeDate.get(Calendar.MONTH), displayMed.inputTimeDate.get(Calendar.DAY_OF_MONTH), displayMed.inputTimeDate.get(Calendar.YEAR)));

        view.findViewById(R.id.deletebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove entry and go back to history page
                MainActivity.medicineManager.remove(position);
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        view.findViewById(R.id.editbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO make switch to add_medicine_fragment and populate elements, then overwrite after clicking finished
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}
