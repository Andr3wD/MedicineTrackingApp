package com.example.medicinetrackingapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import datastuff.CustomMedicineEntity;
import datastuff.IndividualMedicineEntity;

public class MedicineEditPage extends Fragment {

    public TextView timeChoice;
    public TextView dateChoice;
    public IndividualMedicineEntity editEntry;
    private int position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_medicine_fragment, container, false);
        getActivity().setTitle("Medicine Edit");
        Bundle b = getArguments();
        position = (int) b.get("editMedicine");
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //Mostly an exact copy of input page, but edited to fill in the data and overwrite it on save.

        editEntry = MainActivity.medicineDatabase.individualMedicineDao().getTimeSortedEntry(position);
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
                MainActivity.medicineDatabase.individualMedicineDao().update(editEntry.position, editEntry.name, editEntry.takenDateTime, editEntry.quantity, editEntry.reason, editEntry.dose, editEntry.baseCustomMedicineID);
                if (getFragmentManager() != null) { //TODO make sure actually does something
                    getFragmentManager().popBackStack(); //pops back of stack, which should be the input page added from the history page
                }
                //getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MedicineInputPage()).commit();
            }
        });

        //populate text entries with current data of entry
        ((EditText) view.findViewById(R.id.nameText)).setText(editEntry.name);
        ((EditText) view.findViewById(R.id.reasonText)).setText(editEntry.reason);
        ((EditText) view.findViewById(R.id.quantityText)).setText(Integer.toString(editEntry.quantity));
        ((EditText) view.findViewById(R.id.doseText)).setText(Integer.toString(editEntry.dose));


        //opens the date chooser popup for user to select date and then updates the editEntry with the data
        dateChoice = view.findViewById(R.id.dateButton);
        Calendar takenDateTime = Calendar.getInstance();
        takenDateTime.setTimeInMillis(editEntry.takenDateTime);
        dateChoice.setText(String.format("%s/%s/%s", takenDateTime.get(Calendar.MONTH), takenDateTime.get(Calendar.DAY_OF_MONTH), takenDateTime.get(Calendar.YEAR)));
        //bring up date selection pop-up and save entry
        dateChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                DatePickerDialog t = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTimeInMillis(editEntry.takenDateTime);
                        cal2.set(year, month, dayOfMonth);
                        ((TextView) dateChoice.findViewById(R.id.dateButton)).setText(String.format("%s/%s/%s", month, dayOfMonth, year));
                        editEntry.takenDateTime = cal2.getTimeInMillis();

                    }

                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                t.show();
            }
        });


        //opens the time chooser popup for users to select a time and then updates editEntry with the time
        timeChoice = view.findViewById(R.id.timeButton);
        SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm aa");
        ((TextView) timeChoice.findViewById(R.id.timeButton)).setText(sdf1.format(takenDateTime.getTime()));
        timeChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                TimePickerDialog t = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm aa");
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(editEntry.takenDateTime);
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cal.set(Calendar.MINUTE, minute);
                        editEntry.takenDateTime = cal.getTimeInMillis();
                        ((TextView) timeChoice.findViewById(R.id.timeButton)).setText(sdf1.format(cal.getTime()));
                    }
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);
                t.show();
            }
        });


        List<String> nameList = MainActivity.medicineDatabase.customMedicineDao().getAllNamesSortedId();
        nameList.add(0,"Previous"); //for nothing selected in spinner menu

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nameList); //making and setting adapter to populate spinner with entries and track it
        Spinner dropdown = view.findViewById(R.id.add_medicine_spinner);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ((EditText) view.getRootView().findViewById(R.id.nameText)).setText(editEntry.name);
                    ((EditText) view.getRootView().findViewById(R.id.reasonText)).setText(editEntry.reason);
                    ((EditText) view.getRootView().findViewById(R.id.quantityText)).setText(Integer.toString(editEntry.quantity));
                    ((EditText) view.getRootView().findViewById(R.id.doseText)).setText(Integer.toString(editEntry.dose));
                    editEntry.baseCustomMedicineID = -1;
                } else {
                    CustomMedicineEntity selected = MainActivity.medicineDatabase.customMedicineDao().getAllSortedId().get(position - 1);

                    ((EditText) view.getRootView().findViewById(R.id.nameText)).setText(selected.name);
                    ((EditText) view.getRootView().findViewById(R.id.reasonText)).setText(selected.reason);
                    ((EditText) view.getRootView().findViewById(R.id.quantityText)).setText(Integer.toString(selected.quantity));
                    ((EditText) view.getRootView().findViewById(R.id.doseText)).setText(Integer.toString(selected.dose));
                    editEntry.baseCustomMedicineID = selected.Id;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}
