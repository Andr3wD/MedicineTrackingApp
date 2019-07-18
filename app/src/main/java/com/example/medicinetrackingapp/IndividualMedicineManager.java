package com.example.medicinetrackingapp;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class IndividualMedicineManager implements Serializable {

    public ArrayList<IndividualMedicine> medicineHistoryList;
    public long lastEntryTime;
    Context fileContext;

    IndividualMedicineManager() {
        medicineHistoryList = new ArrayList<>();
    }

    public void add(IndividualMedicine m) {
        for (int i = 0; i < medicineHistoryList.size(); i++) {
            if (m.compareTo(medicineHistoryList.get(i)) > 0) {
                Log.i("test", "Adding new entry1");
                medicineHistoryList.add(i, m);
                save();
                return;
            }
        }
        if (medicineHistoryList.size() == 0) {
            Log.i("test", "Adding new entry2");
            medicineHistoryList.add(m);
            save();
            return;
        }
        Log.i("test", "Not adding new entry");
    }

    public IndividualMedicine findByName(String name) {
        for (IndividualMedicine m : medicineHistoryList) {
            if (m.name.equals(name)) {
                return m;
            }
        }
        return null;
    }

    public void save() {
        ObjectOutputStream os = null;
        FileOutputStream fos = null;
        try {
            fos = fileContext.openFileOutput("testFile", Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);
            os.writeObject(medicineHistoryList);
            os.close();
            fos.close();
            Log.i("test", "Saving data");
        } catch (Exception e) {
            Log.i("test", "Unable to save data");
            e.printStackTrace();
        }

    }

    public void remove(int index) {

    }
}
