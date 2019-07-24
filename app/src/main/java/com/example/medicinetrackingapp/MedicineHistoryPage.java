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
import android.widget.Toast;

import java.util.Calendar;

public class MedicineHistoryPage extends Fragment implements HistoryPageRecyclerAdapter.ItemClickListener, BarcodeScannerPage.OnScanListener {

    MedicineHistoryPage me;
    boolean successfulScan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.medicine_history_fragment, container, false);
        setHasOptionsMenu(true);
        getActivity().setTitle("Medicine History");
        me = this;
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = view.findViewById(R.id.fab); //add entry button
        FloatingActionButton fabscan = view.findViewById(R.id.fabscan); //add entry button


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MedicineInputPage()).addToBackStack(null).commit();
            }
        });

        fabscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarcodeScannerPage b = new BarcodeScannerPage();
                b.setScanListener(me);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, b).addToBackStack(null).commit();
            }
        });


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

        Bundle b = new Bundle();
        b.putInt("displayMedInt", position);
        MedicineHistoryDetailPage page = new MedicineHistoryDetailPage(); //TODO make sure gets correct medicine
        page.setArguments(b);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, page).addToBackStack(null).commit();
        Log.i("test", "clickhistory " + position);
    }

    @Override
    public void onResume() {
        if(successfulScan == false){
            Toast.makeText(getContext(), "Unknown Code", Toast.LENGTH_SHORT).show();
        }
        super.onResume();
    }

    @Override
    public void onScan(String code) {
        getFragmentManager().popBackStack();

        IndividualCustomMedicine data = MainActivity.customMedicineManager.exists(code);
        if (data != null) { //Barcode has already been recorded into remembered medicine
            IndividualMedicine entry = new IndividualMedicine();
            entry.name = data.name;
            entry.quantity = data.quantity;
            entry.reason = data.reason;
            entry.dose = data.dose;
            entry.takenDateTime = Calendar.getInstance();
            //TODO add use to entry edit page and other places
            MainActivity.medicineManager.add(entry);
            getFragmentManager().popBackStack();
            successfulScan = true;
        } else { //doesn't exist already, throw up a toast
            successfulScan = false;
        }
    }
}
