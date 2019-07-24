package com.example.medicinetrackingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RememberMedicinePage extends Fragment implements RememberPageRecyclerAdapter.ItemClickListener {

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
        RememberPageRecyclerAdapter hPRA = new RememberPageRecyclerAdapter(getContext(), MainActivity.customMedicineManager.rememberedMedicineList.toArray(s));
        hPRA.setClickListener(this);
        rV.setAdapter(hPRA);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClick(View view, int position) {
        Bundle b = new Bundle();
        b.putInt("displayRememberMedicinePosition", position);
        RememberMedicineEditPage page = new RememberMedicineEditPage(); //TODO make sure gets correct medicine
        page.setArguments(b);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, page).addToBackStack(null).commit();
    }
}
