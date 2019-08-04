package com.example.medicinetrackingapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import datastuff.CustomMedicineEntity;
import datastuff.IndividualMedicineEntity;

public class MedicineHistoryPage extends Fragment implements HistoryPageRecyclerAdapter.ItemClickListener, BarcodeScannerPage.OnScanListener {

    MedicineHistoryPage me;
    LinearLayoutManager lM;
    boolean loadingNewData = true;
    ArrayList<IndividualMedicineEntity> currentlyLoadedData; //Going to just make loaded data reset every time as to reduce amount of data loaded at a time.
    HistoryPageRecyclerAdapter hPRA;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.medicine_history_fragment, container, false);
        setHasOptionsMenu(true);
        getActivity().setTitle("Medicine History");
        me = this;
        currentlyLoadedData = new ArrayList<>();//At the beginning, I want a few entries already there
        for (int i = 0; i < 20; i++) {
            if (MainActivity.medicineDatabase.individualMedicineDao().size() > i) {
                currentlyLoadedData.add(i, MainActivity.medicineDatabase.individualMedicineDao().getTimeSortedEntry(i));
            }
        }
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = view.findViewById(R.id.fab); //add entry button
        FloatingActionButton fabscan = view.findViewById(R.id.fabscan); //add entry button


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MedicineInputPage inputPage = new MedicineInputPage();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, inputPage).addToBackStack(null).commit();
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
        lM = new LinearLayoutManager(getContext());
        rV.setLayoutManager(lM);

        hPRA = new HistoryPageRecyclerAdapter(getContext(), currentlyLoadedData);
        hPRA.setClickListener(this);
        rV.setAdapter(hPRA);

        /*IndividualMedicineEntity i = new IndividualMedicineEntity();
        i.name = "TestName";
        i.dose = 1;
        i.quantity = 2;
        i.position = 2;
        currentlyLoadedData.add(i);*/

        rV.addOnScrollListener(new RecyclerView.OnScrollListener() {//Infinite scrolling. Add entries as the user scrolls
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {//scroll down
                    Log.i("test", "scrolling down");
                    if (loadingNewData && lM.getChildCount() + lM.findFirstVisibleItemPosition() >= lM.getItemCount()) {
                        loadingNewData = false;
                        for (int i = 1; i <= 5; i++) {
                            currentlyLoadedData.add(currentlyLoadedData.size(), MainActivity.medicineDatabase.individualMedicineDao().findByPosition(currentlyLoadedData.size()));
                        }
                        hPRA.notifyDataSetChanged();
                    }

                }
            }
        });


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

        currentlyLoadedData = new ArrayList<>();//At the beginning, I want a few entries already there
        for (int i = 0; i < 20; i++) {
            if (MainActivity.medicineDatabase.individualMedicineDao().size() > i) {
                currentlyLoadedData.add(i, MainActivity.medicineDatabase.individualMedicineDao().getTimeSortedEntry(i));
            }
        }
        hPRA.notifyDataSetChanged(); //TODO remove and find other solution
        super.onResume();
    }

    @Override
    public void onScan(String code) {
        getFragmentManager().popBackStack();
        List<CustomMedicineEntity> list = MainActivity.medicineDatabase.customMedicineDao().getAllSortedId(); //no sorting would probably be best, or sorting on time, as most recently inputted will probably be used the soonest
        //TODO change to no sorting or time sorting
        CustomMedicineEntity data = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).barcodes.contains(code)) {
                data = list.get(i);
                break;
            }
        }


        if (data != null) { //Barcode has already been recorded into remembered medicine
            IndividualMedicineEntity entry = new IndividualMedicineEntity();
            entry.name = data.name;
            entry.quantity = data.quantity;
            entry.reason = data.reason;
            entry.dose = data.dose;
            entry.takenDateTime = Calendar.getInstance().getTimeInMillis();
            entry.inputTimeDate = Calendar.getInstance().getTimeInMillis();
            //TODO add use to entry edit page and other places
            MainActivity.medicineDatabase.individualMedicineDao().insertAll(entry);
            MainActivity.medicineDatabase.customMedicineDao().update(data.name, data.quantity, data.reason, data.dose, data.Id, data.use, data.barcodes, data.medicineLeft-1);
            getFragmentManager().popBackStack();
        } else { //doesn't exist already, throw up a toast

        }
    }
}
