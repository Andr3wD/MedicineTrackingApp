package com.example.medicinetrackingapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MedicineInputPage extends Fragment {

    public TextView timeChoice;
    public TextView dateChoice;
    public IndividualMedicine newEntry;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_medicine_fragment, container, false);
        setHasOptionsMenu(true);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        newEntry = new IndividualMedicine();
        view.findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newEntry.name = ((TextView) v.getRootView().findViewById(R.id.nameText)).getText().toString();
                newEntry.reason = ((TextView) v.getRootView().findViewById(R.id.reasonText)).getText().toString();
                newEntry.quantity = Long.parseLong(((TextView) v.getRootView().findViewById(R.id.quantityText)).getText().toString());
                newEntry.dose = 0;
                Log.i("test", "Adding to list"); //TODO fix why this isn't working
                MainActivity.medicineManager.add(newEntry);
                getFragmentManager().popBackStack(); //pops back of stack, which should be the input page added from the history page
                //getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MedicineInputPage()).commit();
            }
        });

        //opens the date chooser popup for user to select date and then updates the newEntry with the data
        dateChoice = view.findViewById(R.id.dateButton);
        dateChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                DatePickerDialog t = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar cal2 = Calendar.getInstance();
                        cal2.set(year, month, dayOfMonth);
                        ((TextView) dateChoice.findViewById(R.id.dateButton)).setText(Integer.toString(month) + "/" + Integer.toString(dayOfMonth) + "/" + Integer.toString(year));
                        newEntry.takenDateTime.set(Calendar.YEAR, year);
                        newEntry.takenDateTime.set(Calendar.MONTH, month);//TODO WATCH FOR POSSIBLE DISCREPANCIES
                        newEntry.takenDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    }

                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                t.show();
            }
        });

        //opens the time chooser popup for users to select a time and then updates newEntry with the time
        timeChoice = view.findViewById(R.id.timeButton);
        timeChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                TimePickerDialog t = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ((TextView) timeChoice.findViewById(R.id.timeButton)).setText(hourOfDay + ":" + minute);
                        newEntry.takenDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        newEntry.takenDateTime.set(Calendar.MINUTE, minute);
                    }
                }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false);
                t.show();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
