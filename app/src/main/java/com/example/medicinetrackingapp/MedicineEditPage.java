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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MedicineEditPage extends Fragment {

    public TextView timeChoice;
    public TextView dateChoice;
    public IndividualMedicine editEntry;
    private int position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_medicine_fragment, container, false);
        setHasOptionsMenu(true);
        getActivity().setTitle("Medicine Edit");
        Bundle b = getArguments();
        position = (int) b.get("editMedicine");
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //Mostly an exact copy of input page, but edited to fill in the data and overwrite it on save.

        editEntry = MainActivity.medicineManager.medicineHistoryList.get(position);
        ((Button) view.findViewById(R.id.submit_button)).setText("Update");
        view.findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    editEntry.name = ((TextView) v.getRootView().findViewById(R.id.nameText)).getText().toString();
                    editEntry.reason = ((TextView) v.getRootView().findViewById(R.id.reasonText)).getText().toString();
                    editEntry.quantity = Integer.parseInt(((TextView) v.getRootView().findViewById(R.id.quantityText)).getText().toString());
                    editEntry.dose = Integer.parseInt(((TextView) v.getRootView().findViewById(R.id.doseText)).getText().toString());
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Cannot leave anything empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i("test", "Updating entry " + position);
                MainActivity.medicineManager.save();
                if (getFragmentManager() != null) { //TODO make sure actually does something
                    getFragmentManager().popBackStack(); //pops back of stack, which should be the input page added from the history page
                }
                //getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MedicineInputPage()).commit();
            }
        });

        ((EditText) view.findViewById(R.id.nameText)).setText(editEntry.name);
        ((EditText) view.findViewById(R.id.reasonText)).setText(editEntry.reason);
        ((EditText) view.findViewById(R.id.quantityText)).setText(Integer.toString(editEntry.quantity));
        ((EditText) view.findViewById(R.id.doseText)).setText(Integer.toString(editEntry.dose));

        //opens the date chooser popup for user to select date and then updates the editEntry with the data
        dateChoice = view.findViewById(R.id.dateButton);

        dateChoice.setText(String.format("%s/%s/%s", editEntry.takenDateTime.get(Calendar.MONTH), editEntry.takenDateTime.get(Calendar.DAY_OF_MONTH), editEntry.takenDateTime.get(Calendar.YEAR)));

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
                        editEntry.takenDateTime.set(Calendar.YEAR, year);
                        editEntry.takenDateTime.set(Calendar.MONTH, month);//TODO WATCH FOR POSSIBLE DISCREPANCIES
                        editEntry.takenDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    }

                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                t.show();
            }
        });

        //opens the time chooser popup for users to select a time and then updates editEntry with the time
        timeChoice = view.findViewById(R.id.timeButton);

        SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm aa");

        ((TextView) timeChoice.findViewById(R.id.timeButton)).setText(sdf1.format(editEntry.takenDateTime.getTime()));

        timeChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                TimePickerDialog t = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm aa");

                        editEntry.takenDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        editEntry.takenDateTime.set(Calendar.MINUTE, minute);

                        ((TextView) timeChoice.findViewById(R.id.timeButton)).setText(sdf1.format(editEntry.takenDateTime.getTime()));
                    }
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);
                t.show();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}
