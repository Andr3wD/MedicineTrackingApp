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
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
                try {
                    newEntry.name = ((TextView) v.getRootView().findViewById(R.id.nameText)).getText().toString();
                    newEntry.reason = ((TextView) v.getRootView().findViewById(R.id.reasonText)).getText().toString();
                    newEntry.quantity = Long.parseLong(((TextView) v.getRootView().findViewById(R.id.quantityText)).getText().toString());
                    newEntry.dose = 0;
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Cannot leave anything empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i("test", "Adding to list");
                MainActivity.medicineManager.add(newEntry);
                if (getFragmentManager() != null) { //TODO make sure actually does something
                    getFragmentManager().popBackStack(); //pops back of stack, which should be the input page added from the history page
                }
                //getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MedicineInputPage()).commit();
            }
        });

        //opens the date chooser popup for user to select date and then updates the newEntry with the data
        dateChoice = view.findViewById(R.id.dateButton);

        //default date entry to today
        final Calendar today = Calendar.getInstance();
        dateChoice.setText(String.format("%s/%s/%s", today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), today.get(Calendar.YEAR)));

        newEntry.takenDateTime = today; //this sets both timechoice and datechoice

        //bring up date selection pop-up and save entry
        dateChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                DatePickerDialog t = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar cal2 = Calendar.getInstance();
                        cal2.set(year, month, dayOfMonth);
                        ((TextView) dateChoice.findViewById(R.id.dateButton)).setText(String.format("%s/%s/%s", month, dayOfMonth, year));
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

        //set time button to default current time
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm aa");
        timeChoice.setText(sdf.format(today.getTime()));

        timeChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                TimePickerDialog t = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm aa");

                        newEntry.takenDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        newEntry.takenDateTime.set(Calendar.MINUTE, minute);

                        ((TextView) timeChoice.findViewById(R.id.timeButton)).setText(sdf1.format(newEntry.takenDateTime.getTime()));
                        Log.i("test", "working perfectly fine");
                    }
                }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false);
                t.show();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
