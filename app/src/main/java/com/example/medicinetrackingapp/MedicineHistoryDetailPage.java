package com.example.medicinetrackingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import datastuff.IndividualMedicineEntity;

public class MedicineHistoryDetailPage extends Fragment {
    private IndividualMedicineEntity displayMed;
    private int position;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.history_specific_medicine_fragment, container, false);
        getActivity().setTitle("Details");
        Bundle b = getArguments();
        position = (int) b.get("displayMedInt");
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        displayMed = MainActivity.medicineDatabase.individualMedicineDao().getTimeSortedEntry(position);
        //display all the info in the specific medicine history fragment
        ((TextView) view.findViewById(R.id.doseview)).setText(Integer.toString(displayMed.dose));
        ((TextView) view.findViewById(R.id.nameview)).setText(displayMed.name);
        ((TextView) view.findViewById(R.id.quantityview)).setText(Integer.toString(displayMed.quantity));
        ((TextView) view.findViewById(R.id.reasonview)).setText(displayMed.reason);
        SimpleDateFormat d = new SimpleDateFormat("h:mm aa", Locale.getDefault());
        Calendar takenDateTime = Calendar.getInstance();
        Calendar inputTimeDate = Calendar.getInstance();
        takenDateTime.setTimeInMillis(displayMed.takenDateTime);
        inputTimeDate.setTimeInMillis(displayMed.inputTimeDate);

        ((TextView) view.findViewById(R.id.timetakenview)).setText(String.format(Locale.getDefault(), "%s on %d/%d/%d", d.format(takenDateTime.getTime()), takenDateTime.get(Calendar.MONTH), takenDateTime.get(Calendar.DAY_OF_MONTH), takenDateTime.get(Calendar.YEAR)));
        ((TextView) view.findViewById(R.id.timeinputtedview)).setText(String.format(Locale.getDefault(), "%s on %d/%d/%d", d.format(inputTimeDate.getTime()), inputTimeDate.get(Calendar.MONTH), inputTimeDate.get(Calendar.DAY_OF_MONTH), inputTimeDate.get(Calendar.YEAR)));

        view.findViewById(R.id.deletebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove entry and go back to history page
                MainActivity.medicineDatabase.individualMedicineDao().delete(displayMed);
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        view.findViewById(R.id.editbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putInt("editMedicine", position);
                MedicineEditPage page = new MedicineEditPage(); //TODO make sure gets correct medicine
                page.setArguments(b);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, page).addToBackStack(null).commit();
                //TODO make switch to add_medicine_fragment and populate elements, then overwrite after clicking finished
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}
