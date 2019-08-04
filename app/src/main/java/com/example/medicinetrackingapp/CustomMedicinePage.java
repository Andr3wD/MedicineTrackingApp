package com.example.medicinetrackingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomMedicinePage extends Fragment implements CustomPageRecyclerAdapter.ItemClickListener {

    CustomPageRecyclerAdapter hPRA;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.remember_medicine_fragment, container, false);
        getActivity().setTitle("Custom Medicines");
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RecyclerView rV = ((RecyclerView) view.findViewById(R.id.remember_medicine_recyclerview));
        rV.setLayoutManager(new LinearLayoutManager(getContext()));
        IndividualCustomMedicine[] s = new IndividualCustomMedicine[MainActivity.customMedicineManager.rememberedMedicineList.size()];
        hPRA = new CustomPageRecyclerAdapter(getContext(), MainActivity.medicineDatabase.customMedicineDao().getAllSortedId());
        hPRA.setClickListener(this);
        rV.setAdapter(hPRA);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClick(View view, int position) {
        Bundle b = new Bundle();
        b.putInt("displayRememberMedicineId", hPRA.getItem(position).Id);
        CustomMedicineEditPage page = new CustomMedicineEditPage(); //TODO make sure gets correct medicine
        page.setArguments(b);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, page).addToBackStack(null).commit();
    }
}
