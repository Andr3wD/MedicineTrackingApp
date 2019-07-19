package com.example.medicinetrackingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;

public class MedicineHistoryPage extends Fragment implements HistoryPageRecyclerAdapter.ItemClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.medicine_history_fragment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = view.findViewById(R.id.fab); //add entry button

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MedicineInputPage()).addToBackStack(null).commit();
            }
        });

        //TODO add date separators for medicines taken

        /*
        ArrayList<IndividualMedicine> newList = (ArrayList<IndividualMedicine>) MainActivity.medicineManager.medicineHistoryList.clone();
        for (int i = 1; i < newList.size(); i++) {
            Calendar currentCal = newList.get(i).takenDateTime;
            Calendar previousCal = newList.get(i - 1).takenDateTime;
            if (currentCal.get(Calendar.DAY_OF_MONTH) > previousCal.get(Calendar.DAY_OF_MONTH) || currentCal.get(Calendar.YEAR) > previousCal.get(Calendar.YEAR) || currentCal.get(Calendar.MONTH) > previousCal.get(Calendar.MONTH)) {
                IndividualMedicine temp = new IndividualMedicine();
                temp.takenDateTime = previousCal;
                newList.add(i, temp);
                i++;
            }
        }
        */

        RecyclerView rV = ((RecyclerView) view.findViewById(R.id.history_recyclerview));
        rV.setLayoutManager(new LinearLayoutManager(getContext()));
        IndividualMedicine[] s = new IndividualMedicine[MainActivity.medicineManager.medicineHistoryList.size()];
        HistoryPageRecyclerAdapter hPRA = new HistoryPageRecyclerAdapter(getContext(), MainActivity.medicineManager.medicineHistoryList.toArray(s));
        hPRA.setClickListener(this);
        rV.setAdapter(hPRA);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClick(View view, int position) {
        //TODO switch to bundle instead of constructor
        Bundle b = new Bundle();
        b.putInt("displayMedInt",position);
        MedicineHistoryDetailPage page = new MedicineHistoryDetailPage(); //TODO make sure gets correct medicine
        page.setArguments(b);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, page).commit();
        Log.i("test", "clickhistory " + position);
    }
}
