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

public class MedicineInputPage extends Fragment {

    public TextView timeChoice;
    public TextView dateChoice;
    public IndividualMedicineEntity newEntry;
    private int position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_medicine_fragment, container, false);
        getActivity().setTitle("Add Medicine");
        position = MainActivity.medicineDatabase.individualMedicineDao().size() + 1;
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        newEntry = new IndividualMedicineEntity();
        view.findViewById(R.id.submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //grab all data from text entries and populate newEntry with it
                    newEntry.name = ((TextView) v.getRootView().findViewById(R.id.nameText)).getText().toString();
                    newEntry.reason = ((TextView) v.getRootView().findViewById(R.id.reasonText)).getText().toString();
                    newEntry.quantity = Integer.parseInt(((TextView) v.getRootView().findViewById(R.id.quantityText)).getText().toString());
                    newEntry.dose = Integer.parseInt(((TextView) v.getRootView().findViewById(R.id.doseText)).getText().toString());
                    newEntry.position = position;
                    newEntry.inputTimeDate = Calendar.getInstance().getTimeInMillis();

                    //deal with custom preset medicines and tracking the medicine left
                    if (newEntry.baseCustomMedicineID != -1) { //no custom medicine preset selected
                        CustomMedicineEntity base = MainActivity.medicineDatabase.customMedicineDao().findById(newEntry.baseCustomMedicineID);
                        if (base.medicineLeft - newEntry.quantity >= 0) {
                            MainActivity.medicineDatabase.customMedicineDao().update(base.name, base.quantity, base.reason, base.dose, base.Id, base.use, base.barcodes, base.medicineLeft - newEntry.quantity);
                        } else if (base.medicineLeft != -1) { //if -1, then won't be tracking medicineLeft
                            Toast.makeText(getContext(), "ERROR, medicine selected has already been used up", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                } catch (Exception e) {
                    Toast.makeText(getContext(), "Cannot leave anything empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i("test", "Adding to list");
                MainActivity.medicineDatabase.individualMedicineDao().insertAll(newEntry);
                /*InputMethodManager iMM = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                iMM.hideSoftInputFromWindow(getView().getWindowToken(), 0);*/
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

        newEntry.takenDateTime = today.getTimeInMillis(); //this sets both timechoice and datechoice

        //bring up date selection pop-up and save entry
        dateChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                DatePickerDialog t = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar takenDateTime = Calendar.getInstance();
                        takenDateTime.setTimeInMillis(newEntry.takenDateTime);
                        takenDateTime.set(year, month, dayOfMonth);
                        ((TextView) dateChoice.findViewById(R.id.dateButton)).setText(String.format("%s/%s/%s", month, dayOfMonth, year));

                        /*
                        takenDateTime.set(Calendar.YEAR, year);
                        takenDateTime.set(Calendar.MONTH, month);//TODO WATCH FOR POSSIBLE DISCREPANCIES
                        takenDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        */

                        newEntry.takenDateTime = takenDateTime.getTimeInMillis();
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
                        Calendar takenDateTime = Calendar.getInstance();
                        takenDateTime.setTimeInMillis(newEntry.takenDateTime);

                        takenDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        takenDateTime.set(Calendar.MINUTE, minute);

                        ((TextView) timeChoice.findViewById(R.id.timeButton)).setText(sdf1.format(takenDateTime.getTime()));

                        newEntry.takenDateTime = takenDateTime.getTimeInMillis();
                    }
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);
                t.show();
            }
        });

        List<String> nameList = MainActivity.medicineDatabase.customMedicineDao().getAllNamesSortedId();
        nameList.add(0, "None"); //for no selection

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nameList);
        Spinner dropdown = view.findViewById(R.id.add_medicine_spinner);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { //selected nothing
                    newEntry.baseCustomMedicineID = -1;
                    ((EditText) view.getRootView().findViewById(R.id.nameText)).setText("");
                    ((EditText) view.getRootView().findViewById(R.id.reasonText)).setText("");
                    ((EditText) view.getRootView().findViewById(R.id.quantityText)).setText("");
                    ((EditText) view.getRootView().findViewById(R.id.doseText)).setText("");
                } else {
                    CustomMedicineEntity selected = MainActivity.medicineDatabase.customMedicineDao().getAllSortedId().get(position - 1);

                    ((EditText) view.getRootView().findViewById(R.id.nameText)).setText(selected.name);
                    ((EditText) view.getRootView().findViewById(R.id.reasonText)).setText(selected.reason);
                    ((EditText) view.getRootView().findViewById(R.id.quantityText)).setText(Integer.toString(selected.quantity));
                    ((EditText) view.getRootView().findViewById(R.id.doseText)).setText(Integer.toString(selected.dose));
                    newEntry.baseCustomMedicineID = selected.Id;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}
