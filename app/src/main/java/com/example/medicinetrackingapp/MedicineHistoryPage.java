package com.example.medicinetrackingapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

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
        //Thanks to https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard https://stackoverflow.com/a/17789187
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        View v = inflater.inflate(R.layout.medicine_history_fragment, container, false);
        getActivity().setTitle("History");
        me = this;
        currentlyLoadedData = new ArrayList<>();//At the beginning, I want a few entries already there
        for (int i = 0; i < 20; i++) {
            if (MainActivity.medicineDatabase.individualMedicineDao().size() > i) {
                currentlyLoadedData.add(MainActivity.medicineDatabase.individualMedicineDao().getTimeSortedEntry(i));
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
                    Log.i("test", "scrolling down " + lM.findFirstVisibleItemPosition() + " " + lM.findLastCompletelyVisibleItemPosition());
                    if (loadingNewData && lM.getChildCount() + lM.findFirstVisibleItemPosition() >= lM.getItemCount()) {
                        Log.i("test", "inside scroll down");
                        loadingNewData = false;
                        for (int i = 1; i <= 5; i++) {
                            Log.i("test", "adding new data");
                            hPRA.mData.add(MainActivity.medicineDatabase.individualMedicineDao().getTimeSortedEntry(hPRA.mData.size()));
                        }
                        recyclerView.post(new Runnable() {
                            public void run() {
                                // There is no need to use notifyDataSetChanged()
                                hPRA.notifyItemRangeInserted(hPRA.mData.size() - 5, 5);
                                /*for (int i = 0; i <= 4; i++) {
                                    hPRA.notifyItemInserted(currentlyLoadedData.size() - i);
                                }*/
                                loadingNewData = true;
                            }
                        });
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
            entry.position = MainActivity.medicineDatabase.individualMedicineDao().size() + 1;
            //TODO add use to entry edit page and other places
            MainActivity.medicineDatabase.individualMedicineDao().insertAll(entry);
            if (data.medicineLeft > 0) {
                MainActivity.medicineDatabase.customMedicineDao().update(data.name, data.quantity, data.reason, data.dose, data.Id, data.use, data.barcodes, data.medicineLeft - 1);
            }
            getFragmentManager().popBackStack();
        } else { //doesn't exist already, throw up a toast

        }
    }
}
